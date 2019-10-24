package com.ibb.model.projectMessage;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午2:11:27
 *
 */
@Entity
@Table(name="pro_source_code"
)

public class ProSourceCode  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer fileId;
     private String codeNum;
     private String proNum;
     private String codeName;
     private String codeType;
     private String codeVersion;
     private String codeComment;
     private String codeDiscFile;
     private String author;
     private String submitOper;
     private String submittime;
     private String comment;
     private String fileName;
     private Integer state;


    // Constructors

    /** default constructor */
    public ProSourceCode() {
    }

	/** minimal constructor */
    public ProSourceCode(Integer fileId, String codeNum, String codeName) {
        this.fileId = fileId;
        this.codeNum = codeNum;
        this.codeName = codeName;
    }
    
    /** full constructor */
    public ProSourceCode(Integer fileId, String codeNum, String codeName, String codeType, String codeVersion, String codeComment, String codeDiscFile, String author, String submitOper, String submittime, String comment, String fileName) {
        this.fileId = fileId;
        this.codeNum = codeNum;
        this.codeName = codeName;
        this.codeType = codeType;
        this.codeVersion = codeVersion;
        this.codeComment = codeComment;
        this.codeDiscFile = codeDiscFile;
        this.author = author;
        this.submitOper = submitOper;
        this.submittime = submittime;
        this.comment = comment;
        this.fileName = fileName;
    }
   
    public ProSourceCode(Integer fileId, String codeNum, String proNum,
			String codeName, String codeType, String codeVersion,
			String codeComment, String codeDiscFile, String author,
			String submitOper, String submittime, String comment, String fileName,Integer state) {
		super();
		this.fileId = fileId;
		this.codeNum = codeNum;
		this.proNum = proNum;
		this.codeName = codeName;
		this.codeType = codeType;
		this.codeVersion = codeVersion;
		this.codeComment = codeComment;
		this.codeDiscFile = codeDiscFile;
		this.author = author;
		this.submitOper = submitOper;
		this.submittime = submittime;
		this.comment = comment;
		this.fileName = fileName;
		this.state=state;
	}

	// Property accessors
    @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="fileId", unique=true, nullable=false)

    public Integer getFileId() {
        return this.fileId;
    }
    
    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }
    
    @Column(name="codeNum", nullable=false, length=100)

    public String getCodeNum() {
        return this.codeNum;
    }
    
    public void setCodeNum(String codeNum) {
        this.codeNum = codeNum;
    }
    
    @Column(name="codeName", nullable=false, length=100)

    public String getCodeName() {
        return this.codeName;
    }
    
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
    
    @Column(name="proNum", nullable=false, length=100)
    
    public String getProNum() {
		return proNum;
	}

	public void setProNum(String proNum) {
		this.proNum = proNum;
	}

	@Column(name="codeType", length=20)

    public String getCodeType() {
        return this.codeType;
    }
    
    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }
    
    @Column(name="codeVersion", length=100)

    public String getCodeVersion() {
        return this.codeVersion;
    }
    
    public void setCodeVersion(String codeVersion) {
        this.codeVersion = codeVersion;
    }
    
    @Column(name="codeComment", length=20)

    public String getCodeComment() {
        return this.codeComment;
    }
    
    public void setCodeComment(String codeComment) {
        this.codeComment = codeComment;
    }
    
    @Column(name="codeDiscFile", length=200)

    public String getCodeDiscFile() {
        return this.codeDiscFile;
    }
    
    public void setCodeDiscFile(String codeDiscFile) {
        this.codeDiscFile = codeDiscFile;
    }
    
    @Column(name="author", length=20)

    public String getAuthor() {
        return this.author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    @Column(name="submitOper", length=20)

    public String getSubmitOper() {
        return this.submitOper;
    }
    
    public void setSubmitOper(String submitOper) {
        this.submitOper = submitOper;
    }
    
    @Column(name="submittime", length=20)

    public String getSubmittime() {
        return this.submittime;
    }
    
    public void setSubmittime(String submittime) {
        this.submittime = submittime;
    }
    
    @Column(name="comment", length=200)

    public String getComment() {
        return this.comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
   
    @Column(name = "fileName", length = 50)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name="state")
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	






}