<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<c:if test="${param.articleInfId != null }">
	<c:set var="articleInfProxy" value="${sdk:getArticlePorxyById(param.articleInfId) }" />
	<c:set var="articleInfPath" value="${articleInfProxy.articleInfPath }"></c:set>
</c:if>
<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>软件工程 Software Egineering</title>
	<link href="statics/css/page.css" type="text/css" rel="stylesheet" />
	<link href="statics/css/ourteam.css" type="text/css" rel="stylesheet" />
</head>

<body>

	<!--页眉-->
	<c:import url="/commons/top.jsp" />

	

		<!--详细内容页面-->
		<div class="teampage">
			
			<div class="teamcontent">
				<div class="teamtitle">
					<ul>
						<li>团队介绍</li>
						<li>Our Team</li>
					</ul>
				</div>
				
				<div class="teammember">
					<ul>
						<li>
							<div class="memimg">肖政宏</div>
							<div class="memwork">
								<ul>
									<li>项目职位：指导教师</li>
									<li>项目工作：需求指导与评审</li>
								</ul>
							</div>
						</li>
						<li>
							<div class="memimg">梅阳阳</div>
							<div class="memwork">
								<ul>
									<li>所在班级：16系统理论</li>
									<li>项目职位：开发工程师</li>
									<li>项目工作：前台开发、后台开发</li>
								</ul>
							</div>
						</li>
						<li>
							<div class="memimg">翁楚钦</div>
							<div class="memwork">
								<ul>
									<li>所在班级：14软件</li>
									<li>项目职位：开发工程师</li>
									<li>项目工作：前台开发、后台开发</li>
								</ul>
							</div>
						</li>
						<li>
							<div class="memimg">梁诗敏</div>
							<div class="memwork">
								<ul>
									<li>所在班级：14系统理论</li>
									<li>项目职位：设计师</li>
									<li>项目工作：页面设计、前端样式</li>
								</ul>
							</div>
						</li>
					</ul>
				</div>
				
			</div>
			
		</div>
		
		
		
	<!--Footer-->
	<c:import url="/commons/bottom.jsp"></c:import>
</body>

</html>