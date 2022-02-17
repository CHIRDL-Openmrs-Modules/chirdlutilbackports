/**
 * 
 */
package org.openmrs.module.chirdlutilbackports.util;

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormInstanceTag;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;
import org.openmrs.util.Security;

/**
 * @author Tammy Dugan
 *
 */
public class Util
{
    
    private static final Logger log = LoggerFactory.getLogger(Util.class);
    
	public static String getFormAttributeValue(Integer formId,
			String attribute, Integer locationTagId,Integer locationId)
	{
		ChirdlUtilBackportsService chirdlUtilBackportsService = Context.getService(ChirdlUtilBackportsService.class);
		FormAttributeValue formAttributeValue =  chirdlUtilBackportsService.getFormAttributeValue(formId, attribute,
				locationTagId,locationId);
		
		if(formAttributeValue != null)
		{
			return formAttributeValue.getValue();
		}
		return null;
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
	 * 
	 * @deprecated This static method has been moved to the FormInstanceTag class.
	 */
	public static FormInstanceTag parseFormInstanceTag(String formInstanceTag) 
			throws NumberFormatException, NoSuchElementException
	{
		if (formInstanceTag == null || formInstanceTag.trim().length() == 0) {
			throw new IllegalArgumentException("formInstanceTag argument must be non-null and have a length greater than 0.");
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


    /**
     * Looks up the encrypted global property value and decrypts the string
     * @param property is the global property
     * @return decrypted property value
     */
    public static String decryptGlobalProperty(String property) {
        
        if (StringUtils.isBlank(property)) {
            return null;
        }
        
       String decryptedValue = null;
       
        try {
            String propertyValue = Context.getAdministrationService().getGlobalProperty(property);
            decryptedValue = Security.decrypt(propertyValue);
        } catch (APIException e) {
            log.error("Error decrypting global property: {}", property, e);
        }
       
       return decryptedValue;
       
    }
}