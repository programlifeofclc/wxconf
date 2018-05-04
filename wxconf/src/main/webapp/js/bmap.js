/**
		 * 地图工具函数
		 * MapTool 参数 opstion : {	
							city:城市, 优先级第三
							addr:位置, 有地址必须有城市  优先级第二
							point:百度位置, //new BMap.Point(117.269945,31.86713)  优先级第一
							level:16 显示的级别
		  					}
		  
		  例子、、	
		  		var maptool = MapTool({city:'北京'});
		 		
			 	初始化地图到一个dom元素 idc为元素id ,callfun 为回调函数
			 	
		 		maptool.init("id",function(data){
		 			//data为{type, target, pixel, point, zoom}
		 		});
		  
		  	(1)、
		  		获得本地城市 回调函数的参数为{
			 							center 	Point 	城市所在中心点。
										level 	Number 	展示当前城市的最佳地图级别，如果您在使用此对象时提供了map实例，则地图级别将根据您提供的地图大小进行调整。
										name 	String 	城市名称。 
		  							}
		  		maptool.getLocalCity(function(ops){
		  			//ops 包含 center, level , name
		  		})
		  		
		  	(2)、
		  		获得本地位置 参数为： 城市 ， 地址 ， 回调函数
		  		maptool.getPoint("北京","石景山区万商大厦",function(point){
		  			//point 为BMap.Point
		  		})
		  	
		  	(3)、
		  		添加比例控件  lr为right or left
		  		maptool.addScaleControl(lr);
		  		
		  		删除比例控件
		  		maptool.removeScaleControl();
		  	
		  	(4)、
		  		添加城市控件  lr为right or left
		  		maptool.addCityControl(lr);
		  		删除比例控件
			 	maptool.removeCityControl();
			  
			  
			(5)、
				中心点移动  point 为BMap.Point
				maptool.panTo(point);
			  
			(6)、
				添加点 参数 lng, lat 例如 116.404, 39.915  可直接传Point
				maptool.addMarker(lng,lat);
			  	
			 	添加自定义点 参数:
			 	lng,lat 例如 116.404, 39.915 
			 	url : ico地址
			 	width,height ico的宽高
			 	**********************************************
			 	此函数的返回值 为BMap.Marker 
			 	但多了一些方法 详情参看addIconMarker
			 	**********************************************
				maptool.addIconMarker(lng,lat,url,width,height);
			  	
			 (7)、
			 	清除打点  参数为空则全部清空，overlays为指定覆盖物
			 	maptool.clearOverlays(overlays);
			 
			 (8)、
			 	给地图添加点击事件 回调函数的参数为 
			 	maptool.click(function(event){
			 		//event {type, target, point, pixel, overlay}
			 	});
			 	
			  (9)、
			 	从一点画线到另一点 自驾
				maptool.drivingP2P(point,point);
			  
			  (10)、
			 	通过位置查询Point 
			 	text：搜索的关键字
			 	callfun:回调函数 参数为Point
				maptool.searchPoint(text,function(point){});
			  
			  
			  	搜索关键字出现多个列表
			  	参数：关键字 ，展示的位置(html标签id) ，回调函数当多个地址其中一个展示标注的时候 参数：{
			  																	poi: LocalResultPoi 通过其marker属性可得到当前的标注。
			  																	html: HTMLElement，气泡内的Dom元素 
			  																	} 
			  	maptool.searchListPoint(text,divid,function({poi,html}){
			  	
			  	});
			   
			  (11) 看方法体 没时间写了
			  
			
		  
		  
		 */	
		function MapTool(opstion){
			var mt = {};
			//工具类的所有参数
			mt.map = null;
			mt.scaleControl = null;
			mt.navigationControl = null;
			mt.cityControl = null;
			mt.convertor = null;
			//i 的值为数字 ： 0最少时间、1最少换乘、2最少步行、3不乘地铁
			mt.routePolicy = [0,2,3,4];
			
			mt.ops = {
					city:null,
					addr:null,
					point:null, //new BMap.Point(117.269945,31.86713)
					level:13
			};
			
			/**
			 * 设定初始参数
			 */
			mt.setOps = function(ops){
				mt.ops = $.extend(mt.ops,ops||{});
			}
			
			/**
			 * 获得本地城市 回调函数 的参数回传为
			    center 	Point 	城市所在中心点。
				level 	Number 	展示当前城市的最佳地图级别，如果您在使用此对象时提供了map实例，则地图级别将根据您提供的地图大小进行调整。
				name 	String 	城市名称。 
			 */
			mt.getLocalCity = function(callfun){
				var myMapCity = new BMap.LocalCity();
				myMapCity.get(callfun);
			}
			
			/**
			 * 获得本地位置 有回调函数  参数回传为 Point
			 */
			mt.getPoint = function(city,addr,callfun){
				// 创建地址解析器实例
				var myGeo = new BMap.Geocoder();
				myGeo.getPoint(addr,callfun, city);
			}
			
			/**
			 * 获得Point 有回调函数  参数回传为 地址
			 */
			mt.getAddrByPoint = function(point,callfun){
				var myGeo = new BMap.Geocoder();
				myGeo.getLocation(point, function(rs){
					var addComp = rs.addressComponents;
					var addr = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
					var short_addr = addComp.street + addComp.streetNumber;
					callfun(addr,short_addr);
				});
			}
			
			
			/**
			 * 开启左右上角的缩放插件lr 为left 或者 right 默认为left
			 */
			mt.addScaleControl = function(lr){
				if(lr == "right"){
					mt.scaleControl = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_RIGHT});
					mt.navigationControl = new BMap.NavigationControl();
				}else{
					mt.scaleControl = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
					mt.navigationControl = new BMap.NavigationControl();
				}
				mt.map.addControl(mt.scaleControl);
				mt.map.addControl(mt.navigationControl);
			}
			
			/**
			 * 移除比例控件
			 */
			mt.removeScaleControl = function(){
				if(mt.scaleControl){
					mt.map.removeControl(mt.scaleControl);
				}
				if(mt.navigationControl){
					mt.map.removeControl(mt.navigationControl);
				}
			}
			
			/**
			 * 开启城市控件 参数 "right" or  "left"
			 */
			mt.addCityControl = function(lr){
				var size = new BMap.Size(10, 20);
				if(lr == "left"){
					mt.cityControl = new BMap.CityListControl({
			              anchor: BMAP_ANCHOR_TOP_LEFT,
			              offset: size
			          });
				}else{
					mt.cityControl = new BMap.CityListControl({
			              anchor: BMAP_ANCHOR_TOP_RIGHT,
			              offset: size
			          });
				}
				mt.map.addControl(mt.cityControl);
			}
			
			/**
			 * 移除城市控件
			 */
			mt.removeCityControl = function(){
				if(mt.cityControl){
					mt.map.removeControl(mt.cityControl);
				}
			}
			
			/**
			 * 初始化地图到一个dom元素 idc 为元素id
			 * callfun 为回调函数 当地图初始化完毕后回调 {type, target, pixel, point, zoom}
			 */
			mt.init = function(idc,callfun){
				var map = new BMap.Map(idc);
				map.addEventListener("load",function(opt){
					callfun?callfun(opt):null;
		        });
				
				if (mt.ops.point){
					map.centerAndZoom(mt.ops.point, mt.ops.level);
				} else if (mt.ops.addr && mt.ops.city) {
					var level = mt.ops.level;
					mt.getPoint(mt.ops.city, mt.ops.addr, function(point){
						map.centerAndZoom(point, level);
					});
				} else if (mt.ops.city){
					map.centerAndZoom(mt.ops.city);
				} else {
					mt.getLocalCity(function(cops) {
						map.centerAndZoom(cops.center, cops.level);
					})
				}
				
				map.enableScrollWheelZoom(true);
				mt.map = map;
			};
			
			/**
			 * 移动地图中心点
			 */
			mt.panTo = function(point){
				mt.map.panTo(point);
			}
			
			/**
			 * 添加点 参数 lng, lat 例如 116.404, 39.915
			 */
			mt.addMarker = function(lng,lat){
				if(arguments.length == 2){
					return mt.addIconMarker(lng,lat);
				}else{
					return mt.addIconMarker(lng.lng,lng.lat);
				}
			}
			
			/**
			 * 添加点 参数 lng,lat 例如 116.404, 39.915 
			 * url : ico地址
			 * width,height
			 */
			mt.addIconMarker = function(lng,lat,url,width,height){
				var point = new BMap.Point(lng,lat);
				var marker;
				if(url){
					width = width?width:20;
					height = height?height:20;
					var myIcon = new BMap.Icon(url, new BMap.Size(width,height));
					marker = new BMap.Marker(point,{icon:myIcon});
				}else{
					marker = new BMap.Marker(point);
				}
				mt.map.addOverlay(marker);
				
				/**
				 * 标记动画
				 */
				marker.anima = function(){
					marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
					return marker;
				}
				/**
				 * 自我删除
				 */
				marker.rem = function(){
					mt.map.removeOverlay(marker);
					return marker;
				}
				/**
				 * 添加下部文字
				 */
				marker.addLabel = function(text){
					var w = - (text.length/2 * 8);
					var label = new BMap.Label(text,{offset:new BMap.Size(w,25)});
					marker.setLabel(label);
					return marker;
				}
				/**
				 * 添加点击事件 回调函数 参数为event{type, target} 
				 */
				marker.click = function(callfun){
					marker.addEventListener("click", callfun||function(a){});
					return marker;
				}
				return marker;
			}
			
			/**
			 * 清空地图打点
			 */
			mt.clearOverlays = function(overlays){
				if(overlays){
					mt.map.removeOverlay(overlays);
				}else{
					mt.map.clearOverlays();
				}
			}
			
			
			/**
			 * 地图点击事件 回调函数 {type, target, point, pixel, overlay}
			 */
			mt.click = function(callfun){
				mt.map.addEventListener("click", callfun||function(a){});
			}
			
			
			
			/**
			 * 从一点画线到另一点 自驾
			 */
			mt.drivingP2P = function(point1,point2){
				var driving2 = new BMap.DrivingRoute(mt.map, {renderOptions:{map: mt.map, autoViewport: true}});    //驾车实例
				driving2.search(point1, point2);    //显示一条公交线路
			}
			
			/**
			 * 参数： 搜索的关键字 展示的位置  回调函数当多个地址其中一个展示标注的时候 参数：poi: LocalResultPoi，通过其marker属性可得到当前的标注。 html: HTMLElement，气泡内的Dom元素 
			 */
			mt.searchListPoint = function(text,divid,callfun){
				var local = new BMap.LocalSearch(mt.map, {
						renderOptions: {
							map: mt.map,
							panel: divid
						}
					});
				local.setInfoHtmlSetCallback(callfun||function(o){});
				local.setResultsHtmlSetCallback(function(container){
					$(container).parent().show();
				});
				local.search(text);
			}
			
			/**
			 * 通过位置查询Point 回调函数的参数为获得第一个Point
			 */
			mt.searchPoint = function(text,callfun){
				var local = new BMap.LocalSearch(mt.map, {
					  onSearchComplete: function(){
						  var pp = local.getResults().getPoi(0).point;
						  callfun?callfun(pp):null;
					  }
				});
				local.search(text);
			}
			
			/**
			 * 搜索公交：start,end,divid,i ： 开始位置 结束位置  渲染的位置 搜索的条件
			 * i 的值为数字 ： 0最少时间、1最少换乘、2最少步行、3不乘地铁
			 */
			mt.searchTransit = function(start,end,divid,i){
				var transit = new BMap.TransitRoute(mt.map, {
					renderOptions: {map: mt.map, panel: divid}
				});
				if(arguments.length == 4)
					transit.setPolicy(mt.routePolicy[i]);
				transit.search(start, end);
			}
			
			/**
			 * 查看徒步路线 参数 开始位置  结束位置  查询反馈的结果divid
			 */
			mt.searchWalking = function(start,end,divid){
				var walking = new BMap.WalkingRoute( mt.map, {renderOptions:{map:mt.map, panel:divid, autoViewport: true}});
				walking.search(start, end);
			}
			
			/**
			 * 国际转百度  points 可为 BMap.Point(116.38632786853032,39.90795884517671)  或者 [BMap.Point(116.38632786853032,39.90795884517671),BMap.Point(116.38632786853032,39.90795884517671)]
			   callfun 为回调函数 points为数组则回传的参数为BMap.Point 数组，points为单点则回传的参数为BMap.Point。 出错不传值
			 */
			mt.gj2baidu = function(points,callfun){
				if(!mt.convertor){
					mt.convertor = new BMap.Convertor();
				}
				if(points instanceof Array){
					mt.convertor.translate(points, 1, 5, function(data){
						if(data.status === 0){
							callfun(data.points);
						}else{
							callfun();
						}
					});
				}else{
					mt.convertor.translate([points], 1, 5, function(data){
						if(data.status === 0){
							callfun(data.points[0]);
						}else{
							callfun();
						}
					})
				}
			}
			
			/**
			 * 输入框自动补全 idc为input id，  callfun为点击下拉框选项时候的回调  参数为object{type,target,item} ,addr 选定的全地址
			 */
			mt.autoComplete = function(idc,callfun){
				var v = $("#" + idc).val();
				var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
						{"input" : idc
						,"location" : mt.map,
						onSearchComplete:function(){
							$(".tangram-suggestion-main").css("zIndex",1060);
							$(".tangram-suggestion table tr td").css("textAlign","left");
							$(".tangram-suggestion table tr td i").css("display","inline");
							$(".address-map-area").css({"width":"500px","height":"250px","marginLeft":"103px","border":"1px solid #CCCCCC"});
						}
					});
				ac.setInputValue(v||"");
				ac.addEventListener("onconfirm",function(e){
					var _value = e.item.value;
					var addr = _value.province +  _value.city +  _value.district +  _value.street +  _value.business;
					var short_addr = _value.street +  _value.business;
					callfun?callfun(e,addr,short_addr):null;
				});
				ac.val = function(text){
					ac.setInputValue(text||"");
				}
				return ac;
			}
			
			
			//设置传递的初始参数;
			opstion ? mt.setOps(opstion):"";
			
			return mt;
		}