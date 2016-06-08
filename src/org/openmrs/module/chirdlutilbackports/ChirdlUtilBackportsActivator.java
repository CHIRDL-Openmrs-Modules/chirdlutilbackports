package org.openmrs.module.chirdlutilbackports;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.chirdlutilbackports.cache.ApplicationCacheManager;

/**
 * Purpose: Checks that module specific global properties have been set 
 *
 * @author Tammy Dugan
 *
 */
public class ChirdlUtilBackportsActivator extends BaseModuleActivator {
	
	public static final String CACHE_EHR_MEDICAL_RECORD = "ehrMedicalRecord";
	public static final String GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_HEAP_SIZE = "chirdlutilbackports.EHRMedicalRecordCacheHeapSize";
	public static final String GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_DISK_SIZE = "chirdlutilbackports.EHRMedicalRecordCacheDiskSize";
	public static final String GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_EXPIRY = "chirdlutilbackports.EHRMedicalRecordCacheExpiry";
	public static final Class<Integer> EHR_MEDICAL_RECORD_KEY_CLASS = Integer.class;
	@SuppressWarnings("rawtypes")
    public static final Class<HashMap> EHR_MEDICAL_RECORD_VALUE_CLASS = HashMap.class;

	private Log log = LogFactory.getLog(this.getClass());

	/**
	 * @see org.openmrs.module.BaseModuleActivator#started()
	 */
	public void started() {
		this.log.info("Starting ChirdlUtilBackports Module");
		
		//check that all the required global properties are set
		checkGlobalProperties();
		
		//initialize the medical record cache
		initializeMedicalRecordCache();
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
		} catch (Exception e)
		{
			this.log.error("Error checking global properties for chirdlutilbackports module");
			this.log.error(e.getMessage(),e);

		}
	}
	
	/**
	 * Initializes the cache for the medical record information from the EHR.
	 */
	private void initializeMedicalRecordCache() {
		this.log.info("Initializing Application Cache Manager");
		ApplicationCacheManager cacheManager = ApplicationCacheManager.getInstance();
		this.log.info("Initializing EHR Medical Record Cache");
		
		long heapSize = ApplicationCacheManager.DEFAULT_HEAP_SIZE;
		String heapSizeStr = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_HEAP_SIZE);
		if (heapSizeStr == null || heapSizeStr.isEmpty()) {
			log.error("Global property " + GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_HEAP_SIZE + " is not set.  The cache heap size will "
					+ "be set to " + heapSize);
		} else {
			try {
				heapSize = Long.parseLong(heapSizeStr);
			} catch (NumberFormatException e) {
				heapSize = ApplicationCacheManager.DEFAULT_HEAP_SIZE;
				log.error("Global property " + GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_HEAP_SIZE + " is not set to a valid integer.  "
						+ "The cache heap size will be set to " + heapSize);
			}
		}
		
		long diskSize = ApplicationCacheManager.DEFAULT_DISK_SIZE;
		String diskSizeStr = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_DISK_SIZE);
		if (diskSizeStr == null || diskSizeStr.isEmpty()) {
			log.error("Global property " + GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_DISK_SIZE + " is not set.  The cache disk size will "
					+ "be set to " + diskSize);
		} else {
			try {
				diskSize = Long.parseLong(diskSizeStr);
			} catch (NumberFormatException e) {
				diskSize = ApplicationCacheManager.DEFAULT_DISK_SIZE;
				log.error("Global property " + GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_DISK_SIZE + " is not set to a valid integer.  "
						+ "The cache disk size will be set to " + diskSize);
			}
		}
		
		long expiration = ApplicationCacheManager.DEFAULT_EXPIRY;
		String expirationStr = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_EXPIRY);
		if (expirationStr == null || expirationStr.isEmpty()) {
			log.error("Global property " + GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_EXPIRY + " is not set.  The items in the cache "
					+ "will expire after " + expiration + "minutes");
		} else {
			try {
				expiration = Long.parseLong(expirationStr);
			} catch (NumberFormatException e) {
				expiration = ApplicationCacheManager.DEFAULT_EXPIRY;
				log.error("Global property " + GLOBAL_PROPERTY_EHR_MEDICAL_RECORD_CACHE_EXPIRY + " is not set to a valid integer.  "
						+ "The items in the cache will expire after " + expiration + "minutes");
			}
		}
		
		cacheManager.createCache(
			CACHE_EHR_MEDICAL_RECORD, EHR_MEDICAL_RECORD_KEY_CLASS, EHR_MEDICAL_RECORD_VALUE_CLASS, heapSize, diskSize, expiration);
	}
	
	/**
	 * Shutdown the application cache
	 */
	private void shutdownCache() {
		ApplicationCacheManager cacheManager = ApplicationCacheManager.getInstance();
		cacheManager.closeCacheManager();
	}
	
	/**
	 * @see org.openmrs.module.BaseModuleActivator#stopped()
	 */
	public void stopped() {
		this.log.info("Shutting down ChirdlUtilBackports Module");
		
		// shutdown the application cache
		this.log.info("Shutting down the Application Cache Manager");
		shutdownCache();
	}

}
