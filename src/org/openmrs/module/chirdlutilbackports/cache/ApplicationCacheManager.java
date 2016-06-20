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
import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.cache.Cache;
import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.CacheRuntimeConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.ResourceType;
import org.ehcache.config.ResourceUnit;
import org.ehcache.config.SizedResourcePool;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.expiry.Expiry;
import org.ehcache.jsr107.Eh107Configuration;
import org.openmrs.api.context.Context;
import org.openmrs.module.chirdlutilbackports.util.ChirdlUtilBackportsConstants;


/**
 * Used to handle setup and use of various application caches.
 * 
 * @author Steve McKee
 */
public class ApplicationCacheManager {
	
	private CacheManager cacheManager = null;
	private Log log = LogFactory.getLog(this.getClass());
		
	private static final String GLOBAL_PROPERTY_CACHE_CONFIG_FILE = "chirdlutilbackports.cacheConfigFile";
	private static final long DEFAULT_HEAP_SIZE = 500;
	private static final long DEFAULT_EXPIRY = 480;
	
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
		if (cache != null) {
			return cache;
		}
		
		log.error("A cache with the name " + cacheName + " is not specified in the Ehcache configuration file.  "
				+ "An in-memory cache will be created with the default cache settings.");
		return createDefaultCache(cacheName, keyType, valueType);
	}
    
    /**
     * Updates the live cache's heap size.
     * 
     * @param cacheName The name of the cache to update
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @param newHeapSize The new heap size of the cache (in MB)
     */
    @SuppressWarnings("unchecked")
    public <K, V> void updateCacheHeapSize(String cacheName, Class<K> keyType, Class<V> valueType, long newHeapSize) {
    	Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
    	if (cache == null) {
    		log.error("Attempt made to update the " + cacheName + " cache heap size, but the cache cannot be found.");
    	} else {
    		Eh107Configuration<K, V> eh107Configuration = cache.getConfiguration(Eh107Configuration.class);
    		CacheRuntimeConfiguration<K, V> runtimeConfiguration = eh107Configuration.unwrap(CacheRuntimeConfiguration.class);
    		ResourcePools pools = ResourcePoolsBuilder.newResourcePoolsBuilder().heap(newHeapSize, MemoryUnit.MB).build();
    		runtimeConfiguration.updateResourcePools(pools);
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
    @SuppressWarnings("unchecked")
    public <K, V> void updateCacheDiskSize(String cacheName, Class<K> keyType, Class<V> valueType, long newDiskSize) {
    	Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
    	if (cache == null) {
    		log.error("Attempt made to update the " + cacheName + " cache disk size, but the cache cannot be found.");
    	} else {
    		Eh107Configuration<K, V> eh107Configuration = cache.getConfiguration(Eh107Configuration.class);
    		CacheRuntimeConfiguration<K, V> runtimeConfiguration = eh107Configuration.unwrap(CacheRuntimeConfiguration.class);
    		ResourcePools pools = ResourcePoolsBuilder.newResourcePoolsBuilder().disk(newDiskSize, MemoryUnit.MB, true).build();
    		runtimeConfiguration.updateResourcePools(pools);
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
     * Returns the string representation of the expiry time of the provided cache.
     * 
     * @param cacheName The name of the cache
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @return String containing the expiry time for the provided cache or null if it cannot be found.
     */
    public <K, V> Long getCacheExpiry(String cacheName, Class<K> keyType, Class<V> valueType) {
    	Expiry<? super String, ? super String> expiry = getExpiry(cacheName, keyType, valueType);
    	if (expiry != null) {
			Duration duration = expiry.getExpiryForCreation(null, null);
			return duration.getLength();
    	}
    	
    	return null;
    }
    
    /**
     * Returns the string representation of the expiry time unit of the provided cache.
     * 
     * @param cacheName The name of the cache
     * @param keyType The key class of the cache
     * @param valueType The value class of the cache
     * @return String containing the expiry time unit for the provided cache or null if it cannot be found.
     */
    public <K, V> String getCacheExpiryUnit(String cacheName, Class<K> keyType, Class<V> valueType) {
    	Expiry<? super String, ? super String> expiry = getExpiry(cacheName, keyType, valueType);
    	if (expiry != null) {
			Duration duration = expiry.getExpiryForCreation(null, null);
			TimeUnit timeUnit = duration.getTimeUnit();
			if (timeUnit != null) {
				return timeUnit.toString();
			}
    	}
    	
    	return null;
    }
    
    /**
     * Returns the location of the cache configuration file.
     * 
     * @return URI containing the location of the cache configuration file or null if the cache was 
     * not configured using an XML file.
     */
    public URI getCacheConfigurationFileLocation() {
    	return cacheManager.getURI();
    }
    
    /**
	 * Creates an instance of a cache with the parameters provided.  This should only be called once per application instantiation.
	 * 
	 * @param cacheName The name of the cache
	 * @param keyType The key type for cache
	 * @param valueType The value type for the cache
	 * @param heapSize The heap size of the cache in MB
	 * @param expiration The amount of time in minutes for the values in the cache will expire
	 * @return Cache object
	 */
    private <K, V> Cache<K, V> createDefaultCache(String cacheName, Class<K> keyType, Class<V> valueType) {
    	long heapSize = DEFAULT_HEAP_SIZE;
		String heapSizeStr = Context.getAdministrationService().getGlobalProperty(ChirdlUtilBackportsConstants.CACHE_DEFAULT_HEAP_SIZE);
		if (heapSizeStr == null || heapSizeStr.isEmpty()) {
			log.error("Global property " + ChirdlUtilBackportsConstants.CACHE_DEFAULT_HEAP_SIZE + " is not set.  The cache heap size will "
					+ "be set to " + heapSize);
		} else {
			try {
				heapSize = Long.parseLong(heapSizeStr);
			} catch (NumberFormatException e) {
				heapSize = ApplicationCacheManager.DEFAULT_HEAP_SIZE;
				log.error("Global property " + ChirdlUtilBackportsConstants.CACHE_DEFAULT_HEAP_SIZE + " is not set to a valid integer.  "
						+ "The cache heap size will be set to " + heapSize);
			}
		}
		
		long expiration = DEFAULT_EXPIRY;
		String expirationStr = Context.getAdministrationService().getGlobalProperty(ChirdlUtilBackportsConstants.CACHE_DEFAULT_EXPIRY);
		if (expirationStr == null || expirationStr.isEmpty()) {
			log.error("Global property " + ChirdlUtilBackportsConstants.CACHE_DEFAULT_EXPIRY + " is not set.  The items in the cache "
					+ "will expire after " + expiration + "minutes");
		} else {
			try {
				expiration = Long.parseLong(expirationStr);
			} catch (NumberFormatException e) {
				expiration = ApplicationCacheManager.DEFAULT_EXPIRY;
				log.error("Global property " + ChirdlUtilBackportsConstants.CACHE_DEFAULT_EXPIRY + " is not set to a valid integer.  "
						+ "The items in the cache will expire after " + expiration + "minutes");
			}
		}

    	CacheConfiguration<K, V> cacheConfig = CacheConfigurationBuilder.newCacheConfigurationBuilder(keyType, valueType,
	        ResourcePoolsBuilder.newResourcePoolsBuilder()
            .heap(heapSize, MemoryUnit.MB)).withExpiry(Expirations.timeToLiveExpiration(Duration.of(expiration, TimeUnit.MINUTES))).build();
		return (Cache<K, V>) cacheManager.createCache(cacheName, Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig));
	}
    
    /**
     * Retrieves the specified SizedResourcePool
     * 
     * @param cacheName The name of the cache to pull the resource pool
     * @param keyType The key class type of the cache
     * @param valueType The value class type of the cache
     * @param resourceType The type of resource pool to get
     * @return SizedResourcePool object or null if the cache specified or resource type specified cannot be found.
     */
    @SuppressWarnings("unchecked")
    private <K, V> SizedResourcePool getSizedResourcePool(String cacheName, Class<K> keyType, Class<V> valueType, ResourceType<SizedResourcePool> resourceType) {
    	Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
    	if (cache == null) {
    		return null;
    	}
    		
		Eh107Configuration<K, V> eh107Configuration = cache.getConfiguration(Eh107Configuration.class);
		CacheRuntimeConfiguration<K, V> runtimeConfiguration = eh107Configuration.unwrap(CacheRuntimeConfiguration.class);
    	SizedResourcePool pool = runtimeConfiguration.getResourcePools().getPoolForResource(resourceType);
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
		String cacheConfigFileStr = Context.getAdministrationService().getGlobalProperty(GLOBAL_PROPERTY_CACHE_CONFIG_FILE);
		URI cacheConfigURI = null;
		if (cacheConfigFileStr == null || cacheConfigFileStr.isEmpty()) {
			log.error("Global property " + GLOBAL_PROPERTY_CACHE_CONFIG_FILE + " is not set.  The cache manager cannot be initialized.");
		} else {
			File cacheConfigFile = new File(cacheConfigFileStr);
			if (!cacheConfigFile.exists() || !cacheConfigFile.canRead()) {
				log.error("Cache configuration file " + cacheConfigFileStr + " define in global property " + GLOBAL_PROPERTY_CACHE_CONFIG_FILE + " does not exist or cannot be read.  "
						+ "Any caches created beyond this point with be created with the global settings defined in the global properties.");
			} else {
				cacheConfigURI = cacheConfigFile.toURI();
			}
		}
		
		CachingProvider provider = Caching.getCachingProvider(Thread.currentThread().getContextClassLoader());
		cacheManager = provider.getCacheManager(cacheConfigURI, null);
	}
	
	/**
	 * Return the Expiry setting of the specified cache.
	 * 
	 * @param cacheName The name of the cache to pull the resource pool
     * @param keyType The key class type of the cache
     * @param valueType The value class type of the cache
	 * @return Expiry object of the specified cache or null if one could not be found
	 */
	@SuppressWarnings("unchecked")
    private <K, V> Expiry<? super String, ? super String> getExpiry(String cacheName, Class<K> keyType, Class<V> valueType) {
		Cache<K, V> cache = cacheManager.getCache(cacheName, keyType, valueType);
    	if (cache == null) {
    		return null;
    	}
    	
    	Eh107Configuration<String, String> eh107Configuration = cache.getConfiguration(Eh107Configuration.class);
    	if (eh107Configuration == null) {
    		return null;
    	}
    	
		CacheRuntimeConfiguration<String, String> runtimeConfiguration = eh107Configuration.unwrap(CacheRuntimeConfiguration.class);
		if (runtimeConfiguration == null) {
			return null;
		}
		
		Expiry<? super String, ? super String> expiry = runtimeConfiguration.getExpiry();
		return expiry;
	}
}