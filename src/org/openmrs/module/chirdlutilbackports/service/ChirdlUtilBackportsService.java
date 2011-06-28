package org.openmrs.module.chirdlutilbackports.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Error;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Session;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.State;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateMapping;

/**
 * Defines services used by this module
 * 
 * @author Tammy Dugan
 */
public interface ChirdlUtilBackportsService {
	
	public LocationTagAttributeValue getLocationTagAttributeValue(Integer locationTagId, String locationTagAttributeName,
	                                                              Integer locationId);
	
	public LocationAttributeValue getLocationAttributeValue(Integer locationId, String locationAttributeName);
	
	public LocationTagAttributeValue getLocationTagAttributeValueById(Integer location_tag_attribute_value_id);
	
	public LocationTagAttribute getLocationTagAttribute(Integer locationTagAttributeId);
	
	public LocationTagAttribute getLocationTagAttribute(String locationTagAttributeName);
	
	public LocationTagAttribute saveLocationTagAttribute(LocationTagAttribute value);
	
	public LocationTagAttributeValue saveLocationTagAttributeValue(LocationTagAttributeValue value);
	
	public LocationAttributeValue saveLocationAttributeValue(LocationAttributeValue value);
	
	public void deleteLocationTagAttribute(LocationTagAttribute value);
	
	public void deleteLocationTagAttributeValue(LocationTagAttributeValue value);
	
	/**
	 * Get state by state name
	 * 
	 * @param initialState
	 * 
	 * @param stateMappingId state name
	 * @return state with given state name
	 */
	public StateMapping getStateMapping(State initialState,Program program);
	
	public Session addSession();
	
	public Session updateSession(Session session);
	
	public Session getSession(int sessionId);
	
	public PatientState addPatientState(Patient patient,State initialState, int sessionId,Integer locationTagId,Integer locationId);
	
	public PatientState updatePatientState(PatientState patientState);

	public PatientState getPrevPatientStateByAction(
			int sessionId, int patientStateId,String action);
	
	public List<PatientState> getPatientStatesWithForm(int sessionId);
	
	public List<PatientState> getUnfinishedPatientStatesAllPatients(Date optionalDateRestriction,Integer locationTagId,Integer locationId);
	
	public List<PatientState> getUnfinishedPatientStateByStateName(String state,Date optionalDateRestriction,Integer locationTagId,Integer locationId);
	
	public PatientState getLastUnfinishedPatientState(Integer sessionId);

	public PatientState getLastPatientState(Integer sessionId);
	
	public List<PatientState> getLastPatientStateAllPatients(Date optionalDateRestriction, Integer programId,
			String startStateName, Integer locationTagId, Integer locationId);
	
	public State getStateByName(String stateName);
	
	public Program getProgramByNameVersion(String name,String version);
	
	public Program getProgram(Integer programId);
	
	public PatientState getPatientStateByEncounterFormAction(Integer encounterId, Integer formId, String action);

	public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance,String action);

	public List<FormAttributeValue> getFormAttributesByName(String attributeName);
	
	public ArrayList<String> getFormAttributesByNameAsString(String attributeName);
	
	public List<State> getStatesByActionName(String actionName);
	
	public State getState(Integer stateId);

public PatientState getPatientState(Integer patientStateId);
	
	public List<PatientState> getPatientStateBySessionState(Integer sessionId,
			Integer stateId);
	
	public List<PatientState> getAllRetiredPatientStatesWithForm(Date thresholdDate);
	
	public List<Session> getSessionsByEncounter(Integer encounterId);
	
	public List<PatientState> getPatientStatesWithFormInstances(String formName, Integer encounterId);
	
	public List<PatientState> getPatientStateByEncounterState(Integer encounterId,
			Integer stateId);
	
	public void saveError(Error error);
	
	public List<Error> getErrorsByLevel(String errorLevel,Integer sessionId);
	
	public Integer getErrorCategoryIdByName(String name);
	
	public Program getProgram(Integer locationTagId,Integer locationId);
	
	public List<FormAttributeValue> getFormAttributeValuesByValue(String value);

	public List<PatientState> getUnfinishedPatientStateByStateSession(
		String stateName,Integer sessionId);
	
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state);
	
	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean isRetired);

	public List<PatientState> getPatientStatesBySession(Integer sessionId,boolean isRetired);
	
	public FormAttributeValue getFormAttributeValue(Integer formId, String formAttributeName,
	                                    			Integer locationTagId,Integer locationId);
	
	public FormInstance addFormInstance(Integer formId, Integer locationId);
	
	public void unretireStatesBySessionId(Integer sessionId);
	
	public void saveFormAttributeValue(FormAttributeValue value);
	
	public FormAttribute getFormAttributeByName(String formAttributeName);
}
