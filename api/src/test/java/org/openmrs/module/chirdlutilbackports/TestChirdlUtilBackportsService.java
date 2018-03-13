package org.openmrs.module.chirdlutilbackports;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.context.Context;
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
}
