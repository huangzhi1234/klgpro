package com.ibb.sys.service;

import com.ibb.common.service.IBaseService;
import com.ibb.sys.model.DictDefBean;

public interface IDictDefService extends IBaseService<DictDefBean, Integer> {
	/**
	 * 级联删除(同时删除字典项)
	 * @param id
	 */
	public void deleteCasecade(int id);
}
