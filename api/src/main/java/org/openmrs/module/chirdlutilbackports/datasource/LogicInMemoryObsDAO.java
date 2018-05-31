package org.openmrs.module.chirdlutilbackports.datasource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.cache.Cache;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.Concept;
import org.openmrs.ConceptName;
import org.openmrs.Obs;
import org.openmrs.logic.Duration;
import org.openmrs.logic.LogicContext;
import org.openmrs.logic.LogicCriteria;
import org.openmrs.logic.LogicExpression;
import org.openmrs.logic.LogicExpressionBinary;
import org.openmrs.logic.LogicTransform;
import org.openmrs.logic.db.LogicObsDAO;
import org.openmrs.logic.op.Operator;
import org.openmrs.module.chirdlutilbackports.cache.ApplicationCacheManager;
import org.openmrs.module.chirdlutilbackports.util.ChirdlUtilBackportsConstants;

/**
 * 
 */
public class LogicInMemoryObsDAO implements LogicObsDAO
{
	
	protected final Log log = LogFactory.getLog(getClass());
	
	public LogicInMemoryObsDAO()
	{
	}

	public List<Obs> getObservations(Cohort who, LogicCriteria logicCriteria, LogicContext logicContext)
	{
		List<Obs> results = new ArrayList<Obs>();
		HashMap<String, Set<Obs>> obsByConceptName = null;

		// look up the obs for each patient in the set
		for (Integer patientId : who.getMemberIds())
		{
			obsByConceptName = getObs(patientId);
			if (obsByConceptName != null)
			{
				List<Obs> patientResults = evaluateLogicCriteria(obsByConceptName,
						logicCriteria.getExpression());
				if(patientResults != null)
				{
					results.addAll(patientResults);
				}
			}
		}

		return results;
	}

	private List<Obs> evaluateLogicCriteria(
			HashMap<String, Set<Obs>> obsByConceptName,
			LogicExpression expression)
	{
		Date indexDate = Calendar.getInstance().getTime();
		Operator transformOperator = null;
		LogicTransform transform = expression.getTransform();
		Integer numResults = null;
		
		if(transform!= null){
			transformOperator = transform.getTransformOperator();
			numResults = transform.getNumResults();
		}
		
		if(numResults == null){
			numResults = 1;
		}
		List<Obs> resultObs = this.getCriterion(obsByConceptName, expression,
				indexDate);

		// Apply First/Last Transform to results
		if (transformOperator == Operator.LAST)
		{
			if(resultObs.size()>0)
			{
				Collections.sort(resultObs, Collections
						.reverseOrder(new ObsComparator()));
			}
		} else if (transformOperator == Operator.FIRST)
		{
			Collections.sort(resultObs, new ObsComparator());
		}else if (transformOperator == Operator.DISTINCT) {
			Set<Obs> distinctElements = new HashSet<Obs>(resultObs);
			resultObs = new ArrayList<Obs>(distinctElements);			
		} 
		
		//make the default sort order in reverse
		//this is specifically needed for the dx and complaints rule
		if(resultObs != null)
		{
			Collections.sort(resultObs, Collections
					.reverseOrder(new ObsComparator()));
		}else
		{
			resultObs = new ArrayList<Obs>();
		}
		
		//return a single result per patient for these operators
		//I don't see an easy way to do this in hibernate so I am
		//doing some postprocessing
		if(transformOperator == Operator.FIRST || transformOperator == Operator.LAST){
			HashMap<Integer,ArrayList<Obs>> nResultMap = new HashMap<Integer,ArrayList<Obs>>();
			
			for(Obs currResult:resultObs){
				Integer currPersonId = currResult.getPersonId();
				ArrayList<Obs> prevResults = nResultMap.get(currPersonId);
				if(prevResults == null){
					prevResults = new ArrayList<Obs>();
					nResultMap.put(currPersonId, prevResults);
				}
				
				if(prevResults.size()<numResults){
					prevResults.add(currResult);
				}
			}
			
			if(nResultMap.values().size()>0){
				resultObs.clear();
				
				for(ArrayList<Obs> currPatientObs:nResultMap.values()){
					resultObs.addAll(currPatientObs);
				}
			}
		}
		return resultObs;
	}

