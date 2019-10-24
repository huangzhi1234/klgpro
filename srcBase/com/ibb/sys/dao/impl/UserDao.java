package com.ibb.sys.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.sys.dao.IUserDao;
import com.ibb.sys.model.UserBean;
/**
 * 用户管理Dao
 * @author kin wong
 */
@Component
public class UserDao extends BaseHibernateDao<UserBean, Integer> implements IUserDao {

	@Override
	public List<UserBean> getUserListWithProId(String sql) {
		return this.executeSQL(sql, null,UserBean.class);
	}


}
