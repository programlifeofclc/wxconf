		/**
    		url:页面地址 必填 <br/>
    		org:要向后台传递的参数{ } 可选<br/>
    		func:加载完毕后执行的函数 可选<br/>
    		option: 模态框参数{  backdrop:boolean或 'static' , width : "30%" 百分比     }   可选<br/>
    		id:自己指定模态框id   可选<br/>
    		调用实例 openModule("/jsp/dome/module.html");
    	*/
		function openModule(url,org,func,option,id){
    		var $load;
	    		if(id){
	    			$load = $("#"+id).length > 0 ? $("#"+id):$("<div id='" + id + "' ></div>");
	    		}else{
	    			$load = $("#moudle_one").length > 0 ? $("#moudle_one"):$("<div id='moudle_one' ></div>");
				}
	    		var backdrop = ((typeof option) =='undefined' || (typeof option.backdrop)=='undefined')?'static':option.backdrop;
    			$load.appendTo($("body"))
    				 .load(url,org, function(){
	    					 			if(id){
	    					 				var n = $(".moudle_class").size() + 1;
				    						if(n == 1){
				    							$("body").css("overflow-y","hidden");
				    						}else{
				    							$(".moudle_index_" + (n - 1) + " .modal").css("overflow-y","hidden");
				    						}
				    						$load.addClass("moudle_class");
				    						$load.addClass("moudle_index_" + n);
	    					 			}
    					 				option && option.width ? $load.find(".modal-dialog").css({width:option.width}):null;
    					 				$load.find("> .modal").modal({
    					 												show:true,
    					 												backdrop:backdrop
    					 											})
											 .on("hidden.bs.modal", function() {
				    													$(this).removeData("bs.modal");
				    													$(this).remove();
																	});
    					 				(typeof func)=="function"?func():"";
						});
		}
		
		/**
		 * 模态框关闭
		 * @param id 打开时候用了id 关闭时候传递id
		 */
		function closeModule(id,func,defaultid){
			if(id){
				$("#"+id).find("> .modal").on("hidden.bs.modal", function() {
					$(this).removeData("bs.modal");
					var n = $(".moudle_class").size();
						$(".moudle_index_" + n).removeClass();
						if(n > 1){
							$(".moudle_index_" + (n - 1) + " .modal").css("overflow-y","auto");
						}else{
							$("body").css("overflow-y","auto");
						}
					if(func){
						func();
					}
				});
				 $("#"+id).find("> .modal").modal("hide");
			}else{
				$("#moudle_one").find("> .modal").on("hidden.bs.modal", function() {
					$(this).removeData("bs.modal");
					$(".moudle_class").removeClass();
					$("body").css("overflow-y","auto");
					if(func){
						func();
					}
				});
				$("#moudle_one").find("> .modal").modal("hide");
			}
			if(defaultid){
				$("#" +defaultid).addClass("" +defaultid);
			}
		}
		
		
		//交易状态
		function showMsg(obj) {
			var msg = "";
			switch (obj) {
					case '00':
						msg = "操作成功！";
						break;
					case '01':
						msg = "操作失败，请刷新后重试！";
						break;
					case '02':
						msg = "无记录！";
						break;
					case '03':
						msg = "未注册！";
						break;
					case '04':
						msg = "未授权！";
						break;
					case '05':
						msg = "未登录！";
						break;
					case '06':
						msg = "未查询到相关数据！";
						break;
					case '07':
						msg = "无法上传档案附件,因档案状态不是暂存或审核未通过状态！";
						break;
					case '08':
						msg = "无权限删除档案附件！";
						break;
					case '09':
						msg = "无法删除档案附件,因档案状态不是暂存或审核未通过状态！";
						break;
					default:
						msg = obj;
						break;
				}
			if(msg != null && msg != '00' && msg != '' ){
				openAlert(msg,"提示:",3000);
			}
			if(obj == '05'){
				window.location.href="/manager";
			}
		}
		
		
		/**
		 * 警告框
		 * @param id 打开时候用了id 关闭时候传递id
		 */
		function openAlert(text,title,delay){
			delay = delay?delay:3000;
			$("#default_meg").html((text?text:''));
			window.setTimeout(function(){
				$("#default_meg").html("");
			}, delay);
		}
		
		

		/**
	 * jquery 依赖 bootstrap 插件扩展
	 * pagination: page 为一个json数据 func 为回调函数 同时会回传两个参数 curPage,pageCount
	 */
		$.fn.extend({
			//获取当前页
			getCurPage:function(){
				return this.data("curPage");
			},
			//分页扩展
			pagination:function(page,func,source){
				var func = func?func:toPage;
				this.empty();
				var pageCount = (page.pageCount > 0)?page.pageCount:20;
				
				this.data("curPage",page.curPage);
				
				this.addClass("pagination pagination-sm  navbar-right")
				
				this.append($("<li><a href='javascript:void(0)' title='首页'><<</a></li>").click(function(){
					func(1,pageCount,source);
				}));
				this.append($("<li><a href='javascript:void(0)' title='上一页'><</a></li>").click(function(){
					func(page.curPage<=1?1:(page.curPage-1),pageCount,source);
				}));
				if(page.maxPage <= 6){
					for(var i = 1; i <= page.maxPage ; i++){
						this.append($("<li " + ((i == page.curPage)?"class='active'":'') + " ><a href='javascript:void(0)'>" + i + "</a></li>").click(i,function(e){
							func(e.data,pageCount,source);
						}));
					}
				}
				
				if(page.maxPage > 6){
					for(var i = 1; i <= page.maxPage ; i++){
						if(i <= 2){
							this.append($("<li " + ((i == page.curPage)?"class='active'":'') + " ><a href='javascript:void(0)'>" + i + "</a></li>").click(i,function(e){
								func(e.data,pageCount,source);
							}));
						}
						if(i > 2 && i <= page.maxPage - 2){
							this.append($("<li><a href='javascript:void(0)'>...</a></li>"));
							if(page.curPage > 2 && page.curPage <= page.maxPage - 2){
								this.append($("<li class='active'><a href='javascript:void(0)'>" + page.curPage + "</a></li>").click(function(){
									func(page.curPage,pageCount);
								}));
							}
							this.append($("<li><a href='javascript:void(0)'>...</a></li>"));
							i = page.maxPage - 2;
						}
						
						if(i > 3 && i > page.maxPage - 2){
							this.append($("<li " + ((i == page.curPage)?"class='active'":'') + "><a href='javascript:void(0)'>" + i + "</a></li>").click(i,function(e){
								func(e.data,pageCount,source);
							}));
						}
					}
				}
				this.append($("<li><a href='javascript:void(0)' title='下一页'>></a></li>").click(function(){
					func((page.curPage == page.maxPage)?page.curPage:(page.curPage+1),pageCount,source);
				}));
				this.append($("<li><a href='javascript:void(0)' title='尾页'>>></a></li>").click(function(){
					func(page.maxPage,pageCount,source);
				}));
				
				//{"filterRecord":0,"pageCount":15,"curPage":1,"totalRecord":27,"maxPage":2}
				
				var div = $("<div class='input-group input-group-sm'></div>");
				
				$("<span class='input-group-addon' style='background:#fff;color:#337ab7;border-left:none'>每页</span>")
				.appendTo(div)
				
				$("<select class='form-control' style='width: 70px'><option>1</option><option>5</option><option>10</option><option>15</option><option>20</option><option>50</option><option>100</option></select>")
				.appendTo(div)
				.val(page.pageCount)
				.change(function(){
					pageCount = $(this).val();
					func(1,pageCount,source);
				})
				
				$("<span class='input-group-addon' style='background:#fff;color:#337ab7;'>条</span>")
				.appendTo(div)
				
				this.append($("<li class='form-inline'></li>").append(div));
				
				this.append("<li><span>共" + page.totalRecord + "条</span></li>");
			  	
				var div2 = $("<div class='input-group input-group-sm' style='width:90px;'></div>");
				$("<input type='text' class='form-control'>").appendTo(div2);
				$("<span class='input-group-addon' style='color:#337ab7;cursor: pointer;'>GO</span>")
				.appendTo(div2)
				.click(function(){
					var index = $(this).siblings("input").val();
					if(index && parseInt(index) > 0 && parseInt(index) <=  page.maxPage){
						func(parseInt(index),pageCount,source);
					}else{
						openAlert("页码超出范围!","警告:",1000);
					}
				});
			  	
			  	this.append($("<li class='form-inline'></li>").append(div2));
			},
			div2Module:function(title){
				var thiz = this;
				var context = thiz.children();
				var id = "mod" + (new Date().getTime());
				title = title?title:"";
				var mcontent = $("<div class='modal-content'><div class='modal-header'><button type='button' class='close' data-dismiss='modal' aria-hidden='true'>&times;</button><h4 class='modal-title'>" + title + "</h4></div></div>");
				mcontent.append(context);
				var mlog = $("<div class='modal-dialog'></div>");
				mlog.append(mcontent);
				var modal = $("<div class='modal fade' id='" + id + "' tabindex='-1' role='dialog'  aria-labelledby='myModalLabel' aria-hidden='true'></div>")
							.append(mlog);
		    	modal.appendTo($("body")).modal({show:true}).on("hidden.bs.modal", function() {
		    		thiz.append(context);
					$(this).removeData("bs.modal");
					modal.remove();
					modal = null;
				});
		    	var close = thiz.close = function(){
		    		modal.modal("hide");
		    	}
		    	thiz.data("dm",close);
		    	return thiz;
			},
			modClose:function(){
				this.data("dm")();
			},
			eachTab:function(data,func){
				this.data("tabfunc",func);
				var thiz = this;
				$.each(data,function(i,v){
					var $tr = $(func(i,v));
					thiz.data("tabindexdata" + i,v);
					thiz.data("tabindex" + i,$tr);
					thiz.append($tr);
				});
			},
			eachTabUpdate:function(index,data,func){
				var oldata = this.data("tabindexdata" + index);
				$.extend(oldata,data);
				var $tr = $(this.data("tabfunc")(index,oldata));
				this.data("tabindex" + index).replaceWith($tr);
				//再次存储
				this.data("tabindex" + index,$tr);
				this.data("tabindexdata" + index,oldata);
				$tr.trigger("click");
				if(func){
					func($tr);
				}
				
			}
		});
		
		//判断是否存在menu
		function menuItem(menuId,str){
			if(menuId instanceof Array){
				$.db_menus = sortArray(menuId);
			}
			else if((typeof menuId) == "number" || (typeof menuId) == "string"){
				if(str === "" || str){
					return findArray($.db_menus,menuId)?str:"";
				}else{
					return findArray($.db_menus,menuId);
				}
			}
			
			function findArray(arr,val){
				if(!arr) return true;//正式上线需要改为false;
				if(arr.length <= 1){
					if(arr[0] == val){
						return true;
					}else{
						return false; 
					}
				}else{
					var i = Math.round(arr.length/2);
					if(arr[i] == val) return true;
					if(arr[i] > val){
						return findArray(arr.slice(0,i),val);
					}else if(arr[i] < val){
						return findArray(arr.slice(i),val);
					}
				}
			}
			
			function sortArray(arr){
				var tmp = null;
				for(var i = 0; i < arr.length; i++){
					for(var j = i + 1; j < arr.length; j++){
						if(arr[i] > arr[j]){
							tmp = arr[i];
							arr[i] = arr[j];
							arr[j] = tmp;
						}
					}
				}
				return arr;
			}
		}
		
		//判断是否存在menu 此方法可带左侧菜单
		function menuButton(leftId,menuId,str){
			if($.currentMenuId != leftId){
				if(str === "" || str){
					return "";
				}else{
					return false;
				}
			}else{
				return menuItem(menuId,str);
			}
		}
		
		
		//按钮权限 传入数据的用户id
		function menuData(dataId){
			var json = jQuery.parseJSON($.cookie("manager"));
			var managerId = json['id'];
			var permissionLevel = json['permissionLevel'];
			if(permissionLevel && permissionLevel == 3 && managerId && managerId != dataId){
				$.alert({
					millis: 2000,
					text: '您没有修改此记录的权限!'
				});
				return false;
			}
			return true;
		}
		
		//仅被刘晨曦的呼叫系统使用
		function authPageLoad(url){
    		$('#page-wrapper').html("");
    		$('#page-wrapper').load(url);
    	}
		
		/**
		 * jquery 静态扩展 消息框 默认-1
		 * 参数:
		 * option:{
		 * 		millis:毫秒数 大于0就会在此时间内自动关闭次框
		 * 		title:标题名 默认为 "消息框"
		 * 		text: 内容
		 * 		top:  距离上部距离
		 * 		width: 宽度 默认的 "300px"
		 * 		buttons: 弹出框的下部按钮为数组格式 例:[ 
		 * 										{
		 * 											name:按钮名称
		 * 										 	handler:点击按钮执行的函数
		 * 											},
		 * 										{
		 * 											name:按钮名称
		 * 											handler:点击按钮执行的函数
		 * 										}	
		 * 									   ]
		 * }
		 */
		$.message = function(option){
			option = option||{};
			var ops = {
					millis:-1,
					title:"消息框",
					text:"",
					top:"100px",
					width:"300px",
					buttons:[{
								name:'确定',
								handler:function(){
									return true;
								}
							},{
								name:'取消',
								handler:function(){
									return true;
								}
							}]
			};
			$.extend(ops,option);
			if($('#mod_message').length > 0){
				$('#mod_message').remove();
			}
			var left = parseInt($("#wrapper").parent().width() - ops.width.replace("px","") )/2 + "px";
			var content = $("<div id='mod_message' class='modal-content'></div>").css({"width":ops.width,"position":"fixed","top":"0px","left":left ,"zIndex":"2051","opacity": 0});
			if(ops.title){
				content.append('<div class="modal-header"><span style="font-size:14px;font-weight: bold;">'+ ops.title +'</span></div>');
			}
			content.append('<div class="modal-body"><div class="bootbox-body">'+ ops.text +'</div></div>');
			if(ops.buttons){
				var footer = $('<div class="modal-footer"></div>');
				$.each(ops.buttons,function(i,o){
					footer.append($('<button class="btn btn-primary" type="button" >' + o.name + '</button>').click(function(){
						if(o.handler()){
							 content.animate({ "top":"0px",opacity:0},300,function(){
								 content.remove();
							 });
						}
					}));
				});
				content.append(footer);
			}
	        $("#wrapper").before(content);
	        content.animate({ "top":ops.top,opacity:1},300 );
	        if(ops.millis > 0){
	        	window.setTimeout(function(){
	        		 content.animate({ "top":"0px",opacity:0},300,function(){
						 content.remove();
					 });
	        	}, ops.millis);
	        }
		}
		
		/**
		 * jquery 静态扩展 警告框
		 * 参数:
		 * option:{
		 * 		millis:毫秒数 大于0就会在此时间内自动关闭次框 默认-1
		 * 		title:标题名 默认为 "警告框"
		 * 		text: 内容
		 * 		top:  距离上部距离
		 * 		width: 宽度 默认的 "300px"
		 * }
		 */
		$.alert = function(option){
			option = option||{};
			var ops = {
					title:"温馨提示",
					buttons:[{
								name:'确定',
								handler:function(){
									if(option && option.callback && (typeof option.callback == 'function')){
										option.callback(true);
									}
									return true;
								}
							}]
			};
			$.extend(ops,option);
			$.message(ops);
		}
		
		/**
		 * jquery 静态扩展 确认框
		 * 参数:
		 * option:{
		 * 		millis:毫秒数 大于0就会在此时间内自动关闭次框 默认-1
		 * 		title:标题名 默认为 "警告框"
		 * 		text: 内容
		 * 		top:  距离上部距离
		 * 		width: 宽度 默认的 "300px",
		 *      callback： 点击 ‘是’ 或者 ‘否’ 按钮后的回调函数  并传递参数  点是传递 true  点否 传递false
		 * }
		 */
		$.confirm = function(option){
			option = option||{};
			var ops = {
					title:"温馨提示",
					buttons:[{
						name:'是',
						handler:function(){
							if(option && option.callback && (typeof option.callback == 'function')){
								option.callback(true);
							}
							return true;
						}
					},{
						name:'否',
						handler:function(){
							if(option && option.callback && (typeof option.callback == 'function')){
								option.callback(false);
							}
							return true;
						}
					}]
			};
			$.extend(ops,option);
			$.message(ops);
		}
		
		
		/**
		 * 右下角提示框
		 */
		$.winInfo = function(option){
			option = option||{};
			var ops = {
					id:"mod_winInfo",
					millis:-1,
					title:"消息框",
					text:"",
					width:230,
					height:350
			};
			$.extend(ops,option);
			if($('#' + ops.id).length > 0){
				$('#' + ops.id).find(".panel-body").empty().append(ops.text);
				return;
			}
			var info = $("<div id='" + ops.id + "' class='panel panel-primary'></div>")
							.css({"height":"1px","width":ops.width,"bottom":"-10px","right":"5px" ,"zIndex":"2051","opacity": 0,"overflow": "hidden","position":"fixed"});
			var closeX = $("<button type='button' class='close' style='color:#fff;line-height:15px'>×</button>").click(function(){
				info.remove();
			});
			$("<div class='panel-heading' ></div>") 
												.append(closeX)
												.append(ops.title)
												.appendTo(info);
			info.append("<div class='panel-body' style='line-height:17px;overflow:auto;height:" + (ops.height - 45) + "px;'>" + ops.text + "</div>");
	        $("#wrapper").before(info);
	        info.animate({ "height":ops.height,opacity:1},500 );
	        if(ops.millis > 0){
	        	window.setTimeout(function(){
	        		info.animate({ "top":"0px",opacity:0},300,function(){
	        			info.remove();
					 });
	        	}, ops.millis);
	        }
		}
		
		//帮助提示
		$.infoTip = function(code,thiz){
			if(code == null || $.trim(code).length == 0)
				return;
			if(!$(thiz).data("tip"))
				$.post("/manager/info/selectInfo",{"code":code},function(data){
					$(thiz).popover({
						trigger : 'hover click',
						title:data.title,
						content:data.remark,
						placement:"auto bottom"
					});
					$(thiz).popover("show");
					$(thiz).data("tip",true);
				},"json");
		}
		
		/**
		 * callback 当选择选项后的回调 
		 * 参数为  value option的值
		 * 		 option 选中的option对象
		 *		 select select对象
		 * 皆为dom对象
		 */
		 $.fn.checkSelect = function(callback){
		    	if($.browser && $.browser.msie) return;
				var thiz = this;
				var $div = $("<div style='position:absolute;z-index:2'></div>").width(thiz.outerWidth());
				var $ul = $("<ul style='max-height:300px;overflow:auto;border:#ccc 1px solid;padding:0px;';></ul>");
				var $ops = thiz.find("option");
					$ops.each(function(i,o){
						$("<li></li>").html($(o).html())
										.appendTo($ul)
										.height(thiz.outerHeight()-5)
										.css({"lineHeight": (thiz.outerHeight()-5) + "px","margin":"0px","paddingLeft":"5px","background":"#fefefe","borderBottom":" #eee 1px solid"})
										.hover(function(){
											$(this).css("background","#eaeaea");
										},function(){
											if($(o).val() != thiz.val()){
												$(this).css("background","#fefefe");
											}
										}).mousedown(function(){
											if($(o).val() != thiz.val()){
												thiz.val($(o).val());
											}
											$div.slideUp(10);
											callback?callback($(o).val(),o,thiz[0]):null;
										}).data("data-val",$(o).val());
					});
				var $lis = $ul.find("li");
				var $input = $("<input type='text' style='width:100%;'>")
							.height(thiz.outerHeight())
							.bind('input propertychange', function() {
										var val = $(this).val();
										$lis.each(function(i,o){
											var $o = $(o);
											if($o.html().indexOf(val) != -1){
												$o.show();
											}else{
												$o.hide();
											} 
										});
							});
				$div.append($input).append($ul);
				$div.insertAfter(thiz).offset(thiz.offset()).hide();
				thiz.mousedown(function(){
					$div.show().offset(thiz.offset());
					$lis.each(function(i,o){
						if(thiz.val() == $(o).data("data-val")){
							$(o).css("background","#eaeaea");
						}else{
							$(o).css("background","#fefefe");
						}
						$(o).show();
					});
					$div.parents("body").mousedown(function(){
						$div.slideUp(10);
						$input.val("");
					});
					$div.mousedown(function(){
						return false;
					});
					$input[0].focus();
					return false;
				});
			}
		
//*************************百度地图函数开始**************************************	
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
					level:16
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
		
//*************************百度地图函数结束**************************************
