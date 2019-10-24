package com.ibb.dao.projectMessage.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.projectMessage.IProAttachDao;
import com.ibb.model.projectMessage.ProAttach;
/**
 * 
 * @author Administrator
 *
 */
@Component
public class ProAttachDao extends BaseHibernateDao<ProAttach, Integer> implements IProAttachDao{

}
