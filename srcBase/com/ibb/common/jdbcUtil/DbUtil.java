package com.ibb.common.jdbcUtil;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
    
	//MySql 配置JDBC
	private static String dbUserName="root";
//	private static String dbUrl="jdbc:mysql://192.168.1.20:3306/yicms";
//	private static String dbPassword="bao0149162536mysql";
	private static String dbUrl="jdbc:mysql://localhost:3306/yicms";
	private static String dbPassword="root";
	
	private static String jdbcName="com.mysql.jdbc.Driver";
	

	/**
	 * 获取连接
	 * @return
	 * @throws Exception
	 */
	public static Connection getCon() throws Exception{
		Class.forName(jdbcName);
		Connection con=DriverManager.getConnection(dbUrl,dbUserName,dbPassword);
		return con;
	}
	
	public static void closeCon(Connection con) throws Exception{
		if(con!=null){
			con.close();
		}
	}
	
	public static void main(String[] args) {
		DbUtil dbUtil=new DbUtil();
		try {
			dbUtil.getCon();
			System.out.println("数据库连接成功！~");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
