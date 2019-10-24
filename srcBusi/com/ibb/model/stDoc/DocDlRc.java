package com.ibb.model.stDoc;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * DocDlRc entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="doc_dl_rc"

)

public class DocDlRc  implements java.io.Serializable {


    // Fields    

     private Integer dlId;//主键
     private Integer stdocId;//文档id
     private Integer dlMan;//用户id
     private String dlTime;//下载时间
     private Integer dlNum;//下载次数


    // Constructors

    /** default constructor */
    public DocDlRc() {
    }

    
    /** full constructor */
    public DocDlRc(Integer dlId, Integer stdocId, Integer dlMan, String dlTime,
			Integer dlNum) {
		super();
		this.dlId = dlId;
		this.stdocId = stdocId;
		this.dlMan = dlMan;
		this.dlTime = dlTime;
		this.dlNum = dlNum;
	}

   
    // Property accessors
    @Id @GeneratedValue
    
    @Column(name="dlId", unique=true, nullable=false)

    public Integer getDlId() {
        return this.dlId;
    }
    
   
	public void setDlId(Integer dlId) {
        this.dlId = dlId;
    }
    
    @Column(name="stdocId")

    public Integer getStdocId() {
        return this.stdocId;
    }
    
    public void setStdocId(Integer stdocId) {
        this.stdocId = stdocId;
    }
    
    @Column(name="dlMan")

    public Integer getDlMan() {
        return this.dlMan;
    }
    
    public void setDlMan(Integer dlMan) {
        this.dlMan = dlMan;
    }
    
    @Column(name="dlTime", length=20)

    public String getDlTime() {
        return this.dlTime;
    }
    
    public void setDlTime(String dlTime) {
        this.dlTime = dlTime;
    }
   
    
    @Column(name="dlNum", length=50)

    public Integer getDlNum() {
        return this.dlNum;
    }
    
    public void setDlNum(Integer dlNum) {
        this.dlNum = dlNum;
    }








}