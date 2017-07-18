<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/commons/taglibs.jsp" %>
<c:set var="userToRepairRecordId" value="${param.userToRepairRecordId}"/>
<c:set var="aMapData" value="${sdk:getAMapDataByUserToRepairRecordId(userToRepairRecordId )}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>报修位置显示</title>
    <link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>
    <script type="text/javascript" src="http://webapi.amap.com/maps?v=1.3&key=267b137939c571fa3f59564ed9cfffaa&plugin=AMap.Geocoder"></script>
    <script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>
</head>
<body onload="regeocoder()">
<div id="container"></div>
<div id="tip">
    <b>经纬度 ${aMapData.centerPosition } 的地理编码结果:</b>
    <span id="result"></span>
</div>
<script type="text/javascript">

    var map = new AMap.Map("container", {
        resizeEnable: true,
		zoom: 18
    }),    
    lnglatXY = ${aMapData.centerPosition }; //已知点坐标
    function regeocoder() {  //逆地理编码
        var geocoder = new AMap.Geocoder({
            radius: 1000,
            extensions: "all"
        });        
        geocoder.getAddress(lnglatXY, function(status, result) {
            if (status === 'complete' && result.info === 'OK') {
                geocoder_CallBack(result);
            }
        });        
        var marker = new AMap.Marker({  //加点
            map: map,
            position: lnglatXY
        });
        map.setFitView();
    }
    function geocoder_CallBack(data) {
        var address = data.regeocode.formattedAddress; //返回地址描述
        document.getElementById("result").innerHTML = address;
    }
</script>
</body>
</html>