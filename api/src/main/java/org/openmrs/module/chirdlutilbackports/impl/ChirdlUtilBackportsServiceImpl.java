package org.openmrs.module.chirdlutilbackports.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.openmrs.CareSetting;
import org.openmrs.Encounter;
import org.openmrs.FieldType;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.Order;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.api.APIException;
import org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ChirdlLocationAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ChirdlLocationAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Error;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Session;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.State;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateMapping;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;

/**
 * Defines implementations of services used by this module
 * 
 * @author Tammy Dugan
 */
public class ChirdlUtilBackportsServiceImpl implements ChirdlUtilBackportsService {
	
	private ChirdlUtilBackportsDAO dao;
	/**
	 * Empty constructor
	 */
	public ChirdlUtilBackportsServiceImpl() {
	}
	
	/**
	 * @return ChirdlUtilBackportsDAO
	 */
	public ChirdlUtilBackportsDAO getChirdlUtilBackportsDAO() {
		return this.dao;
	}
	
	/**
	 * Sets the DAO for this service. The dao allows interaction with the database.
	 * 
	 * @param dao
	 */
	public void setChirdlUtilBackportsDAO(ChirdlUtilBackportsDAO dao) {
		this.dao = dao;
	}
	
	public LocationTagAttributeValue getLocationTagAttributeValue(Integer locationTagId, String locationTagAttributeName,
	                                                              Integer locationId) {
		return getChirdlUtilBackportsDAO().getLocationTagAttributeValue(locationTagId, locationTagAttributeName, locationId);
	}
	
	public ChirdlLocationAttributeValue getLocationAttributeValue(Integer locationId, String locationAttributeName) {
		return getChirdlUtilBackportsDAO().getLocationAttributeValue(locationId, locationAttributeName);
	}
	
	public LocationTagAttributeValue getLocationTagAttributeValueById(Integer location_tag_attribute_value_id) {
		return getChirdlUtilBackportsDAO().getLocationTagAttributeValueById(location_tag_attribute_value_id);
	}
	
	public LocationTagAttribute getLocationTagAttribute(Integer locationTagAttributeId) {
		return getChirdlUtilBackportsDAO().getLocationTagAttribute(locationTagAttributeId);
	}
	
	public LocationTagAttribute getLocationTagAttribute(String locationTagAttributeName) {
		return getChirdlUtilBackportsDAO().getLocationTagAttribute(locationTagAttributeName);
	}
	
	public LocationTagAttribute saveLocationTagAttribute(LocationTagAttribute value) {
		return getChirdlUtilBackportsDAO().saveLocationTagAttribute(value);
	}
	
	public LocationTagAttributeValue saveLocationTagAttributeValue(LocationTagAttributeValue value) {
		return getChirdlUtilBackportsDAO().saveLocationTagAttributeValue(value);
	}
	
	public ChirdlLocationAttributeValue saveLocationAttributeValue(ChirdlLocationAttributeValue value) {
		return getChirdlUtilBackportsDAO().saveLocationAttributeValue(value);
	}
	
	public void deleteLocationTagAttribute(LocationTagAttribute value) {
		getChirdlUtilBackportsDAO().deleteLocationTagAttribute(value);
	}
	
	public void deleteLocationTagAttributeValue(LocationTagAttributeValue value) {
		getChirdlUtilBackportsDAO().deleteLocationTagAttributeValue(value);
	}
	
	public FormInstance addFormInstance(Integer formId, Integer locationId) {
		return getChirdlUtilBackportsDAO().addFormInstance(formId, locationId);
	}
	
