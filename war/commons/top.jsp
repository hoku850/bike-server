<%@ page language="java" contentType="text/html;charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/commons/taglibs.jsp" %>
<!--页眉-->
<div class="top">
	<!--页眉容器-->
	<div class="bodyRow">
		<!--logo-->
		<div class="logo">
			<a href="/"><img src="statics/images/t-logo.png" /></a>
		</div>
		<c:if test="${empty param.p || param.p != 'index'}">
		<!--menu-->
		<div class="menu">
			<ul>
				<li><a href="${webRoot}/index.jsp">首页</a></li>
				<li><a href="${webRoot}/newdetail.jsp?articleInfId=50000">专业介绍</a></li>
				<li><a href="${webRoot}/newdetail.jsp?articleInfId=50003">专业建设</a></li>
				<li><a href="${webRoot}/newdetail.jsp?articleInfId=50001">培养方案</a></li>
				<li><a href="${webRoot}/newdetail.jsp?articleInfId=50002">师资力量</a></li>
				<li><a href="${webRoot}/newdetail.jsp?articleInfId=50004">教材建设</a></li>
				<li><a href="${webRoot}/newdetail.jsp?articleInfId=50006">科研成果</a></li>
				<li><a href="#">实训教学</a>
					<ul>
						<li><a href="${webRoot}/subdetail.jsp">实训基地</a></li>
						<li><a href="${webRoot}/subnewlist.jsp?treeNodeId=50012">实训课程</a></li>
						<li><a href="${webRoot}/subnewlist.jsp?treeNodeId=50013">实训成果</a></li>
					</ul>
				</li>
				<li><a href="${webRoot}/newlist.jsp?treeNodeId=50020">学生竞赛</a></li>
				<li><a href="#">学生风采</a>
					<ul>
						<li><a href="${webRoot}/subnewlist.jsp?treeNodeId=50041">作品展示</a></li>
						<li><a href="${webRoot}/subnewlist.jsp?treeNodeId=50042">获奖详情</a></li>
					</ul>
				</li>
				<li><a href="${webRoot}/newlist.jsp?treeNodeId=50030">创新创业</a></li>
				<li><a href="${webRoot}/newdetail.jsp?articleInfId=50005">精品课程</a></li>
			</ul>
		</div>
		</c:if>
	</div>
</div>