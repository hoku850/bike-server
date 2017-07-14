<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<%--参数一定要treeNodeId， num没有的时候默认是第一页，limit可以没有 --%>
<c:set var="curTreeNodeTree" value="${sdk:getTree(param.treeNodeId) }" />
<c:set var="articleInfRowDtoPage" value="${sdk:findArticleInfPageByArticleCategoryId(param.treeNodeId, param.page == null ? 0: param.page - 1, 2)}" />

<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>软件工程 Software Egineering</title>
	<link href="statics/css/page.css" type="text/css" rel="stylesheet" />
	<link href="statics/css/newlist.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${webRoot }/statics/script/jquery-1.8.2.min.js"></script>
</head>

<body>

	<!--页眉-->
	<c:import url="/commons/top.jsp" />

	<!--页面容器-->
	<div class="pageContainer">

		<!--当前位置-->
		<div class="place">
			当前位置：首页 -> ${curTreeNodeTree.treeNodeNm }
		</div>

		<!--新闻列表-->
		<div id="newList" class="newListStyle">
			<!--当前页面name-->
			<div class="pageTitle">
				<div>${param.newListTitle }</div>
				<div class="newTtileImg">${curTreeNodeTree.treeNodeNm } <img src="statics/images/title_arrows.png" /></div>
			</div>
			<!--新闻列表-->
			<div class="newContent">
					<!-- 更多新闻列表 -->
					<div class="newListHeight">
					<c:forEach items="${articleInfRowDtoPage.list}" var="articleInfRowDto">
							<ul class="newHeight">
								<li class="newTietle"><img src="statics/images/list_icon.png">
									<p><a href="${webRoot}/sublistdetail.jsp?articleInfId=${articleInfRowDto.articleInfId}"> ${articleInfRowDto.articleTitle }</a></p>
								</li>
								<li class="newDate">${articleInfRowDto.releaseTimeYYYY_MM_DDStr }</li>
							</ul>
					</c:forEach>
					</div>
					<!--页码-->
					<div >
						<sdk:pageTag page="${articleInfRowDtoPage}" format="[<nncnn>]" pageClass="page" iconNavButton="true"/>
					</div>
			</div>				
		</div>
	</div>
		<!--页尾-->
		<c:import url="/commons/bottom.jsp"></c:import>
</body>
</html>
