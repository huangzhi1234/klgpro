package com.ibb.sys.service.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.impl.BaseService;
import com.ibb.sys.dao.IUserDao;
import com.ibb.sys.dao.IUserRoleDao;
import com.ibb.sys.dao.impl.UserDao;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.model.UserRoleBean;
import com.ibb.sys.service.IUserService;

/**
 * 用户管理services
 * 
 * @author kin wong
 */
@Component
public class UserService extends BaseService<UserBean, Integer> implements IUserService {
	private IUserDao userDao;
	
	@Autowired
	private IUserRoleDao userRoleDao;
	
	@Autowired
	@Qualifier("userDao")
	@Override
	public void setBaseDao(IBaseDao<UserBean, Integer> baseDao) {
		this.baseDao = baseDao;
        this.userDao = (UserDao) baseDao;
	}

	@Override
	public void deleteCasecade(int id) {
		delete(id);
		
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("pk.userId", id));
		List<UserRoleBean> userRoleList = userRoleDao.queryListByCondition(query);
		for (UserRoleBean userRole : userRoleList)
			userRoleDao.deleteObject(userRole);
	}

	@Override
	public List<UserBean> getUserListWithProId(String proId) {
		StringBuffer sql = new StringBuffer(" select a.userId,a.imgSrc,");
		sql.append("a.userPwd,a.userName,a.userPhone,a.userMail, ");
		sql.append("a.userAct,a.actTime,a.userAge,a.userSex, ");
		sql.append("a.descript,a.departId,a.position ");
		sql.append(" from tbl_sys_user as a ");
		sql.append(" left join tbl_user_pro as b ");
		sql.append(" on a.userId=b.userId ");
		sql.append(" where b.proId="+proId);
		
		return userDao.getUserListWithProId(sql.toString());
	}

	@Override
	public List<UserBean> getUserListByCondition(String condition) {
		StringBuffer sql = new StringBuffer(" select a.userId,a.imgSrc,");
		sql.append("a.userPwd,a.userName,a.userPhone,a.userMail, ");
		sql.append("a.userAct,a.actTime,a.userAge,a.userSex, ");
		sql.append("a.descript,a.departId,a.position ");
		sql.append(" from tbl_sys_user as a ");
		sql.append("where a.userAct like '%"+condition+"%'  ");
		sql.append(" or a.userName like '%"+condition+"%' ");
		sql.append(" or a.userPhone like '%"+condition+"%' ");
		sql.append(" or a.userMail like '%"+condition+"%' ");
		List<UserBean> list = userDao.executeSQL(sql.toString(), null,UserBean.class);
		return list;
	}
}
