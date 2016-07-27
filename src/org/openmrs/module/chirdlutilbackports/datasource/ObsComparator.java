/**
 * 
 */
package org.openmrs.module.chirdlutilbackports.datasource;

import java.util.Comparator;

import org.openmrs.Obs;

/**
 * @author tmdugan
 *
 */
public class ObsComparator implements Comparator<Obs>
{
	//sort obs in ascending order by obs datetime
	public int compare(Obs obs1, Obs obs2)
	{
		return obs1.getObsDatetime().compareTo(obs2.getObsDatetime());
	}

}
