package com.ibb.sys.service.impl;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ibb.common.dao.IBaseDao;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.service.impl.BaseService;
import com.ibb.sys.dao.IDictItemDao;
import com.ibb.sys.dao.impl.DictItemDao;
import com.ibb.sys.model.DictItemBean;
import com.ibb.sys.service.IDictItemService;

/**
 * 数据字典项目管理services
 * 
 * @author kin wong
 */
@Component
public class DictItemService extends BaseService<DictItemBean, Integer> implements IDictItemService {
	private IDictItemDao dictItemDao;
	
	@Autowired
	@Qualifier("dictItemDao")
	@Override
	public void setBaseDao(IBaseDao<DictItemBean, Integer> baseDao) {
		this.baseDao = baseDao;
        this.dictItemDao = (DictItemDao) baseDao;
	}

	@Override
	public DictItemBean save(DictItemBean dictItem) {
		ConditionQuery query = new ConditionQuery();
		query.add(Restrictions.eq("dictCode", dictItem.getDictCode().trim()));
		dictItem.setItemOrder(dictItemDao.queryCountByCondition(query) + 1);
		return super.save(dictItem);
	}

	@Override
	public void delete(Integer id) {
		DictItemBean dictItem = dictItemDao.get(id);
		deleteObject(dictItem);
	}

	@Override
	public void deleteObject(DictItemBean dictItem) {
		super.deleteObject(dictItem);
		
		OrderBy order = new OrderBy();
		ConditionQuery query = new ConditionQuery();
		order.add(Order.asc("itemOrder"));
		query.add(Restrictions.eq("dictCode", dictItem.getDictCode().trim()));
		query.add(Restrictions.gt("itemOrder", dictItem.getItemOrder()));
		List<DictItemBean> dictItemList = dictItemDao.queryListByCondition(query,order);
		updateItemOrder(dictItemList);
	}

	protected void updateItemOrder(List<DictItemBean> dictItemList) {
		for(int i = 0; i < dictItemList.size(); i++){
			dictItemList.get(i).setItemOrder(dictItemList.get(i).getItemOrder() - 1);
			super.update(dictItemList.get(i));
		}
	}

	@Override
	public void updateOrder(int itemId, String orderType) {
		DictItemBean dictItem = dictItemDao.get(itemId);
		int itemOrder = dictItem.getItemOrder();
		
		OrderBy order = new OrderBy();
		ConditionQuery query = new ConditionQuery();
		order.add(Order.asc("itemOrder"));
		query.add(Restrictions.eq("dictCode", dictItem.getDictCode().trim()));
		List<DictItemBean> dictItemList = dictItemDao.queryListByCondition(query,order);
		
		if("1".equals(orderType) && itemOrder > 1){//TOP
			for(int i = 0; i < itemOrder - 1; i++){
				dictItemList.get(i).setItemOrder(i + 2);
				super.update(dictItemList.get(i));
			}
			
			dictItem.setItemOrder(1);
			super.update(dictItem);
		}else if("2".equals(orderType) && itemOrder > 1){//UP
			dictItem.setItemOrder(itemOrder - 1);
			dictItemList.get(itemOrder - 2).setItemOrder(itemOrder);
			super.update(dictItem);
			super.update(dictItemList.get(itemOrder - 2));
		}else if("3".equals(orderType) && itemOrder < dictItemList.size()){//DOWN
			dictItem.setItemOrder(itemOrder + 1);
			dictItemList.get(itemOrder).setItemOrder(itemOrder);
			super.update(dictItem);
			super.update(dictItemList.get(itemOrder));
		}else if("4".equals(orderType) && itemOrder < dictItemList.size()){//LOW
			for(int i = itemOrder; i < dictItemList.size(); i++){
				dictItemList.get(i).setItemOrder(i);
				super.update(dictItemList.get(i));
			}
			dictItem.setItemOrder(dictItemList.size());
			super.update(dictItem);
		}
	}
}
