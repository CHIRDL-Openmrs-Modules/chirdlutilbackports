package org.openmrs.module.chirdlutilbackports;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.FieldType;
import org.openmrs.Form;
import org.openmrs.FormField;
import org.openmrs.PersonAttribute;
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.FormService;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ChirdlLocationAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;

public class TestChirdlUtilBackportsService extends BaseModuleContextSensitiveTest {
    
    private static final String FORM_NAME_PSF = "PSF";
    private static final String FORM_NAME_PWS = "PWS";
    
    private static final String STATE_PSF_PROCESS = "PSF_process";
    private static final String STATE_PWS_PROCESS = "PWS_process";
    private static final String STATE_PSF_WAIT_FOR_SUBMISSION = "PSF WAIT FOR ELECTRONIC SUBMISSION";
    private static final String STATE_TEST = "TEST STATE";
    
    private static final String FIELD_TYPE_MERGE_FIELD = "Merge Field";
    
    private static final String PERSON_ATTRIBUTE_TYPE_TEST = "TestPersonAttributeType";
    private static final String PERSON_ATTRIBUTE_TYPE_DOES_NOT_EXIST = "DoesNotExist";
    private static final String PERSON_ATTRIBUTE_VALUE_12344 = "1234-4";
    
    private static final String ROLE_SYSTEM_DEVELOPER = "System Developer";
    
    private static final String FORM_ATTRIBUTE_TEST = "TestFormAttribute";
    
    private static final String ENCOUNTER_ATTRIBUTE_TEST = "TestEncounterAttribute";
    private static final String ENCOUNTER_ATTRIBUTE_TEST_2 = "TestEncounterAttribute2";
    private static final String ENCOUNTER_ATTRIBUTE_VALUE_TEST_23189 = "EncounterAttributeValueFor_23189";
    private static final String ENCOUNTER_ATTRIBUTE_VALUE_TEST_23190 = "EncounterAttributeValueFor_23190";
    
    private static final String LOCATION_ATTRIBUTE_TEST = "TestLocationAttribute";
    private static final String LOCATION_TAG_ATTRIBUTE_TEST = "TestLocationTagAttribute";
    private static final String LOCATION_TAG_ATTRIBUTE_VALUE_TEST = "TestLocationTagAttributeValue";
    
    private static final String VOID_REASON_TESTING = "Testing";
    
    private static final String PROGRAM_NAME_TEST_PROGRAM = "test_program";
    
    private static final String GENERAL_TEST_STRING = "Test";
    
    private static final Integer ENCOUNTER_ID_TEST_23189 = new Integer(23189);
    private static final Integer ENCOUNTER_ID_TEST_23190 = new Integer(23190);
    private static final Integer ENCOUNTER_ID_TEST_23191 = new Integer(23191);
    private static final Integer ENCOUNTER_ID_TEST_23192 = new Integer(23192);
    private static final Integer SESSION_ID_TEST_23190 = new Integer(23190);
    private static final Integer FORM_ID_TEST_8970 = new Integer(8970);
    private static final Integer FORM_ATTRIBUTE_ID_TEST_1 = new Integer(1);
    private static final Integer LOCATION_ID_TEST_9999 = new Integer(9999);
    private static final Integer LOCATION_ID_TEST_8992 = new Integer(8992);
    private static final Integer LOCATION_ID_TEST_8993 = new Integer(8993);
    private static final Integer LOCATION_TAG_ID_TEST_2987 = new Integer(2987);
    private static final Integer LOCATION_TAG_ID_TEST_2988 = new Integer(2988);
    private static final Integer LOCATION_TAG_ID_TEST_2989 = new Integer(2989);
    private static final Integer PROGRAM_ID_TEST_1 = new Integer(1);
    private static final Integer PROGRAM_TAG_MAP_ID_TEST_1 = new Integer(1);
    
    @Before
    public void runBeforeEachTest() throws Exception{
        // create the basic user and give it full rights
        initializeInMemoryDatabase();
        
        executeDataSet("dbunitFiles/patientStates.xml");
        authenticate(); 
    }
    
	/**
	 * Test to make sure that all service methods have the Authorized annotation
	 * @throws Exception
	 */
	@Test
	@SkipBaseSetup
	public void checkAuthorizationAnnotations() throws Exception {
		Method[] allMethods = ChirdlUtilBackportsService.class.getDeclaredMethods();
		for (Method method : allMethods) {
		    if (Modifier.isPublic(method.getModifiers())) {
		        Authorized authorized = method.getAnnotation(Authorized.class);
		        Assert.assertNotNull("Authorized annotation not found on method " + method.getName(), authorized);
		    }
		}
	}
	
