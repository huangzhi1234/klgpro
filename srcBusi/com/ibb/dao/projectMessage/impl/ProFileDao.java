package com.ibb.dao.projectMessage.impl;


import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ibb.common.dao.hibernate3.BaseHibernateDao;
import com.ibb.dao.projectMessage.IProFileDao;
import com.ibb.model.projectMessage.ProFile;

@Component
public class ProFileDao extends BaseHibernateDao<ProFile, Integer> implements IProFileDao{

	@Override
	public JSONArray getMenu(int parentId,String proNum) {
		String hql="";
        JSONArray array=new JSONArray();
        hql="FROM ProFile p WHERE p.parentId = "+parentId+" and p.proNum='"+proNum+"' and p.state=1";
        for(ProFile proFile:find(hql)){
            JSONObject jo=new JSONObject();
            jo.put("fileId", proFile.getFileId());
            jo.put("fileName",proFile.getFileName() );
            jo.put("address",proFile.getAddress() );
            jo.put("submitOper", proFile.getSubmitOper());
            jo.put("submitTime", proFile.getSubmitTime());
            jo.put("parentId",proFile.getParentId());
            jo.put("proNum", proFile.getProNum());
             if(proFile.getParentId()==0){
                    jo.put("state","closed");               
                }
                else{
                    jo.put("state","open");
                    System.out.println(parentId);
                }
            array.add(jo);
        }
        return array;
	}

}
