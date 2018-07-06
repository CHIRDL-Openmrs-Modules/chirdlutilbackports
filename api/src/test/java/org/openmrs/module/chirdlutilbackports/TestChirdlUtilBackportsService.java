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
import org.openmrs.Role;
import org.openmrs.User;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.UserService;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.EncounterAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;

public class TestChirdlUtilBackportsService extends BaseModuleContextSensitiveTest {
	private ChirdlUtilBackportsService chirdlUtilBackportsService;
    
    @Before
    public void runBeforeEachTest() throws Exception{
        // create the basic user and give it full rights
        initializeInMemoryDatabase();
        
        executeDataSet("dbunitFiles/patientStates.xml");
        authenticate();
        
        chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
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
		ArrayList<String> stateNames = new ArrayList<String>();
		List<PatientState> patientStates = null;
		Integer encounterId = null;
		
		// Look for scanned paper PSF and PWS
		encounterId = 23189;
		stateNames.add("PSF_process");
		patientStates = chirdlUtilBackportsService.getPatientStatesByFormNameAndState("PSF", stateNames, encounterId, false);
		Assert.assertEquals("Number of patient state rows did not match", 1, patientStates.size());
		patientStates.clear();
		stateNames.clear();
		
		stateNames.add("PWS_process");
		patientStates = chirdlUtilBackportsService.getPatientStatesByFormNameAndState("PWS", stateNames, encounterId, false);
		Assert.assertEquals("Number of patient state rows did not match", 1, patientStates.size());
		patientStates.clear();
		stateNames.clear();
		
		// Look for submitted electronic PSF and PWS - NOTE: PWS_process is used for both paper and electronic
		encounterId = 23190;
		stateNames.add("PSF WAIT FOR ELECTRONIC SUBMISSION");
		patientStates = chirdlUtilBackportsService.getPatientStatesByFormNameAndState("PSF", stateNames, encounterId, false);
		Assert.assertEquals("Number of patient state rows did not match", 1, patientStates.size());
		patientStates.clear();
		stateNames.clear();
		
		stateNames.add("PWS_process");
		patientStates = chirdlUtilBackportsService.getPatientStatesByFormNameAndState("PWS", stateNames, encounterId, false);
		Assert.assertEquals("Number of patient state rows did not match", 1, patientStates.size());
		patientStates.clear();
		stateNames.clear();	
	}
	
	/**
	 * Tests getting form attribute value by form id, form attribute, location tag id, and location id
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getFormAttributeValue(Integer, FormAttribute, Integer, Integer)
	 * @throws Exception
	 */
	@Test
	public void should_getFormAttributeValue() throws Exception{
	    FormAttribute formAttribute = chirdlUtilBackportsService.getFormAttributeById(1);
	    FormAttributeValue formAttributeValue = chirdlUtilBackportsService.getFormAttributeValue(8970, formAttribute, 2987, 8992);
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
	    UserService userService = Context.getUserService();
	    Role role = userService.getRole("System Developer");
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
	    List<FormAttributeValue> formAttributeValues = chirdlUtilBackportsService.getFormAttributeValues(1, 8992, 2987);
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
	    List<FormAttributeValue> formAttributeValues = chirdlUtilBackportsService.getAllFormAttributeValuesByFormId(8970);
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
	    //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    FormAttribute attribute = chirdlUtilBackportsService.getFormAttributeById(1);
	    Assert.assertEquals("Form attribute name did not match the expected form attribute name.", attribute.getName(), "TestFormAttribute");
	}
	
