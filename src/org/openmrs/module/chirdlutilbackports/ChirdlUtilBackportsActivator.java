package org.openmrs.module.chirdlutilbackports;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.ContextAuthenticationException;
import org.openmrs.module.Activator;

/**
 * Purpose: Checks that module specific global properties have been set 
 *
 * @author Tammy Dugan
 *
 */
public class ChirdlUtilBackportsActivator implements Activator {

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * @see org.openmrs.module.Activator#startup()
	 */
	public void startup() {
		this.log.info("Starting ChirdlUtilBackports Module");
		
		//check that all the required global properties are set
		checkGlobalProperties();
	}

	private void checkGlobalProperties()
	{
		try
		{
			AdministrationService adminService = Context.getAdministrationService();
			Context.authenticate(adminService
					.getGlobalProperty("scheduler.username"), adminService
					.getGlobalProperty("scheduler.password"));
			Iterator<GlobalProperty> properties = adminService
					.getAllGlobalProperties().iterator();
			GlobalProperty currProperty = null;
			String currValue = null;
			String currName = null;

			while (properties.hasNext())
			{
				currProperty = properties.next();
				currName = currProperty.getProperty();
				if (currName.startsWith("chirdlutilbackports"))
				{
					currValue = currProperty.getPropertyValue();
					if (currValue == null || currValue.length() == 0)
					{
						this.log.error("You must set a value for global property: "
								+ currName);
					}
				}
			}
		} catch (ContextAuthenticationException e)
		{
			this.log.error("Error checking global properties for chirdlutilbackports module");
			this.log.error(e.getMessage(),e);

		}
	}
	
	/**
	 * @see org.openmrs.module.Activator#shutdown()
	 */
	public void shutdown() {
		this.log.info("Shutting down ChirdlUtilBackports Module");
	}

}
