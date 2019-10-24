package com.ibb.dao.patch.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.patch.IPatchInfoDao;
import com.ibb.model.patch.PatchInfo;

@Component
public class PatchInfoDao extends BaseHibernateDao<PatchInfo, Integer> implements IPatchInfoDao {

}
