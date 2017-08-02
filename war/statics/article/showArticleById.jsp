<%-- <%@ page mcontentType="text/html; charset=UTF-8" language="java" 
    pageEncoding="utf-8"%> --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglibs.jsp" %>
<c:set var="articleInfId" value="${param.articleInfId}" />
<c:set var="articleInf" value="${sdk:getArticleById(articleInfId )}" />
<c:set var="cont" value="${articleInf.getArticleContStr()}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta name="viewport" content="width=device-width,
minimum-scale=1.0, maximum-scale=2.0" />
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<title>${articleInf.articleTitle}</title>
		<style>
			body {
				text-align: center
			}
			
			.div {
				margin: 0 auto;
				width: 400px;
				/*height: 100px;*/
				border: 1px solid #F00;
			}
		</style>
	</head>

	<body>
		<!--<div style="margin: 0 auto;border: 1px solid #F00;width: 400px;">
			<h1>不能开锁吗</h1> 不能开锁？
			<p>
				有什么事情打电话去800823823问好不好。
			</p>
		</div>-->
		<h1>文章信息from id : ${articleInfId}</h1>
		${articleInf.getArticleContStr()}
	</body>
	<script type="text/javascript">
		function show() {

		}
	</script>

</html>