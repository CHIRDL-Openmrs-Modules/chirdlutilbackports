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


/**
 * Hold the piece of statistical data for a cache.
 *
 * @author Steve McKee
 */
public class CacheStatistic {
	
	private String name;
	private Object value;
	
	/**
	 * Constructor method
	 * 
	 * @param name The statistic name
	 * @param value The statistic value
	 */
	public CacheStatistic(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
    /**
     * @return the name
     */
    public String getName() {
    	return name;
    }
	
    /**
     * @return the value
     */
    public Object getValue() {
    	return value;
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
    	StringBuilder buffer = new StringBuilder("Name: ");
    	buffer.append(name);
    	buffer.append("\n");
    	buffer.append("Value: ");
    	buffer.append(value);
    	buffer.append("\n");
    	
    	return buffer.toString();
    }
    
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + (name == null ? 0 : name.hashCode());
        hash = hash * 31 + (value == null ? 0 : value.hashCode());
        
        return hash;
    }
    
	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (!super.equals(obj)){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		CacheStatistic other = (CacheStatistic) obj;
		if (name == null) {
			if (other.name != null){
				return false;
			}
		} else if (!name.equals(other.name)){
			return false;
		}
		if (value == null) {
			if (other.value != null){
				return false;
			}
		} else if (!value.equals(other.value)){
			return false;
		}
		return true;
	}
}
