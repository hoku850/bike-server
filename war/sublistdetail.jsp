<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>

<%--
	数据对象介绍：
		articleInfList：			用来显示左侧的文章列表
		articleInfProxy：				用来显示右侧的文章详情
		highLineArticleInfId：	用来标记哪个文章标题被选中
		
 --%>
<c:if test="${param.articleInfId != null }">
	<c:set var="articleInfProxy" value="${sdk:getArticlePorxyById(param.articleInfId) }" />
	<c:set var="articleInfList" value="${articleInfProxy.getArticleInfRowDtoList(100) }" />
	<c:set var="highLineArticleInfId" value="${param.articleInfId }" />
	<c:set var="articleInfPath" value="${articleInfProxy.articleInfPath }"></c:set>
</c:if>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>软件工程 Software Egineering</title>
	<link href="statics/css/page.css" type="text/css" rel="stylesheet" />
	<link href="statics/css/sublistdetail.css" type="text/css" rel="stylesheet" />
</head>

<body>

	<!--页眉-->
	<c:import url="/commons/top.jsp" />

	<!--页面容器-->
	<div class="pageContainer">

		<!--当前位置-->
		<div class="place">
			当前位置：首页
			<c:forEach items="${articleInfPath }" var="path"> -> ${path.treeNodeNm }
			</c:forEach>
		</div>

		<!--页面内容-->
		<div class="subPage">
			<!--左侧栏-->
			<div class="subLeft">
				<!--左侧栏标题-->
				<div class="subTitle">${articleInfPath[fn:length(articleInfPath)-1].treeNodeNm }</div>
				<!--左侧栏新闻列表-->
				<div class="subnewList">
					<c:forEach items="${ articleInfList}" var="v">
						<ul>
							<li class="subName">
								<img src="statics/images/list_icon.png" />
								<a href="#">
									<p>${v.articleTitle }</p>
								</a>
							</li>
						</ul>
					</c:forEach>
				</div>
				<!--左侧栏按钮-->
				<div class="subMore">
					<a href="${webRoot }/newlist.jsp?treeNodeId=${articleInfProxy.articleCategoryId }"><img src="statics/images/more_btn.png" /></a>
				</div>
			</div>

			<!--右侧栏-->
			<div class="subRight">
				<!--当前页面-->
				<div class="pageTitle">
					<div>${articleInfPath[fn:length(articleInfPath)-1].treeNodeNm } </div>
					<div class="newTtileImg"><img src="statics/images/title_arrows.png" /></div>
				</div>

				<!--详细内容标题-->
				<div class="detailTitle">
					<div class="detaille">
						<h2>${articleInfProxy.articleTitle }</h2></div>
					<div class="newpublic">
						<span>发布者：${articleInfProxy.releaseUser.userNm }</span>
						<span class="public02">发布日期：${articleInfProxy.releaseTimeYYYY_MM_DDStr }</span>
					</div>
				</div>

				<!--详细内容-->
				<div class="detailContent">
					${articleInfProxy.articleContStr }
				</div>
			</div>
		</div>

		<div class="clear"></div>

		<!--页尾-->
		<c:import url="/commons/bottom.jsp"></c:import>
</body>

</html>