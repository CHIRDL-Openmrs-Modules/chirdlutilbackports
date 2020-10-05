package org.openmrs.module.chirdlutilbackports.service;

import java.util.ArrayList;
import java.util.Date;
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
import org.openmrs.annotation.Authorized;
import org.openmrs.api.APIException;
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
import org.openmrs.util.PrivilegeConstants;

/**
 * Defines services used by this module
 * 
 * @author Tammy Dugan
 */
public interface ChirdlUtilBackportsService {
	
	final static int OPERATION_SUCCESS=1;
	final static int OPERATION_FAIL=0;
	
	@Authorized()
	public LocationTagAttributeValue getLocationTagAttributeValue(Integer locationTagId, String locationTagAttributeName,
	                                                              Integer locationId);
	
	@Authorized(PrivilegeConstants.GET_LOCATIONS)
	public ChirdlLocationAttributeValue getLocationAttributeValue(Integer locationId, String locationAttributeName);
	
	@Authorized()
	public LocationTagAttributeValue getLocationTagAttributeValueById(Integer location_tag_attribute_value_id);
	
	@Authorized()
	public LocationTagAttribute getLocationTagAttribute(Integer locationTagAttributeId);
	
	@Authorized()
	public LocationTagAttribute getLocationTagAttribute(String locationTagAttributeName);
	
	@Authorized()
	public LocationTagAttribute saveLocationTagAttribute(LocationTagAttribute value);
	
	@Authorized()
	public LocationTagAttributeValue saveLocationTagAttributeValue(LocationTagAttributeValue value);
	
	@Authorized()
	public ChirdlLocationAttributeValue saveLocationAttributeValue(ChirdlLocationAttributeValue value);
	
	@Authorized()
	public void deleteLocationTagAttribute(LocationTagAttribute value);
	
	@Authorized()
	public void deleteLocationTagAttributeValue(LocationTagAttributeValue value);
	
