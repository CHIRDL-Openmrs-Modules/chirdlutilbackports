package org.openmrs.module.chirdlutilbackports;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttribute;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.LocationTagAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.openmrs.test.SkipBaseSetup;

public class TestChirdlUtilBackportsService extends BaseModuleContextSensitiveTest {
	
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
		executeDataSet("dbunitFiles/patientStates.xml");
		Context.authenticate("user1", "testpassword");
		
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
     * Tests deleting a chirdlutilbackports_location_tag_attribute_value record
     * 
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getEncounterAttributeValueByValue(String, String)
     * 
     * @throws Exception
     */
	@Test
	public void should_getEncounterAttributeValueByValue() throws Exception{
	    
	}
	
	/**
     * Tests deleting a chirdlutilbackports_location_tag_attribute_value record
     * 
     * @see org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService#getPatientStatesBySessionId(Integer, List, boolean)
     * 
     * @throws Exception
     */
	@Test
	public void should_getPatientStatesBySessionId() throws Exception{
	    executeDataSet("dbunitFiles/patientStates.xml");
        Context.authenticate("user1", "testpassword");
        
        ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
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
        Context.authenticate("user1", "testpassword");
        
        String locationTagStr = "TestAttribute";
        ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
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
