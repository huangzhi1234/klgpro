package com.ibb.common.model;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 抽象model
 * 
 * @author kin wong
 */
public abstract class AbstractModel implements java.io.Serializable {
    
    private static final long serialVersionUID = 2035013017939483936L;


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
