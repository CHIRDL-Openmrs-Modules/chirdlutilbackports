/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.chirdlutilbackports;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.hibernate.search.annotations.Field;
import org.openmrs.Auditable;
import org.openmrs.BaseOpenmrsObject;
import org.openmrs.OpenmrsMetadata;
import org.openmrs.Retireable;
import org.openmrs.User;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * In OpenMRS, we distinguish between data and metadata within our data model. Metadata represent
 * system and descriptive data such as data types &mdash; a relationship type or encounter type.
 * Metadata are generally referenced by clinical data but don't represent patient-specific data
 * themselves. This provides a default abstract implementation of the OpenmrsMetadata interface
 * 
 * This is the same code as OpenMRS' BaseOpenmrsMetadata object except that it doesn't require 
 * the use of the name and description fields.  This should be used in places where we want to 
 * be able to audit and retire rows in a database for tables that do not contain patient 
 * information.
 * 
 * @since 1.5
 * @see OpenmrsMetadata
 */
@MappedSuperclass
public abstract class BaseChirdlMetadata extends BaseOpenmrsObject implements Auditable, Retireable {
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "creator")
	private User creator;
	
	@Column(name = "date_created", nullable = false)
	private Date dateCreated;
	
	@ManyToOne
	@JoinColumn(name = "changed_by")
	private User changedBy;
	
	@Column(name = "date_changed")
	private Date dateChanged;
	
	@Column(name = "retired", nullable = false)
	@Field
	private Boolean retired = Boolean.FALSE;
	
	@Column(name = "date_retired")
	private Date dateRetired;
	
	@ManyToOne
	@JoinColumn(name = "retired_by")
	private User retiredBy;
	
	@Column(name = "retire_reason", length = 255)
	private String retireReason;
	
	//***** Constructors *****
	
	/**
	 * Default Constructor
	 */
	public BaseChirdlMetadata() {
	}
	
	//***** Property Access *****
	
	/**
	 * @see org.openmrs.Auditable#getCreator()
	 */
	public User getCreator() {
		return creator;
	}
	
	/**
	 * @see org.openmrs.Auditable#setCreator(org.openmrs.User)
	 */
	public void setCreator(User creator) {
		this.creator = creator;
	}
	
	/**
	 * @see org.openmrs.Auditable#getDateCreated()
	 */
	public Date getDateCreated() {
		return dateCreated;
	}
	
	/**
	 * @see org.openmrs.Auditable#setDateCreated(java.util.Date)
	 */
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	/**
	 * @see org.openmrs.Auditable#getChangedBy()
	 */
	public User getChangedBy() {
		return changedBy;
	}
	
	/**
	 * @see org.openmrs.Auditable#setChangedBy(org.openmrs.User)
	 */
	public void setChangedBy(User changedBy) {
		this.changedBy = changedBy;
	}
	
	/**
	 * @see org.openmrs.Auditable#getDateChanged()
	 */
	public Date getDateChanged() {
		return dateChanged;
	}
	
	/**
	 * @see org.openmrs.Auditable#setDateChanged(java.util.Date)
	 */
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	
	/**
	 * @deprecated as of 2.0, use {@link #getRetired()}
	 * 
	 * @see org.openmrs.Retireable#isRetired()
	 */
	@Deprecated
	@JsonIgnore
	public Boolean isRetired() {
		return getRetired();
	}
	
	/**
	 * This method delegates to {@link #isRetired()}. This is only needed for jstl syntax like
	 * ${fieldType.retired} because the return type is a Boolean object instead of a boolean
	 * primitive type.
	 * 
	 * @see org.openmrs.Retireable#isRetired()
	 */
	public Boolean getRetired() {
		return retired;
	}
	
	/**
	 * @see org.openmrs.Retireable#setRetired(java.lang.Boolean)
	 */
	public void setRetired(Boolean retired) {
		this.retired = retired;
	}
	
	/**
	 * @see org.openmrs.Retireable#getDateRetired()
	 */
	public Date getDateRetired() {
		return dateRetired;
	}
	
	/**
	 * @see org.openmrs.Retireable#setDateRetired(java.util.Date)
	 */
	public void setDateRetired(Date dateRetired) {
		this.dateRetired = dateRetired;
	}
	
	/**
	 * @see org.openmrs.Retireable#getRetiredBy()
	 */
	public User getRetiredBy() {
		return retiredBy;
	}
	
	/**
	 * @see org.openmrs.Retireable#setRetiredBy(org.openmrs.User)
	 */
	public void setRetiredBy(User retiredBy) {
		this.retiredBy = retiredBy;
	}
	
	/**
	 * @see org.openmrs.Retireable#getRetireReason()
	 */
	public String getRetireReason() {
		return retireReason;
	}
	
	/**
	 * @see org.openmrs.Retireable#setRetireReason(java.lang.String)
	 */
	public void setRetireReason(String retireReason) {
		this.retireReason = retireReason;
	}
	
}
