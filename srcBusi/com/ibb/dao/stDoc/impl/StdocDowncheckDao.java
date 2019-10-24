package com.ibb.dao.stDoc.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.stDoc.IStdocDowncheckDao;
import com.ibb.model.stDoc.StdocDowncheck;

@Component
public class StdocDowncheckDao extends BaseHibernateDao<StdocDowncheck, Integer> implements IStdocDowncheckDao{

}
