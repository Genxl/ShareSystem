var Img={
	id:"#index_img",
	left:".BIG_LEFT",
	right:".BIG_RIGHT",
	size:0,
	cur:1,
	timeout:1800,
	dir:0,
	timer:null,
	w:935,
	
	init:function(){
		Img.size=$(Img.id+" a").length;	
		Img.cur=Img.size;
		Img.setTime();
		$(Img.left).click(function(){Img.dir=0;Img.run();Img.setTime();});
		$(Img.right).click(function(){Img.dir=1;Img.run();Img.setTime();});
	},
	setTime:function(){clearInterval(Img.timer);Img.timer=setInterval(function(){Img.run();},7500);},
	run:function(){
		if(Img.size<=1)
			return ;
		var second=Img.getSecond();
		//alert(second);
		$(Img.id+" a[index="+second+"]").css({"z-index":Img.size-1,"left":"0px"});
		if(Img.dir!=0)
			$(Img.id+" a[index="+second+"]").css({"right":"0px"});
			
		$(Img.id+" a[index="+Img.cur+"]").stop().animate(Img.getAnimateParam(),{duration:Img.timeout,queue: false,complete:function(){
			$(Img.id+" a[index="+Img.cur+"]").css("z-index",0);
			$(Img.id+" a[index="+second+"]").css("z-index",Img.size);
			Img.cur=second;
			return ;
		}});
	},
	//获取每二张图片的index值
	getSecond:function(){
		if(Img.dir==0)//往左
			return Img.cur==1?Img.size:(Img.cur-1)%Img.size;
		else{//往右
			var i=Img.cur==Img.size?1:(Img.cur+1)%Img.size;	
			return i==0?Img.size:i;
		}
	},
	getAnimateParam:function(){
		if(Img.dir==0)
			return {"left":-Img.w+"px"};
		else
			return {"right":-Img.w+"px","left":Img.w+"px"};
	}
}