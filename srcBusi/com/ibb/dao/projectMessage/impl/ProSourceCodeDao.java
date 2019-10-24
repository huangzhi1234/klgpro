package com.ibb.dao.projectMessage.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.projectMessage.IProSourceCodeDao;
import com.ibb.model.projectMessage.ProSourceCode;

/**
 * 
 * @author WangGangWei 
 * @date 2017年4月20日 下午2:58:46
 *
 */
@Component
public class ProSourceCodeDao extends BaseHibernateDao<ProSourceCode, Integer> implements IProSourceCodeDao {
	
}
