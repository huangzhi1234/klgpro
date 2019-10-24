package com.ibb.dao.projectMessage.impl;

import org.springframework.stereotype.Repository;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.projectMessage.IProCustomerMemberDao;
import com.ibb.model.projectMessage.ProCustomerMember;
@Repository
public class ProCustomerMemberDao extends BaseHibernateDao<ProCustomerMember, Integer> implements IProCustomerMemberDao{

}
