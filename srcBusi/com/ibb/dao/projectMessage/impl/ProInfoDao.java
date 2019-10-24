package com.ibb.dao.projectMessage.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.projectMessage.IProInfoDao;
import com.ibb.model.projectMessage.ProInfo;
/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午2:28:57
 *
 */
@Component
public class ProInfoDao extends BaseHibernateDao<ProInfo, Integer> implements IProInfoDao {
	
}
