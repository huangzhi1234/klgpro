package com.ibb.dao.patch.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.patch.IPatchDlRcDao;
import com.ibb.model.patch.PatchDlRc;

@Component
public class PatchDlRcDao extends BaseHibernateDao<PatchDlRc, Integer> implements IPatchDlRcDao {

}
