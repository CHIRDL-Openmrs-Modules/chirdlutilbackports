/**
 * 
 */
package org.openmrs.module.chirdlutilbackports;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleClassLoader;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.chirdlutilbackports.action.ProcessStateAction;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.State;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateAction;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;

/**
 * @author tmdugan
 */
public class BaseStateActionHandler implements StateActionHandler {
	
	private static Log log = LogFactory.getLog(BaseStateActionHandler.class);
	
	private static BaseStateActionHandler stateActionHandler = null;
	
	public static BaseStateActionHandler getInstance() {
		if (stateActionHandler == null) {
			stateActionHandler = new BaseStateActionHandler();
		}
		return stateActionHandler;
	}
	
	protected BaseStateActionHandler() {
		
	}
	
	protected ProcessStateAction loadProcessStateAction(StateAction stateAction) {
		
		ProcessStateAction processStateAction = null;
		
		try {
			String stateActionClass = stateAction.getActionClass();
			Collection<ModuleClassLoader> moduleClassLoaders = ModuleFactory.getModuleClassLoaders();
			Class classInstatiation = null;
			for(ModuleClassLoader moduleClassLoader:moduleClassLoaders){
				try {
	                classInstatiation = Class.forName(stateActionClass,true,moduleClassLoader);
	                if(classInstatiation != null){
	                	break;
	                }
                }
                catch (Exception e) {
                }
			}
			if (stateActionClass == null) {
				return null;
			}
			//class the initialization method is in
			processStateAction = (ProcessStateAction) classInstatiation.newInstance();
		}
		catch (Exception e) {
			log.error(e.getMessage());
			log.error(e);
		}
		return processStateAction;
	}
	
	public void processAction(StateAction stateAction, Patient patient, PatientState patientState,
	                                       HashMap<String, Object> parameters) {
		if (stateAction == null) {
			return;
		}
		
		// lookup the patient again to avoid lazy initialization errors
		PatientService patientService = Context.getPatientService();
		Integer patientId = patient.getPatientId();
		patient = patientService.getPatient(patientId);
		
		ProcessStateAction processStateAction = loadProcessStateAction(stateAction);
		
		if (processStateAction != null) {
			processStateAction.processAction(stateAction, patient, patientState, parameters);
		}
	}
	
	public static void changeState(Patient patient, Integer sessionId, State currState, StateAction action,
	                               HashMap<String, Object> parameters, Integer locationTagId, Integer locationId) {
		ChirdlUtilBackportsService chirdlutilbackportsService = Context.getService(ChirdlUtilBackportsService.class);
		List<org.openmrs.module.chirdlutilbackports.hibernateBeans.Error> errors = null;
		// change to error state if fatal error exists for session
		//only look up errors for consume state, for now
		if (action != null && action.getActionName().equalsIgnoreCase("CONSUME FORM INSTANCE")) {
			errors = chirdlutilbackportsService.getErrorsByLevel("Fatal", sessionId);
		}
		if (errors != null && errors.size() > 0) {
			//open an error state
			FormInstance formInstance = (FormInstance)parameters.get("formInstance");
			currState = chirdlutilbackportsService.getStateByName("ErrorState");
			chirdlutilbackportsService.addPatientState(patient, currState, sessionId, locationTagId, locationId, 
				formInstance);
		} else {
			Program program = chirdlutilbackportsService.getProgram(locationTagId, locationId);
			StateManager.changeState(patient, sessionId, currState, program, parameters, locationTagId, locationId,
			    BaseStateActionHandler.getInstance());
		}
	}
	
	public void changeState(PatientState patientState, HashMap<String, Object> parameters) {
		StateAction stateAction = patientState.getState().getAction();
		if (stateAction == null) {
			return;
		}
		
		ProcessStateAction processStateAction = loadProcessStateAction(stateAction);
		
		if (processStateAction != null) {
			processStateAction.changeState(patientState, parameters);
		}
	}
}
