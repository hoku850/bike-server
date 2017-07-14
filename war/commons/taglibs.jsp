<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://www.ccframe.org/sdk" prefix="sdk"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set value="${pageContext.request.contextPath}" var="webRoot" />
<%
    response.setHeader("cache-control","max-age=5,public,must-revalidate"); //one day
    response.setDateHeader("expires", -1);
%>


