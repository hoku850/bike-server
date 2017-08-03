<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglibs.jsp" %>
<c:set var="orgList" value="${sdk:getOrgList()}"/>
<c:forEach items="${pageContext.request.cookies}" var="co"> 
  <c:if test="${co.name == 'lastLoginId'}">
    <c:set var="lastLoginId" value="${co.value}"/>
  </c:if>
  <c:if test="${fn:contains(co.name,'lastOrgId')}">
    <c:set var="lastOrgId" value="${co.value}"/>
  </c:if>
</c:forEach>
<c:if test="${empty lastOrgId}"><c:set var="lastOrgId" value="1"/></c:if>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="gwt:property" content="locale=zh" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>后台管理登陆</title>
    <link href="style/login.css" rel="stylesheet" type="text/css"/>
		<link rel="shortcut icon" type="image/ico" href="${webRoot}/favicon.ico"/>
    <script type="text/javascript" language="javascript" src="${webRoot}/common-statics/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" language="javascript" src="${webRoot}/common-statics/jquery.sha512.js"></script>
    <script type="text/javascript" language="javascript" src="${webRoot}/common-statics/jquery.corner.js"></script>
    <script type="text/javascript" language="javascript" src="${webRoot}/common-statics/jquery.form.js"></script>
    <script type="text/javascript" language="javascript" src="${webRoot}/common-statics/jquery.cookie.min.js"></script>
    <script type="text/javascript" language="javascript">
	function checkLoginForm() {
		var loginId = $("#loginId").val();
		var orgId = $("input[name='orgId']:checked").val();
		var useruserPsw = $("#userPsw").val();
		var validateCode = $("#validateCode").val();
		var LoginURI = $("#LoginURI").val();
		
		if (loginId == "" || loginId == "请输入管理员用户名" || loginId == null) {
			$("#error_hint").css("color","#FF3333").text("请输入管理员用户名");
			return false;
		}
		if (useruserPsw == "" || useruserPsw == null) {
			$("#error_hint").css("color","#FF3333").text("请输入管理员密码");
			return false;
		}
		if (validateCode == "" || validateCode== "输入左侧验证码" || validateCode == null) {
			$("#error_hint").css("color","#FF3333").text("请输入验证码");
		    return false;
		}
		//非空验证结束
		var params = $("#loginForm").formToArray();
		$.ajaxSettings['contentType'] = "application/x-www-form-urlencoded; charset=utf-8";
		$(".btnLogin").attr("disabled",true);
		$(".btnLogin").after("<img src='images/waiting.gif' style='float:left; margin-top:4px'></img>");
		$.ajax({	
			type:"POST",
			url:"<c:url value='/admin/org/mainFrame/doLogin.json'/>",
			data:{loginId:loginId,userPsw:$.sha512(useruserPsw),validateCode:validateCode,LoginURI:LoginURI},
			dataType: "json",
			success:function(data) {
				$.cookie('lastLoginId', loginId, {expires:7}); 
				$.cookie('lastOrgId', orgId, {expires:7}); 
				location.href = "<c:url value="/admin.jsp"/><c:if test="${!empty param}">?<%=request.getQueryString()%></c:if>";
			},
			error:function(XMLHttpRequest, textStatus) {
				if (XMLHttpRequest.status == 500) {
					var result = eval("(" + XMLHttpRequest.responseText + ")");
					//var error = eval("(" + result + ")");
					$(".btnLogin").attr("disabled",false);
					$(".btnLogin + img").remove();
					$("#error_hint").css("color","#FF3333").text(result.errorObjectResp.errorText);
					$("#captchaImg").attr("src", "<c:url value="/captchaImg?c=47,94,149&t="/>" + Math.random());
				}
			}
		});
		return false;
	}

	function inputValidate(input, text){
		input.blur(function(){
			if(input.val()==''){
				input.val(text);
				input.css('color','#aaa');
			}
		});
		input.focus(function(){
			if(input.val()==text){
				input.val('');
				input.css('color','#333');
			}
		});
		input.val(text);
	}

	$(document).ready(function(){
		$("#body_center_float").corner();
		if(!(!$.support.opacity && !$.support.style && window.XMLHttpRequest==undefined)){ // browser > IE6
			$(".login_input").corner("round 5px");
		}
		$("#captchaImg").attr("src", "${webRoot}/captchaImg?c=47,94,149&t=" + Math.random());
		$("#captchaImg,#refreshImg").click(function(){
			$("#captchaImg").attr("src", "${webRoot}/captchaImg?c=47,94,149&t=" + Math.random());
		});
		inputValidate($("#loginId"), "请输入管理员用户名");
<c:if test="${!empty lastLoginId}">
	$("#loginId").css('color','#333');
	$("#loginId").val("${lastLoginId}");
</c:if>
		inputValidate($("#userPsw"), "");
		inputValidate($("#validateCode"), "输入左侧验证码");

		$("#userPswHint").focus(function(){
			$("#userPswHint").hide();
			$("#userPsw").show().focus();
		});
		$("#userPsw").blur(function() {
			if ($("#userPsw").val()=="") {
				$("#userPswHint").show();
				$("#userPsw").hide();
			}
		});

	})
   </script>
<c:if test="${fn:length(list) == 1}">
   <style type="text/css">
   <!--
   	.login_row_div{height:42px;}
   	.login_select_zone{display:none;}
   -->
   </style>
</c:if>
</head>

<body>
	<div id="body_center">
		<div id="body_top" style="color:white">欢迎使用 <c:out value="${sdk:getParamValue('siteNm')}"/> 系统平台</div>
		<div style="height:560px;"></div>
		<div id="body_bottom" style="color:white">Copyright 2010-2015 <c:out value="${sdk:getParamValue('siteUrl')}"/></div>
	</div>
	<div id="body_center_float">
		<div id="body_center_content">
		    <form method="post"  id="loginForm" name="loginForm">
		    <input style="display:none;" type="text" name="loginId"><!-- 360的样式兼容 -->
		    <input style="display:none;" type="password" name="userPsw"><!-- 360的样式兼容 -->
			<div style="height:40px"></div>
			<div id="left_logo"><img src="<c:url value="/ccframe/images/left_logo.gif"/>"/></div>
			<div id="login_form_div">
				<div id="login_form_title">运营商后台管理<span>ver 1.0</span></div>
				<div id="error_hint" class="login_row_div">请输入管理员登录账户及密码</div>
				<div class="login_row_div">
					<input type="text" maxlength="32" class="login_input" id="loginId" name="loginId"></input>
				</div>
				<div class="login_row_div">
					<input style="display:none" type="password" maxlength="18" class="login_input" id="userPsw" name="userPsw"></input>
					<input type="text" maxlength="18" class="login_input" id="userPswHint" name="userPswHint" value="请输入管理员密码"></input>
				</div>
				<div class="login_row_div login_select_zone">

				</div>
				<div class="login_row_div">
					<img id="captchaImg" width="100" height="35"/>
					<img id="refreshImg" src="<c:url value="/ccframe/images/refresh_side.gif"/>" width="24" height="37"/>
					<input type="text" maxlength="4" class="login_input" id="validateCode" name="validateCode"></input>
				</div>
				<div class="login_row_div">
					<input class="btnLogin" type="image" src="images/login_button.gif" width="95" height="28" onclick="return checkLoginForm();"/>
					<div style="color:#333;float:left;padding-top:8px;padding-left:15px">忘记密码？</div>
				</div>	
			</div>
			</form>
		</div>
	</div>
</body>
</html>
