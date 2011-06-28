/**
 * 
 */
package org.openmrs.module.chirdlutilbackports.util;

import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.hibernateBeans.FormAttributeValue;
import org.openmrs.module.chirdlutilbackports.service.ChirdlUtilBackportsService;

/**
 * @author Tammy Dugan
 *
 */
public class Util
{
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
}
