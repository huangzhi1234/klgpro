package com.ibb.dao.stDoc.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.stDoc.IStDocDao;
import com.ibb.model.stDoc.StdocInfo;

@Component
public class stDocDao extends BaseHibernateDao<StdocInfo, Integer> implements IStDocDao{

}
