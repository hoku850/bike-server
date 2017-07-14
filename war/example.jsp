<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglibs.jsp" %>
<%@ page import="org.ccframe.subsys.core.domain.code.*" %>
<c:set var="articleInfProxyList" value="${sdk:findTest(200, 10)}"/>
<
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
    <title>SDK演示页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="Cache-Control" content="no-cache, must-revalidate">
    <meta http-equiv="Content-Type" content="text/HTML; charset=utf-8">

	<script type="text/javascript" language="javascript" src="${webRoot}/common-statics/jquery-1.11.1.min.js"></script>
</head>
<body style="font-size:14px;">
	<c:forEach items="${articleInfProxyList}" var="articleInfProxy" varStatus="status">
	<div style="float:left;width:50px;height:60px;border:1px solid #333333">ID:${articleInfProxy.articleInfId}</div>
	<div style="float:left;width:160px;height:60px;border:1px solid #333333">标题：${articleInfProxy.articleTitle}</div>
	<div style="float:left;width:500px;height:60px;border:1px solid #333333;overflow:hidden">内容：${articleInfProxy.articleContStr}</div>
	<div style="float:left;width:250px;height:60px;border:1px solid #333333">发布时间：${articleInfProxy.releaseTimeStr}</div>
	<div style="float:left;width:120px;height:60px;border:1px solid #333333">姓名：${articleInfProxy.releaseUser.userNm}</div>
	<div style="float:left;width:100px;height:60px;border:1px solid #333333">审核状态：
		<c:if test="${sdk:enumFromCode('ApproveStatCodeEnum', articleInfProxy.approveStatCode) == 'NOT_SUBMIT'}">未提交</c:if>
		<c:if test="${sdk:enumFromCode('ApproveStatCodeEnum', articleInfProxy.approveStatCode) == 'QUEUE'}">未处理</c:if>
		<c:if test="${sdk:enumFromCode('ApproveStatCodeEnum', articleInfProxy.approveStatCode) == 'APPROVE'}">同意</c:if>
		<c:if test="${sdk:enumFromCode('ApproveStatCodeEnum', articleInfProxy.approveStatCode) == 'DENY'}">拒绝</c:if>
	</div>
	<div style="clear:both"></div>
	</c:forEach>
</body>
	
</html>
