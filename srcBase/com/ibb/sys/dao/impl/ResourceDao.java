package com.ibb.sys.dao.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.sys.dao.IResourceDao;
import com.ibb.sys.model.ResourceBean;
/**
 * 资源管理Dao
 * @author kin wong
 */
@Component
public class ResourceDao extends BaseHibernateDao<ResourceBean, Integer> implements IResourceDao {
	private final static String queryListByUserId_sql = "select distinct resource from ResourceBean as resource, "
			+ " UserRoleBean as userRole, RoleResourceBean as roleResource " 
			+ " where userRole.pk.roleId = roleResource.pk.roleId and roleResource.pk.resourceId = resource.resourceId and userRole.pk.userId = ?";

	@Override
	public List<ResourceBean> queryListByUserId(final int userId) {
		return list(queryListByUserId_sql, userId);
	}
}
