/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.chirdlutilbackports.cache;

import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.openmrs.api.context.Context;


/**
 * Used to handle setup and use of various application caches.
 * 
 * @author Steve McKee
 */
public class ApplicationCacheManager {
	
	private CacheManager cacheManager = null;
	private Log log = LogFactory.getLog(this.getClass());
	
	public static final String CACHE_EHR_MEDICAL_RECORD = "ehrMedicalRecord";
	public static final String CACHE_IMMUNIZATION = "immunization";
	
	private static final String GLOBAL_PROPERTY_CACHE_DIRECTORY = "chirdlutilbackports.cacheDirectory";
	private static final String GLOBAL_PROPERTY_CACHE_HEAP_SIZE = "chirdlutilbackports.cacheHeapSize";
	private static final String GLOBAL_PROPERTY_CACHE_DISK_SIZE = "chirdlutilbackports.cacheDiskSize";
	private static final String GLOBAL_PROPERTY_CACHE_EXPIRY = "chirdlutilbackports.cacheExpiry";
	
	private static final String JAVA_TEMP_DIRECTORY = "java.io.tmpdir";
	/* Default heap size in MB */
	private static final int DEFAULT_HEAP_SIZE = 32;
	/* Default disk size in MB */
	private static final int DEFAULT_DISK_SIZE = 100;
	/* Default expiration time in minutes */
	private static final int DEFAULT_EXPIRY = 720;
	
	private static int heapSize;
	private static int diskSize;
	private static int expiration;
	
	/**
	 * Private Constructor Method
	 */
	private ApplicationCacheManager() {
		initializeCacheManager();
	}
	
	/**
	 * Returns the singleton instance of the ApplicationCacheManager.
	 * 
	 * @return ApplicationCacheManager
	 */
	public static ApplicationCacheManager getInstance() {
		return ApplicationCacheManagerHolder.INSTANCE;
	}
	
	/**
	 * Closes the application cache manager.  This should only be called when the application is 
	 * being shutdown.
	 */
	public void closeCacheManager() {
		cacheManager.close();
	}
	
	/**
	 * Retrieves an instance of the cache specified.  If the cache does not exist, it will be created.
	 * 
	 * @param cacheName The name of the cache
	 * @param keyType The key type for cache
	 * @param valueType The value type for the cache
	 * @return Cache object
	 */
    public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
		Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
		if (cache == null) {
			return (Cache<K, V>) cacheManager.createCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType,
				        ResourcePoolsBuilder.newResourcePoolsBuilder()
				            .heap(heapSize, MemoryUnit.MB)
				            .disk(diskSize, MemoryUnit.MB, true)).withExpiry(Expirations.timeToLiveExpiration(Duration.of(expiration, TimeUnit.MINUTES))));
		} else {
			return cache;
		}
	}
	
	/**
	 * Class to hold the singleton instance of the ApplicationCacheManager.
	 *
	 * @author Steve McKee
	 */
	private static class ApplicationCacheManagerHolder { 
		public static final ApplicationCacheManager INSTANCE = new ApplicationCacheManager();
	}
 
	/**
	 * Initializes the setting and preferences for the application cache manager.
	 */
	private void initializeCacheManager() {
		String cacheLocation = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_CACHE_DIRECTORY);
		if (cacheLocation == null || cacheLocation.isEmpty()) {
			cacheLocation = System.getProperty(JAVA_TEMP_DIRECTORY);
			log.error("Global property " + GLOBAL_PROPERTY_CACHE_DIRECTORY + " is not set.  The cache location will "
					+ "be set to " + cacheLocation);
		}
		
		heapSize = DEFAULT_HEAP_SIZE;
		String heapSizeStr = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_CACHE_HEAP_SIZE);
		if (heapSizeStr == null || heapSizeStr.isEmpty()) {
			log.error("Global property " + GLOBAL_PROPERTY_CACHE_HEAP_SIZE + " is not set.  The cache heap size will "
					+ "be set to " + heapSize);
		} else {
			try {
				heapSize = Integer.parseInt(heapSizeStr);
			} catch (NumberFormatException e) {
				heapSize = DEFAULT_HEAP_SIZE;
				log.error("Global property " + GLOBAL_PROPERTY_CACHE_HEAP_SIZE + " is not set to a valid integer.  "
						+ "The cache heap size will be set to " + heapSize);
			}
		}
		
		diskSize = DEFAULT_DISK_SIZE;
		String diskSizeStr = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_CACHE_DISK_SIZE);
		if (diskSizeStr == null || diskSizeStr.isEmpty()) {
			log.error("Global property " + GLOBAL_PROPERTY_CACHE_DISK_SIZE + " is not set.  The cache disk size will "
					+ "be set to " + diskSize);
		} else {
			try {
				diskSize = Integer.parseInt(diskSizeStr);
			} catch (NumberFormatException e) {
				diskSize = DEFAULT_DISK_SIZE;
				log.error("Global property " + GLOBAL_PROPERTY_CACHE_DISK_SIZE + " is not set to a valid integer.  "
						+ "The cache disk size will be set to " + diskSize);
			}
		}
		
		expiration = DEFAULT_EXPIRY;
		String expirationStr = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_CACHE_EXPIRY);
		if (expirationStr == null || expirationStr.isEmpty()) {
			log.error("Global property " + GLOBAL_PROPERTY_CACHE_EXPIRY + " is not set.  The items in the cache "
					+ "will expire after " + expiration + "minutes");
		} else {
			try {
				expiration = Integer.parseInt(expirationStr);
			} catch (NumberFormatException e) {
				expiration = DEFAULT_EXPIRY;
				log.error("Global property " + GLOBAL_PROPERTY_CACHE_EXPIRY + " is not set to a valid integer.  "
						+ "The items in the cache will expire after " + expiration + "minutes");
			}
		}
		
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
			    .with(CacheManagerBuilder.persistence(cacheLocation))
			    .build(true);
	}
}
