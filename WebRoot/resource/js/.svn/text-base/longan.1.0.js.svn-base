/**
 * 前端总脚本
 * 2011-12-24
 * 圣诞快乐！
 * 
 * 需要Juqery的支持
 * @author 集成显卡
 */

var Longan=(function(){
	var ROOT="";
	var name="";
	var header="";
	
	var _getLoading=function(info){
		if(info=='') info="正在加载数据....";
		return "<div  style='text-align:center'><span class='LOADING'></span></div><div style='text-align:center'>"+info+"</div>";
	}
	
	return {
		//设置系统的根目录
		initRoot:function(url){ROOT=url;},
		initUser:function(n,h){name=n;header=h;},
		ROOT:function(){return ROOT;},
		name:function(){return name;},
		header:function(){return header;},
		
		ajaxGet:function(url,callBack){
			$.get(ROOT+url,{},function(data,state){
				if (state=="success"){
					try{
						var d=eval("(" + data + ")");
						if(d.error=='0'){
							callBack(d.message);
						}else
							alert(d.message);
					}catch(e){
						alert("出错了！可能是您没有登录"+e);
					}
				}
			});
		},
		/**使用POST的形式发送数据，服务器返回的数据格式为 'error':'0','message':'' 如果error为0 ，没调用回调函数callback*/
		ajaxPost:function(url,params,callBack){
			$.post(ROOT+url,params,function(data,stauts){if (stauts == "success") {
				var d=eval("(" + data + ")");
					if(d.error=='0'){
						callBack(d.message);
					}else
						alert(d.message);
			}});
		},
		//加载页面信息
		loadAndShow:function(url,div,info,callBack){
			$(div).html(_getLoading(info));
			$.get(ROOT+url,{},function(data,state){
				if (state=="success"){
					$(div).html(data);
					callBack();
				}
			});
		},
		//加载页面信息
		load:function(url,callBack){
			$.get(ROOT+url,{},function(data,state){
				if (state=="success"){
					callBack(data);
				}
			});
		},
		getLoading:function(info){
			return _getLoading(info);
		},
		/**
		 * 显示/隐藏元素
		 * @param {Object} id  对象id
		 * @param {Object} type 是显示还是隐藏。可以选择  hide 和 show ，其他为自动
		 * @param {Object} moveType 动画类型,1为 show 2为 fadein 3为slideDown
		 * @param {Object} timeout 动画时间
		 * @return {TypeName} 
		 */
		display:function(id,type,moveType,timeout){
			var item=$(id);
			var isShow=type=="show"?true:(type=="hide"?false:item.css("display")=="none");
			switch(moveType){
				case 3:{ 
					if(isShow) item.slideDown(timeout);else item.slideUp(timeout); return ;
				}
				case 2:{
					if(isShow) item.fadeIn(timeout);else item.fadeOut(timeout);return ;
				}
				case 1:{
					if(isShow) item.show(timeout);else item.hide(timeout);return ;
				}
			}
		},
		//是否全是数字与字母
		isLawChar:function(str){var reg=/^(\s)*(\w)+$/; return reg.test(str);},
		//是否全是数字
		isDigital:function(str){var reg=/^(\s)*(\d)+$/; return reg.test(str);},
		//是否为空
		isEmpty:function(str){var reg=/^(\s)*$/;return reg.test(str);},
		
		/**
		 * 更换元素内容
		 * n1 是元素默认的值，n2是切换后的内容
		 * 如果已经是n2了，就会改变成n1
		 */
		switchName:function(obj,n1,n2){
			if($(obj).html()==n1) $(obj).html(n2); else $(obj).html(n1);
		},
		
		artLoad:function(aid,head,url){
			$.dialog({id:aid,title:head,fixed:true,lock:true});
			Longan.load(url,function(data){
				$.dialog.get(aid).content(data);
			});
		},
		alert:function(c){$.alert(c);},
		//去除全部空格
		killBR:function(str){return str.replace(/\s+/g,"");},
		//页面滑动到指定的id
		scrollTo:function(id){
	       var $body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html')
					: $('body'))
					: $('html,body');
			var obj = $("#"+id);//$(this).attr('tab');
			if (!obj) {
				return true;
			}
			var v = $(obj).offset();
			v = v.top;
			$body.animate({scrollTop : v}, 400);
		},
		//-----------------------------------------------
		//checkBox
		//-----------------------------------------------
		select_all:function(name){$("input[name=" + name + "]").each(function() {$(this).attr("checked", true);});},
		select_no:function(name){$("input[name=" + name + "]").each(function() {$(this).attr("checked", false);});},
		select_other:function(name){
			$("input[name=" + name + "]").each(function() {
				if ($(this).attr("checked")) {
					$(this).attr("checked", false);
				} else {
					$(this).attr("checked", true);
				}
			});},
		/**获取一个checkbox的全部选中的值，以@连接*/
		get_select_list:function(name) {
			var value = "";
			$("input[name=" + name + "]").each(function() {
				if ($(this).attr("checked"))
					value += $(this).val() + "@";
			});
			return value.substring(0, value.length - 1);
		}
	}
})();

