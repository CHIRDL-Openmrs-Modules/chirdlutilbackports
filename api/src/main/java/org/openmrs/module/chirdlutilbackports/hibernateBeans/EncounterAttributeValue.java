package org.openmrs.module.chirdlutilbackports.hibernateBeans;

import java.util.Date;
import org.openmrs.BaseOpenmrsData;

/**
 * DWE CHICA-633
 * Stores the value for encounter level attributes
 */
public class EncounterAttributeValue extends BaseOpenmrsData implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Integer encounterAttributeValueId;
	private EncounterAttribute encounterAttribute;
	private Integer encounterId;
	private String valueText;
	private Date valueDateTime;

	// Constructors
	
	/**
	 * Default constructor
	 */
	public EncounterAttributeValue()
	{
		super();
	}
	
	/**
	 * @param encounterAttribute
	 * @param encounterId
	 * @param valueText
	 */
	public EncounterAttributeValue(EncounterAttribute encounterAttribute, Integer encounterId, String valueText)
	{
		super();
		this.encounterAttribute = encounterAttribute;
		this.encounterId = encounterId;
		this.valueText = valueText;
	}
	

	/** Creates an encounter attribute value for Date value type.
	 * @param encounterAttribute
	 * @param encounterId
	 * @param valueDateTime
	 */
	public EncounterAttributeValue(EncounterAttribute encounterAttribute, Integer encounterId, Date valueDateTime)
	{
		super();
		this.encounterAttribute = encounterAttribute;
		this.encounterId = encounterId;
		this.valueDateTime = valueDateTime;
	}
	
	// Methods
	
	/**
	 * @return encounterAttributeValueId
	 */
	public Integer getEncounterAttributeValueId() 
	{
		return encounterAttributeValueId;
	}
	
	/**
	 * @param encounterAttributeValueId
	 */
	public void setEncounterAttributeValueId(Integer encounterAttributeValueId)
	{
		this.encounterAttributeValueId = encounterAttributeValueId;
	}
	
	/**
	 * @return encounterAttribute
	 */
	public EncounterAttribute getEncounterAttribute()
	{
		return encounterAttribute;
	}
	
	/**
	 * @param encounterAttribute
	 */
	public void setEncounterAttribute(EncounterAttribute encounterAttribute)
	{
		this.encounterAttribute = encounterAttribute;
	}
	
	/**
	 * @return encounterId
	 */
	public Integer getEncounterId()
	{
		return encounterId;
	}
	
	/**
	 * @param encounterId
	 */
	public void setEncounterId(Integer encounterId)
	{
		this.encounterId = encounterId;
	}
	
	/**
	 * @return valueText
	 */
	public String getValueText() 
	{
		return valueText;
	}
	
	/**
	 * @param valueText
	 */
	public void setValueText(String valueText)
	{
		this.valueText = valueText;
	}
	
	@Override
	public Integer getId()
	{
		return getEncounterAttributeValueId();
	}

	@Override
	public void setId(Integer id)
	{
		setEncounterAttributeValueId(id);
	}
	
	public Date getValueDateTime() {
		return this.valueDateTime;
	}

	public void setValueDateTime(Date valueDateTime) {
		this.valueDateTime = valueDateTime;
	}

}
