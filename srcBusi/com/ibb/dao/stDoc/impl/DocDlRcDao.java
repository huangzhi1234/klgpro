package com.ibb.dao.stDoc.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.stDoc.IDocDlRcDao;
import com.ibb.model.stDoc.DocDlRc;
@Component
public class DocDlRcDao extends BaseHibernateDao<DocDlRc, Integer> implements IDocDlRcDao{

}