var User=(function(){
	var URL={
		"delDoc":"/user/doc/del"
	}
	var ID={"p_a":"#change_pass","p_div":"#change_pass_div","p_info":"#change_pass_info","p_do":"#change_pass_do","p_old":"#pass_old","p_new":"#pass_new"
	}
	
	var initPassword=function(){
		$(ID.p_a).click(function(){
			Longan.display(ID.p_div,'auto',3,'fast');
			Longan.switchName(this,"修改密码","隐藏");
		});
		$(ID.p_do).click(function(){
			var oldP=$(ID.p_old).val();
			var newP=$(ID.p_new).val();
			if(Longan.isEmpty(newP)){alert("新密码不能为空！");return ;}
			if(!Longan.isLawChar(newP)){alert("新密码必须是数字与字母的组合");return ;}
			Longan.ajaxPost("/user/changePass",{"oldP":oldP,"newP":newP},function(data){
				alert(data);
				$(ID.p_a).trigger("click");
			});
		});
	}
	var initUpgrade=function(){
		if(canUpgrade()){
			$("#toUpgrade").click(function(){
					Longan.ajaxPost("/user/upgrade",{},function(data){
						alert(data);
					});
			});
		}else{$("#toUpgrade").css("background-color","#aaaaad").html("还不能升级哦");}
	}
	var canUpgrade=function(){
		return parseInt($("#now_integral").html())>=parseInt($("#next_integral").html());
	}
	var initExchange=function(){
		$("#exchange").click(function(){
			Longan.display("#exchange_div",'auto',3,'fast');
			Longan.switchName(this,"积分/西大币兑换","隐藏");
		});
		$("#exchange_ok").click(function(){
			var integral=$("#exchange_integral").val();
			var type=$("#exchange_type").val();
			var price=parseInt($("#xdb_rate").html())*parseInt(integral);
			if(Longan.isEmpty(integral)){alert("请输入需要兑换的积分！");return ;}
			if(!Longan.isDigital(integral)){alert("积分应该是纯数字！");return ;}
			
			/*询问用户*/
			var info=(type=="toXDB")?(integral+"积分兑换成"+price+"西大币"):(price+"西大币兑换为"+integral+"积分")+"？";
			if(confirm("确定将"+info)){
				Longan.ajaxPost("/user/exchange",{"changeType":type,"integral":integral},function(data){
					alert(data);
				});
			}
		});
		$("#checkMyXDB").click(function(){
			Longan.ajaxPost("/user/getMyXDB",{},function(data){
				alert(data);
			});
		});
	}
	
	return {
		personal:function(){
			initPassword();
			initUpgrade();
			initExchange();
		},
		delDoc:function(id){
			if(confirm("确定删除这个文档？")){
				var url=URL.delDoc+"?ids="+id;
				Longan.ajaxGet(url,function(data){
					$("#doc_"+id).hide();
				});
			}
		}
	}
})();

var File=(function(){
	var id=-1;
	//获取指定ID的swf
	var _getFlashMovie=function() {
		var movieName="longan";
		if (window.document[movieName]){
		    return window.document[movieName];
		  }else if (navigator.appName.indexOf("Microsoft")==-1){
		    if (document.embeds && document.embeds[movieName])
		    return document.embeds[movieName];
		  }else{
		    return document.getElementById(movieName);
		  }
	}
	
	return {
		init:function(){
			try{
				getFlashMovie().setParams(setting);
			}catch(e){
				setTimeout("File.init();",2000);
			}
		},
		response:function(data){
			var d=eval("(" + data + ")");
			if(d.error=='0'){
				alert("上传成功！请保存文档信息");
			}else
				alert(d.message);
		}
	}
})();

