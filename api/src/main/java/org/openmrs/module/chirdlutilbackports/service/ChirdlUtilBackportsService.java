package org.openmrs.module.chirdlutilbackports.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.openmrs.FieldType;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.Patient;
import org.openmrs.PersonAttribute;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Error;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ChirdlLocationAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ChirdlLocationAttributeValue;
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

/**
 * Defines services used by this module
 * 
 * @author Tammy Dugan
 */
public interface ChirdlUtilBackportsService {
	
	final static int OPERATION_SUCCESS=1;
	final static int OPERATION_FAIL=0;
	
	public LocationTagAttributeValue getLocationTagAttributeValue(Integer locationTagId, String locationTagAttributeName,
	                                                              Integer locationId);
	
	public ChirdlLocationAttributeValue getLocationAttributeValue(Integer locationId, String locationAttributeName);
	
	public LocationTagAttributeValue getLocationTagAttributeValueById(Integer location_tag_attribute_value_id);
	
	public LocationTagAttribute getLocationTagAttribute(Integer locationTagAttributeId);
	
	public LocationTagAttribute getLocationTagAttribute(String locationTagAttributeName);
	
	public LocationTagAttribute saveLocationTagAttribute(LocationTagAttribute value);
	
	public LocationTagAttributeValue saveLocationTagAttributeValue(LocationTagAttributeValue value);
	
	public ChirdlLocationAttributeValue saveLocationAttributeValue(ChirdlLocationAttributeValue value);
	
	public void deleteLocationTagAttribute(LocationTagAttribute value);
	
	public void deleteLocationTagAttributeValue(LocationTagAttributeValue value);
	
