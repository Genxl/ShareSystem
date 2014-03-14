/**
 * 分享模块脚本
 * @author 集成显卡 2012.4.13
 */
var SHARE={
	itemCur:3,
	maxItem:8,//最大选项数
	topicNum:0,//题目数
	complete:0,//已经完成
	time:0,//时间
	timer:null,
	info:"",//保存一些数据
	apply:function(){
		Longan.artLoad("STORE","申请开通题库","/share/yask/apply");	
	},
	applyDo:function(){
		var name=$("#apply_info").val();
		var data=$("#apply_data").val();
		Longan.ajaxPost("/share/yask/applyDo",{"info":name,"data":data},function(data){
			$.dialog.get("STORE").close();
			Longan.alert(data+"<br />管理员通过后，系统会自动为您创建'"+name+"'这个题库。");
		});
	}
};
if(!Longan){
	alert("无法找到 LONGAN 对象，程序功能将会出现错误！");
}
SHARE.topic={
	add:function(){
		Longan.artLoad("TOPIC","添加题目","/share/yask/topic/add");
	},
	/*
	 * 遍历 topic_option_div 中的p，之后如果answer=-1，则报错
	 */
	addDo:function(){
		var name=$("#topic_name").val();
		var type=$("#topic_type").val();
		var val=$('input:radio[name="t_type_"'+type+']:checked').val();
		
		if(Longan.isEmpty(name)){
			Longan.alert("标题不能为空！");return false;
		}
		if(val==null){
			Longan.alert("请选择一个答案！");return false;
		}
		var option="";
		if(type==1){
			var an=-1;
			var cur=0;
			$("#topic_option_div").find("p").each(function(){
				val=$('input:radio[name="t_type_1"]:checked').val();
				var item=$(this).find(".textIn");
				if(!Longan.isEmpty(item.val())){
					cur++;
					option+=(" "+Longan.killBR(item.val()));
					if(val==$(this).find('input:radio[name="t_type_1"]').val())
						an=cur;	
				}
			});
			if(an==-1){
				Longan.alert("检测题目设置错误！请不要留空");return false;
			}
		}else if(type==0){
			an=val;
		}
		Longan.ajaxPost("/share/yask/topic/add",{"topic.name":name,"topic.answer":an,"topic.items":option,"topic.type":type},function(data){
			$("#topic_error").html(data);
			window.location.reload();
		});
	},
	addItem:function(){
		if($("#topic_option_div >p").length>=SHARE.maxItem){ Longan.alert("最多可以添加8个选项！");return false;}
		var html='<p style="padding:0px"><a href="javascript:;" onclick="SHARE.topic.delItem(this);">-</a>&nbsp;'
				+'<input name="" type="text" class="textIn"/>&nbsp;<input type="radio" name="t_type_1" value="'+(SHARE.itemCur++)+'"/></p>';
		$("#topic_option_div").append($(html));
	},
	delTopics:function(url){
		var id=Longan.get_select_list('topic_box');
		if(id.length>0){
			$.confirm('你真的要删除这些题目么，操作是不可逆的？', function () {
				Longan.ajaxGet(url+id,function(data){
					$("input[name=topic_box]").each(function() {
						if ($(this).attr("checked")) {
							$("#topic_"+$(this).attr("value")).slideUp('fast');
						}
					});
				});
			}, function () {});
		}else{
			Longan.alert("请先选择题目");
		}
	},
	delSelect:function(){
		SHARE.topic.delTopics("/share/yask/topic/del?ids=");
	},
	//传递一个paper参数
	delSelectFromPaper:function(pid){
		SHARE.topic.delTopics("/share/yask/topic/delFromPaper?id="+pid+"&ids=");
	},
	//删除当前的选项 p 元素
	delItem:function(obj){$(obj).parent().remove();},
	addToPaper:function(){
		Longan.artLoad("TOPIC","将选中题目添加到？","/share/yask/paper/diy");
	},
	addToPaperDo:function(){
		var pid=$("#paper_select").val();
		var ids=Longan.get_select_list('topic_box');
		Longan.ajaxGet("/share/yask/topic/addToPaper?id="+pid+"&ids="+ids,function(data){
			$.dialog.get("TOPIC").close();
			Longan.alert(data);
		});
	}
};