	/**
	 * Tests saving an encounter attribute value
	 * 
	 * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#saveEncounterAttributeValue(EncounterAttributeValue)
	 * @throws Exception
	 */
	@Test
	public void should_saveEncounterAttributeValue() throws Exception{
	    //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    EncounterAttribute encounterAttribute = chirdlUtilBackportsService.getEncounterAttributeByName("TestEncounterAttribute");
	    EncounterAttributeValue encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(23192, encounterAttribute, false);
	    
	    Assert.assertNotNull("Encounter attribute value was not found.", encounterAttributeValue);
	    
	    // void and save
	    encounterAttributeValue.setVoided(true);
        encounterAttributeValue.setVoidedBy(Context.getAuthenticatedUser());
        encounterAttributeValue.setVoidReason("Testing");
        encounterAttributeValue.setDateVoided(new Date());
        
        chirdlUtilBackportsService.saveEncounterAttributeValue(encounterAttributeValue);
        
        // Should no longer find the attribute since it has been voided
        encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(23192, encounterAttribute, false);
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
	    //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    EncounterAttribute encounterAttribute = chirdlUtilBackportsService.getEncounterAttributeByName("TestEncounterAttribute");
	    Assert.assertNotNull("TestEncounterAttribute was not found.", encounterAttribute);
	    
	    // This one should not exist
	    encounterAttribute = chirdlUtilBackportsService.getEncounterAttributeByName("TestEncounterAttribute2");
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
	    //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    EncounterAttributeValue encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByName(23189, "TestEncounterAttribute");
	    Assert.assertNotNull("Encounter attribute value was not found.", encounterAttributeValue);
	    
	    // This encounter has 2 encounter attribute value records and should cause a hibernate exception due to non-unique result
	    boolean threwException = false;
	    try{
	        encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByName(23191, "TestEncounterAttribute");
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
	    //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    EncounterAttribute encounterAttribute = chirdlUtilBackportsService.getEncounterAttributeByName("TestEncounterAttribute");
	    EncounterAttributeValue encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(23189, encounterAttribute, false);
	    
	    Assert.assertNotNull("Encounter attribute value was not found.", encounterAttributeValue);
	    
	    // This one should only be found when we include voided
	    encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(23190, encounterAttribute, true);
	    Assert.assertNotNull("Encounter attribute value was not found.", encounterAttributeValue);
	    
	    // Now it should be null since we are excluding voided
	    encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByAttribute(23190, encounterAttribute, false);
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
	    //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
	    Integer locationId = new Integer(8992);
	    Program program = chirdlUtilBackportsService.getProgramByLocation(locationId); 
        List<PatientState> currUnfinishedStates = chirdlUtilBackportsService.getLastPatientStateAllPatientsByLocation(
                null, program.getProgramId(), program.getStartState().getName(), locationId);
        
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
        //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        Program program = chirdlUtilBackportsService.getProgramByLocation(8992);
        Assert.assertNotNull("Program not found for location.", program);
        
        // Location does not exist in dataset, we should not find a program
        program = chirdlUtilBackportsService.getProgramByLocation(9999);
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
        //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        EncounterAttributeValue encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByValue("EncounterAttributeValueFor_23189", "TestEncounterAttribute");
        
        Assert.assertNotNull("Encounter attribute value not found.", encounterAttributeValue);
        
        // Should not find a record here because it is voided in the dataset
        encounterAttributeValue = chirdlUtilBackportsService.getEncounterAttributeValueByValue("EncounterAttributeValueFor_23190", "TestEncounterAttribute");
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
        //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        List<String> stateNames = new ArrayList<>();
        String stateNameStr = "TEST STATE";
        stateNames.add(stateNameStr);
        
        // One record is retired and one is not
        Map<String, List<PatientState>> stateMap = chirdlUtilBackportsService.getPatientStatesBySessionId(23190, stateNames, false);
        List<PatientState> patientStates = stateMap.get(stateNameStr);
        Assert.assertEquals("Number of patient state rows did not match in should_getPatientStatesBySessionId()", 1, patientStates.size());
        
        // Include retired
        stateMap.clear();
        patientStates.clear();
        stateMap = chirdlUtilBackportsService.getPatientStatesBySessionId(23190, stateNames, true);
        patientStates = stateMap.get(stateNameStr);
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
	    executeDataSet("dbunitFiles/locationTagAttributeValues.xml");
       
        String locationTagStr = "TestAttribute";
        //ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
        LocationTagAttribute locationTagAttribute = chirdlUtilBackportsService.getLocationTagAttribute(locationTagStr);
        LocationTagAttributeValue locationTagAttributeValue = chirdlUtilBackportsService.getLocationTagAttributeValue(2987, locationTagStr, 8992);
        
        // Delete the record
        chirdlUtilBackportsService.deleteLocationTagAttributeValueByValue(locationTagAttribute, "TestValue");
        
        // Query again and make sure the object is null
        locationTagAttributeValue = chirdlUtilBackportsService.getLocationTagAttributeValue(2987, locationTagStr, 8992);
        Assert.assertNull("Location tag attribute value was not deleted.", locationTagAttributeValue);
        
        // Make sure this record still exits
        locationTagAttributeValue = chirdlUtilBackportsService.getLocationTagAttributeValue(2988, locationTagStr, 8992);
        Assert.assertNotNull("Location tag attribute value was not found.", locationTagAttributeValue);
	}
}
