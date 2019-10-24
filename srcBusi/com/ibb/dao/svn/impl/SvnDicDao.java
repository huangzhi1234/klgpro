package com.ibb.dao.svn.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.internal.wc.admin.SVNEntry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.svn.ISvnDicDao;
import com.ibb.model.svn.SvnDicInfo;

@Component
public class SvnDicDao extends BaseHibernateDao<SvnDicInfo, Integer> implements ISvnDicDao{

	
}
