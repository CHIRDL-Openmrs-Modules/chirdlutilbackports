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
 * Holds information to store in the chirdlutilbackports_form_instance_attribute_value table
 * 
 * @author Tammy Dugan
 */
public class FormInstanceAttributeValue implements java.io.Serializable {
	
	// Fields
	private Integer formInstanceAttributeValueId = null;
	private Integer formId = null;
	private Integer formInstanceId = null;
	private Integer locationId = null;
	private Integer formInstanceAttributeId = null;
	private String value = null;
	
	// Constructors
	
	/** default constructor */
	public FormInstanceAttributeValue() {
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
	 * @return the formId
	 */
	public Integer getFormId() {
		return this.formId;
	}
	
	/**
	 * @param formId the formId to set
	 */
	public void setFormId(Integer formId) {
		this.formId = formId;
	}
	
	/**
	 * @return the formInstanceAttributeValueId
	 */
	public Integer getFormInstanceAttributeValueId() {
		return this.formInstanceAttributeValueId;
	}
	
	/**
	 * @param formInstanceAttributeValueId the formInstanceAttributeValueId to set
	 */
	public void setFormInstanceAttributeValueId(Integer formInstanceAttributeValueId) {
		this.formInstanceAttributeValueId = formInstanceAttributeValueId;
	}
	
	/**
	 * @return the value
	 */
	public String getValue() {
		return this.value;
	}
	
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
    /**
     * @return the formInstanceId
     */
    public Integer getFormInstanceId() {
    	return formInstanceId;
    }

    /**
     * @param formInstanceId the formInstanceId to set
     */
    public void setFormInstanceId(Integer formInstanceId) {
    	this.formInstanceId = formInstanceId;
    }
	
    /**
     * @return the locationId
     */
    public Integer getLocationId() {
    	return locationId;
    }

    /**
     * @param locationId the locationId to set
     */
    public void setLocationId(Integer locationId) {
    	this.locationId = locationId;
    }
}
