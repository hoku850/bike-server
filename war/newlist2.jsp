<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<%-- 分页的演示页面，不用了删除 --%>
<c:set var="curTreeNodeTree" value="${sdk:getTree(param.treeNodeId) }" />
<c:set var="articleInfRowDtoPage1" value="${sdk:findArticleInfPageByArticleCategoryId(param.treeNodeId, param.sbpage == null ? 0: param.sbpage - 1, 2)}" />
<c:set var="articleInfRowDtoPage2" value="${sdk:findArticleInfPageByArticleCategoryId(param.treeNodeId, param.sopage == null ? 0: param.sopage - 1, 2)}" />

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
					<div class="newListHeight" style="height:150px">
					<c:forEach items="${articleInfRowDtoPage1.list}" var="articleInfRowDto">
							<ul class="newHeight">
								<li class="newTietle"><img src="statics/images/list_icon.png">
									<p><a href="${webRoot}/sublistdetail.jsp?articleInfId=${articleInfRowDto.articleInfId}"> ${articleInfRowDto.articleTitle }</a></p>
								</li>
								<li class="newDate">${articleInfRowDto.releaseTimeYYYY_MM_DDStr }</li>
							</ul>
					</c:forEach>
					</div>
				<!--页码-->
<!-- 				<div class="newPage"> -->
					<sdk:pageTag page="${articleInfRowDtoPage1}" format="[<nncnn>]" pagePerfix="sb" iconNavButton="true"/>
			</div>
			<div class="newContent">
					<!-- 更多新闻列表 -->
					<div class="newListHeight" style="height:150px">
					<c:forEach items="${articleInfRowDtoPage2.list}" var="articleInfRowDto">
							<ul class="newHeight">
								<li class="newTietle"><img src="statics/images/list_icon.png">
									<p><a href="${webRoot}/sublistdetail.jsp?articleInfId=${articleInfRowDto.articleInfId}"> ${articleInfRowDto.articleTitle }</a></p>
								</li>
								<li class="newDate">${articleInfRowDto.releaseTimeYYYY_MM_DDStr }</li>
							</ul>
					</c:forEach>
					</div>
				<!--页码-->
<!-- 				<div class="newPage"> -->
					<sdk:pageTag page="${articleInfRowDtoPage2}" format="[]nnnncnn" pagePerfix="so"/>
			</div>
		</div>

</div>
		<!--页尾-->
		<c:import url="/commons/bottom.jsp"></c:import>
</body>

</html>

<script type="text/javascript">
	

	//选中翻页按钮
	$("#button${num}").addClass("pager");
	$("#button${num}").addClass("pagerspace");
	$("#button${num}").addClass("lock");
	

		
</script>