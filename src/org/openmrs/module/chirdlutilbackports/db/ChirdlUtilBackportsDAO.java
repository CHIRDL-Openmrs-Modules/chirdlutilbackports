package org.openmrs.module.chirdlutilbackports.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openmrs.FieldType;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Error;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ObsAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Session;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.State;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateAction;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateMapping;
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
	
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state,boolean includeRetired);
	
	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean isRetired);

	public List<PatientState> getPatientStatesBySession(Integer sessionId,boolean isRetired);

	public ArrayList<String> getFormAttributesByNameAsString(String attributeName);
	
	public Integer getErrorCategoryIdByName(String name);
	
	public FormAttribute getFormAttributeByName(String formAttributeName);

	public void saveFormAttributeValue(FormAttributeValue value);
	
	/**
	 * Retrieves an observation attribute by observation attribute name.
	 * 
	 * @param obsAttributeName The observation attribute name used to do the lookup.
	 * @return ObsAttribute object or null if cannot be found matching the provided observation attribute name.
	 */
	public ObsAttribute getObsAttributeByName(String obsAttributeName);
	
	/**
	 * Retrieves a list of observation attributes by observation attribute name.
	 * 
	 * @param attributeName The observation attribute name used to do the lookup.
	 * @return List of ObsAttribute objects or null if an error occurs.
	 */
	public List<ObsAttributeValue> getObsAttributesByName(String attributeName);
	
	/**
	 * Retrieves a list of observation attribute names as strings by observation attribute name.
	 * 
	 * @param attributeName The observation attribute name used to do the lookup.
	 * @return List of String objects or null if an error occurs.
	 */
	public List<String> getObsAttributesByNameAsString(String attributeName);
	
	/**
	 * Retrieves an observation attribute value.
	 * 
	 * @param obsId The observation ID.
	 * @param obsAttributeName The observation attribute name.
	 * @return ObsAttributeValue object or null if a match cannot be found.
	 */
	public ObsAttributeValue getObsAttributeValue(Integer obsId, String obsAttributeName);
	
	/**
	 * Retrieves a list of observation attribute values.
	 * 
	 * @param value The value of the of the observation attribute value.
	 * @return List of ObsAttributeValue objects or null if an error occurs.
	 */
	public List<ObsAttributeValue> getObsAttributeValuesByValue(String value);

	/**
	 * Saves an observation attribute value.
	 * 
	 * @param value The observations attribute value to save.
	 */
	public void saveObsAttributeValue(ObsAttributeValue value);
	
	/**
	 * Retrieves a form instance attribute by form instance attribute name.
	 * 
	 * @param formInstanceAttributeName The form instance attribute name used to do the lookup.
	 * @return FormInstanceAttribute object or null if cannot be found matching the provided form instance attribute name.
	 */
	public FormInstanceAttribute getFormInstanceAttributeByName(String formInstanceAttributeName);
	
	/**
	 * Retrieves a list of form instance attribute values by form instance attribute name.
	 * 
	 * @param attributeName The form instance attribute name used to do the lookup.
	 * @return List of FormInstanceAttributeValue objects or null if an error occurs.
	 */
	public List<FormInstanceAttributeValue> getFormInstanceAttributesByName(String attributeName);
	
	/**
	 * Retrieves a list of form instance attribute value names as strings by form instance attribute name.
	 * 
	 * @param attributeName The form instance attribute name used to do the lookup.
	 * @return List of String objects or null if an error occurs.
	 */
	public List<String> getFormInstanceAttributesByNameAsString(String attributeName);
	
	/**
	 * Retrieves a form instance attribute value.
	 * 
	 * @param formId The form ID.
	 * @param formInstanceId The form instance ID.
	 * @param locationId The location ID.
	 * @param formInstanceAttributeName The form instance attribute name.
	 * @return FormInstanceAttributeValue object or null if a match cannot be found.
	 */
	public FormInstanceAttributeValue getFormInstanceAttributeValue(Integer formId, Integer formInstanceId, 
	                                                                Integer locationId, String formInstanceAttributeName);
	
	/**
	 * Retrieves a list of form instance attribute values.
	 * 
	 * @param value The value of the form instance attribute value.
	 * @return List of FormInstanceAttributeValue objects or null if an error occurs.
	 */
	public List<FormInstanceAttributeValue> getFormInstanceAttributeValuesByValue(String value);

	/**
	 * Saves a form instance attribute value.
	 * 
	 * @param value The form instance attribute value to save.
	 */
	public void saveFormInstanceAttributeValue(FormInstanceAttributeValue value);
	
	/**
	 * Returns all available programs.
	 * 
	 * @return List of Program objects.
	 */
	public List<Program> getAllPrograms();
	
	/**
	 * Returns a location attribute by name.  This will return null if one is not found by the name provided.
	 * 
	 * @param locationAttributeName
	 * @return LocationAttribute object
	 */
	public LocationAttribute getLocationAttribute(String locationAttributeName);
	
	/**
	 * Returns a program by name.
	 * 
	 * @param name The name of the program.
	 * @return Program by the name specified or null if one is not found by that name.
	 */
	public Program getProgram(String name);
	
	/**
	 * Saves a new program or updates an existing program.
	 * 
	 * @param program The program to save or update.
	 * @return The updated program.
	 */
	public Program saveProgram(Program program);
	
	/**
	 * Deletes a program.
	 * 
	 * @param program The program to delete.
	 */
	public void deleteProgram(Program program);
	
	/**
	 * Saves a new program tag map or updates an existing program tag map.
	 * 
	 * @param programTagMap The program tag map to save or update.
	 * @return The updated program tag map.
	 */
	public ProgramTagMap saveProgramTagMap(ProgramTagMap programTagMap);
	
	/**
	 * Deletes a program tag map.
	 * 
	 * @param programTagMap The program tag map to delete.
	 */
	public void deleteProgramTagMap(ProgramTagMap programTagMap);
	
	/**
	 * Returns all location attributes.
	 * 
	 * @return List containing all location attributes.
	 */
	public List<LocationAttribute> getAllLocationAttributes();
	
	/**
	 * Returns all location tag attributes.
	 * 
	 * @return List containing all location tag attributes.
	 */
	public List<LocationTagAttribute> getAllLocationTagAttributes();
	
	/**
	 * Returns all form attributes.
	 * 
	 * @return List containing all form attributes.
	 */
	public List<FormAttribute> getAllFormAttributes();
	
	/**
	 * Returns all FormFields for a specified form with the specified field types.
	 * 
	 * @param form The form containing the form fields wanted.
	 * @param fieldTypes The fields with the field types to return.
	 * @param ordered If true, the form fields will be ordered by field number.
	 * 
	 * @return List containing FormField objects.
	 */
	public List<FormField> getFormFields(Form form, List<FieldType> fieldTypes, boolean ordered);
}
