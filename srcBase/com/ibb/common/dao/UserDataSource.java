package com.ibb.common.dao;

import org.logicalcobwebs.proxool.ProxoolDataSource;

import com.ibb.common.util.SecureUtil;

/**
 * 用户自定义数据源管理器【添加密码加密】
 * 
 * @author kin wong
 */
public class UserDataSource extends ProxoolDataSource {
	@Override
	public synchronized void setPassword(String password) {
		//String pwd=SecureUtil.getDesString(password);
		//System.out.println("----------------pwd------------------------------:"+pwd);
		super.setPassword(password);
	} 
}
