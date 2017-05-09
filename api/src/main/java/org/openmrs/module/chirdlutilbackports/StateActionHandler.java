/**
 * 
 */
package org.openmrs.module.chirdlutilbackports;

import java.util.HashMap;

import org.openmrs.Patient;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.StateAction;

/**
 * @author tmdugan
 *
 */
public interface StateActionHandler
{
	public void processAction(StateAction stateAction, Patient patient,
			PatientState patientState,HashMap<String,Object> parameters);
}
