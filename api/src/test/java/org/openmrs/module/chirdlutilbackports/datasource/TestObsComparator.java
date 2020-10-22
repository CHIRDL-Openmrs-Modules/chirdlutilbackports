package org.openmrs.module.chirdlutilbackports.datasource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.Obs;

/**
 * Test class for ObsComparator
 * 
 * @author Steve McKee
 */
public class TestObsComparator {
	
	/**
	 * Test multiple date comparison variations for the ObsComparator.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testObsComparator() throws Exception {
		Obs obs1 = new Obs(Integer.valueOf(1));
		Obs obs2 = new Obs(Integer.valueOf(2));
		
		// Test if obs1 has no date and obs2 does, that obs1 comes first when sorted.
		List<Obs> obsList = new ArrayList<>();
		obs2.setObsDatetime(new Date());
		obsList.add(obs1);
		obsList.add(obs2);
		Collections.sort(obsList, new ObsComparator());
		Assert.assertEquals(Integer.valueOf(1), obsList.get(0).getObsId());
		
		// Test if obs1 has a date and obs2 does not, that obs2 comes first when sorted.
		obs2.setObsDatetime(null);
		obs1.setObsDatetime(new Date());
		obsList.clear();
		obsList.add(obs2);
		obsList.add(obs1);
		Collections.sort(obsList, new ObsComparator());
		Assert.assertEquals(Integer.valueOf(2), obsList.get(0).getObsId());
		
		// Test when both have a date and obs1 should be last since it's the most recent.
		obsList.clear();
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		cal.add(Calendar.HOUR_OF_DAY, -1);
		obs2.setObsDatetime(cal.getTime());
		obs1.setObsDatetime(new Date());
		obsList.add(obs1);
		obsList.add(obs2);
		Collections.sort(obsList, new ObsComparator());
		Assert.assertEquals(Integer.valueOf(1), obsList.get(1).getObsId());
		
		// Make sure the comparator handles null for both dates.  The order should remain the same as they were added.
		obsList.clear();
		obs1.setObsDatetime(null);
		obs2.setObsDatetime(null);
		obsList.add(obs1);
		obsList.add(obs2);
		Collections.sort(obsList, new ObsComparator());
		Assert.assertEquals(Integer.valueOf(1), obsList.get(0).getObsId());
	}
}