	private ArrayList<Obs> getCriterion(
			HashMap<String, Set<Obs>> obsByConceptName,
			LogicExpression expression, Date indexDate)
	{

		Operator operator = expression.getOperator();
		Object leftOperand = null;
		if(expression instanceof LogicExpressionBinary){
			leftOperand = ((LogicExpressionBinary) expression).
					getLeftOperand();
		}
		Object rightOperand = expression.getRightOperand();
		ArrayList<Obs> results = new ArrayList<Obs>();
		ArrayList<Obs> leftOperandResults = new ArrayList<Obs>();

		String rootToken = expression.getRootToken();
	
		if(rootToken != null){

			Set<Obs> conceptObs = obsByConceptName.get(rootToken);
			if(conceptObs != null){
				leftOperandResults.addAll(conceptObs);
				results.addAll(conceptObs);
			}
		}
		
		if (operator == Operator.BEFORE)
		{
			results = compare("obsDatetime", rightOperand, leftOperandResults, "LT");

		} else if (operator == Operator.AFTER)
		{
			results = compare("obsDatetime", rightOperand, leftOperandResults, "GT");
		} else if (operator == Operator.AND || operator == Operator.OR)
		{
			ArrayList<Obs> leftResults = null;
			ArrayList<Obs> rightResults = null;

			if (leftOperand instanceof LogicExpression)
			{
				leftResults = this.getCriterion(obsByConceptName,
						(LogicExpression) leftOperand, indexDate);
			}
			if (rightOperand instanceof LogicExpression)
			{
				rightResults = this.getCriterion(obsByConceptName,
						(LogicExpression) rightOperand, indexDate);
			}

			if (leftResults != null && rightResults != null)
			{
				if (operator == Operator.AND)
				{
					results = and(leftResults, rightResults);
				}
				if (operator == Operator.OR)
				{
					results = or(leftResults, rightResults);
				}
			}

		} else if (operator == Operator.NOT)
		{
			// ignore this one for now
		} else if (operator == Operator.CONTAINS) {
			if (rightOperand instanceof Concept) {
				results = compare("valueCoded", rightOperand, leftOperandResults, "EQ");
			} else if (rightOperand instanceof String) {
				Concept concept = new Concept();
				ConceptName conceptName = new ConceptName();
				conceptName.setName((String) rightOperand);
				conceptName.setLocale(new Locale("en_US"));
				concept.addName(conceptName);
				results = compare("valueCoded", concept, leftOperandResults, "EQ");
			} else
				this.log.error("Invalid operand value for CONTAINS operation");
		} else if (operator == Operator.EQUALS)
		{
			if (rightOperand instanceof Float
					|| rightOperand instanceof Integer
					|| rightOperand instanceof Double){
				results = compare("valueNumeric", rightOperand, leftOperandResults, "EQ");
			}
			else if (rightOperand instanceof String){
				results = compare("valueText", rightOperand, leftOperandResults, "EQ");
			}
			else if (rightOperand instanceof Date){
				results = compare("valueDatetime", rightOperand, leftOperandResults, "EQ");
			}
			else if (rightOperand instanceof Concept){
				results = compare("valueCoded", rightOperand, leftOperandResults, "EQ");
			}
			else
				this.log.error("Invalid operand value for EQUALS operation");

		} else if (operator == Operator.LTE)
		{
			if (rightOperand instanceof Float
					|| rightOperand instanceof Integer
					|| rightOperand instanceof Double){
				results = compare("valueNumeric", rightOperand, leftOperandResults,
						"LTE");
			}
			else if (rightOperand instanceof Date){
				results = compare("valueDatetime", rightOperand, leftOperandResults,
						"LTE");
			}
			else
				this.log
						.error("Invalid operand value for LESS THAN EQUAL operation");

		} else if (operator == Operator.GTE)
		{
			if (rightOperand instanceof Float
					|| rightOperand instanceof Integer
					|| rightOperand instanceof Double){
				results = compare("valueNumeric", rightOperand, leftOperandResults,
						"GTE");
			}
			else if (rightOperand instanceof Date){
				results = compare("valueDatetime", rightOperand, leftOperandResults,
						"GTE");
			}
			else
				this.log
						.error("Invalid operand value for GREATER THAN EQUAL operation");
		} else  if (operator == Operator.LT)
		{
			if (rightOperand instanceof Float
					|| rightOperand instanceof Integer
					|| rightOperand instanceof Double){
				results = compare("valueNumeric", rightOperand, leftOperandResults,
						"LT");
			}
			else if (rightOperand instanceof Date){
				results = compare("valueDatetime", rightOperand, leftOperandResults,
						"LT");
			}
			else
				this.log.error("Invalid operand value for LESS THAN operation");

		}else if (operator == Operator.GT)
		{
			if (rightOperand instanceof Float
					|| rightOperand instanceof Integer
					|| rightOperand instanceof Double){
				results = compare("valueNumeric", rightOperand, leftOperandResults,
						"GT");
			}
			else if (rightOperand instanceof Date){
				results = compare("valueDatetime", rightOperand, leftOperandResults,
						"GT");
			}
			else
				this.log.error("Invalid operand value for GREATER THAN operation");

		} else if (operator == Operator.EXISTS)
		{
			// EXISTS can be handled on the higher level (above
			// LogicService, even) by coercing the Result into a Boolean for
			// each patient
		} else if (operator == Operator.ASOF
		        && rightOperand instanceof Date) {
			indexDate = (Date) rightOperand;
			results = compare("obsDatetime", indexDate, leftOperandResults, "LT");

		} else if (operator == Operator.WITHIN
		        && rightOperand instanceof Duration) {
			
			Duration duration = (Duration) rightOperand;
			Calendar within = Calendar.getInstance();
			within.setTime(indexDate);

			if (duration.getUnits() == Duration.Units.YEARS) {
				within.add(Calendar.YEAR, duration.getDuration().intValue());
			} else if (duration.getUnits() == Duration.Units.MONTHS) {
				within.add(Calendar.MONTH, duration.getDuration().intValue());
			} else if (duration.getUnits() == Duration.Units.WEEKS) {
				within.add(Calendar.WEEK_OF_YEAR, duration.getDuration()
				                                            .intValue());
			} else if (duration.getUnits() == Duration.Units.DAYS) {
				within.add(Calendar.DAY_OF_YEAR, duration.getDuration()
				                                           .intValue());
			} else if (duration.getUnits() == Duration.Units.MINUTES) {
				within.add(Calendar.MINUTE, duration.getDuration().intValue());
			} else if (duration.getUnits() == Duration.Units.SECONDS) {
				within.add(Calendar.SECOND, duration.getDuration().intValue());
			}

			if(indexDate.compareTo(within.getTime())>0){
				results = compareBetween("obsDatetime",within.getTime(),indexDate, leftOperandResults);
			}else{
				results = compareBetween("obsDatetime",indexDate, within.getTime(),leftOperandResults);
			}
		}
		
		return results;
	}
	
