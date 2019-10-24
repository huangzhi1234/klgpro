package com.ibb.dao.projectMessage.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.projectMessage.IUserProDao;
import com.ibb.model.projectMessage.UserProBean;
import com.ibb.sys.model.UserBean;
/**
* 用户查看项目权限管理Dao
* @author kin wong
*/
@Component
public class UserProDao extends BaseHibernateDao<UserProBean, Integer> implements IUserProDao{


}