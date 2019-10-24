<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.ibb.poi.WarnMessage" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>文件上传</title>
    
    <%--可引入文件(请在Attribute中填写，多个用逗号分开)：commonTag,easyUI,jQuery,My97DatePicker--%>
	<%request.setAttribute("import", "easyUI"); %>
	<%@include file="../inc/import.jsp" %>
 </head>
	<body>
	<!--  
    <form id="addForm" action="${ctx}/mgr/finance/upload" method="post" enctype="multipart/form-data">
        <div style="margin-bottom:20px">
        <div>选择要上传的文件:</div><br><br><br>
        <input class="easyui-filebox" id="file" name="file" data-options="prompt:'请选择您要上传的文件'" style="width:480">
        </div>
        <div style="margin-left:50px">
        <input id="btnToAdd" type="submit" class="btn"  value="确认上传"/>
        </div>
        </form>
        
  -->
    
    
   <%-- <form id="addForm" action="${ctx}/mgr/finance/upload" method="post" enctype="multipart/form-data">
					<div class="formtitle"><span>上传文件</span></div>
						<ul class="forminfo">
							<li>
							<input class="easyui-filebox" id="file" name="file" data-options="prompt:'请选择您要上传的文件'" style="width:320">
							</li>
							<li><label>&nbsp;</label><input id="btnToUpdate" type="submit" class="btn" value="上传"/></li>	
						</ul>
				</form> --%>
	
    
    <%
    try{
    
     List<WarnMessage> errlist =(List<WarnMessage>)request.getAttribute("errlist");
     if(errlist !=null){
          if(errlist.size()>0){
    
    %>
    
     <!--	编辑弹窗	-->

				
      <div class="formtitle"><span>文件上传失败,错误日志如下</span></div>
						<ul class="forminfo">
							<li>
						<%
						 for(int i=0;i<errlist.size();i++){
						WarnMessage warnMessage= errlist.get(i);
						 %>
						 <%=i %>+"."+"第["+<%=warnMessage.getRowname() %>+"行]第["+<%=warnMessage.getCellName() %>+"列]:"+<%=warnMessage.getErrorMsg() %><br>
						 <%
						 }
						 %>
							</li>	
						</ul>
    <%
    }else{
    
     %>
    
						<ul class="forminfo">
							<li>
							文件上传成功！
							</li>	
						</ul>
  
    <%
    
    }
     }
    
    
    }catch(Exception e){
      e.printStackTrace();
    }
    
     %>
     
      <ul class="forminfo">
				 <c:forEach items="${errlist}" var="p">			
							<li>
							${p.rowname}+","+${p.cellName}+","+${p.errorMsg}
							</li>
				 
				 </c:forEach>
				 </ul>
  </body>
</html>
