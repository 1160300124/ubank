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
map = new AMap.Map('container', {  
	resizeEnable: true,  
	zoom:11,  
});  

AMap.plugin(["AMap.Autocomplete","AMap.PlaceSearch"], function(){  
	
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
	AMap.event.addListener(autocomplete, "select", function(e){
	     //TODO 针对选中的poi实现自己的功能
	     //placeSearch.search(e.poi.name)
	     
	     var markerOption = {
	            map: map,
	            icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_r.png"
	        };
	     var marker = new AMap.Marker(markerOption);
         marker.setPosition(e.poi.location);
         $("#hidden_addr").val(e.poi.location);
	});
	

});  
</script>
</html>