/**
 * 
 */
package org.openmrs.module.chirdlutilbackports;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.Program;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.State;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateAction;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateMapping;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;

/**
 * @author Tammy Dugan
 * 
 * Do NOT synchronize these methods. I tried it and it
 * caused deadlocks. tmdugan 4/28/10
 */
public class StateManager
{
	private static Log log = LogFactory.getLog(StateManager.class);
		
	/**
	 * Changes from the current state to the next state
	 * as determined by the chirdlutilbackports_state_mapping table
	 * @param patient Patient that is being processed
	 * @param sessionId current session for the Patient
	 * @param currState current state of the process
	 * @return
	 */
	public static PatientState changeState(Patient patient, Integer sessionId,
			State currState, Program program,HashMap<String,Object> parameters,
			Integer locationTagId,Integer locationId,StateActionHandler stateActionHandler)
	{
		ChirdlUtilBackportsService chirdlUtilBackportsService = Context
				.getService(ChirdlUtilBackportsService.class);
		PatientState patientState = null;
		//look program back up since we are crossing sessions
		program = chirdlUtilBackportsService.getProgram(program.getProgramId());

		StateMapping currMapping = null;

		final State START_STATE = program.getStartState();
		
		// start at the initial state or move to the next one
		if (currState == null)
		{
			currState = START_STATE;
		} else
		{
			currMapping = chirdlUtilBackportsService.getStateMapping(currState,program);
			if(currMapping != null){
				currState = currMapping.getNextState();
			}
		}

		currMapping = chirdlUtilBackportsService.getStateMapping(currState,program);

		patientState = chirdlUtilBackportsService.addPatientState(patient, currState,
				sessionId,locationTagId,locationId);

		processStateAction(currState.getAction(), patient, patientState, 
			program, parameters, stateActionHandler, currMapping);

		if (currMapping == null)
		{
			endState(patientState);
		}

		return patientState;
	}

	private static void processStateAction(StateAction stateAction,
			Patient patient, PatientState patientState,Program program,
			HashMap<String,Object> parameters,StateActionHandler stateActionHandler, 
			StateMapping currMapping)
	{
		if (stateAction == null)
		{
			endState(patientState);
			if (currMapping != null) {
				changeState(patient, patientState.getSessionId(), patientState
						.getState(),program,parameters,patientState.getLocationTagId(),
						patientState.getLocationId(),stateActionHandler);
			}
			return;
		}
		stateActionHandler.processAction(stateAction,patient,patientState,parameters);
	}
	
	public static PatientState runState(Patient patient, Integer sessionId,
			State currState,HashMap<String,Object> parameters,
			Integer locationTagId,Integer locationId,
			StateActionHandler stateActionHandler)
	{
		ChirdlUtilBackportsService chirdlUtilBackportsService = Context
			.getService(ChirdlUtilBackportsService.class);
		StateAction stateAction = currState.getAction();
		PatientState patientState = chirdlUtilBackportsService.addPatientState(patient,
				currState, sessionId,locationTagId,locationId);

		stateActionHandler.processAction(stateAction, patient, patientState,parameters);
		return patientState;
	}

	public static void endState(PatientState patientState)
	{
		ChirdlUtilBackportsService chirdlUtilBackportsService = Context
		.getService(ChirdlUtilBackportsService.class);
		patientState.setEndTime(new java.util.Date());
		chirdlUtilBackportsService.updatePatientState(patientState);// set the end time for
							// the initial state
	}

}
