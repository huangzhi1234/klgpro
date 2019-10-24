package com.ibb.tab;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.ibb.common.jdbcUtil.DbUtil;

public class MyTag extends SimpleTagSupport{ 
	private String tablevalue = "";
	private String targetcolumn = "";
	private String columnname = "";
	private String columnvalue = "";
	
	public String getTablevalue() {
		return tablevalue;
	}

	public void setTablevalue(String tablevalue) {
		this.tablevalue = tablevalue;
	}

	public String getTargetcolumn() {
		return targetcolumn;
	}

	public void setTargetcolumn(String targetcolumn) {
		this.targetcolumn = targetcolumn;
	}

	public String getColumnname() {
		return columnname;
	}

	public void setColumnname(String columnname) {
		this.columnname = columnname;
	}

	public String getColumnvalue() {
		return columnvalue;
	}

	public void setColumnvalue(String columnvalue) {
		this.columnvalue = columnvalue;
	}

	

	@Override
	public void doTag() throws JspException, IOException {
		PageContext ctx = (PageContext) getJspContext();
		JspWriter out = ctx.getOut();
		// SimpleDateFormat sdf = new SimpleDateFormat("yyyy 年 MM 月 dd 日 ");
		// out.println(sdf.format(new Date()));
		// String val = commonDao.querySingleValue(tablevalue, targetcolumn,
		// columnname, columnvalue);
		java.sql.Connection conn = null;
		//DbUtil dbUtil = new DbUtil();

		String sql = "select " + targetcolumn + " from " + tablevalue
				+ " where " + columnname + "='" + columnvalue + "'";
		String colLabel = "";
		String colValue = "";
		ResultSet rs = null;
		Statement stmt = null;
		try {
			conn = DbUtil.getCon();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			//int colCount = metaData.getColumnCount();
			
			if (rs.next()) {
				//for (int i = 0; i < colCount; i++) {
					colLabel = metaData.getColumnName(1);
					colValue = rs.getString(colLabel);
				//}
			}/*else{
				 rs.close();  
		         stmt.close();  
		         conn.close();
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(stmt != null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			try {
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		String val = colValue;
		out.print(val);
	}
}