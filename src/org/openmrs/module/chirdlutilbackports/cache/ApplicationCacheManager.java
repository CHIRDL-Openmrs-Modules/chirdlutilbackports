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

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.config.ResourceUnit;
import org.ehcache.config.SizedResourcePool;
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
	private String cacheLocation = null;
	
	/* Default heap size in MB */
	public static final long DEFAULT_HEAP_SIZE = 32;
	/* Default disk size in MB */
	public static final long DEFAULT_DISK_SIZE = 100;
	/* Default expiration time in minutes */
	public static final long DEFAULT_EXPIRY = 720;
	
	private static final String GLOBAL_PROPERTY_CACHE_DIRECTORY = "chirdlutilbackports.cacheDirectory";
	private static final String DEFAULT_CACHE_DIRECTORY = "applicationCache";
	private static final String JAVA_TEMP_DIRECTORY = "java.io.tmpdir";
	
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
	 * Retrieves an instance of the cache specified.  If the cache does not exist, null will be returned.
	 * 
	 * @param cacheName The name of the cache
	 * @param keyType The key type for cache
	 * @param valueType The value type for the cache
	 * @return Cache object or null if it does not exist
	 */
    public <K, V> Cache<K, V> getCache(String cacheName, Class<K> keyType, Class<V> valueType) {
		Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
		return cache;
	}
    
    /**
	 * Creates an instance of a cache with the parameters provided.  This should only be called once per application instantiation.
	 * 
	 * @param cacheName The name of the cache
	 * @param keyType The key type for cache
	 * @param valueType The value type for the cache
	 * @param heapSize The heap size of the cache in MB
	 * @param diskSize The maximum amount of disk space usage of the cache in MB
	 * @param expiration The amount of time in minutes for the values in the cache will expire
	 * @return Cache object
	 */
    public <K, V> Cache<K, V> createCache(String cacheName, Class<K> keyType, Class<V> valueType, long heapSize, long diskSize, long expiration) {
		return (Cache<K, V>) cacheManager.createCache(cacheName, CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType,
			        ResourcePoolsBuilder.newResourcePoolsBuilder()
			            .heap(heapSize, MemoryUnit.MB)
			            .disk(diskSize, MemoryUnit.MB, true)).withExpiry(Expirations.timeToLiveExpiration(Duration.of(expiration, TimeUnit.MINUTES))));
	}
    
    /**
     * Updates the live cache's heap size.
     * 
     * @param cacheName The name of the cache to update
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @param newHeapSize The new heap size of the cache (in MB)
     */
    public <K, V> void updateCacheHeapSize(String cacheName, Class<K> keyType, Class<V> valueType, long newHeapSize) {
    	Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
    	if (cache == null) {
    		log.error("Attempt made to update the " + cacheName + " cache heap size, but the cache cannot be found.");
    	} else {
    		ResourcePools pools = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(newHeapSize, MemoryUnit.MB).build();
    		cache.getRuntimeConfiguration().updateResourcePools(pools);
    	}
    	
    }
    
    /**
     * Updates the live cache's heap size.
     * 
     * @param cacheName The name of the cache to update
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @param newDiskSize The new heap size of the cache (in MB)
     */
    public <K, V> void updateCacheDiskSize(String cacheName, Class<K> keyType, Class<V> valueType, long newDiskSize) {
    	Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
    	if (cache == null) {
    		log.error("Attempt made to update the " + cacheName + " cache disk size, but the cache cannot be found.");
    	} else {
    		ResourcePools pools = ResourcePoolsBuilder.newResourcePoolsBuilder().disk(newDiskSize, MemoryUnit.MB, true).build();
    		cache.getRuntimeConfiguration().updateResourcePools(pools);
    	}
    	
    }
    
    /**
     * Clears the cache for the provided cache name.
     * 
     * @param cacheName The name of the cache to clear
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     */
    public <K, V> void clearCache(String cacheName, Class<K> keyType, Class<V> valueType) {
    	Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
    	if (cache == null) {
    		log.error("Attempt made to clear the " + cacheName + " cache, but the cache cannot be found.");
    	} else {
    		cache.clear();
    	}
    }
    
    /**
     * Returns the current heap size of the requested cache.
     * 
     * @param cacheName The name of the cache
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @return Long with the heap size or null if it is not configured
     */
    public <K, V> Long getCacheHeapSize(String cacheName, Class<K> keyType, Class<V> valueType) {
    	SizedResourcePool pool = getSizedResourcePool(cacheName, keyType, valueType, ResourceType.Core.HEAP);
    	if (pool == null) {
    		return null;
    	}
    	
    	return pool.getSize();
    }
    
    /**
     * Returns the current heap size unit of the requested cache.
     * 
     * @param cacheName The name of the cache
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @return String with the heap size unit or null if it is not configured
     */
    public <K, V> String getCacheHeapSizeUnit(String cacheName, Class<K> keyType, Class<V> valueType) {
    	SizedResourcePool pool = getSizedResourcePool(cacheName, keyType, valueType, ResourceType.Core.HEAP);
    	if (pool == null) {
    		return null;
    	}
    	
    	ResourceUnit unit = pool.getUnit();
    	if (unit == null) {
    		return null;
    	}
    	
    	return unit.toString();
    }
    
    /**
     * Returns the current disk size of the requested cache.
     * 
     * @param cacheName The name of the cache
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @return Long with the disk size or null if it is not configured
     */
    public <K, V> Long getCacheDiskSize(String cacheName, Class<K> keyType, Class<V> valueType) {
    	SizedResourcePool pool = getSizedResourcePool(cacheName, keyType, valueType, ResourceType.Core.DISK);
    	if (pool == null) {
    		return null;
    	}
    	
    	return pool.getSize();
    }
    
    /**
     * Returns the current disk size unit of the requested cache.
     * 
     * @param cacheName The name of the cache
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @return Long with the disk size unit or null if it is not configured
     */
    public <K, V> String getCacheDiskSizeUnit(String cacheName, Class<K> keyType, Class<V> valueType) {
    	SizedResourcePool pool = getSizedResourcePool(cacheName, keyType, valueType, ResourceType.Core.DISK);
    	if (pool == null) {
    		return null;
    	}
    	
    	ResourceUnit unit = pool.getUnit();
    	if (unit == null) {
    		return null;
    	}
    	
    	return unit.toString();
    }
    
    /**
     * Returns the location of the cache.
     * 
     * @return String containing the location of the cache.
     */
    public String getCacheLocation() {
    	return cacheLocation;
    }
    
    private <K, V> SizedResourcePool getSizedResourcePool(String cacheName, Class<K> keyType, Class<V> valueType, ResourceType<SizedResourcePool> resourceType) {
    	Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
    	if (cache == null) {
    		return null;
    	}
    		
    	SizedResourcePool pool = cache.getRuntimeConfiguration().getResourcePools().getPoolForResource(resourceType);
    	return pool;
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
		cacheLocation = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_CACHE_DIRECTORY);
		if (cacheLocation == null || cacheLocation.isEmpty()) {
			cacheLocation = System.getProperty(JAVA_TEMP_DIRECTORY) + File.separator + DEFAULT_CACHE_DIRECTORY;
			log.error("Global property " + GLOBAL_PROPERTY_CACHE_DIRECTORY + " is not set.  The cache location will "
					+ "be set to " + cacheLocation);
		}
		
		cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
			    .with(CacheManagerBuilder.persistence(cacheLocation))
			    .build(true);
	}
}
