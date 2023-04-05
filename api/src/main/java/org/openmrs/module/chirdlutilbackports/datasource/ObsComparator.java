/**
 * 
 */
package org.openmrs.module.chirdlutilbackports.datasource;

import java.util.Comparator;
import java.util.Date;

import org.openmrs.Obs;

/**
 * @author tmdugan
 *
 */
public class ObsComparator implements Comparator<Obs> {
	//sort obs in ascending order by obs datetime
	@Override
	public int compare(Obs obs1, Obs obs2) {
		Date obs1Date = obs1.getObsDatetime();
		Date obs2Date = obs2.getObsDatetime();
		if (obs1Date == null && obs2Date == null) {
			return 0;
		}
		
		if (obs1Date == null) {
			return -1; 
		}
		
		if (obs2Date == null) {
			return 1;
		}
		
		return obs1Date.compareTo(obs2Date);
	}

}
