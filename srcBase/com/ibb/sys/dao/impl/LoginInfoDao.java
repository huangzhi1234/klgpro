package com.ibb.sys.dao.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.sys.dao.ILoginInfoDao;
import com.ibb.sys.model.LoginInfo;
@Component
public class LoginInfoDao extends BaseHibernateDao<LoginInfo, Integer> implements ILoginInfoDao{

}
