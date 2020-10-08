/**
 * 
 */
package org.openmrs.module.chirdlutilbackports.datasource;

import java.util.HashMap;
import java.util.Set;

import org.openmrs.Obs;
import org.openmrs.logic.datasource.ObsDataSource;

/**
 * @author Tammy Dugan
 * 
 */
public class ObsInMemoryDatasource extends ObsDataSource
{
	public static final String NAME = "RMRS";
	
	protected LogicInMemoryObsDAO logicObsInMemoryDAO;
	
	public void setLogicObsInMemoryDAO(LogicInMemoryObsDAO logicObsInMemoryDAO) {
		this.logicObsInMemoryDAO = logicObsInMemoryDAO;
	}
	
	@Override
	public LogicInMemoryObsDAO getLogicObsDAO() {
		return this.logicObsInMemoryDAO;
	}
	
	public void deleteObsByPatientId(Integer patientId)
	{
		this.getLogicObsDAO().deleteObsByPatientId(patientId);
	}

	public Set<Obs> getObsByConceptName(Integer patientId,
			String conceptName)
	{
		return this.getLogicObsDAO().getObsByConceptName(patientId, conceptName);
	}
	
	public HashMap<String, Set<Obs>> getObs(Integer patientId){
		return this.getLogicObsDAO().getObs(patientId);
	}
	
	public void clearObs() {
	    this.getLogicObsDAO().clearObs();
	}

	/**
	 * Saves the concept name to Set of Obs mapping for the the provided patient ID.  This will 
	 * replace any existing data being stored.
	 * 
	 * @param patientId The patient identifier
	 * @param conceptObsMap Map of concept name to set of Obs objects
	 */
	public void saveObs(Integer patientId, HashMap<String, Set<Obs>> conceptObsMap) {
		this.getLogicObsDAO().saveObs(patientId, conceptObsMap);
	}
	
	/**
	 * Saves an observation with the given patient ID and concept name.
	 * 
	 * @param patientId The patient identifier
	 * @param conceptName The name of the concept
	 * @param observation The observation to save
	 */
	public void saveOb(Integer patientId, String conceptName, Obs observation) {
		this.getLogicObsDAO().saveOb(patientId, conceptName, observation);
	}
}
