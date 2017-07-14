<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<%--参数一定要treeNodeId， num没有的时候默认是第一页，limit可以没有 --%>
<!-- 
数据对象介绍 
	curTreeNodeTree:	treeNodeId对应的树节点
	supTreeNodeTree：	curTreeNodeTree的父节点
	articleInfRowDtoPage: 
	treeNodeTreeList：	用来显示左侧列表
-->
<c:set var="curTreeNodeTree" value="${sdk:getTree(param.treeNodeId) }" />
<c:set var="articleInfRowDtoPage" value="${sdk:findArticleInfPageByArticleCategoryId(param.treeNodeId, param.sbpage == null ? 0: param.sbpage - 1, 2)}" />
<c:set var="supTreeNodeTree" value="${sdk:getTree(curTreeNodeTree.upperTreeNodeId) }" />
<c:set var="treeNodeTreeList" value="${supTreeNodeTree.subNodeTree }" />

<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>软件工程 Software Egineering</title>
	<link href="statics/css/page.css" type="text/css" rel="stylesheet" />
	<link href="statics/css/subnewlist.css" type="text/css" rel="stylesheet" />
	<script type="text/javascript" src="${webRoot }/statics/script/jquery-1.8.2.min.js"></script>
</head>

<body>

	<!--页眉-->
	<c:import url="/commons/top.jsp" />

	<!--页面容器-->
	<div class="pageContainer">

		<!--当前位置-->
		<div class="place">
			当前位置：首页 -> ${supTreeNodeTree.treeNodeNm } -> ${curTreeNodeTree.treeNodeNm }
		</div>

		<!--页面内容-->
		<div class="subPage">
			<!--左侧栏-->
			<div class="subLeft">
				<ul>
					<li class="subTitle">${supTreeNodeTree.treeNodeNm }</li>
					<c:forEach items="${treeNodeTreeList }" var="v">
						<li id="treeNodeTree${v.treeNodeId }" onclick="changePage('${v.treeNodeNm}', ${v.treeNodeId })" class="subNav"><a href="#">${v.treeNodeNm }</a></li>
					</c:forEach>
				</ul>
			</div>

			<!--右侧栏-->
			<div class="subRight">
				<!--当前页面-->
				<div class="pageTitle">
					<div>${curTreeNodeTree.treeNodeNm }</div>
					<div class="newTtileImg"><img src="statics/images/title_arrows.png" /></div>
				</div>

				<!--新闻列表-->
				<div class="newContent">
					<c:forEach items="${articleInfRowDtoPage.list}" var="articleInfRowDto">
						<ul class="newHeight">
							<li class="newTietle"><img src="statics/images/list_icon.png">
								<p><a href="${webRoot }/sublistdetail.jsp?articleInfId=${articleInfRowDto.articleInfId}"> ${articleInfRowDto.articleTitle }</a></p>
							</li>
							<li class="newDate"> ${articleInfRowDto.releaseTimeYYYY_MM_DDStr }</li>
						</ul>
					</c:forEach>
				</div>
				
				<!--页码-->
					<div >
						<sdk:pageTag page="${articleInfRowDtoPage}" format="[<nncnn>]" pageClass="page" iconNavButton="true"/>
					</div>
				<!--右侧栏 end-->
			</div>
			<!--页面内容 end-->
		</div>
		<!--页面容器 end-->
	</div>

	<!--页尾-->
	<c:import url="/commons/bottom.jsp"></c:import>

</body>

</html>
<script type="text/javascript">
	//选中传入的articleinfId对应的文章
	$("#treeNodeTree${param.treeNodeId }").addClass("lock");
	
	function changePage(treeNodeNm, treeNodeId) {
		if (treeNodeNm == "实训基地") {
			window.location.href = "${wobRoot}/subdetail.jsp";
		} else {
			window.location.href = "${wobRoot}/subnewlist.jsp?treeNodeId=" + treeNodeId;
		}
	}
</script>