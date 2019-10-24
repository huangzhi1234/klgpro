package com.ibb.dao.stDoc.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.stDoc.IStdocUploadcheckDao;
import com.ibb.model.stDoc.StdocUploadcheck;

@Component
public class StdocUploadcheckDao extends BaseHibernateDao<StdocUploadcheck, Integer> implements IStdocUploadcheckDao{

}