var Document=(function(){
	var URL={
		"comment":"/document/comment/",
		"category":"category/list"
	}
	var ID={
		"comment":"#comment_div","c_area":"#input_area","c_input":"#submit_input","c_tip":"#input_tip","c_go":"#commentGo",
		"c_exit":"#commentExit","c_label":"#input_label",
		"category":"#category","cate_div":"#categoryMenuParent","cate_tree":"#categoryMenu",
		"logs":"#down_log_div"
	}
	var maxLength=140;//最大评论字数
	var did=-1;//文档ID
	var page=1;
	var cateTree=null;//分类树
	
	var bindComment=function(){
		$(ID.c_area).focus(function(){
			$(ID.c_input).show();
			$(ID.c_area).stop().animate({"height":"60px"},{duration:200,queue: false,complete:function(){
				$(this).addClass("normal_input");
				if($(this).val()=="我要评论...") $(this).attr("value","");
			}});
		});//.blur(function(){Document.cannelComment();})
		//添加发表和取消的事件监听
		$(ID.c_go).click(function(){submitComment()});
		$(ID.c_exit).click(function(){Document.cannelComment()});
	}
	
	var checkLength=function(){
		var l=$(ID.c_area).val().length;
		if(l>maxLength){
			$(ID.c_area).attr("value",$(ID.c_area).val().substring(0,maxLength-1));
		}
		$(ID.c_tip).html(l+"/"+maxLength);
	}
	var submitComment=function(){
		if($(ID.c_area).val().length<5){
			$(ID.c_label).html("请输入内容，内容不少于5个字符");
			return false;
		}
		var content=$(ID.c_area).val();
		var url=URL.comment+"add";
		Longan.ajaxPost(url,{"id":did,"comment.content":content},function(data){
			Document.insertComment(content);
		});
	}
	var insertComment=function(content){
		var html='<div class="top_comment">'+
			'<div class="left_part"><a href=""><img src="'+Longan.ROOT()+'/resource/header/'+Longan.header()+'" class="comment_face"/></a></div>'+
			'<div class="right_part"><a href="">'+Longan.name()+'</a>：'+content+
			'&nbsp;&nbsp;刚刚</div>'+
			'<div class="clear"></div></div>';
		$(ID.comment).prepend(html);
		$(ID.c_area).val("");
	}
	
	var getCategory=function(){
		if(cateTree!=null){
				if($(ID.cate_div).css("display")=="none")
					$(ID.cate_div).slideDown("fast");
				else
					$(ID.cate_div).slideUp("fast");
				return false;
			}
			
			$.ajax({
	          type: "post",url:URL.category,dataType: 'text',
	          success: function(data){
				  var d=eval(data);
            	  cateTree= $(ID.cate_tree).zTree({
                  isSimpleData: true,
                  treeNodeKey: "id",
                  treeNodeParentKey: "pid",
                  callback: {
                      click: function(event, treeId, node){
							if($("#categoryId").val() != node.id&&!node.isParent){
								$("#categoryId").val(node.id);
                          		$("#category").val(node.name);
                          		 $(ID.cate_div).slideUp("fast");
							}
                          return false;
                      }
                  }
              }, d);
              $(ID.cate_div).slideDown("fast");
	          }
	      });
	}
	var bindDoc=function(){
		$(ID.category).click(function(){
			getCategory();
			return false;
		});
	}
	return {
		//======================评论=======================
		initComment:function(id){
			did=id;
			bindComment();
			Document.getComments(did,page);
		},
		getComments:function(id,p){
			Longan.loadAndShow(URL.comment+"list?id="+id+"&page="+p,ID.comment,"正在加载评论....",function(){
				//完成加载后，因为是ajax，分页时需要对 A 全部处理
				$(".PAGE a").each(function(){
					$(this).attr("href","javascript:;");
					var p=parseInt($(this).html().replace("[",""));
					if(p>0){
						$(this).bind("click",function(){
							Document.getComments(did,p);
						});
					}else{$(this).remove();}
				});
			});
		},
		cannelComment:function(){
			$(ID.c_input).hide();
			$(ID.c_area).stop().animate({"height":"20px"},{duration:400,queue: false,complete:function(){
				$(this).removeClass("normal_input");
				if($(this).val()=="") $(this).attr("value","我要评论...");
			}});
		},
		checkLength:function(){
			checkLength();
		},
		insertComment:function(content){
			insertComment(content);
		},
		initDoc:function(id){
			did=id;
			bindDoc();
		},
		getDownlogs:function(){
			Longan.loadAndShow("/document/downLogs?doc_id="+did,ID.logs,"正在加载下载记录....",function(){
			});
		},
		report:function(){
			var id=did;
			if(confirm("当有足够的用户举报文档，文档会被锁定。确定举报这个文档么？")){
				var url="/user/report?id="+id;
				Longan.ajaxGet(url,function(data){
					alert(data);
				});
			}
		}
	}
})();