SHARE.paper={
	add:function(){
		//Longan.artLoad("PAPER","添加题目","/share/yask/paper/add");
	},
	del:function(id){
		$.confirm('你真的要删除这个试卷么，操作是不可逆的？', function () {
		    var url="/share/yask/paper/del?id="+id;
			Longan.ajaxGet(url,function(data){
				$("#paper_"+id).slideUp('800');
			});
		}, function () {});
	},
	addInit:function(bi,ji){
		SHARE.paper.hideTip();
		$("#bornType").change(function(){SHARE.paper.hideTip();$("#borntip_"+$(this).val()).show();});
		$("#bornType").val(bi);
		$("#borntip_"+bi).show();
		$("#joinType").val(ji);
		$("#toRandom").click(function(){
			SHARE.paper.random();
		});
	},
	hideTip:function(){
		for(var i=0;i<3;i++)
			$("#borntip_"+i).hide();
	},
	random:function(){
		var num=$("#topicNum").val();
		if(!Longan.isDigital(num)||parseInt(num)<=0){
			Longan.alert("请输入正确的题目数！他应该是大于0的整数");
			return false;
		}
		Longan.artLoad("PAPER","随机产生题目","/share/yask/paper/randomTopic?id="+num);	
	},
	reRandom:function(){
		$.dialog.get("PAPER").close();
		$("#toRandom").trigger("click");
	},
	setElement:function(value){$("#elements").val(value);Longan.alert("element设置为："+value);},
	//执行start
	initStart:function(pid,time,typeId){
		SHARE.time=time;
		$.dialog({id:"PAPER",title:"雨无声分享平台--问答系统",content:Longan.getLoading("正在配置问卷，请稍候...")});
		$(".moving").each(function(){
			var i=$(this).attr("index");
			$("#paperForm").append("<input class='hAnswer' type='hidden' name='item' id='answer_"+i+"' value='' />");
			var temp=$(this).attr("items").split(" ");
			for(var n=1;n<temp.length;n++){
				if(typeId==0){
					$(this).find(".item").append("<li class='item_p' pid="+i+" value='"+n+"'>"+temp[n]+"</li>");
				}else if(typeId==1){
					$(this).append("<p class='item_p' pid="+i+" value='"+n+"'><label><input type='radio' name='radio_"+i+"'/>"+temp[n]+"</label></p>");
				}
			}
			SHARE.topicNum++;
			SHARE.info+="<li class='mark' id='mark_"+i+"' tab='"+i+"'>"+SHARE.topicNum+"</li>";
		});
		$.dialog.get("PAPER").content(Longan.getLoading("配置完成！正在同步服务器时间,同步成功后自动开始计时..."));
		Longan.ajaxGet("/share/yask/checkTime?id="+pid,function(data){
			$.dialog.get("PAPER").close();
			
			SHARE.timer=window.setInterval(SHARE.paper.runTimer,1000);
			SHARE.paper.start();
		});
	},
	runTimer:function(){
		SHARE.time--;
		if(SHARE.time<0){
			window.clearInterval(SHARE.timer);
			document.getElementById("BAR_SUBMIT").disabled=true;
		}else{
			$(".TIME").html(SHARE.time);
		}
	},
	submit:function(){
		if(SHARE.time<=0){
			Longan.alert("已经到时间了=.=");
			return false;
		}
		var i=0;
		var D=$("#temp_data");
		D.val("");
		$(".hAnswer").each(function(){
			if($(this).val()!=""){
				D.val(D.val()+$(this).val()+"@");
				i++;
			}
		});
		if(i<SHARE.topicNum){
			Longan.alert("还有"+(SHARE.topicNum-i)+"道题目没做哦！");
			return false;
		}
		$("#paperForm").submit();
	},
	start:function(){
		//向侧边滑块添加内容
		var obj=$("#SHARE_BAR");
		obj.html("题目数："+SHARE.topicNum+"&nbsp;&nbsp;已完成：<span id='complete'>"+SHARE.complete+"</span>");
		obj.html(obj.html()+"<div class='timeInfo'>答题时间还有：<span class='TIME'>"+SHARE.time+"</span>秒</div>");
		obj.html(obj.html()+"<ul>"+SHARE.info+"<li class='clear'><li></ul>");
		obj.html(obj.html()+"<div style='text-align:center;padding:4px'><a href='javascript:;' id='BAR_SUBMIT' onclick='SHARE.paper.submit();' class='BUTTON2'>交卷</a></div>");
		
		obj.find(".mark").click(function(){
			Longan.scrollTo("topic_"+$(this).attr("tab"));
		});
	},
	resultList:function(){
		var pid=$("#p_id").val();
		$("#paper_select").val(pid).change(function(){
			$("#p_id").val($(this).val());
		});
	}
};