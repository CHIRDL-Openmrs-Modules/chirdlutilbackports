package org.openmrs.module.chirdlutilbackports.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.module.chirdlutilbackports.hibernateBeans.Error;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Session;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.State;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateAction;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateMapping;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.springframework.transaction.annotation.Transactional;

/**
 * ChirdlUtilBackports related database functions
 * 
 * @author Tammy Dugan
 */
@Transactional
public interface ChirdlUtilBackportsDAO {
	
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
	 * Returns the value of a form attribute from the chirdlutilbackports_form_attribute_value table
	 * @param formId id of the form to find an attribute for
	 * @param formAttributeName name of the form attribute
	 * @return FormAttributeValue value of the attribute for the given form
	 */
	public FormAttributeValue getFormAttributeValue(Integer formId, String formAttributeName,Integer locationTagId, Integer locationId);
	
	/**
	 * Get state by name
	 * 
	 * @param initialState name of state
	 * @return state with given name
	 */
	public StateMapping getStateMapping(State initialState,Program program);
	
	public List<PatientState> getUnfinishedPatientStatesAllPatients(Date optionalDateRestriction,Integer locationTagId,Integer locationId);
	
	public Session addSession(Session session);
	
	public Session updateSession(Session session);
	
	public Session getSession(int sessionId);
	
	public PatientState addUpdatePatientState(PatientState patientState);
	
	public List<PatientState> getPatientStatesWithForm(int sessionId);
	
	public PatientState getPrevPatientStateByAction(
			int sessionId, int patientStateId,String action);
	
	public StateAction getStateActionByName(String action);
	
	public List<PatientState> getUnfinishedPatientStateByStateName(String state,Date optionalDateRestriction, Integer locationTagId, Integer locationId);

	public PatientState getLastUnfinishedPatientState(Integer sessionId);

	public PatientState getLastPatientState(Integer sessionId);
	
	public List<PatientState> getLastPatientStateAllPatients(
			Date optionalDateRestriction,Integer programId,String startStateName, Integer locationTagId, Integer locationId);
	
	public State getStateByName(String stateName);
	
	public Program getProgramByNameVersion(String name,String version);
	
	public Program getProgram(Integer programId);

	public PatientState getPatientStateByEncounterFormAction(Integer encounterId, Integer formId, String action);
	
	public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance,String action);

	public List<FormAttributeValue> getFormAttributesByName(String attributeName);

	public List<State> getStatesByActionName(String actionName);
	
	public State getState(Integer stateId);
	
	/**
	 * Adds a new row to the chirdlutilbackports_form_instance table
	 * @param formInstance new form instance to add
	 * @return FormInstance newly added form instance
	 */
	public FormInstance addFormInstance(Integer formId, Integer locationId);
	
	public PatientState getPatientState(Integer patientStateId);
	
	public List<PatientState> getPatientStateBySessionState(Integer sessionId,
			Integer stateId);
	
	public List<PatientState> getAllRetiredPatientStatesWithForm(Date thresholdDate);
	
	public List<Session> getSessionsByEncounter(int encounterId);
	
	public List<PatientState> getPatientStatesWithFormInstances(String formName, Integer encounterId);
		
	public List<PatientState> getPatientStateByEncounterState(Integer encounterId,
			Integer stateId);
	
	public void saveError(Error error);
	
	public List<Error> getErrorsByLevel(String errorLevel,Integer sessionId);

	
	public Program getProgram(Integer locationTagId,Integer locationId);
	
	public List<FormAttributeValue> getFormAttributeValuesByValue(String value);
	
	public List<PatientState> getUnfinishedPatientStateByStateSession(
		String stateName,Integer sessionId);
	
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state);
	
	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean isRetired);

	public List<PatientState> getPatientStatesBySession(Integer sessionId,boolean isRetired);

	public ArrayList<String> getFormAttributesByNameAsString(String attributeName);
	
	public Integer getErrorCategoryIdByName(String name);
	
	public FormAttribute getFormAttributeByName(String formAttributeName);

	public void saveFormAttributeValue(FormAttributeValue value);
}
