package org.openmrs.module.chirdlutilbackports.hibernateBeans;

/**
 * Holds information to store in the chirdlutilbackports_form_attribute_value table
 * 
 * @author Tammy Dugan
 */
public class FormAttributeValue implements java.io.Serializable {

	// Fields
	private Integer formAttributeValueId = null;
	private Integer formId = null;
	private Integer formAttributeId = null;
	private String value = null;
	private Integer locationTagId = null;
	private Integer locationId = null;
	
	// Constructors

	/** default constructor */
	public FormAttributeValue() {
	}
	
	public FormAttributeValue(Integer formId, Integer formAttributeId, Integer locationTagId, Integer locationId, String formAttributeValue){
		this.formId = formId;
		this.formAttributeId = formAttributeId;
		this.locationTagId = locationTagId;
		this.locationId = locationId;
		this.value = formAttributeValue;
	}

	/**
	 * @return the formAttributeId
	 */
	public Integer getFormAttributeId()
	{
		return this.formAttributeId;
	}

	/**
	 * @param formAttributeId the formAttributeId to set
	 */
	public void setFormAttributeId(Integer formAttributeId)
	{
		this.formAttributeId = formAttributeId;
	}

	/**
	 * @return the formId
	 */
	public Integer getFormId()
	{
		return this.formId;
	}

	/**
	 * @param formId the formId to set
	 */
	public void setFormId(Integer formId)
	{
		this.formId = formId;
	}

	

	/**
	 * @return the formAttributeValueId
	 */
	public Integer getFormAttributeValueId()
	{
		return this.formAttributeValueId;
	}

	/**
	 * @param formAttributeValueId the formAttributeValueId to set
	 */
	public void setFormAttributeValueId(Integer formAttributeValueId)
	{
		this.formAttributeValueId = formAttributeValueId;
	}

	/**
	 * @return the value
	 */
	public String getValue()
	{
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value)
	{
		this.value = value;
	}

	public Integer getLocationTagId()
	{
		return this.locationTagId;
	}

	public void setLocationTagId(Integer locationTagId)
	{
		this.locationTagId = locationTagId;
	}

	public Integer getLocationId()
	{
		return this.locationId;
	}

	public void setLocationId(Integer locationId)
	{
		this.locationId = locationId;
	}
}