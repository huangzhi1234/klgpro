package com.ibb.dao.projectMessage.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.projectMessage.IProMemberDao;
import com.ibb.model.projectMessage.ProMember;

/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午2:56:28
 *
 */
@Component
public class ProMemberDao extends BaseHibernateDao<ProMember, Integer> implements IProMemberDao {
	
}
