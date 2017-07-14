<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<c:if test="${param.articleInfId != null }">
	<c:set var="articleInfProxy" value="${sdk:getArticlePorxyById(param.articleInfId) }" />
</c:if>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>软件工程 Software Egineering</title>
	<link href="statics/css/page.css" type="text/css" rel="stylesheet" />
	<link href="statics/css/newdetail.css" type="text/css" rel="stylesheet" />
</head>

<body>

	<!--页眉-->
	<c:import url="/commons/top.jsp" />

	<!--页面容器-->
	<div class="pageContainer">
		<!--当前位置-->
		<div class="place">
			当前位置：首页 -> <a href="#">${articleInfProxy.articleTitle }</a>
		</div>

		<!--详细内容页面-->
		<div class="detailPage">
			<!--详细内容标题-->
			<div class="detailTitle">
				<div>
					<h2>${articleInfProxy.articleTitle }</h2>
				</div>
				<div class="newpublic">
					<span>发布者：${articleInfProxy.articleAuthor }</span>
					<span class="public02">发布日期：${articleInfProxy.releaseTimeYYYY_MM_DDStr }</span>
				</div>
			</div>
			<!--详细内容-->
			<div class="detailContent">
				<p>${articleInfProxy.articleContStr }</p>
			</div>
		</div>
	</div>
	<!--Footer-->
	<c:import url="/commons/bottom.jsp"></c:import>
</body>

</html>