<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>

<%--
	数据对象介绍：
		articleInfList：			用来显示左侧的文章列表
		articleInfProxy：				用来显示右侧的文章详情
		highLineArticleInfId：	用来标记哪个文章标题被选中
		
 --%>
<%--不传参数时，默认选中最新哪一个文章 --%>
 <c:if test="${param.articleInfId == null }">
 	<c:set var="articleInfId" value="${sdk:findArticleInfByArticleCategoryId(50011,1)[0].articleInfId }"/>
 	<jsp:forward page="${wobRoot}/subdetail.jsp?articleInfId=${articleInfId }"></jsp:forward>
 </c:if>
<c:if test="${param.articleInfId != null }">
	<c:set var="articleInfProxy" value="${sdk:getArticlePorxyById(param.articleInfId) }"/>
	<c:set var="articleInfList" value="${articleInfProxy.getArticleInfRowDtoList(100) }"/>
	<c:set var="highLineArticleInfId" value="${param.articleInfId }"/>
	<c:set var="articleInfPath" value="${articleInfProxy.articleInfPath }"></c:set>
</c:if>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>软件工程 Software Egineering</title>
		<link href="statics/css/page.css" type="text/css" rel="stylesheet" />
		<link href="statics/css/subdetail.css" type="text/css" rel="stylesheet" />
		<script type="text/javascript" src="${webRoot }/statics/script/jquery-1.8.2.min.js"></script>
	</head>
	<body>
		
			
		<!--页眉-->
		<c:import url="/commons/top.jsp"/>
		
		<!--页面容器-->
		<div class="pageContainer">
		

			<!--当前位置-->
			<div class="place">
				当前位置：首页<c:forEach items="${articleInfPath }" var="path"> -> ${path.treeNodeNm }</c:forEach>
			</div>
			
			<!--页面内容-->
			<div class="subPage">
				
				<!--左侧栏-->
				<div class="subLeft">
					<ul>
						<li class="subTitle">${articleInfPath[fn:length(articleInfPath)-1].treeNodeNm }</li>
						<!-- articleInf的title -->
						<c:forEach items="${ articleInfList}" var = "v">
							<!-- chuqin:控制是否选中用js -->
							<li class="subNav" id="${v.articleInfId }" onclick="showsubdetail(${v.articleInfId })"><a href="#">${v.articleTitle }</a></li>
						</c:forEach>
					</ul>
				</div>
				
				
				<!--右侧栏-->
				<div class="subRight">
					
					<!--当前页面name-->
					<div class="pageTitle">
						<div>${articleInfPath[fn:length(articleInfPath)-1].treeNodeNm } </div>
						<div class="newTtileImg"><img src="statics/images/title_arrows.png" /></div>
					</div>
					
					<!--详细内容标题-->
					<div class="detailTitle">
						<div class="detaille"><h2>${articleInfProxy.articleTitle }</h2></div>
						<div class="newpublic">
							<span>发布者：${articleInfProxy.releaseUser.userNm }</span>
							<span class="public02">发布日期：${articleInfProxy.releaseTimeYYYY_MM_DDStr }</span>
						</div>
					</div>
					
				<!--详细内容-->
				<div class="detailContent">
					<!-- 需要显示简介吗 -->
					${articleInfProxy.articleContStr }
				</div>
			</div>
		</div>
		
		<div class="clear"></div>
		
		</div>
		<!--页尾-->
		<c:import url="/commons/bottom.jsp"></c:import>
	</body>
</html>

<script type="text/javascript">//选中传入的articleinfId对应的文章
$("#${highLineArticleInfId}").addClass("lock");

function showsubdetail(articleInfId) {
	window.location.href = "${wobRoot}/subdetail.jsp?articleInfId=" + articleInfId;
}</script>


<