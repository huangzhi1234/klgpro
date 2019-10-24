package com.ibb.dao.score.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.score.IProScoreDao;
import com.ibb.model.score.ProScore;
@Repository
public class ProScoreDao extends BaseHibernateDao<ProScore, Integer> implements IProScoreDao{
	

}
