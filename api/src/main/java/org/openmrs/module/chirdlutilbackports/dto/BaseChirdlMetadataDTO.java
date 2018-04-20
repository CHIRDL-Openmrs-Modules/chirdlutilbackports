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

import java.util.Date;

import org.openmrs.module.chirdlutilbackports.BaseChirdlMetadata;

/**
 * DTO class for the BaseChirdlMetadata class
 * 
 * @author Steve McKee
 */
public abstract class BaseChirdlMetadataDTO extends BaseOpenmrsObjectDTO {
	
	private static final long serialVersionUID = 1L;
	
	private Integer creator;
	private Date dateCreated;
	private Integer changedBy;
	private Date dateChanged;
	private Boolean retired = Boolean.FALSE;
	private Date dateRetired;
	private Integer retiredBy;
	private String retireReason;
	
	/**
	 * Constructor method
	 */
	public BaseChirdlMetadataDTO() {
	}
	
	/**
	 * Constructor method
	 */
	public BaseChirdlMetadataDTO(BaseChirdlMetadata baseChirdlMetadata) {
		convertFrom(baseChirdlMetadata, this);
	}
	
	/**
	 * @return The ID of the User who created the data
	 */
	public Integer getCreator() {
		return creator;
	}
	
	/**
	 * Set the ID of the User who created the data
	 * 
	 * @param creator
	 */
	public void setCreator(Integer creator) {
		this.creator = creator;
	}
	
	/**
	 * @return the dateCreated
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	
	/**
	 * Set the dateCreated
	 * 
	 * @param dateCreated The dateCreated to set
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	/**
	 * @return The ID of the User who changed the data
	 */
	public Integer getChangedBy() {
		return changedBy;
	}
	
	/**
	 * Set the ID of the User who changed the data
	 * @param changedBy The ID of the user who changed the data
	 */
	public void setChangedBy(Integer changedBy) {
		this.changedBy = changedBy;
	}
	
	/**
	 * @return the dataChanged
	 */
	public Date getDateChanged() {
		return dateChanged;
	}
	
	/**
	 * Sets the date changed
	 * @param dateChanged The date the data was changed
	 */
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	
	/**
	 * @return whether or not the data is retired
	 */
	public Boolean getRetired() {
		return retired;
	}
	
	/**
	 * Sets the retired status
	 * @param retired The retired status to set
	 */
	public void setRetired(Boolean retired) {
		this.retired = retired;
	}
	
	/**
	 * @return The date retired
	 */
	public Date getDateRetired() {
		return dateRetired;
	}
	
	/**
	 * Sets the retired date
	 * @param dateRetired The date retired
	 */
	public void setDateRetired(Date dateRetired) {
		this.dateRetired = dateRetired;
	}
	
	/**
	 * @return The ID of the User who retired the data
	 */
	public Integer getRetiredBy() {
		return retiredBy;
	}
	
	/**
	 * Set the ID of the User who retired the data
	 * @param retiredBy The ID of the User who retired the data
	 */
	public void setRetiredBy(Integer retiredBy) {
		this.retiredBy = retiredBy;
	}
	
	/**
	 * @return The retire reason
	 */
	public String getRetireReason() {
		return retireReason;
	}
	
	/**
	 * Sets the retire reason
	 * @param retireReason The reason for retiring the data
	 */
	public void setRetireReason(String retireReason) {
		this.retireReason = retireReason;
	}
	
	/**
	 * Populates the BaseChirdlMetadataDTO from the BaseChirdlMetadata.
	 * 
	 * @param source The BaseChirdlMetadata object
	 * @param destination The BaseChirdlMetadataDTO object
	 */
	public static void convertFrom(BaseChirdlMetadata source, BaseChirdlMetadataDTO destination) {
		if (source == null || destination == null) {
			return;
		}
		
		if (source.getChangedBy() != null) {
			destination.setChangedBy(source.getChangedBy().getUserId());
		}
		
		if (source.getCreator() != null) {
			destination.setCreator(source.getCreator().getUserId());
		}
		
		destination.setDateChanged(source.getDateChanged());
		destination.setDateCreated(source.getDateCreated());
		destination.setDateRetired(source.getDateRetired());
		destination.setRetired(source.getRetired());
		if (source.getRetiredBy() != null) {
			destination.setRetiredBy(source.getRetiredBy().getUserId());
		}
		
		destination.setRetireReason(source.getRetireReason());
		BaseOpenmrsObjectDTO.convertFrom(source, destination);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((changedBy == null) ? 0 : changedBy.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((dateChanged == null) ? 0 : dateChanged.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateRetired == null) ? 0 : dateRetired.hashCode());
		result = prime * result + ((retireReason == null) ? 0 : retireReason.hashCode());
		result = prime * result + ((retired == null) ? 0 : retired.hashCode());
		result = prime * result + ((retiredBy == null) ? 0 : retiredBy.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseChirdlMetadataDTO other = (BaseChirdlMetadataDTO) obj;
		if (changedBy == null) {
			if (other.changedBy != null)
				return false;
		} else if (!changedBy.equals(other.changedBy))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (dateChanged == null) {
			if (other.dateChanged != null)
				return false;
		} else if (!dateChanged.equals(other.dateChanged))
			return false;
		if (dateCreated == null) {
			if (other.dateCreated != null)
				return false;
		} else if (!dateCreated.equals(other.dateCreated))
			return false;
		if (dateRetired == null) {
			if (other.dateRetired != null)
				return false;
		} else if (!dateRetired.equals(other.dateRetired))
			return false;
		if (retireReason == null) {
			if (other.retireReason != null)
				return false;
		} else if (!retireReason.equals(other.retireReason))
			return false;
		if (retired == null) {
			if (other.retired != null)
				return false;
		} else if (!retired.equals(other.retired))
			return false;
		if (retiredBy == null) {
			if (other.retiredBy != null)
				return false;
		} else if (!retiredBy.equals(other.retiredBy))
			return false;
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BaseChirdlMetadataDTO [creator=" + creator + ", dateCreated=" + dateCreated + ", changedBy=" + changedBy
		        + ", dateChanged=" + dateChanged + ", retired=" + retired + ", dateRetired=" + dateRetired + ", retiredBy="
		        + retiredBy + ", retireReason=" + retireReason + "]";
	}
}