	public int deleteFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId, Integer locationId, String formAttributeValue);
	
	public int deleteFormAttributeValue(FormAttributeValue fav);
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
	
	public PatientState addPatientState(Patient patient,State initialState, int sessionId,Integer locationTagId,Integer locationId,FormInstance formInstance);
	
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
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state, boolean includeRetired);

	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean isRetired);

	public List<PatientState> getPatientStatesBySession(Integer sessionId,boolean isRetired);
	
	public FormAttributeValue getFormAttributeValue(Integer formId, String formAttributeName,
	                                    			Integer locationTagId,Integer locationId);
	
	public FormInstance addFormInstance(Integer formId, Integer locationId);
	
	public void unretireStatesBySessionId(Integer sessionId);
	
	/**
	 * save or update the FormAttributeValue object according to form id, form attribute id, location id, location tag id as in-fact primary key
	 * @param value
	 */
	public void saveFormAttributeValue(FormAttributeValue value);
	
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
	public int saveFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId, Integer locationId, String formAttributeValue);
	

	public FormAttribute getFormAttributeByName(String formAttributeName);
	
	/**
	 * Retrieves an observation attribute by observation attribute name.
	 * 
	 * @param obsAttributeName The observation attribute name used to do the lookup.
	 * @return ObsAttribute object or null if cannot be found matching the provided observation attribute name.
	 */
	public ObsAttribute getObsAttributeByName(String obsAttributeName);
	
	/**
	 * Retrieves a list of observation attribute values by observation attribute name.
	 * 
	 * @param attributeName The observation attribute name used to do the lookup.
	 * @return List of ObsAttributeValue objects or null if an error occurs.
	 */
	public List<ObsAttributeValue> getObsAttributesByName(String attributeName);
	
	/**
	 * Retrieves a list of observation attribute value names as strings by observation attribute name.
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
	 * @param value The value of the observation attribute value.
	 * @return List of ObsAttributeValue objects or null if an error occurs.
	 */
	public List<ObsAttributeValue> getObsAttributeValuesByValue(String value);

	/**
	 * Saves an observation attribute value.
	 * 
	 * @param value The observation attribute value to save.
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
	public ChirdlLocationAttribute getLocationAttribute(String locationAttributeName);
	
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
	public List<ChirdlLocationAttribute> getAllLocationAttributes();
	
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
	
	/**
	 * return all FormAttributes that are eligible to edit for administrators
	 * @return a list of FormAttributes 
	 */
	public List<FormAttribute> getAllEditableFormAttributes();
	
	/**
	 * For a given FormAttribute fa, get all the FormAttributeValues that are existed in database for fa
	 * @param fa
	 * @return a Set of FormAttributeValues that is existed in database for fa.
	 */
	public List<String> getCurrentFormAttributeValueStrCollection(FormAttribute fa);
	
	/**
	 * Retrieves a person attribute by value.
	 * 
	 * @param personAttributeTypeName The person attribute type.
	 * @param value The value to match.
	 * @return PersonAttribute object or null if a match cannot be found.
	 */
	public PersonAttribute getPersonAttributeByValue(String personAttributeTypeName, String value);
	
	/**
	 * Returns the value of a form attribute from the chirdlutilbackports_form_attribute_value table.
	 * 
	 * @param formId id of the form to find an attribute for
	 * @param formAttribute the form attribute to use to find the value
	 * @param locationTagId the location tag id
	 * @param locationId the location id
	 * @return FormAttributeValue value of the attribute for the given form
	 */
	public FormAttributeValue getFormAttributeValue(Integer formId, FormAttribute formAttributeId, Integer locationTagId, Integer locationId);
	
	/**
	 * Returns a list of Users with the provided Role.
	 * 
	 * @param role The Role used to filter the list of users.
	 * @param includeRetired If true, all users with the provided role will be returned.  If false, only non-retired users will be returned.
	 * @return List of User objects containing the provided role.
	 */
	public List<User> getUsersByRole(Role role, boolean includeRetired);
	
	/**
	 * Retrieves form attribute values
	 * 
	 * @param attributeId The attribute identifier.  This is a required parameter.
	 * @param locationId The location identifier.  This is an optional parameter.
	 * @param locationTagId The location tag identifier.  This is an optional parameter.
	 * @return List of FormAttributeValue objects
	 */
	public List<FormAttributeValue> getFormAttributeValues(Integer attributeId, Integer locationId, Integer locationTagId);

	/**
	 * DWE CHICA-334 3/27/15
	 * 
	 * Given a formId, return a list of form attribute values, which includes form name,
	 * location name, location tag name, attribute name, and attribute value
	 * 
	 * @param formId
	 * @return List containing FormAttributeValue objects
	 */
	public List<FormAttributeValue> getAllFormAttributeValuesByFormId(Integer formId);
	
	/**
	 * DWE CHICA-334 3/27/15
	 * 
	 * Given a formAttributeId, return a FormAttribute object
	 * 
	 * @param formAttributeId
	 * @return the FormAttribute object
	 */
	public FormAttribute getFormAttributeById(Integer formAttributeId);
	
	/**
	 * DWE CHICA-633
	 * Gets a EncounterAttribute
	 * @param encounterAttribute
	 */
	public EncounterAttribute getEncounterAttributeByName(String encounterAttributeName) throws HibernateException;
	
	/**
	 * DWE CHICA-633
	 * Saves or updates a EncounterAttributeValue
	 * @param encounterAttributeValue
	 */
	public EncounterAttributeValue saveEncounterAttributeValue(EncounterAttributeValue encounterAttributeValue) throws HibernateException;
	
	/**
	 * DWE CHICA-633
	 * Gets a EncounterAttributeValue for the encounterId and encounterAttributeName
	 * @param encounterId
	 * @param encounterAttributeName
	 * @return EncounterAttributeValue
	 */
	public EncounterAttributeValue getEncounterAttributeValueByName(Integer encounterId, String encounterAttributeName) throws HibernateException;
	
	/**
	 * DWE CHICA-633
	 * Gets a EncounterAttributeValue for the encounterId and encounterAttribute
	 * @param encounterId
	 * @param encounterAttribute
	 * @return EncounterAttributeValue
	 */
	public EncounterAttributeValue getEncounterAttributeValueByAttribute(Integer encounterId, EncounterAttribute encounterAttribute) throws HibernateException;
	
	/**
	 * DWE CHICA-761
	 * Get last patient state for all patients by location
	 * 
	 * @param optionalDateRestriction
	 * @param programId
	 * @param startStateName
	 * @param locationId
	 * @return
	 * @throws HibernateException
	 */
	public List<PatientState> getLastPatientStateAllPatientsByLocation(Date optionalDateRestriction, Integer programId, String startStateName, Integer locationId) throws HibernateException;
	
	/**
	 * DWE CHICA-761
	 * Get program by location
	 * NOTE: This requires that the same program is being used for all location tags for a location
	 * 
	 * @param locationId
	 * @return
	 * @throws HibernateException
	 */
	public Program getProgramByLocation(Integer locationId) throws HibernateException;
	
	/**
	 * DWE CHICA-784
	 * Get the encounterAttributeValue by value_text and encounter attribute name
	 * 
	 * @param attributeValue
	 * @param encounterAttributeName
	 * @return
	 * @throws HibernateException
	 */
	public EncounterAttributeValue getEncounterAttributeValueByValue(String attributeValue, String encounterAttributeName) throws HibernateException;
	
	/**
	 * CHICA-862
	 * Get the patient states by session id
	 * @param sessionId
	 * @param stateNames
	 * @param retired
	 * @return Map containing patient states
	 * @throws HibernateException
	 */
	
	public Map<String, List<PatientState>> getPatientStatesBySessionId(Integer sessionId, List<String> stateNames, boolean retired) throws HibernateException;
}