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
package org.openmrs.module.chirdlutilbackports.util;

import java.util.HashMap;


/**
 * Constants class
 * 
 * @author Steve McKee
 */
public class ChirdlUtilBackportsConstants {
	
	/*
	 * Constants for cache
	 */
	public static final String CACHE_EHR_MEDICAL_RECORD = "ehrMedicalRecord";
	public static final Class<Integer> CACHE_EHR_MEDICAL_RECORD_KEY_CLASS = Integer.class;
	@SuppressWarnings("rawtypes")
    public static final Class<HashMap> CACHE_EHR_MEDICAL_RECORD_VALUE_CLASS = HashMap.class;
	/*
	 * 
	 */
	
	/*
	 * Constants for global properties
	 */
	public static final String GLOBAL_PROPERTY_CACHE_CONFIG_FILE = "chirdlutilbackports.cacheConfigFile";
	public static final String GLOBAL_PROPERTY_CACHE_DEFAULT_HEAP_SIZE = "chirdlutilbackports.cacheDefaultHeapSize";
	public static final String GLOBAL_PROPERTY_CACHE_DEFAULT_EXPIRY = "chirdlutilbackports.cacheDefaultExpiry";
	/*
	 * 
	 */
	
	/*
	 * Constants for hibernate mapping entity names
	 */
	public static final String PATIENT_STATE_ENTITY= "chirdlutilbackportsPatientState";
	public static final String PROGRAM_ENTITY= "chirdlutilbackportsProgram";
}
