package org.openmrs.module.chirdlutilbackports.hibernateBeans;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Extension of the FormInstance class. This class provides the additional location tag information.
 * 
 * @author Steve McKee
 */
public class FormInstanceTag extends FormInstance {
	
	private static final long serialVersionUID = 1L;
	
	private Integer locationTagId;
	
	/**
	 * Default constructor
	 */
	public FormInstanceTag() {
	}
	
	/**
	 * Constructor method
	 * 
	 * @param formInstance
	 * @param locationTagId
	 */
	public FormInstanceTag(FormInstance formInstance, Integer locationTagId) {
		super(formInstance.getLocationId(), formInstance.getFormId(), formInstance.getFormInstanceId());
		this.locationTagId = locationTagId;
	}
	
	/**
	 * Constructor method
	 * 
	 * @param locationId
	 * @param formId
	 * @param formInstanceId
	 * @param locationTagId
	 */
	public FormInstanceTag(Integer locationId, Integer formId, Integer formInstanceId, Integer locationTagId) {
		super(locationId, formId, formInstanceId);
		this.locationTagId = locationTagId;
	}
	
	/**
	 * @return the locationTagId
	 */
	public Integer getLocationTagId() {
		return locationTagId;
	}
	
	/**
	 * @param locationTagId the locationTagId to set
	 */
	public void setLocationTagId(Integer locationTagId) {
		this.locationTagId = locationTagId;
	}
	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obs) {
		if (obs == null || !(obs instanceof FormInstanceTag)) {
			return false;
		}
		
		FormInstanceTag formInstance = (FormInstanceTag) obs;
		
		if ((formInstanceId == null && formInstance.getFormInstanceId() == null) || 
				(formInstanceId != null && this.formInstanceId.equals(formInstance.getFormInstanceId()))) {
			if ((formId == null && formInstance.getFormId() == null) || 
					(formId != null && this.formId.equals(formInstance.getFormId()))) {
				if ((locationId == null && formInstance.getLocationId() == null) || 
						(locationId != null && this.locationId.equals(formInstance.getLocationId()))) {
					if ((locationTagId == null && formInstance.getLocationTagId() == null) || 
							(locationTagId != null && this.locationTagId.equals(formInstance.getLocationTagId()))) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance#hashCode()
	 */
	@Override
	public int hashCode() {
		Integer formInstanceHashValue = this.formInstanceId;
		Integer formHashValue = this.formId;
		Integer locationHashValue = this.locationId;
		Integer locationTagHashValue = this.locationTagId;
		
		int hash = 7;
		
		if (this.formInstanceId == null) {
			formInstanceHashValue = 0;
		}
		
		if (this.formId == null) {
			formHashValue = 0;
		}
		
		if (this.locationId == null) {
			locationHashValue = 0;
		}
		
		if (this.locationTagId == null) {
			locationTagHashValue = 0;
		}
		
		hash = 31 * hash + formInstanceHashValue;
		hash = 31 * hash + formHashValue;
		hash = 31 * hash + locationHashValue;
		hash = 31 * hash + locationTagHashValue;
		return hash;
	}
	
	/**
	 * @see org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstance#toString()
	 */
	@Override
	public String toString() {
		return this.locationId+"_"+this.locationTagId+"_"+this.formId+"_"+this.formInstanceId;
	}
	
	
	/**
	 * Parses a String into a FormInstanceTag object.  The String must be in the format: 
	 * locationId_locationTagId_formId_formInstanceId.
	 * 
	 * @param formInstanceTag String containing the information to parse.
	 * @return FormInstanceTag object.
	 * 
	 * @throws NumberFormatException if one of the values in the String is not an Integer.
	 * @throws NoSuchElementException if the incorrect number of values are in the String.
	 */
	public static FormInstanceTag parseFormInstanceTag(String formInstanceTag) 
			throws NumberFormatException, NoSuchElementException {
		if (formInstanceTag == null || formInstanceTag.trim().length() == 0) {
			throw new IllegalArgumentException(
				"formInstanceTag argument must be non-null and have a length greater than 0.");
		}
		
		//parse the location_id, location_tag_id, form_id, and form_instance_id
		//from the selected form
		StringTokenizer tokenizer = new StringTokenizer(formInstanceTag, "_");
		Integer locationId = Integer.parseInt(tokenizer.nextToken());
		Integer locationTagId = Integer.parseInt(tokenizer.nextToken());
		Integer formId = Integer.parseInt(tokenizer.nextToken());
		Integer formInstanceId = Integer.parseInt(tokenizer.nextToken());
		
		return new FormInstanceTag(locationId, formId, formInstanceId, locationTagId);
	}
}
