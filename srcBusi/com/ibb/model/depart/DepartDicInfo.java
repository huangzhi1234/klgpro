package com.ibb.model.depart;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * DepartDicInfo entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="depart_dic_info"
)

public class DepartDicInfo  implements java.io.Serializable {


    // Fields    

     private Integer departId;
     private String dicNum;
     private String dicName;
     private String level;
     private String operId;
     private String dicType;
     private String propertiyValue;


    // Constructors

    /** default constructor */
    public DepartDicInfo() {
    }

	/** minimal constructor */
    public DepartDicInfo(String dicNum, String dicName, String level, String operId) {
        this.dicNum = dicNum;
        this.dicName = dicName;
        this.level = level;
        this.operId = operId;
    }
    
    /** full constructor */
    public DepartDicInfo(String dicNum, String dicName, String level, String operId, String dicType, String propertiyValue) {
        this.dicNum = dicNum;
        this.dicName = dicName;
        this.level = level;
        this.operId = operId;
        this.dicType = dicType;
        this.propertiyValue = propertiyValue;
    }

   
    // Property accessors
    @Id @GeneratedValue
    
    @Column(name="departId", unique=true, nullable=false)

    public Integer getDepartId() {
        return this.departId;
    }
    
    public void setDepartId(Integer departId) {
        this.departId = departId;
    }
    
    @Column(name="dicNum", nullable=false, length=10)

    public String getDicNum() {
        return this.dicNum;
    }
    
    public void setDicNum(String dicNum) {
        this.dicNum = dicNum;
    }
    
    @Column(name="dicName", nullable=false, length=200)

    public String getDicName() {
        return this.dicName;
    }
    
    public void setDicName(String dicName) {
        this.dicName = dicName;
    }
    
    @Column(name="level", nullable=false, length=2)

    public String getLevel() {
        return this.level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    @Column(name="operId", nullable=false, length=10)

    public String getOperId() {
        return this.operId;
    }
    
    public void setOperId(String operId) {
        this.operId = operId;
    }
    
    @Column(name="dicType", length=10)

    public String getDicType() {
        return this.dicType;
    }
    
    public void setDicType(String dicType) {
        this.dicType = dicType;
    }
    
    @Column(name="propertiyValue", length=500)

    public String getPropertiyValue() {
        return this.propertiyValue;
    }
    
    public void setPropertiyValue(String propertiyValue) {
        this.propertiyValue = propertiyValue;
    }
   








}