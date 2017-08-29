$(function(){
	var map,addMarker; 
	var auto;
	var geocoder;  
	var placeSearch;  
	var infoWindow ;  
	var markers = [];  
	mapInit();
});

function mapInit(){  
	// 加入高的地图  
	map = new AMap.Map('container', {  
		resizeEnable: true,  
		zoom:11,  
		center: [114.06, 22.53]  
	});  
	
	AMap.plugin(["AMap.Autocomplete","AMap.PlaceSearch","AMap.ToolBar","AMap.Scale"], function(){  
		//加载输入提示插件  
		var autocomplete = new AMap.Autocomplete({
	        city: "", //默认全国
	        input: "attendance_addr"
	    });
		
		var placeSearch = new AMap.PlaceSearch({
            city:'',
            map:map
		});
		
	    AMap.event.addListener(autocomplete, "select", function(e) {//e含返回POI信息
	        var markerOption = {
	            map: map,
	            icon: "/images/result/icon-map-pos-hot.png"
	        };
	        marker = new AMap.Marker(markerOption);
	        marker.setPosition(e.poi.location);
	        
	        placeSearch.search(e.poi.name)
	    });
		
		map.addControl(new AMap.ToolBar());  

		map.addControl(new AMap.Scale());  

	});  
	
	AMap.service('AMap.Geocoder',function(){//回调函数  
		//实例化Geocoder  
		geocoder = new AMap.Geocoder({  
			city: "全国"//城市，默认：“全国”  
		});  
	});  
	// 初始化加载  
//	myMapViewLocation();  
	AMap.service(["AMap.PlaceSearch"], function() {  
		placeSearch = new AMap.PlaceSearch();  
	});  
	infoWindow = new AMap.InfoWindow({offset: new AMap.Pixel(0, -30)});//信息窗口  
	//为地图注册click事件获取鼠标点击出的经纬度坐标  
//	var clickEventListener = map.on('click', function(e) {  
//	$("input[name=longitude]").val(e.lnglat.lng);  
//	$("input[name=latitude]").val(e.lnglat.lat);  
//	// 填写地址  
//	writeAddress([e.lnglat.lng,e.lnglat.lat]);  
//	});  
	//键盘点击搜索  
	//placeSearch.search("北京");  
	$("#tip").on("keydown",function(event){  
		if(event = event || window.event){  
			if(event.keyCode==13){  
				mapsearch();  
			}  
		}  
	});    
}  

function select(e) {  
    placeSearch.setCity(e.poi.adcode);  
    placeSearch.search(e.poi.name);  //关键字查询查询  
} 

// 实例化点标记  
function addMarker(lnglatXY) {  
    if(map.getAllOverlays()!=''){  
    map.remove(marker);  
    map.remove(markers);  
    }  
    marker = new AMap.Marker({  
        icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",  
        position: lnglatXY  
    });  
    marker.setMap(map);  
    map.setFitView();// 执行定位  
}  

//地图搜索  
function mapsearch(){  
	//查询前先移除所有标注  
	if(map.getAllOverlays()!=''){  
		map.remove(marker);  
		map.remove(markers);  
	}  
	var searchVal = $("#tip").val();  
	placeSearch.search(searchVal,function(status, result){  
		if (status === 'complete' && result.info === 'OK') {  
			var poiArr = result.poiList.pois;  
			var str = "<ul>";  
			for(var i=0;i<poiArr.length;i++){  
				//在地图上创建标注点  
				marker = new AMap.Marker({  
					icon: "http://webapi.amap.com/theme/v1.3/markers/n/mark_b.png"  
				});  
				marker.setPosition(new AMap.LngLat(poiArr[i].location.lng,poiArr[i].location.lat));  
				marker.setMap(map);  
				marker.setLabel({//label默认蓝框白底左上角显示，样式className为：amap-marker-label  
					offset: new AMap.Pixel(3, 0),//修改label相对于maker的位置  
					content: String.fromCharCode(65+i)  
				});  
				marker.content = poiArr[i].name+"<br/>"+poiArr[i].address;  
				marker.on('click', markerClick);  
				markers.push(marker);  
				map.setFitView();  
				str+='<li>';  
				str+='<div class="res-data">';  
				str+='<div class="left res-marker">';  
				str+='<span>'+String.fromCharCode(65+i)+'</span>';  
				str+='</div>';  
				str+='<div class="left res-address">';  
				str+='<div class="title">'+poiArr[i].name+'</div>';  
				str+='<div>POI：<span class="poi">'+poiArr[i].id+'</span></div>';  
				str+='<div>地址：<span class="rout">'+poiArr[i].address+'</span></div>';  
				str+='<div>坐标：经度<span class="point">'+poiArr[i].location.lng+",纬度,"+poiArr[i].location.lat+'</span></div>';  
				str+='</div>';  
				str+='</div>';  
				str+='</li>';  
			}  
			str+='</ul>';  
			$("#panel").html(str);  
		}else{  
			new CAlert().toast_ERROR("未查询到相关地址");  
		}  
	});  

}  