	private ArrayList<Obs> compareBetweenObsDateTime(Date firstDate,
			Date lastDate, ArrayList<Obs> prevResults)
	{
		ArrayList<Obs> results = new ArrayList<Obs>();

		for (Obs currObs : prevResults)
		{
			Date currObsDate = currObs.getObsDatetime();

			if (currObsDate != null
					&& currObsDate.compareTo(firstDate) >= 0&&
					currObsDate.compareTo(lastDate) <= 0)
			{
				results.add(currObs);
			}
		}

		return results;
	}

	private ArrayList<Obs> compareBetween(String component, Object comparisonOperand1,
			Object comparisonOperand2,
			ArrayList<Obs> prevResults)
	{
		if (component.equalsIgnoreCase("obsDatetime")
				&& comparisonOperand1 instanceof Date&&comparisonOperand2 instanceof Date)
		{
			return compareBetweenObsDateTime((Date) comparisonOperand1,(Date) comparisonOperand2,
					prevResults);
		}
		
		return prevResults;
	}
	
	private ArrayList<Obs> compare(String component, Object comparisonOperand,
			ArrayList<Obs> prevResults, String comparator)
	{
		if (component.equalsIgnoreCase("obsDatetime")
				&& comparisonOperand instanceof Date)
		{
			return compareObsDateTime((Date) comparisonOperand, prevResults,
					comparator);
		}
		if (component.equalsIgnoreCase("valueNumeric"))

		{
			if (comparisonOperand instanceof Double)
			{
				return compareValueNumeric((Double) comparisonOperand,
						prevResults, comparator);
			}
			if (comparisonOperand instanceof Integer)
			{
				return compareValueNumeric((Integer) comparisonOperand,
						prevResults, comparator);
			}

			if (comparisonOperand instanceof Float)
			{
				return compareValueNumeric((Float) comparisonOperand,
						prevResults, comparator);
			}
		}
		if (component.equalsIgnoreCase("valueDatetime")
				&& comparisonOperand instanceof Date)
		{
			return compareValueDate((Date) comparisonOperand, prevResults,
					comparator);
		}

		if (component.equalsIgnoreCase("valueCoded")
				&& comparisonOperand instanceof Concept)
		{
			return compareValueConcept((Concept) comparisonOperand,
					prevResults, comparator);
		}
		
		if (component.equalsIgnoreCase("valueText")
				&& comparisonOperand instanceof String)
		{
			return compareValueText((String) comparisonOperand,
					prevResults, comparator);
		}
		return prevResults;
	}

