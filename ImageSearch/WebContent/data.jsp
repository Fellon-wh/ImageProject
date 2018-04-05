<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.alibaba.fastjson.*" %>
<%@ page import="java.util.*,com.feilong.spider.*" %>
<%
	String pageNum = request.getParameter("pageNum");
	String category = request.getParameter("category");
	List<Image> images = SpiderUtil.getImageList(category, pageNum);
	out.print(JSONArray.toJSONString(images, true));
%>