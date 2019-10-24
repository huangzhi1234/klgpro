package com.ibb.sys.service.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.service.impl.BaseService;
import com.ibb.sys.dao.IDictDefDao;
import com.ibb.sys.dao.IDictItemDao;
import com.ibb.sys.dao.impl.DictDefDao;
import com.ibb.sys.model.DictDefBean;
import com.ibb.sys.model.DictItemBean;
import com.ibb.sys.service.IDictDefService;

/**
 * 数据字典定义管理services
 * 
 * @author kin wong
 */
@Component
public class DictDefService extends BaseService<DictDefBean, Integer> implements IDictDefService {
	private IDictDefDao dictDefDao;
	
	@Autowired
	private IDictItemDao dictItemDao;
	
	@Autowired
	@Qualifier("dictDefDao")
	@Override
	public void setBaseDao(IBaseDao<DictDefBean, Integer> baseDao) {
		this.baseDao = baseDao;
        this.dictDefDao = (DictDefDao) baseDao;
	}

	@Override
	public void deleteCasecade(int id) {
		DictDefBean dictDefBean = get(id);
		
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("dictCode", dictDefBean.getDictCode()));
		List<DictItemBean> dictItemList = dictItemDao.queryListByCondition(query);
		for (DictItemBean dictItem : dictItemList)
			dictItemDao.deleteObject(dictItem);
		delete(id);
	}
}