	@Authorized()
	public int deleteFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId, Integer locationId, String formAttributeValue);
	
	@Authorized()
	public int deleteFormAttributeValue(FormAttributeValue fav);
	
	/**
	 * Get state by state name
	 * 
	 * @param initialState
	 * 
	 * @param stateMappingId state name
	 * @return state with given state name
	 */
	@Authorized()
	public StateMapping getStateMapping(State initialState,Program program);
	
	@Authorized()
	public Session addSession();
	
	@Authorized()
	public Session updateSession(Session session);
	
	@Authorized()
	public Session getSession(int sessionId);
	
	@Authorized()
	public PatientState addPatientState(Patient patient,State initialState, int sessionId,Integer locationTagId,Integer locationId,FormInstance formInstance);
	
	@Authorized()
	public PatientState updatePatientState(PatientState patientState);

	@Authorized()
	public PatientState getPrevPatientStateByAction(
			int sessionId, int patientStateId,String action);
	
	@Authorized()
	public List<PatientState> getPatientStatesWithForm(int sessionId);
	
	@Authorized()
	public List<PatientState> getUnfinishedPatientStatesAllPatients(Date optionalDateRestriction,Integer locationTagId,Integer locationId);
	
	@Authorized()
	public List<PatientState> getUnfinishedPatientStateByStateName(String state,Date optionalDateRestriction,Integer locationTagId,Integer locationId);
	
	@Authorized()
	public PatientState getLastUnfinishedPatientState(Integer sessionId);

	@Authorized()
	public PatientState getLastPatientState(Integer sessionId);
	
	@Authorized()
	public List<PatientState> getLastPatientStateAllPatients(Date optionalDateRestriction, Integer programId,
			String startStateName, Integer locationTagId, Integer locationId);
	
	@Authorized()
	public State getStateByName(String stateName);
	
	@Authorized()
	public Program getProgramByNameVersion(String name,String version);
	
	@Authorized()
	public Program getProgram(Integer programId);
	
	@Authorized()
	public PatientState getPatientStateByEncounterFormAction(Integer encounterId, Integer formId, String action);

	@Authorized()
	public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance,String action);
	
	@Authorized()
    public PatientState getPatientStateByFormInstanceAction(FormInstance formInstance,String action, boolean includeRetired);

	@Authorized()
	public List<FormAttributeValue> getFormAttributesByName(String attributeName);
	
	@Authorized()
	public ArrayList<String> getFormAttributesByNameAsString(String attributeName);
	
	@Authorized()
	public List<State> getStatesByActionName(String actionName);
	
	@Authorized()
	public State getState(Integer stateId);

	@Authorized()
	public PatientState getPatientState(Integer patientStateId);
	
	@Authorized()
	public List<PatientState> getPatientStateBySessionState(Integer sessionId,
			Integer stateId);
	
	@Authorized()
	public List<PatientState> getAllRetiredPatientStatesWithForm(Date thresholdDate);
	
	@Authorized()
	public List<Session> getSessionsByEncounter(Integer encounterId);
	
	@Authorized()
	public List<PatientState> getPatientStatesWithFormInstances(String formName, Integer encounterId);
	
	@Authorized()
	public List<PatientState> getPatientStateByEncounterState(Integer encounterId,
			Integer stateId);
	
	@Authorized()
	public void saveError(Error error);
	
	@Authorized()
	public List<Error> getErrorsByLevel(String errorLevel,Integer sessionId);
	
	@Authorized()
	public Integer getErrorCategoryIdByName(String name);
	
	@Authorized()
	public Program getProgram(Integer locationTagId,Integer locationId);
	
	@Authorized()
	public List<FormAttributeValue> getFormAttributeValuesByValue(String value);

	@Authorized()
	public List<PatientState> getUnfinishedPatientStateByStateSession(
		String stateName,Integer sessionId);
	
	@Authorized()
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state);
	
	@Authorized()
	public List<PatientState> getPatientStateByFormInstanceState(FormInstance formInstance, State state, boolean includeRetired);

	@Authorized()
	public List<PatientState> getPatientStatesByFormInstance(FormInstance formInstance, boolean includeRetired);

	@Authorized()
	public List<PatientState> getPatientStatesBySession(Integer sessionId,boolean isRetired);
	
	@Authorized()
	public FormAttributeValue getFormAttributeValue(Integer formId, String formAttributeName,
	                                    			Integer locationTagId,Integer locationId);
	
	@Authorized()
	public FormInstance addFormInstance(Integer formId, Integer locationId);
	
	@Authorized()
	public void unretireStatesBySessionId(Integer sessionId);
	
	/**
	 * save or update the FormAttributeValue object according to form id, form attribute id, location id, location tag id as in-fact primary key
	 * @param value
	 */
	@Authorized()
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
	@Authorized()
	public int saveFormAttributeValue(Integer formId, String formAttributeName, Integer locationTagId, Integer locationId, String formAttributeValue);
	
	@Authorized()
	public FormAttribute getFormAttributeByName(String formAttributeName);
	
	/**
	 * Retrieves an observation attribute by observation attribute name.
	 * 
	 * @param obsAttributeName The observation attribute name used to do the lookup.
	 * @return ObsAttribute object or null if cannot be found matching the provided observation attribute name.
	 */
	@Authorized()
	public ObsAttribute getObsAttributeByName(String obsAttributeName);
	
	/**
	 * Retrieves a list of observation attribute values by observation attribute name.
	 * 
	 * @param attributeName The observation attribute name used to do the lookup.
	 * @return List of ObsAttributeValue objects or null if an error occurs.
	 */
	@Authorized()
	public List<ObsAttributeValue> getObsAttributesByName(String attributeName);
	
	/**
	 * Retrieves a list of observation attribute value names as strings by observation attribute name.
	 * 
	 * @param attributeName The observation attribute name used to do the lookup.
	 * @return List of String objects or null if an error occurs.
	 */
	@Authorized()
	public List<String> getObsAttributesByNameAsString(String attributeName);
	
	/**
	 * Retrieves an observation attribute value.
	 * 
	 * @param obsId The observation ID.
	 * @param obsAttributeName The observation attribute name.
	 * @return ObsAttributeValue object or null if a match cannot be found.
	 */
	@Authorized()
	public ObsAttributeValue getObsAttributeValue(Integer obsId, String obsAttributeName);
	
	/**
	 * Retrieves a list of observation attribute values.
	 * 
	 * @param value The value of the observation attribute value.
	 * @return List of ObsAttributeValue objects or null if an error occurs.
	 */
	@Authorized()
	public List<ObsAttributeValue> getObsAttributeValuesByValue(String value);

	/**
	 * Saves an observation attribute value.
	 * 
	 * @param value The observation attribute value to save.
	 */
	@Authorized()
	public void saveObsAttributeValue(ObsAttributeValue value);
	
	/**
	 * Retrieves a form instance attribute by form instance attribute name.
	 * 
	 * @param formInstanceAttributeName The form instance attribute name used to do the lookup.
	 * @return FormInstanceAttribute object or null if cannot be found matching the provided form instance attribute name.
	 */
	@Authorized()
	public FormInstanceAttribute getFormInstanceAttributeByName(String formInstanceAttributeName);
	
	/**
	 * Retrieves a list of form instance attribute values by form instance attribute name.
	 * 
	 * @param attributeName The form instance attribute name used to do the lookup.
	 * @return List of FormInstanceAttributeValue objects or null if an error occurs.
	 */
	@Authorized()
	public List<FormInstanceAttributeValue> getFormInstanceAttributesByName(String attributeName);
	
	/**
	 * Retrieves a list of form instance attribute value names as strings by form instance attribute name.
	 * 
	 * @param attributeName The form instance attribute name used to do the lookup.
	 * @return List of String objects or null if an error occurs.
	 */
	@Authorized()
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
	@Authorized()
	public FormInstanceAttributeValue getFormInstanceAttributeValue(Integer formId, Integer formInstanceId, 
	                                                                Integer locationId, String formInstanceAttributeName);
	
	/**
	 * Retrieves a list of form instance attribute values.
	 * 
	 * @param value The value of the form instance attribute value.
	 * @return List of FormInstanceAttributeValue objects or null if an error occurs.
	 */
	@Authorized()
	public List<FormInstanceAttributeValue> getFormInstanceAttributeValuesByValue(String value);

	/**
	 * Saves a form instance attribute value.
	 * 
	 * @param value The form instance attribute value to save.
	 */
	@Authorized()
	public void saveFormInstanceAttributeValue(FormInstanceAttributeValue value);
	
	/**
	 * Returns all available programs.
	 * 
	 * @return List of Program objects.
	 */
	@Authorized()
	public List<Program> getAllPrograms();
	
	/**
	 * Returns a location attribute by name.  This will return null if one is not found by the name provided.
	 * 
	 * @param locationAttributeName
	 * @return LocationAttribute object
	 */
	@Authorized()
	public ChirdlLocationAttribute getLocationAttribute(String locationAttributeName);
	
	/**
	 * Returns a program by name.
	 * 
	 * @param name The name of the program.
	 * @return Program by the name specified or null if one is not found by that name.
	 */
	@Authorized()
	public Program getProgram(String name);
	
	/**
	 * Saves a new program or updates an existing program.
	 * 
	 * @param program The program to save or update.
	 * @return The updated program.
	 */
	@Authorized()
	public Program saveProgram(Program program);
	
	/**
	 * Deletes a program.
	 * 
	 * @param program The program to delete.
	 */
	@Authorized()
	public void deleteProgram(Program program);
	
	/**
	 * Saves a new program tag map or updates an existing program tag map.
	 * 
	 * @param programTagMap The program tag map to save or update.
	 * @return The updated program tag map.
	 */
	@Authorized()
	public ProgramTagMap saveProgramTagMap(ProgramTagMap programTagMap);
	
	/**
	 * Deletes a program tag map.
	 * 
	 * @param programTagMap The program tag map to delete.
	 */
	@Authorized()
	public void deleteProgramTagMap(ProgramTagMap programTagMap);
	
	/**
	 * Returns all location attributes.
	 * 
	 * @return List containing all location attributes.
	 */
	@Authorized()
	public List<ChirdlLocationAttribute> getAllLocationAttributes();
	
	/**
	 * Returns all location tag attributes.
	 * 
	 * @return List containing all location tag attributes.
	 */
	@Authorized()
	public List<LocationTagAttribute> getAllLocationTagAttributes();
	
	/**
	 * Returns all form attributes.
	 * 
	 * @return List containing all form attributes.
	 */
	@Authorized()
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
	@Authorized()
	public List<FormField> getFormFields(Form form, List<FieldType> fieldTypes, boolean ordered);
	
	/**
	 * return all FormAttributes that are eligible to edit for administrators
	 * @return a list of FormAttributes 
	 */
	@Authorized()
	public List<FormAttribute> getAllEditableFormAttributes();
	
	/**
	 * For a given FormAttribute fa, get all the FormAttributeValues that are existed in database for fa
	 * @param fa
	 * @return a Set of FormAttributeValues that is existed in database for fa.
	 */
	@Authorized()
	public List<String> getCurrentFormAttributeValueStrCollection(FormAttribute fa);
	
	/**
	 * Retrieves a person attribute by value.
	 * 
	 * @param personAttributeTypeName The person attribute type.
	 * @param value The value to match.
	 * @return PersonAttribute object or null if a match cannot be found.
	 */
	@Authorized()
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
	@Authorized()
	public FormAttributeValue getFormAttributeValue(Integer formId, FormAttribute formAttributeId, Integer locationTagId, Integer locationId);
	
	/**
	 * Returns a list of Users with the provided Role.
	 * 
	 * @param role The Role used to filter the list of users.
	 * @param includeRetired If true, all users with the provided role will be returned.  If false, only non-retired users will be returned.
	 * @return List of User objects containing the provided role.
	 */
	@Authorized()
	public List<User> getUsersByRole(Role role, boolean includeRetired);
	
	/**
	 * Retrieves form attribute values
	 * 
	 * @param attributeId The attribute identifier.  This is a required parameter.
	 * @param locationId The location identifier.  This is an optional parameter.
	 * @param locationTagId The location tag identifier.  This is an optional parameter.
	 * @return List of FormAttributeValue objects
	 */
	@Authorized()
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
	@Authorized()
	public List<FormAttributeValue> getAllFormAttributeValuesByFormId(Integer formId);
	
	/**
	 * DWE CHICA-334 3/27/15
	 * 
	 * Given a formAttributeId, return a FormAttribute object
	 * 
	 * @param formAttributeId
	 * @return the FormAttribute object
	 */
	@Authorized()
	public FormAttribute getFormAttributeById(Integer formAttributeId);
	
	/**
	 * DWE CHICA-633
	 * Gets a EncounterAttribute
	 * @param encounterAttribute
	 */
	@Authorized()
	public EncounterAttribute getEncounterAttributeByName(String encounterAttributeName) throws HibernateException;
	
	/**
	 * DWE CHICA-633
	 * Saves or updates a EncounterAttributeValue
	 * @param encounterAttributeValue
	 */
	@Authorized()
	public EncounterAttributeValue saveEncounterAttributeValue(EncounterAttributeValue encounterAttributeValue) throws HibernateException;
	
	/**
	 * DWE CHICA-633
	 * Gets a EncounterAttributeValue for the encounterId and encounterAttributeName
	 * @param encounterId
	 * @param encounterAttributeName
	 * @return EncounterAttributeValue
	 */
	@Authorized()
	public EncounterAttributeValue getEncounterAttributeValueByName(Integer encounterId, String encounterAttributeName) throws HibernateException;
	
	/**
	 * DWE CHICA-633
	 * Gets a EncounterAttributeValue for the encounterId and encounterAttribute
	 * @param encounterId
	 * @param encounterAttribute
	 * @param includeVoided
	 * @return EncounterAttributeValue
	 */
	@Authorized()
	public EncounterAttributeValue getEncounterAttributeValueByAttribute(Integer encounterId, EncounterAttribute encounterAttribute, boolean includeVoided) throws HibernateException;
	
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
	@Authorized()
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
	@Authorized()
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
	@Authorized()
	public EncounterAttributeValue getEncounterAttributeValueByValue(String attributeValue, String encounterAttributeName) throws HibernateException;
	
	/**
	 * CHICA-862
	 * Get the patient states by session id
	 * @param sessionId
	 * @param stateNames
	 * @param includeRetired
	 * @return Map containing patient states
	 * @throws HibernateException
	 */
	@Authorized()
	public Map<String, List<PatientState>> getPatientStatesBySessionId(Integer sessionId, List<String> stateNames, boolean includeRetired) throws HibernateException;
	
	/**
	 * CHICA-993 
	 * Deletes LocationTagAttributeValue records by LocationTagAttribute and the value passed in
	 * @param locationTagAttribute - the LocationTagAttribute
	 * @param value - the value, such as a formId
	 */
	@Authorized()
	public void deleteLocationTagAttributeValueByValue(LocationTagAttribute locationTagAttribute, String value);
	
	/**
	 * CHICA-1169
	 * Get a list of PatientState objects by encounter, formName, and stateNames
	 * @param formName
	 * @param stateNames
	 * @param encounterId
	 * @param includeRetired
	 * @return
	 * @throws APIException
	 */
	@Authorized()
	public List<PatientState> getPatientStatesByFormNameAndState(String formName, List<String> stateNames, Integer encounterId, boolean includeRetired) throws APIException;
}