	@Test
	public void testGetPatientStatesByFormNameAndState() throws Exception {
		ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
		ArrayList<String> stateNames = new ArrayList<>();
		List<PatientState> patientStates = null;
		
		// Look for scanned paper PSF and PWS
		stateNames.add(STATE_PSF_PROCESS);
		patientStates = chirdlUtilBackportsService.getPatientStatesByFormNameAndState(FORM_NAME_PSF, stateNames, ENCOUNTER_ID_TEST_23189, false);
		Assert.assertEquals("Number of patient state rows did not match", 1, patientStates.size());
		patientStates.clear();
		stateNames.clear();
		
		stateNames.add(STATE_PWS_PROCESS);
		patientStates = chirdlUtilBackportsService.getPatientStatesByFormNameAndState(FORM_NAME_PWS, stateNames, ENCOUNTER_ID_TEST_23189, false);
		Assert.assertEquals("Number of patient state rows did not match", 1, patientStates.size());
		patientStates.clear();
		stateNames.clear();
		
		// Look for submitted electronic PSF and PWS - NOTE: PWS_process is used for both paper and electronic
		stateNames.add(STATE_PSF_WAIT_FOR_SUBMISSION);
		patientStates = chirdlUtilBackportsService.getPatientStatesByFormNameAndState(FORM_NAME_PSF, stateNames, ENCOUNTER_ID_TEST_23190, false);
		Assert.assertEquals("Number of patient state rows did not match", 1, patientStates.size());
		patientStates.clear();
		stateNames.clear();
		
		stateNames.add(STATE_PWS_PROCESS);
		patientStates = chirdlUtilBackportsService.getPatientStatesByFormNameAndState(FORM_NAME_PWS, stateNames, ENCOUNTER_ID_TEST_23190, false);
		Assert.assertEquals("Number of patient state rows did not match", 1, patientStates.size());
		patientStates.clear();
		stateNames.clear();	
	}
	
	/**
	 * Tests getting a location attribute by name
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getLocationAttribute(String)
	 * @throws Exception
	 */
	@Test
	public void should_getLocationAttribute() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    ChirdlLocationAttribute locationAttribute = chirdlUtilBackportsService.getLocationAttribute(LOCATION_ATTRIBUTE_TEST);
	    Assert.assertNotNull("Location attribute was not found.", locationAttribute);
	    
