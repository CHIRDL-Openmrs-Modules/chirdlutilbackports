package org.openmrs.module.chirdlutilbackports.hibernateBeans;

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
}
