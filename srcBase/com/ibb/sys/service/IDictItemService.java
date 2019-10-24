package com.ibb.sys.service;

import com.ibb.common.service.IBaseService;
import com.ibb.sys.model.DictItemBean;

public interface IDictItemService extends IBaseService<DictItemBean, Integer> {
	/**
	 * 调整记录排序
	 * @param itemId 记录id
	 * @prram orderType 调整类型
	 */
	public void updateOrder(int itemId, String orderType);
}
