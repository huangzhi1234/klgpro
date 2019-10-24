package com.ibb.dao.projectMessage.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.projectMessage.IProSubInfoDao;
import com.ibb.model.projectMessage.ProSubInfo;

@Component
public class ProSubInfoDao  extends BaseHibernateDao<ProSubInfo, Integer> implements IProSubInfoDao{

}
