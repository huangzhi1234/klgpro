package com.ibb.dao.patch.impl;

import org.springframework.stereotype.Component;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.patch.IUploadCheckDao;
import com.ibb.model.patch.UploadCheck;
@Component
public class UploadCheckDao extends BaseHibernateDao<UploadCheck, Integer> implements IUploadCheckDao{

}
