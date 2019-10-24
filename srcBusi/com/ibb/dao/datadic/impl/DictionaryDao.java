package com.ibb.dao.datadic.impl;

import org.springframework.stereotype.Repository;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.datadic.IDictionaryDao;
import com.ibb.model.datadic.TBaseDataDictionary;

/**
 * 数据字典Dao
 */
@Repository("dictionaryDao")
public class DictionaryDao extends BaseHibernateDao<TBaseDataDictionary, String> implements IDictionaryDao {

}