	    locationAttribute = chirdlUtilBackportsService.getLocationAttribute(GENERAL_TEST_STRING);
	    Assert.assertNull("Location attribute was found, but was not expected.", locationAttribute);
	}
	
	/**
	 * Tests getting a program by name
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getProgram(String)
	 * @throws Exception
	 */
	@Test
	public void should_getProgram() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        Program program = chirdlUtilBackportsService.getProgram(PROGRAM_NAME_TEST_PROGRAM);
        Assert.assertNotNull("Unable to get program by name.", program);
	}
	
	/**
	 * Tests saving a new ProgramTagMap
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveProgramTagMap(ProgramTagMap)
	 * @throws Exception
	 */
	@Test
	public void should_saveProgramTagMap() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    Program program = chirdlUtilBackportsService.getProgram(PROGRAM_NAME_TEST_PROGRAM);
        ProgramTagMap programTagMap = new ProgramTagMap();
        programTagMap.setProgram(program);
        programTagMap.setLocationId(LOCATION_ID_TEST_8993);
        programTagMap.setLocationTagId(LOCATION_TAG_ID_TEST_2989);
        
        programTagMap = chirdlUtilBackportsService.saveProgramTagMap(programTagMap);
        Assert.assertNotNull("Unable to save new ProgramTagMap", programTagMap.getProgramTagMapId());
	}
	
	/**
	 * Tests deleting a ProgramTagMap
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#deleteProgramTagMap(org.openmrs.module.chirdlutilbackports.hibernateBeans.ProgramTagMap)
	 * @throws Exception
	 */
	@Test
	public void should_deleteProgramTagMap() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    ProgramTagMap programTagMap = new ProgramTagMap();
	    programTagMap.setProgramTagMapId(PROGRAM_TAG_MAP_ID_TEST_1);
	    programTagMap.setProgramId(PROGRAM_ID_TEST_1);
	    programTagMap.setLocationId(LOCATION_ID_TEST_8992);
	    programTagMap.setLocationTagId(LOCATION_TAG_ID_TEST_2987);
	    
	    try{
	        chirdlUtilBackportsService.deleteProgramTagMap(programTagMap);
	    }catch(Exception e){
	        Assert.assertFalse("Unable to delete ProgramTagMap", true);
	    }   
	}
	
	/**
	 * Tests getting a list of all location tag attributes
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllLocationAttributes()
	 * @throws Exception
	 */
	@Test
	public void should_getAllLocationAttributes() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    List<ChirdlLocationAttribute> locationAttributes = chirdlUtilBackportsService.getAllLocationAttributes();
	    Assert.assertEquals("Number of location attribute rows did not match.", 1, locationAttributes.size());
	}
	
	/**
	 * Tests getting a list of all location tag attributes
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllLocationTagAttributes()
	 * @throws Exception
	 */
	@Test
	public void should_getAllLocationTagAttributes() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    List<LocationTagAttribute> locationTagAttributes = chirdlUtilBackportsService.getAllLocationTagAttributes();
	    Assert.assertNotNull("Location tag attributes were not found.", locationTagAttributes);
	}
	
	/**
	 * Tests getting a list of all form attributes
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllFormAttributes()
	 * @throws Exception
	 */
	@Test
	public void should_getAllFormAttributes() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        List<FormAttribute> formAttributes = chirdlUtilBackportsService.getAllFormAttributes();
        Assert.assertEquals("Number of form attribute rows did not match.", 2, formAttributes.size());
	}
	
	/**
	 * Tests getting a list of form fields for a form by field type
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormFields(org.openmrs.Form, List, boolean)
	 * @throws Exception
	 */
	@Test
	public void should_getFormFields() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    FormService formService = Context.getFormService();
	    Form form = formService.getForm(FORM_ID_TEST_8970);
	    FieldType mergeFieldType = formService.getFieldTypeByName(FIELD_TYPE_MERGE_FIELD);
	    
	    List<FieldType> fieldTypes = new ArrayList<>();
	    fieldTypes.add(mergeFieldType);
	    
	    List<FormField> formFields = chirdlUtilBackportsService.getFormFields(form, fieldTypes, true);
	    Assert.assertEquals("Number of form field rows did not match.", 3, formFields.size());
	    
	    formFields = chirdlUtilBackportsService.getFormFields(form, null, true);
	    Assert.assertEquals("Number of form field rows did not match.", 4, formFields.size());
	}
	
	/**
	 * Test getting a list of all form attributes
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllEditableFormAttributes()
	 * @throws Exception
	 */
	@Test
	public void should_getAllEditableFormAttributes() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    List<FormAttribute> formAttributes = chirdlUtilBackportsService.getAllEditableFormAttributes();
	    Assert.assertEquals("Number of form attribute rows did not match.", 2, formAttributes.size());
	}
	
	/**
	 * Tests getting a list of form attribute value strings by form attribute
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getCurrentFormAttributeValueStrCollection(FormAttribute)
	 * @throws Exception
	 */
	@Test
	public void should_getCurrentFormAttributeValueStrCollection() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    FormAttribute formAttribute = chirdlUtilBackportsService.getFormAttributeById(FORM_ATTRIBUTE_ID_TEST_1);
	    List<String> formAttributeValueStrings = chirdlUtilBackportsService.getCurrentFormAttributeValueStrCollection(formAttribute);
	    Assert.assertEquals("Number of form attribute value rows did not match.", 1, formAttributeValueStrings.size());
	    
	    formAttributeValueStrings.clear();
        formAttributeValueStrings = chirdlUtilBackportsService.getCurrentFormAttributeValueStrCollection(null);
        Assert.assertNull("Form attribute values were found, but not expected.", formAttributeValueStrings);
	}
	
	/**
	 * Tests getting a PersonAttribute by value
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getPersonAttributeByValue(String, String)
	 * @throws Exception
	 */
	@Test
	public void should_getPersonAttributeByValue() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    PersonAttribute personAttribute = chirdlUtilBackportsService.getPersonAttributeByValue(PERSON_ATTRIBUTE_TYPE_TEST, PERSON_ATTRIBUTE_VALUE_12344);
	    Assert.assertNotNull("Person attribute value was not found.", personAttribute);
	    
	    personAttribute = chirdlUtilBackportsService.getPersonAttributeByValue(PERSON_ATTRIBUTE_TYPE_DOES_NOT_EXIST, PERSON_ATTRIBUTE_VALUE_12344);
	    Assert.assertNull("Person attribute was found but was not expected.", personAttribute);
	    
	    personAttribute = chirdlUtilBackportsService.getPersonAttributeByValue(PERSON_ATTRIBUTE_TYPE_TEST, null);
	    Assert.assertNull("Person attribute was found but was not expected.", personAttribute);  
	    
	    personAttribute = chirdlUtilBackportsService.getPersonAttributeByValue(null, null);
        Assert.assertNull("Person attribute was found but was not expected.", personAttribute);  
	}
	
	/**
	 * Tests getting form attribute value by form id, form attribute, location tag id, and location id
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormAttributeValue(Integer, FormAttribute, Integer, Integer)
	 * @throws Exception
	 */
	@Test
	public void should_getFormAttributeValue() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    FormAttribute formAttribute = chirdlUtilBackportsService.getFormAttributeById(FORM_ATTRIBUTE_ID_TEST_1);
	    FormAttributeValue formAttributeValue = chirdlUtilBackportsService.getFormAttributeValue(FORM_ID_TEST_8970, formAttribute, LOCATION_TAG_ID_TEST_2987, LOCATION_ID_TEST_8992);
	    Assert.assertNotNull("Form attribute value not found.", formAttributeValue);
	}
	
	/**
	 * Tests getting a list of users by role
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getUsersByRole(org.openmrs.Role, boolean)
	 * @throws Exception
	 */
	@Test
	public void should_getUsersByRole() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    UserService userService = Context.getUserService();
	    Role role = userService.getRole(ROLE_SYSTEM_DEVELOPER);
	    List<User> users = chirdlUtilBackportsService.getUsersByRole(role, false);
	    
	    Assert.assertNotNull("Number of user rows was not greater than 0.", users.size() > 0);
	    
	    users = chirdlUtilBackportsService.getUsersByRole(role, true);
	    Assert.assertNotNull("Number of user rows was not greater than 0.", users.size() > 0);
	}
	
	/**
	 * Tests getting form attribute values by form attribute id, location id, and location tag id
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormAttributeValues(Integer, Integer, Integer)
	 * @throws Exception
	 */
	@Test
	public void should_getFormAttributeValues() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    List<FormAttributeValue> formAttributeValues = chirdlUtilBackportsService.getFormAttributeValues(FORM_ATTRIBUTE_ID_TEST_1, LOCATION_ID_TEST_8992, LOCATION_TAG_ID_TEST_2987);
	    Assert.assertEquals("Number of form attribute value rows did not match.", 1, formAttributeValues.size());
	}
	
	/**
	 * Tests getting all form attribute values by form id
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getAllFormAttributeValuesByFormId(Integer)
	 * @throws Exception
	 */
	@Test
	public void should_getAllFormAttributeValuesByFormId() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    List<FormAttributeValue> formAttributeValues = chirdlUtilBackportsService.getAllFormAttributeValuesByFormId(FORM_ID_TEST_8970);
	    Assert.assertEquals("Number of form attribute value rows did not match.", 2, formAttributeValues.size());
	}
	
	/**
	 * Tests getting a form attribute by id
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormAttributeById(Integer)
	 * @throws Exception
	 */
	@Test
	public void should_getFormAttributeById() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    FormAttribute attribute = chirdlUtilBackportsService.getFormAttributeById(FORM_ATTRIBUTE_ID_TEST_1);
	    Assert.assertEquals("Form attribute name did not match the expected form attribute name.", attribute.getName(), FORM_ATTRIBUTE_TEST);
	}
	
	/**
	 * Tests saving an encounter attribute value
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveEncounterAttributeValue(EncounterAttributeValue)
	 * @throws Exception
	 */
	@Test
	public void should_saveEncounterAttributeValue() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    EncounterAttribute encounterAttribute = chirdlUtilBackportsService.getEncounterAttributeByName(ENCOUNTER_ATTRIBUTE_TEST);
	    EncounterAttributeValue encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(ENCOUNTER_ID_TEST_23192, encounterAttribute, false);
	    
	    Assert.assertNotNull("Encounter attribute value was not found.", encounterAttributeValue);
	    
	    // void and save
	    encounterAttributeValue.setVoided(true);
        encounterAttributeValue.setVoidedBy(Context.getAuthenticatedUser());
        encounterAttributeValue.setVoidReason(VOID_REASON_TESTING);
        encounterAttributeValue.setDateVoided(new Date());
        
        chirdlUtilBackportsService.saveEncounterAttributeValue(encounterAttributeValue);
        
        // Should no longer find the attribute since it has been voided
        encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(ENCOUNTER_ID_TEST_23192, encounterAttribute, false);
        Assert.assertNull("Encounter attribute value should not have been found because it should be voided.", encounterAttributeValue);
	}
	
	/**
	 * Tests getting chirdlutilbackports_encounter_attribute by name
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeByName(String)
	 * @throws Exception
	 */
	@Test
	public void should_getEncounterAttributeByName() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    EncounterAttribute encounterAttribute = chirdlUtilBackportsService.getEncounterAttributeByName(ENCOUNTER_ATTRIBUTE_TEST);
	    Assert.assertNotNull("TestEncounterAttribute was not found.", encounterAttribute);
	    
	    // This one should not exist
	    encounterAttribute = chirdlUtilBackportsService.getEncounterAttributeByName(ENCOUNTER_ATTRIBUTE_TEST_2);
	    Assert.assertNull("TestEncounterAttribute2 was found but should not exist.", encounterAttribute);
	}
	
	/**
	 * Tests getting chirdlutilbackports_encounter_attribute_value by encounter attribute name
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeValueByName(Integer, String)
	 * @throws Exception
	 */
	@Test
	public void should_getEncounterAttributeValueByName() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    EncounterAttributeValue encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByName(ENCOUNTER_ID_TEST_23189, ENCOUNTER_ATTRIBUTE_TEST);
	    Assert.assertNotNull("Encounter attribute value was not found.", encounterAttributeValue);
	    
	    // This encounter has 2 encounter attribute value records and should cause a hibernate exception due to non-unique result
	    boolean threwException = false;
	    try{
	        encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByName(ENCOUNTER_ID_TEST_23191, ENCOUNTER_ATTRIBUTE_TEST);
	    }catch(HibernateException e){
	        // We are expecting this exception to be generated because the result is non-unique
	        threwException = true;
	    }
	    
	    Assert.assertTrue("Method getEncounterAttributeValueByName did not throw an exception as expected.", threwException);   
	}
	
	/**
	 * Tests getting chirdlutilbackports_encounter_attribute_value by encounter attribute
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeValueByAttribute(Integer, org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttribute, boolean)
	 * @throws Exception
	 */
	@Test
	public void should_getEncounterAttributeValueByAttribute() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    EncounterAttribute encounterAttribute = chirdlUtilBackportsService.getEncounterAttributeByName(ENCOUNTER_ATTRIBUTE_TEST);
	    EncounterAttributeValue encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(ENCOUNTER_ID_TEST_23189, encounterAttribute, false);
	    
	    Assert.assertNotNull("Encounter attribute value was not found.", encounterAttributeValue);
	    
	    // This one should only be found when we include voided
	    encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(ENCOUNTER_ID_TEST_23190, encounterAttribute, true);
	    Assert.assertNotNull("Encounter attribute value was not found.", encounterAttributeValue);
	    
	    // Now it should be null since we are excluding voided
	    encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(ENCOUNTER_ID_TEST_23190, encounterAttribute, false);
	    Assert.assertNull("A voided encounter attribute value was found but should have been excluded", encounterAttributeValue);
	}
	
	/**
	 * Tests getting the last patient state for all patients by location
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getLastPatientStateAllPatientsByLocation(java.util.Date, Integer, String, Integer)
	 * @throws Exception
	 */
	@Test
	public void should_getLastPatientStateAllPatientsByLocation() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    Program program = chirdlUtilBackportsService.getProgramByLocation(LOCATION_ID_TEST_8992); 
        List<PatientState> currUnfinishedStates = chirdlUtilBackportsService.getLastPatientStateAllPatientsByLocation(
                null, program.getProgramId(), program.getStartState().getName(), LOCATION_ID_TEST_8992);
        
        Assert.assertEquals("Number of patient state rows did not match", 1, currUnfinishedStates.size());
	}
	
	/**
	 * Tests getting chirdlutilbackports_program by location id
	 * NOTE: This is used assuming that all tags for a location use the same program
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getProgramByLocation(Integer)
	 * 
	 * @throws Exception
	 */
	@Test
	public void should_getProgramByLocation() throws Exception{
        ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        Program program = chirdlUtilBackportsService.getProgramByLocation(LOCATION_ID_TEST_8992);
        Assert.assertNotNull("Program not found for location.", program);
        
        // Location does not exist in dataset, we should not find a program
        program = chirdlUtilBackportsService.getProgramByLocation(LOCATION_ID_TEST_9999);
        Assert.assertNull("Program was found, but should not exist.", program);
	}
	
	/**
     * Tests getting chirdlutilbackports_encounter_attribute_value by value
     * 
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeValueByValue(String, String)
     * 
     * @throws Exception
     */
	@Test
	public void should_getEncounterAttributeValueByValue() throws Exception{
        ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        EncounterAttributeValue encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByValue(ENCOUNTER_ATTRIBUTE_VALUE_TEST_23189, ENCOUNTER_ATTRIBUTE_TEST);
        
        Assert.assertNotNull("Encounter attribute value not found.", encounterAttributeValue);
        
        // Should not find a record here because it is voided in the dataset
        encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByValue(ENCOUNTER_ATTRIBUTE_VALUE_TEST_23190, ENCOUNTER_ATTRIBUTE_TEST);
        Assert.assertNull("Encounter attribute was found, but should not exist.", encounterAttributeValue);
	}
	
	/**
     * Tests getting patient state map by session id
     * 
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getPatientStatesBySessionId(Integer, List, boolean)
     * 
     * @throws Exception
     */
	@Test
	public void should_getPatientStatesBySessionId() throws Exception{
	    ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        List<String> stateNames = new ArrayList<>();
        stateNames.add(STATE_TEST);
        
        // One record is retired and one is not
        Map<String, List<PatientState>> stateMap = chirdlUtilBackportsService.getPatientStatesBySessionId(SESSION_ID_TEST_23190, stateNames, false);
        List<PatientState> patientStates = stateMap.get(STATE_TEST);
        Assert.assertEquals("Number of patient state rows did not match in should_getPatientStatesBySessionId()", 1, patientStates.size());
        
        // Include retired
        stateMap.clear();
        patientStates.clear();
        stateMap = chirdlUtilBackportsService.getPatientStatesBySessionId(23190, stateNames, true);
        patientStates = stateMap.get(STATE_TEST);
        Assert.assertEquals("Number of patient state rows did not match in should_getPatientStatesBySessionId()", 2, patientStates.size());
	}
	
	/**
	 * Tests deleting a chirdlutilbackports_location_tag_attribute_value record
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#deleteLocationTagAttributeValueByValue(LocationTagAttribute, String)
	 * 
	 * @throws Exception
	 */
	@Test
	public void should_deleteLocationTagAttributeValueByValue() throws Exception{
        ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        LocationTagAttribute locationTagAttribute = chirdlUtilBackportsService.getLocationTagAttribute(LOCATION_TAG_ATTRIBUTE_TEST);
        LocationTagAttributeValue locationTagAttributeValue = chirdlUtilBackportsService.getLocationTagAttributeValue(LOCATION_TAG_ID_TEST_2987, LOCATION_TAG_ATTRIBUTE_TEST, LOCATION_ID_TEST_8992);
        
        // Delete the record
        chirdlUtilBackportsService.deleteLocationTagAttributeValueByValue(locationTagAttribute, LOCATION_TAG_ATTRIBUTE_VALUE_TEST);
        
        // Query again and make sure the object is null
        locationTagAttributeValue = chirdlUtilBackportsService.getLocationTagAttributeValue(LOCATION_TAG_ID_TEST_2987, LOCATION_TAG_ATTRIBUTE_TEST, LOCATION_ID_TEST_8992);
        Assert.assertNull("Location tag attribute value was not deleted.", locationTagAttributeValue);
        
        // Make sure this record still exits
        locationTagAttributeValue = chirdlUtilBackportsService.getLocationTagAttributeValue(LOCATION_TAG_ID_TEST_2988, LOCATION_TAG_ATTRIBUTE_TEST, LOCATION_ID_TEST_8992);
        Assert.assertNotNull("Location tag attribute value was not found.", locationTagAttributeValue);
	}
}
