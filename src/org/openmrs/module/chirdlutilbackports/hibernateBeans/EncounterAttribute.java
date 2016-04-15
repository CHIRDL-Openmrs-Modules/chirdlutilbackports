package org.openmrs.module.chirdlutilbackports.hibernateBeans;

/**
 * DWE CHICA-633
 * Encounter level attributes stored in chirdlutilbackports_encounter_attribute table
 */
public class EncounterAttribute implements java.io.Serializable 
{
	private static final long serialVersionUID = 1L;
	
	// Fields
	private Integer encounterAttributeId = null;
	private String name = null;
	private String description = null;
	
	// Constructors
	
	/**
	 * Default constructor
	 */
	public EncounterAttribute()
	{
		
	}
	
	// Methods
	
	/**
	 * @return encounterAttributeId
	 */
	public Integer getEncounterAttributeId()
	{
		return encounterAttributeId;
	}
	
	/**
	 * @param encounterAttributeId
	 */
	public void setEncounterAttributeId(Integer encounterAttributeId)
	{
		this.encounterAttributeId = encounterAttributeId;
	}
	
	/**
	 * @return name
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @return description
	 */
	public String getDescription()
	{
		return description;
	}
	
	/**
	 * @param description
	 */
	public void setDescription(String description)
	{
		this.description = description;
	}
}
