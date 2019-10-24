package com.ibb.common.jdbcUtil;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

//封装向页面输送数据
public class ResponseUtil {

	public static void write(HttpServletResponse response, Object o) throws Exception {
		// 简单IO输入输出流,向页面输东西
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(o.toString());
		out.flush();
		out.close();
	}
}