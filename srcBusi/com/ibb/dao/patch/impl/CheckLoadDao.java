package com.ibb.dao.patch.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.patch.ICheckLoadDao;
import com.ibb.model.patch.CheckLoad;

@Component
public class CheckLoadDao extends BaseHibernateDao<CheckLoad, Integer> implements ICheckLoadDao{

}
