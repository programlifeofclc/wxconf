try{
	/**
	 * 构造函数
	 */
	function wx_clc(opstion) {
		this.ops = {
			debug : false,
			url : window.location.href.split("#")[0],
			jsApiList:[]
		};
		this.ops = $.extend(this.ops, opstion || {});
	}

	/**
	 * 对象扩展
	 */
	wx_clc.prototype = {
		apis : [
			 "onMenuShareTimeline",
			 "onMenuShareAppMessage",
			 "onMenuShareQQ",
			 "onMenuShareWeibo",
			 "onMenuShareQZone",
			 "startRecord",
			 "onVoiceRecordEnd",
			 "playVoice",
			 "pauseVoice",
			 "stopVoice",
			 "onVoicePlayEnd",
			 "uploadVoice",
			 "downloadVoice",
			 "chooseImage",
			 "previewImage",
			 "uploadImage",
			 "downloadImage",
			 "translateVoice",
			 "getNetworkType",
			 "openLocation",
			 "getLocation",
			 "hideOptionMenu",
			 "showOptionMenu",
			 "hideMenuItems",
			 "showMenuItems",
			 "hideAllNonBaseMenuItem",
			 "showAllNonBaseMenuItem",
			 "closeWindow",
			 "scanQRCode",
			 "chooseWXPay",
			 "openProductSpecificView",
			 "addCard",
			 "chooseCard",
			 "openCard"
		],
		apik : {
			"onMenuShareTimeline" 	: "onMenuShareTimeline",
			"onMenuShareAppMessage" : "onMenuShareAppMessage",
			"onMenuShareQQ" 		: "onMenuShareQQ",
			"onMenuShareWeibo" 		: "onMenuShareWeibo",
			"onMenuShareQZone" 		: "onMenuShareQZone",
			"startRecord" 			: "startRecord",
			"onVoiceRecordEnd" 		: "onVoiceRecordEnd",
			"playVoice"				: "playVoice",
			"pauseVoice" 			: "pauseVoice",
			"stopVoice" 			: "stopVoice",
			"onVoicePlayEnd" 		: "onVoicePlayEnd",
			"uploadVoice" 			: "uploadVoice",
			"downloadVoice" 		: "downloadVoice",
			"chooseImage" 			: "chooseImage",
			"previewImage" 			: "previewImage",
			"uploadImage" 			: "uploadImage",
			"downloadImage" 		: "downloadImage",
			"translateVoice" 		: "translateVoice",
			"getNetworkType" 		: "getNetworkType",
			"openLocation" 			: "openLocation",
			"getLocation" 			: "getLocation",
			"hideOptionMenu" 		: "hideOptionMenu",
			"showOptionMenu" 		: "showOptionMenu",
			"hideMenuItems" 		: "hideMenuItems",
			"showMenuItems" 		: "showMenuItems",
			"hideAllNonBaseMenuItem" : "hideAllNonBaseMenuItem",
			"showAllNonBaseMenuItem" : "showAllNonBaseMenuItem",
			"closeWindow" 			: "closeWindow",
			"scanQRCode" 			: "scanQRCode",
			"chooseWXPay" 			: "chooseWXPay",
			"openProductSpecificView" : "openProductSpecificView",
			"addCard" 				: "addCard",
			"chooseCard" 			: "chooseCard",
			"openCard" 				: "openCard"
		},
		alert : function(msg) {
			if (this.ops.debug)
				alert(msg);
		},
		setJsApiIndex : function(index) {
			if (index) {
				thiz = this;
				var apr = thiz.ops.jsApiList;
				var strArr = index.split(",");
				$.each(strArr, function(i, v) {
					var val = thiz.apis[v];
					if (val)
						apr.push(val);
				})
				thiz.alert("jsApiList-size:" + apr.length);
			}
		},
		setJsApi : function() {
			var apr = this.ops.jsApiList;
			for (var i = 0; i < arguments.length; i++) {
				apr.push(arguments[i]);
			}
			this.alert("jsApiList-size:" + apr.length);
		},
		config : function(currUrl) {
			var thiz = this;
			$.ajax({
				type : "POST",
				url : currUrl,
				async : false,
				data : {
					url : this.ops.url
				},
				dataType : "json",
				success : function(json) {
					thiz.alert(json);
					if (json.msgcode == "1") {
						wx.config({
							debug : thiz.ops.debug, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
							appId : json.data.appId, // 必填，公众号的唯一标识
							timestamp : json.data.timestamp, // 必填，生成签名的时间戳
							nonceStr : json.data.noncestr, // 必填，生成签名的随机串
							signature : json.data.signature,// 必填，签名
							jsApiList : thiz.ops.jsApiList
						});
					}
				}
			});
		},
		on : function(apiKey, ops) {
			var thiz = this;
			var apr = thiz.ops.jsApiList;
			apr.push(apiKey);
			wx.ready(function(res) {
				ops = $.extend({
					title : "分享"
				}, ops);
				wx[apiKey](ops);
			});
		},
		err : function(res) {
			wx.error(function(res) {
				// config信息验证失败会执行error函数，如签名过期导致验证失败，
				// 具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
				this.alert("wx error:" + res);
			});
		}

	}
    
}catch (e) {
	alert("er:" + e);
}