	public FormAttributeValue getFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId,
	                                                Integer locationId) {
		return getChirdlUtilBackportsDAO().getFormAttributeValue(formId, formAttributeName, locationTagId, locationId);
	}
	
	/**
	 * Get state mapping by initial state name
	 * 
	 * @param stateMappingId state mapping unique id
	 * @return state with given state name
	 */
	public StateMapping getStateMapping(State initialState,Program program)  {
		return getChirdlUtilBackportsDAO().getStateMapping(initialState,program);
	}
	
	public Session addSession() {
		
		Session session = new Session();
		
		return getChirdlUtilBackportsDAO().addSession(session);
	}
	
	public Session getSession(int sessionId)
	{
		return getChirdlUtilBackportsDAO().getSession(sessionId);
	}
		
	public Session updateSession(Session session) {
		
		return getChirdlUtilBackportsDAO().updateSession(session);
	}
	
	public PatientState addPatientState(Patient patient,State initialState,
			int sessionId,Integer locationTagId,Integer locationId,FormInstance formInstance)
	{
		PatientState patientState = new PatientState();
		patientState.setStartTime(new java.util.Date());
		patientState.setPatient(patient);
		patientState.setState(initialState);
		patientState.setSessionId(sessionId);
		patientState.setLocationId(locationId);
		patientState.setLocationTagId(locationTagId);
		if (formInstance != null) {
			patientState.setFormInstance(formInstance);
		}
		
		return getChirdlUtilBackportsDAO().addUpdatePatientState(patientState);
	}
	
	public PatientState updatePatientState(PatientState patientState)
	{
		return getChirdlUtilBackportsDAO().addUpdatePatientState(patientState);
	}
		
	public List<PatientState> getPatientStatesWithForm(int sessionId){
		return getChirdlUtilBackportsDAO().getPatientStatesWithForm(sessionId);
	}
	
	public PatientState getPrevPatientStateByAction(
			int sessionId, int patientStateId,String action)
	{
		return getChirdlUtilBackportsDAO().getPrevPatientStateByAction(
				sessionId, patientStateId,action);
	}
	
	public List<PatientState> getUnfinishedPatientStatesAllPatients(Date optionalDateRestriction,
			Integer locationTagId,Integer locationId)
	{
		return getChirdlUtilBackportsDAO().getUnfinishedPatientStatesAllPatients(optionalDateRestriction,locationTagId,locationId);
	}
	
	public List<PatientState> getUnfinishedPatientStateByStateName(String state,Date optionalDateRestriction,
			Integer locationTagId, Integer locationId){
		return getChirdlUtilBackportsDAO().getUnfinishedPatientStateByStateName(state,optionalDateRestriction,locationTagId,locationId);
	}
	public PatientState getLastUnfinishedPatientState(Integer sessionId){
		return getChirdlUtilBackportsDAO().getLastUnfinishedPatientState(sessionId);
	}
	
	public PatientState getLastPatientState(Integer sessionId){
		return getChirdlUtilBackportsDAO().getLastPatientState(sessionId);
	}
	
	public List<PatientState> getLastPatientStateAllPatients(Date optionalDateRestriction, 
			Integer programId,String startStateName,Integer locationTagId, Integer locationId){
		return getChirdlUtilBackportsDAO().getLastPatientStateAllPatients(optionalDateRestriction, programId,startStateName, locationTagId,locationId);
	}
	public State getStateByName(String stateName){
		return getChirdlUtilBackportsDAO().getStateByName(stateName);
	}
	
	public Program getProgramByNameVersion(String name,String version){
		return getChirdlUtilBackportsDAO().getProgramByNameVersion(name, version);
	}
	
	public Program getProgram(Integer programId){
		return getChirdlUtilBackportsDAO().getProgram(programId);
	}
	
	public PatientState getPatientStateByEncounterFormAction(Integer encounterId, Integer formId, String action){
		return getChirdlUtilBackportsDAO().getPatientStateByEncounterFormAction(encounterId, formId, action);
	}
	
	public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance,String action){

		return getChirdlUtilBackportsDAO().getPatientStateByFormInstanceAction(formInstance, action, false);
	}
	
    public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance,String action, boolean includeRetired) {
        return getChirdlUtilBackportsDAO().getPatientStateByFormInstanceAction(formInstance, action, includeRetired);

    }
	
	public List<FormAttributeValue> getFormAttributesByName(String attributeName){
		return getChirdlUtilBackportsDAO().getFormAttributesByName(attributeName);
	}
	
	public ArrayList<String> getFormAttributesByNameAsString(String attributeName){
		return getChirdlUtilBackportsDAO().getFormAttributesByNameAsString(attributeName);
	}
	
	public List<State> getStatesByActionName(String actionName){
		return getChirdlUtilBackportsDAO().getStatesByActionName(actionName);
	}
	
	public State getState(Integer stateId){
		return getChirdlUtilBackportsDAO().getState(stateId);
	}
	public PatientState getPatientState(Integer patientStateId){
		return getChirdlUtilBackportsDAO().getPatientState(patientStateId);
	}
	
	public List<PatientState> getPatientStateBySessionState(Integer sessionId,
			Integer stateId){
		return getChirdlUtilBackportsDAO().getPatientStateBySessionState(sessionId,stateId);
	}
	
	public List<PatientState> getAllRetiredPatientStatesWithForm(Date thresholdDate){
		return getChirdlUtilBackportsDAO().getAllRetiredPatientStatesWithForm(thresholdDate);
	}
	
	public List<Session> getSessionsByEncounter(Integer encounterId){
		return getChirdlUtilBackportsDAO().getSessionsByEncounter(encounterId);
	}
	
	public List<PatientState> getPatientStatesWithFormInstances(String formName, Integer encounterId){
		return getChirdlUtilBackportsDAO().getPatientStatesWithFormInstances(formName, encounterId);
	}

	public List<PatientState> getPatientStateByEncounterState(Integer encounterId,
			Integer stateId){
		return getChirdlUtilBackportsDAO().getPatientStateByEncounterState(encounterId,stateId);
	}
	
	public void saveError(Error error){
		getChirdlUtilBackportsDAO().saveError(error);
	}
	
	public List<Error> getErrorsByLevel(String errorLevel,Integer sessionId){
		return getChirdlUtilBackportsDAO().getErrorsByLevel(errorLevel, sessionId);
	}
	
	public Integer getErrorCategoryIdByName(String name){
		return getChirdlUtilBackportsDAO().getErrorCategoryIdByName(name);
	}
	
	public Program getProgram(Integer locationTagId,Integer locationId){
		return getChirdlUtilBackportsDAO().getProgram(locationTagId, locationId);
	}

	public List<FormAttributeValue> getFormAttributeValuesByValue(String value){
		return getChirdlUtilBackportsDAO().getFormAttributeValuesByValue(value);
	}
	
	public List<PatientState> getUnfinishedPatientStateByStateSession(
		String stateName,Integer sessionId){
		return getChirdlUtilBackportsDAO().getUnfinishedPatientStateByStateSession(stateName, sessionId);
	}
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state) {
		return getChirdlUtilBackportsDAO().getPatientStateByFormInstanceState(formInstance, state);
	}
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state, boolean includeRetired) {
		return getChirdlUtilBackportsDAO().getPatientStateByFormInstanceState(formInstance, state,includeRetired);
	}
	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean includeRetired) {
		return getChirdlUtilBackportsDAO().getPatientStatesByFormInstance(formInstance, includeRetired);
	}
	
	public List<PatientState> getPatientStatesBySession(Integer sessionId,boolean isRetired){
		return getChirdlUtilBackportsDAO().getPatientStatesBySession(sessionId, isRetired);
	}

	
	public void unretireStatesBySessionId(Integer sessionId){
		List<PatientState> patientStates = 
			this.getPatientStatesBySession(sessionId,true);
		
		for(PatientState patientState:patientStates){
			patientState.setRetired(false);
			patientState.setDateRetired(null);
			this.updatePatientState(patientState);
		}
	}
	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#saveFormAttributeValue(FormAttributeValue value)
	 */
	public void saveFormAttributeValue(FormAttributeValue value){
		List<FormAttributeValue> valuesStored = dao.getFormAttributeValues(value.getFormId(), value.getFormAttributeId(), value.getLocationTagId(), value.getLocationId());
		if(valuesStored!=null){
			for(FormAttributeValue fav: valuesStored){
				dao.deleteFormAttributeValue(fav);
			}
		}
		getChirdlUtilBackportsDAO().saveFormAttributeValue(value);
	}
	
	public FormAttribute getFormAttributeByName(String formAttributeName){
		return getChirdlUtilBackportsDAO().getFormAttributeByName(formAttributeName);
	}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributeByName(java.lang.String)
	 */
    public ObsAttribute getObsAttributeByName(String obsAttributeName) {
	    return getChirdlUtilBackportsDAO().getObsAttributeByName(obsAttributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributesByName(java.lang.String)
	 */
    public List<ObsAttributeValue> getObsAttributesByName(String attributeName) {
	    return getChirdlUtilBackportsDAO().getObsAttributesByName(attributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributesByNameAsString(java.lang.String)
	 */
    public List<String> getObsAttributesByNameAsString(String attributeName) {
	    return getChirdlUtilBackportsDAO().getObsAttributesByNameAsString(attributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributeValue(java.lang.Integer, java.lang.String)
	 */
    public ObsAttributeValue getObsAttributeValue(Integer obsId, String obsAttributeName) {
	    return getChirdlUtilBackportsDAO().getObsAttributeValue(obsId, obsAttributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getObsAttributeValuesByValue(java.lang.String)
	 */
    public List<ObsAttributeValue> getObsAttributeValuesByValue(String value) {
	    return getChirdlUtilBackportsDAO().getObsAttributeValuesByValue(value);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveObsAttributeValue(org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttributeValue)
	 */
    public void saveObsAttributeValue(ObsAttributeValue value) {
		getChirdlUtilBackportsDAO().saveObsAttributeValue(value);
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormInstanceAttributeByName(java.lang.String)
     */
    public FormInstanceAttribute getFormInstanceAttributeByName(String formInstanceAttributeName) {
		return getChirdlUtilBackportsDAO().getFormInstanceAttributeByName(formInstanceAttributeName);
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormInstanceAttributesByName(java.lang.String)
     */
    public List<FormInstanceAttributeValue> getFormInstanceAttributesByName(String attributeName) {
		return getChirdlUtilBackportsDAO().getFormInstanceAttributesByName(attributeName);
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormInstanceAttributesByNameAsString(java.lang.String)
     */
    public List<String> getFormInstanceAttributesByNameAsString(String attributeName) {
		return getChirdlUtilBackportsDAO().getFormInstanceAttributesByNameAsString(attributeName);
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormInstanceAttributeValue(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
     */
    public FormInstanceAttributeValue getFormInstanceAttributeValue(Integer formId, Integer formInstanceId,
                                                                    Integer locationId, String formInstanceAttributeName) {
		return getChirdlUtilBackportsDAO().getFormInstanceAttributeValue(formId, formInstanceId, locationId, 
			formInstanceAttributeName);
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormInstanceAttributeValuesByValue(java.lang.String)
     */
    public List<FormInstanceAttributeValue> getFormInstanceAttributeValuesByValue(String value) {
		return getChirdlUtilBackportsDAO().getFormInstanceAttributeValuesByValue(value);
    }

    /**
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveFormInstanceAttributeValue(org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttributeValue)
     */
    public void saveFormInstanceAttributeValue(FormInstanceAttributeValue value) {
		getChirdlUtilBackportsDAO().saveFormInstanceAttributeValue(value);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllPrograms()
	 */
    public List<Program> getAllPrograms() {
	    return getChirdlUtilBackportsDAO().getAllPrograms();
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getLocationAttribute(java.lang.String)
	 */
    public ChirdlLocationAttribute getLocationAttribute(String locationAttributeName) {
	    return getChirdlUtilBackportsDAO().getLocationAttribute(locationAttributeName);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveProgram(org.openmrs.module.chirdlutilbackports.hibernateBeans.Program)
	 */
    public Program saveProgram(Program program) {
	    return getChirdlUtilBackportsDAO().saveProgram(program);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#deleteProgram(org.openmrs.module.chirdlutilbackports.hibernateBeans.Program)
	 */
    public void deleteProgram(Program program) {
	    getChirdlUtilBackportsDAO().deleteProgram(program);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveProgramTagMap(org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap)
	 */
    public ProgramTagMap saveProgramTagMap(ProgramTagMap programTagMap) {
	    return getChirdlUtilBackportsDAO().saveProgramTagMap(programTagMap);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#deleteProgramTagMap(org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap)
	 */
    public void deleteProgramTagMap(ProgramTagMap programTagMap) {
	    getChirdlUtilBackportsDAO().deleteProgramTagMap(programTagMap);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getProgram(java.lang.String)
	 */
    public Program getProgram(String name) {
	    return getChirdlUtilBackportsDAO().getProgram(name);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllLocationAttributes()
	 */
    public List<ChirdlLocationAttribute> getAllLocationAttributes() {
	    return getChirdlUtilBackportsDAO().getAllLocationAttributes();
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllLocationTagAttributes()
	 */
    public List<LocationTagAttribute> getAllLocationTagAttributes() {
	    return getChirdlUtilBackportsDAO().getAllLocationTagAttributes();
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllFormAttributes()
	 */
    public List<FormAttribute> getAllFormAttributes() {
	    return getChirdlUtilBackportsDAO().getAllFormAttributes();
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormFields(org.openmrs.Form, java.util.List, boolean)
	 */
    public List<FormField> getFormFields(Form form, List<FieldType> fieldTypes, boolean ordered) {
	    return getChirdlUtilBackportsDAO().getFormFields(form, fieldTypes, ordered);
    }
    
	/**
	 * save the form attribute value by form id, form attribute name, location tag id and location id.
	 * 
	 * @param formId form id
	 * @param formAttributeName formAttribute name
	 * @param locationTagId locationTag id
	 * @param locationId locationTag id
	 * @return an integer sign to show whether the value is stored successfully. value ChirdlUtilBackportsService.OPERATION_SUCCESS
	 * means success and ChirdlUtilBackportsService.FAIL means failed.
	 */
	@Override
	public int saveFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId, Integer locationId, String formAttributeValue) {
		FormAttribute formAttribute = getFormAttributeByName(formAttributeName);
		if(formAttribute==null){
			return ChirdlUtilBackportsService.OPERATION_FAIL;
		}
		Integer formAttributeId = formAttribute.getFormAttributeId();
		FormAttributeValue item = new FormAttributeValue(formId, formAttributeId, locationTagId, locationId, formAttributeValue);
		saveFormAttributeValue(item);
		
		return ChirdlUtilBackportsService.OPERATION_SUCCESS;
	}

	@Override
	public int deleteFormAttributeValue(Integer formId,
			String formAttributeName, Integer locationTagId,
			Integer locationId, String formAttributeValue) {
		FormAttribute formAttribute = this.getFormAttributeByName(formAttributeName);
		if(formAttribute==null){
			return OPERATION_FAIL;
		}
		FormAttributeValue fav = new FormAttributeValue(formId, formAttribute.getFormAttributeId(), locationTagId, locationId, formAttributeValue);
		try{
			getChirdlUtilBackportsDAO().deleteFormAttributeValue(fav);		
		}
		catch(HibernateException e){
			return OPERATION_FAIL;
		}
		return OPERATION_SUCCESS;
	}

	@Override
	public int deleteFormAttributeValue(FormAttributeValue fav) {
		if(fav==null){
			return OPERATION_FAIL;
		}
		try{
			getChirdlUtilBackportsDAO().deleteFormAttributeValue(fav);		
		}
		catch(HibernateException e){
			return OPERATION_FAIL;
		}
		return OPERATION_SUCCESS;
	}

	@Override
	public List<FormAttribute> getAllEditableFormAttributes() {
		return this.getChirdlUtilBackportsDAO().getEditableFormAttributes();
	}

	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getEditableFormAttributes(FormAttribute fa)
	 */
	@Override
	public List<String> getCurrentFormAttributeValueStrCollection(FormAttribute fa) {
		return this.getChirdlUtilBackportsDAO().getCurrentFormAttributeValueStrCollection(fa);
	}
	
	/**
	 * DWE CHICA-334 3/27/15
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getAllFormAttributeValuesByFormId(Integer)
	 */
	@Override
	public List<FormAttributeValue> getAllFormAttributeValuesByFormId(Integer formId)
	{
		return this.getChirdlUtilBackportsDAO().getAllFormAttributeValuesByFormId(formId);
	}
	
	/**
	 * DWE CHICA-334 3/27/15
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.db.ChirdlUtilBackportsDAO#getFormAttributeById(Integer)
	 */
	@Override
	public FormAttribute getFormAttributeById(Integer formAttributeId)
	{
		return this.getChirdlUtilBackportsDAO().getFormAttributeById(formAttributeId);
	}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getPersonAttributeByValue(java.lang.String, java.lang.String)
	 */
    public PersonAttribute getPersonAttributeByValue(String personAttributeTypeName, String value) {
	    return getChirdlUtilBackportsDAO().getPersonAttributeByValue(personAttributeTypeName, value);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormAttributeValue(java.lang.Integer, org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute, java.lang.Integer, java.lang.Integer)
	 */
    public FormAttributeValue getFormAttributeValue(Integer formId, FormAttribute formAttribute, Integer locationTagId,
                                                    Integer locationId) {
	    return getChirdlUtilBackportsDAO().getFormAttributeValue(formId, formAttribute, locationTagId, locationId);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getUsersByRole(org.openmrs.Role, boolean)
	 */
    public List<User> getUsersByRole(Role role, boolean includeRetired) {
	    return getChirdlUtilBackportsDAO().getUsersByRole(role, includeRetired);
    }

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormAttributeValues(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
    public List<FormAttributeValue> getFormAttributeValues(Integer attributeId, Integer locationId, Integer locationTagId) {
	    return getChirdlUtilBackportsDAO().getFormAttributeValues(attributeId, locationId, locationTagId);
    }
    
    /**
     * DWE CHICA-633
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeByName(String)
     */
    @Override
    public EncounterAttribute getEncounterAttributeByName(String encounterAttributeName) throws HibernateException
    {
    	return getChirdlUtilBackportsDAO().getEncounterAttributeByName(encounterAttributeName);
    }
    
    /**
     * DWE CHICA-633
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveEncounterAttributeValue(EncounterAttributeValue)
     */
    @Override
    public EncounterAttributeValue saveEncounterAttributeValue(EncounterAttributeValue encounterAttributeValue) throws HibernateException
    {
    	return getChirdlUtilBackportsDAO().saveEncounterAttributeValue(encounterAttributeValue);
    }
    
    /**
     * DWE CHICA-633
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeValueByName(Integer, String)
     */
    @Override
    public EncounterAttributeValue getEncounterAttributeValueByName(Integer encounterId, String encounterAttributeName) throws HibernateException
    {
    	return getChirdlUtilBackportsDAO().getEncounterAttributeValueByName(encounterId, encounterAttributeName);
    }

    /**
     * DWE CHICA-633
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeValueByAttribute(Integer, EncounterAttribute, boolean)
     */
	@Override
	public EncounterAttributeValue getEncounterAttributeValueByAttribute(Integer encounterId, EncounterAttribute encounterAttribute, boolean includeVoided) throws HibernateException 
	{
		return getChirdlUtilBackportsDAO().getEncounterAttributeValueByAttribute(encounterId, encounterAttribute, includeVoided);
	}   
	
	/**
	 * DWE CHICA-761
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getLastPatientStateAllPatientsByLocation(Date, Integer, String, Integer)
	 */
	public List<PatientState> getLastPatientStateAllPatientsByLocation(Date optionalDateRestriction, Integer programId, String startStateName, Integer locationId) throws HibernateException
	{
		return getChirdlUtilBackportsDAO().getLastPatientStateAllPatientsByLocation(optionalDateRestriction, programId, startStateName, locationId);
	}
	
	/**
	 * DWE CHICA-761
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getProgramByLocation(Integer)
	 */
	public Program getProgramByLocation(Integer locationId) throws HibernateException
	{
		return getChirdlUtilBackportsDAO().getProgramByLocation(locationId);
	}
	
	/**
	 * DWE CHICA-784
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeValueByValue(String, String) 
	 */
	public EncounterAttributeValue getEncounterAttributeValueByValue(String attributeValue, String encounterAttributeName) throws HibernateException
	{
		return getChirdlUtilBackportsDAO().getEncounterAttributeValueByValue(attributeValue, encounterAttributeName);
	}
	
	/**
	 * CHICA-862
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getPatientStatesBySessionId(Integer, List, boolean) 
	 */
	public Map<String, List<PatientState>> getPatientStatesBySessionId(Integer sessionId, List<String> stateNames, boolean includeRetired) throws HibernateException
    {
           Map<String, List<PatientState>> returnMap = new HashMap<String, List<PatientState>>();

           List<PatientState> patientStates = getChirdlUtilBackportsDAO().getPatientStatesBySessionId(sessionId, stateNames, includeRetired);

           if(patientStates != null && patientStates.size() > 0)
           {
                  for(PatientState state : patientStates)
                  {
                        List<PatientState> mappedPatientStates = returnMap.get(state.getState().getName());
                        if(mappedPatientStates == null)
                        {
                               mappedPatientStates = new ArrayList<PatientState>();
                        }
                        mappedPatientStates.add(state);
                        returnMap.put(state.getState().getName(), mappedPatientStates);
                  }
           }
           return returnMap;
    }
	
	/**
	 * CHICA-993 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#deleteLocationTagAttributeValueByValue(LocationTagAttribute, String)
	 */
	public void deleteLocationTagAttributeValueByValue(LocationTagAttribute locationTagAttribute, String value)
	{
		getChirdlUtilBackportsDAO().deleteLocationTagAttributeValueByValue(locationTagAttribute, value);
	}
	
	/**
	 * CHICA-1169
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getPatientStatesByFormNameAndState(java.lang.String, java.util.List, java.lang.Integer, boolean)
	 */
	public List<PatientState> getPatientStatesByFormNameAndState(String formName, List<String> stateNames, Integer encounterId, boolean includeRetired) throws APIException
	{
		return getChirdlUtilBackportsDAO().getPatientStatesByFormNameAndState(formName, stateNames, encounterId, includeRetired);
	}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getPeopleByBirthDate(
	 * java.util.Date, boolean)
	 */
	@Override
	public List<Person> getPeopleByBirthDate(Date birthDate, boolean includeVoided) {
		return getChirdlUtilBackportsDAO().getPeopleByBirthDate(birthDate, includeVoided);
	}

	/**
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getOrders(
	 * org.openmrs.Patient, java.util.List, java.util.List, java.util.List, boolean)
	 */
	@Override
	public List<Order> getOrders(Patient patient, List<CareSetting> careSettings, List<OrderType> orderTypes,
	        List<Encounter> encounters, boolean includeVoided) {
		return getChirdlUtilBackportsDAO().getOrders(patient, careSettings, orderTypes, encounters, includeVoided);
	}
}
