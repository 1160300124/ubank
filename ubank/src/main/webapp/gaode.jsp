<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>优发展银行管理系统</title>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/images/login_logo.png" type="image/x-icon">
	<link href="<%=request.getContextPath()%>/css/bootstrap/bootstrap.min.css" rel="stylesheet" />
	<script src="<%=request.getContextPath()%>/js/jquery/jquery-1.12.3.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/bootstrap/bootstrap.min.js" type="text/javascript" ></script>
	<script src="<%=request.getContextPath()%>/js/common.js" type="text/javascript"></script>
	<link rel="stylesheet" href="http://cache.amap.com/lbs/static/main1119.css"/>  
	<script src="http://webapi.amap.com/maps?v=1.3&key=3b5fffa91c70b0e9b8118efcdc02ad4c"></script>
	<script src="http://webapi.amap.com/ui/1.0/main.js" type="text/javascript"></script>  
	<script type="text/javascript" src="http://webapi.amap.com/demos/js/liteToolbar.js"></script>  
	<script type="text/javascript" src="http://cache.amap.com/lbs/static/addToolbar.js"></script>

</head>
<body>
 	<form class="form-horizontal" role="form" id="map_form" style="width:100%;height:600px;background-color:#ccc;">
	      <div class="form-group" >
	           <div  id="container"></div>
	      </div>
	      <div class="form-group">
	       	<div class="col-sm-3" for="ds_host"></div>
	        <div class="col-sm-6">
	           <input class="form-control" id="attendance_addr" type="text" placeholder="请输入考勤地点"/>
	           <input type="hidden" id="hidden_addr" />
	        </div>
	      </div>
     </form>
</body>
<script type="text/javascript">
//加入高的地图  
var map = new AMap.Map('container', {  
	resizeEnable: true,  
	zoom:11,  
});  

//鼠标点击，获取经纬度坐标  
function getLnglat(e){    
    $("#hidden_addr").val(e.poi.location);
}

AMap.plugin(["AMap.Autocomplete","AMap.PlaceSearch","AMap.Geolocation"], function(){  
	
	var autoOptions = {
        city: "", //城市，默认全国
        input: "attendance_addr"//使用联想输入的input的id
    };
	//加载输入提示插件  
	var autocomplete = new AMap.Autocomplete(autoOptions);
	var placeSearch = new AMap.PlaceSearch({
        city : '',
        map : map
	});
	
/* 	var geolocation = new AMap.Geolocation({
        enableHighAccuracy: true,//是否使用高精度定位，默认:true
        timeout: 10000,          //超过10秒后停止定位，默认：无穷大
        maximumAge: 0,           //定位结果缓存0毫秒，默认：0
        convert: true,           //自动偏移坐标，偏移后的坐标为高德坐标，默认：true
        showButton: true,        //显示定位按钮，默认：true
        buttonPosition: 'LB',    //定位按钮停靠位置，默认：'LB'，左下角
        buttonOffset: new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
        showMarker: true,        //定位成功后在定位到的位置显示点标记，默认：true
        showCircle: true,        //定位成功后用圆圈表示定位精度范围，默认：true
        panToLocation: true,     //定位成功后将定位到的位置作为地图中心点，默认：true
        zoomToAccuracy:true      //定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
    });
    map.addControl(geolocation);
    geolocation.getCurrentPosition(); */
    
	AMap.event.addListener(autocomplete, "select", function(e){
	     //TODO 针对选中的poi实现自己的功能
	     //placeSearch.search(e.poi.name)
	     
	     var markerOption = {
	            map: map,
	            icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_r.png",
	            animation:"AMAP_ANIMATION_DROP",
	            draggable:true,  //点标记可拖拽  
	            cursor:'move',  
	            raiseOnDrag:true //鼠标拖拽点标记时开启点标记离开地图的效果
	        };
	     var marker = new AMap.Marker(markerOption);
         marker.setPosition(e.poi.location);
         $("#hidden_addr").val(e.poi.location);
	});
	

});  
</script>
</html>