	private ArrayList<Obs> compareValueConcept(Concept comparisonOperand,
			ArrayList<Obs> prevResults, String comparator)
	{
		ArrayList<Obs> results = new ArrayList<Obs>();

		for (Obs currObs : prevResults)
		{
			if (comparator.equalsIgnoreCase("EQ"))
			{
				Concept codedAnswer = currObs.getValueCoded();
				
				if (codedAnswer != null && codedAnswer.getName().getName().equals(
						comparisonOperand.getName().getName()))
				{
					results.add(currObs);
				}
			}
		}

		return results;
	}

	private ArrayList<Obs> compareObsDateTime(Date comparisonOperand,
			ArrayList<Obs> prevResults, String comparator)
	{
		ArrayList<Obs> results = new ArrayList<Obs>();

		for (Obs currObs : prevResults)
		{
			Date currObsDate = currObs.getObsDatetime();

			if (comparator.equalsIgnoreCase("LT"))
			{
				if (currObsDate!=null&&currObsDate.compareTo(comparisonOperand) < 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("LTE"))
			{
				if (currObsDate!=null&&currObsDate.compareTo(comparisonOperand) <= 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("GT"))
			{
				if (currObsDate!=null&&currObsDate.compareTo(comparisonOperand) > 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("GTE"))
			{
				if (currObsDate!=null&&currObsDate.compareTo(comparisonOperand) >= 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("EQ"))
			{
				if (currObsDate!=null&&currObsDate.compareTo(comparisonOperand) == 0)
				{
					results.add(currObs);
				}
			}
		}

		return results;
	}

	private ArrayList<Obs> compareValueNumeric(Integer comparisonOperand,
			ArrayList<Obs> prevResults, String comparator)
	{
		return compareValueNumeric(comparisonOperand.doubleValue(),
				prevResults, comparator);
	}

	private ArrayList<Obs> compareValueNumeric(Float comparisonOperand,
			ArrayList<Obs> prevResults, String comparator)
	{
		return compareValueNumeric(comparisonOperand.doubleValue(),
				prevResults, comparator);
	}

	private ArrayList<Obs> compareValueNumeric(Double comparisonOperand,
			ArrayList<Obs> prevResults, String comparator)
	{
		ArrayList<Obs> results = new ArrayList<Obs>();

		for (Obs currObs : prevResults)
		{
			Double currObsNumeric = currObs.getValueNumeric();

			if (comparator.equalsIgnoreCase("LT"))
			{
				if (currObsNumeric!=null&&currObsNumeric.compareTo(comparisonOperand) < 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("LTE"))
			{
				if (currObsNumeric!=null&&currObsNumeric.compareTo(comparisonOperand) <= 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("GT"))
			{
				if (currObsNumeric!=null&&currObsNumeric.compareTo(comparisonOperand) > 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("GTE"))
			{
				if (currObsNumeric!=null&&currObsNumeric.compareTo(comparisonOperand) >= 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("EQ"))
			{
				if (currObsNumeric!=null&&currObsNumeric.compareTo(comparisonOperand) == 0)
				{
					results.add(currObs);
				}
			}
		}

		return results;
	}
	
	private ArrayList<Obs> compareValueText(String comparisonOperand,
			ArrayList<Obs> prevResults, String comparator)
	{
		ArrayList<Obs> results = new ArrayList<Obs>();

		for (Obs currObs : prevResults)
		{
			if (comparator.equalsIgnoreCase("EQ"))
			{
				String currObsText = currObs.getValueText();
				if (currObsText!=null&&currObsText.compareTo(comparisonOperand) == 0)
				{
					results.add(currObs);
				}
			}
		}

		return results;
	}

	private ArrayList<Obs> compareValueDate(Date comparisonOperand,
			ArrayList<Obs> prevResults, String comparator)
	{
		ArrayList<Obs> results = new ArrayList<Obs>();

		for (Obs currObs : prevResults)
		{
			Date currValueDatetime = currObs.getValueDatetime();

			if (comparator.equalsIgnoreCase("LT"))
			{
				if (currValueDatetime!=null&&currValueDatetime.compareTo(comparisonOperand) < 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("LTE"))
			{
				if (currValueDatetime!=null&&currValueDatetime.compareTo(comparisonOperand) <= 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("GT"))
			{
				if (currValueDatetime!=null&&currValueDatetime.compareTo(comparisonOperand) > 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("GTE"))
			{
				if (currValueDatetime!=null&&currValueDatetime.compareTo(comparisonOperand) >= 0)
				{
					results.add(currObs);
				}
			}
			if (comparator.equalsIgnoreCase("EQ"))
			{
				if (currValueDatetime!=null&&currValueDatetime.compareTo(comparisonOperand) == 0)
				{
					results.add(currObs);
				}
			}
		}

		return results;
	}

	private ArrayList<Obs> and(ArrayList<Obs> leftResults,
			ArrayList<Obs> rightResults)
	{
		ArrayList<Obs> results = new ArrayList<Obs>();
		results.addAll(leftResults);
		results.retainAll(rightResults);

		return results;
	}

	private ArrayList<Obs> or(ArrayList<Obs> leftResults,
			ArrayList<Obs> rightResults)
	{
		ArrayList<Obs> results = new ArrayList<Obs>();
		results.addAll(leftResults);
		results.addAll(rightResults);

		return results;
	}

	public Set<Obs> getObsByConceptName(Integer patientId,
			String conceptName)
	{
		if (patientId == null)
		{
			return new HashSet<Obs>();
		}

		HashMap<String, Set<Obs>> obsById = getObs(patientId);

		if (obsById == null)
		{
			// TODO call query kite
			return new HashSet<Obs>();
		}

		return obsById.get(conceptName);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * Return a HashMap of concept name to Obs for a given patient.  Any changes to this map 
	 * WILL NOT be persisted to the cache.  Please call the saveObs method to persist the data.
	 * 
	 * @param patientId The patient identifier.
	 * @return HashMap of concept name to a Set of Obs or null if the cache is not instantiated or 
	 * a record does not exist for the provided patient ID.
	 */
    public HashMap<String, Set<Obs>> getObs(Integer patientId){
		if (patientId == null) {
			return null;
		}
		
		Cache<Integer, HashMap> obsCache = getCache();
		if (obsCache == null) {
			return null;
		}
		try {
			return obsCache.get(patientId);
		} catch (Exception e) {
			log.error("Error retrieving data from in memory cache", e);
			return null;
		}
	}
	
	

	@SuppressWarnings("rawtypes")
    public void deleteObsByPatientId(Integer patientId)
	{
		if (patientId != null) {
			Cache<Integer, HashMap> obsCache = getCache();
			if (obsCache != null) {
				obsCache.remove(patientId);
			}
		}
	}

	@SuppressWarnings("rawtypes")
    public void clearObs() {
    	Cache<Integer, HashMap> obsCache = getCache();
    	if (obsCache != null) {
	        obsCache.clear();
    	}
    }

	/**
     * @see org.openmrs.logic.db.LogicObsDAO#getAllQuestionConceptIds()
     */
    public List<Integer> getAllQuestionConceptIds() {
	    // TODO Auto-generated method stub
	    return null;
    }
    
    /**
	 * Saves the concept name to Set of Obs mapping for the the provided patient ID.  This will 
	 * replace any existing data being stored.
	 * 
	 * @param patientId The patient identifier
	 * @param conceptObsMap Map of concept name to set of Obs objects
	 */
    @SuppressWarnings("rawtypes")
    public void saveObs(Integer patientId, HashMap<String, Set<Obs>> conceptObsMap) {
    	if (patientId != null && conceptObsMap != null) {
	    	Cache<Integer, HashMap> obsCache = getCache();
	    	if (obsCache != null) {
	    		obsCache.put(patientId, conceptObsMap);
	    	}
    	}
	}
    
    /**
	 * Saves an observation with the given patient ID and concept name.
	 * 
	 * @param patientId The patient identifier
	 * @param conceptName The name of the concept
	 * @param observation The observation to save
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public void saveOb(Integer patientId, String conceptName, Obs observation) {
		if (patientId == null || conceptName == null || observation == null) {
			return;
		}
		
		Cache<Integer, HashMap> obsCache = getCache();
		
    	if (obsCache == null) {
    		return;
    	}
    	
    	HashMap<String, Set<Obs>> conceptObs = obsCache.get(patientId);
    	if (conceptObs == null) {
    		conceptObs = new HashMap<String, Set<Obs>>();
    	}
    	
    	Set<Obs> obs = conceptObs.get(conceptName);
    	if (obs == null) {
    		obs = new HashSet<Obs>();
    	}
    	
    	obs.add(observation);
    	conceptObs.put(conceptName, obs);
    	obsCache.put(patientId, conceptObs);
	}
    
	/**
	 * Retrieves the cache for the in-memory observations
	 * 
	 * @return Cache object for in-memory observations
	 */
    @SuppressWarnings("rawtypes")
    private Cache<Integer, HashMap> getCache() {
    	ApplicationCacheManager cacheManager = ApplicationCacheManager.getInstance();
		return cacheManager.getCache(
			ChirdlUtilBackportsConstants.CACHE_EHR_MEDICAL_RECORD, 
			ChirdlUtilBackportsConstants.CACHE_EHR_MEDICAL_RECORD_KEY_CLASS, 
			ChirdlUtilBackportsConstants.CACHE_EHR_MEDICAL_RECORD_VALUE_CLASS);
    }
}
