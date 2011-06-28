/**
 * 
 */
package org.openmrs.module.chirdlutilbackports.action;

import java.util.HashMap;

import org.openmrs.Patient;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateAction;

/**
 * @author tmdugan
 *
 */
public interface ProcessStateAction
{
	public void processAction(StateAction stateAction, Patient patient,
			PatientState patientState,HashMap<String,Object> parameters);

	public void changeState(PatientState patientState,
			HashMap<String,Object> parameters);
}
