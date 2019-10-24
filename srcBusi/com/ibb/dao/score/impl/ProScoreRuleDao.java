package com.ibb.dao.score.impl;

import org.springframework.stereotype.Repository;

import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.score.IProScoreRuleDao;
import com.ibb.model.score.ProScoreRule;

@Repository
public class ProScoreRuleDao extends BaseHibernateDao<ProScoreRule, Integer> implements IProScoreRuleDao{

}
