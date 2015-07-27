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
	
	private LogicInMemoryObsDAO logicObsInMemoryDAO;
	
	public void setLogicObsInMemoryDAO(LogicInMemoryObsDAO logicObsInMemoryDAO) {
		this.logicObsInMemoryDAO = logicObsInMemoryDAO;
	}
	
	@Override
	public LogicInMemoryObsDAO getLogicObsDAO() {
		return logicObsInMemoryDAO;
	}
	
	public void deleteObsByPatientId(Integer patientId)
	{
		((LogicInMemoryObsDAO) this.getLogicObsDAO())
				.deleteObsByPatientId(patientId);
	}

	public Set<Obs> getObsByConceptName(Integer patientId,
			String conceptName)
	{
		return ((LogicInMemoryObsDAO) this.getLogicObsDAO())
				.getObsByConceptName(patientId, conceptName);
	}
	
	public HashMap<Integer, HashMap<String, Set<Obs>>> getObs(){
		return ((LogicInMemoryObsDAO) this.getLogicObsDAO()).getObs();
	}
	
	public void clearObs() {
	    ((LogicInMemoryObsDAO) this.getLogicObsDAO()).clearObs();
	}

}
