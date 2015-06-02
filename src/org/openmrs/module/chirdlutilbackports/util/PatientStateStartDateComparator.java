/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.chirdlutilbackports.util;

import java.util.Comparator;
import java.util.Date;

import org.openmrs.module.chirdlutilbackports.hibernateBeans.PatientState;


/**
 * Comparator used to sort a list of Patient State objects by start time.
 * 
 * @author Steve McKee
 */
public class PatientStateStartDateComparator implements Comparator<PatientState> {
	
	public static final int ASCENDING = 0;
	public static final int DESCENDING = 1;
	
	private int sort;
	
	/**
	 * Constructor method
	 * 
	 * @param sort Sorting method used.  Use constant ASCENDING or DESCENDING
	 */
	public PatientStateStartDateComparator(int sort) {
		this.sort = sort;
	}

	@Override
    public int compare(PatientState o1, PatientState o2) {
		
		// start time is a required field so no need to check for null dates
	    Date o1StartDate = o1.getStartTime();
	    Date o2StartDate = o2.getStartTime();
	    
	    if (sort == ASCENDING) {
	    	return o1StartDate.compareTo(o2StartDate);
	    } else if (sort == DESCENDING) {
	    	return o2StartDate.compareTo(o1StartDate);
	    } else {
	    	return 0;
	    }
    }
}
