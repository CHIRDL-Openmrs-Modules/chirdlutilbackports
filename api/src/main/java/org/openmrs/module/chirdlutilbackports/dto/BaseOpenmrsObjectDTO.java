/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chirdlutilbackports.dto;

import java.io.Serializable;

import org.openmrs.BaseOpenmrsObject;
import org.openmrs.OpenmrsObject;

/**
 * DTO class for the BaseOpenmrsObject class
 * 
 * @author Steve McKee
 */
public abstract class BaseOpenmrsObjectDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String uuid;
	
	/**
	 * @see org.openmrs.OpenmrsObject#getUuid()
	 */
	public String getUuid() {
		return uuid;
	}
	
	/**
	 * @see org.openmrs.OpenmrsObject#setUuid(java.lang.String)
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Populates the BaseOpenmrsObjectDTO from the BaseOpenmrsObject.
	 * 
	 * @param source The BaseOpenmrsObject object
	 * @param destination The BaseOpenmrsObjectDTO object
	 */
	public static void convertFrom(BaseOpenmrsObject source, BaseOpenmrsObjectDTO destination) {
		if (source == null || destination == null) {
			return;
		}
		
		destination.setUuid(source.getUuid());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseOpenmrsObjectDTO other = (BaseOpenmrsObjectDTO) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseOpenmrsObjectDTO [uuid=" + uuid + "]";
	}
}