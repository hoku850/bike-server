<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglibs.jsp" %>
<c:set var="userToRepairRecordId" value="50001"/>
<c:set var="aMapData" value="${sdk:getAMapDataByUserToRepairRecordId(userToRepairRecordId )}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>保修地点</title>
    <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
    <script src="http://webapi.amap.com/maps?v=1.3&key=267b137939c571fa3f59564ed9cfffaa"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
</head>
<body>
<div id="container"></div>
<script>

	// 地图核心
    var map = new AMap.Map('container', {
        resizeEnable: true,
        center: ${aMapData.centerPosition },
        zoom: ${aMapData.zoom }
    });
    
    // 标记点
    var marker = new AMap.Marker({
        icon: ${aMapData.icon },
        position: ${aMapData.iconPosition }
    });
    marker.setMap(map);

</script>
</body>
</html>