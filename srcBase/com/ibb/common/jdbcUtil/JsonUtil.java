package com.ibb.common.jdbcUtil;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import org.apache.commons.httpclient.util.DateUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

		
		public static JSONArray formatRsToJsonArray(ResultSet rs)throws Exception{
			// 用这个,百度查到的.纵向集合. ResultSet是横向集合
			ResultSetMetaData md=rs.getMetaData();
			int num=md.getColumnCount();
			JSONArray array=new JSONArray();
			while(rs.next()){
				JSONObject mapOfColValues=new JSONObject();
				for(int i=1;i<=num;i++){
					Object o=rs.getObject(i);
				// 每个纵向的键值对封装进去
					if(o instanceof Date){
						mapOfColValues.put(md.getColumnName(i), DateUtil.formatDate((Date)o, "yyyy-MM-dd"));
					}else{
						mapOfColValues.put(md.getColumnName(i), rs.getObject(i));					
					}
				}
				array.add(mapOfColValues);
			}
			return array;
		}
	}