package org.openmrs.module.chirdlutilbackports.hibernateBeans;

import java.util.Date;

import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;


public class Error implements java.io.Serializable {
	
	// Fields
	private Integer errorID = null;
	private Integer errorCategory = null;
	private Integer sessionId = null;
	private String message = null;
	private String details = null;
	private Date dateTime = null;
	private String level = null;
	
	/**
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	//constructor
	public Error(){}
	
	public Error( String errorLevel, String catString, String desc
			, String trace, Date date, Integer sessionId){
		ChirdlUtilBackportsService cs = (ChirdlUtilBackportsService) Context.getService(ChirdlUtilBackportsService.class);
		errorCategory = cs.getErrorCategoryIdByName(catString);
		if (errorCategory == null){
			errorCategory = cs.getErrorCategoryIdByName("General Error");
		}
		this.sessionId = sessionId;
		message = desc;
		details = trace;
		dateTime = date;
		level = errorLevel;
	}

	
	public Integer getErrorID()
	{
		return this.errorID;
	}

	public void setErrorID(Integer errorID)
	{
		this.errorID = errorID;
	}

	/**
	 * @return the errorCategory
	 */
	public Integer getErrorCategory() {
		return errorCategory;
	}

	/**
	 * @param errorCategory the errorCategory to set
	 */
	public void setErrorCategory(Integer errorCategory) {
		this.errorCategory = errorCategory;
	}

	public Integer getSessionId()
	{
		return this.sessionId;
	}

	public void setSessionId(Integer sessionId)
	{
		this.sessionId = sessionId;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getDetails()
	{
		return this.details;
	}

	public void setDetails(String details)
	{
		this.details = details;
	}

	/**
	 * @return the dateTime
	 */
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param now the dateTime to set
	 */
	public void setDateTime(Date now) {
		this.dateTime = now;
	}
}