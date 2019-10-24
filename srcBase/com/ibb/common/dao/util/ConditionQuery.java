package com.ibb.common.dao.util;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;

import java.util.ArrayList;
import java.util.List;

/**
 * 条件查询工具
 * 
 * @author kin wong
 */
public class ConditionQuery {
    private List<Criterion> criterions = new ArrayList<Criterion>();
    
    public void add(Criterion criterion) {
        criterions.add(criterion);
    }
    
    /**
     * 把集合中的条件，加入到Hibernate中的criteria去。
     * criterion 是 Criteria 的查询条件。Criteria 提供了 add(Criterion criterion) 方法来添加查询条件。
     * @param criteria
     */
    public void build(Criteria criteria) {
        for(Criterion criterion : criterions) {
            criteria.add(criterion);
        }
    }
        
}
