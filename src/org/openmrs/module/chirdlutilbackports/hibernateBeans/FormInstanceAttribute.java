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
package org.openmrs.module.chirdlutilbackports.hibernateBeans;

/**
 * Holds information to store in the chirdlutilbackports_form_instance_attribute table
 * 
 * @author Steve McKee
 */
public class FormInstanceAttribute implements java.io.Serializable {
	
	// Fields
	private Integer formInstanceAttributeId = null;
	private String name = null;
	private String description = null;
	
	// Constructors
	
	/** default constructor */
	public FormInstanceAttribute() {
	}
	
	/**
	 * @return the formInstanceAttributeId
	 */
	public Integer getFormInstanceAttributeId() {
		return this.formInstanceAttributeId;
	}
	
	/**
	 * @param formInstanceAttributeId the formInstanceAttributeId to set
	 */
	public void setFormInstanceAttributeId(Integer formInstanceAttributeId) {
		this.formInstanceAttributeId = formInstanceAttributeId;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
}
