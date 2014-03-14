<!--
//页面的滑动图片效果，add by ：集成显卡 2012.4.13
//修改 WriteQqStr_OK 方法里面的内容即可
function WriteQqStr_OK()
{
	//右边
	document.write('<DIV id=backi_OK style="RIGHT: 0px; OVERFLOW: visible; POSITION: absolute; TOP: 200px;margin-top:0px;text-align:left;background-color:#f1f3f4;border:1px solid #dadada">');
	document.write('<DIV style="padding:5px" id="SHARE_BAR"></DIV>');
	document.write('<DIV style="border-top:1px solid #adadad;padding:5px;text-align:right;font-weight:bold">');
	document.write('<table border="0"><tr><td>雨无声分享平台</td><td><span class="LOGO_GREEN"></span></td></tr></table>');
	document.write('</DIV>');
	document.write('</DIV>');
}

function close_float_left_OK(){document.getElementById("backi_OK").style.display='none';}

lastScrollY_OK=0; 
function heartBeat_OK(){ 
	var diffY_OK;
	if (document.documentElement && document.documentElement.scrollTop)
	    diffY_OK = document.documentElement.scrollTop;
	else if (document.body)
	    diffY_OK = document.body.scrollTop
	else
	    {}
	percent_OK=.1*(diffY_OK-lastScrollY_OK); 
	if(percent_OK>0)percent_OK=Math.ceil(percent_OK); 
	else percent_OK=Math.floor(percent_OK); 
	document.getElementById("backi_OK").style.top=parseInt(document.getElementById("backi_OK").style.top)+percent_OK+"px";
	lastScrollY_OK=lastScrollY_OK+percent_OK; 
} 
if (!document.layers) {WriteQqStr_OK();window.setInterval("heartBeat_OK()",10) }

//
function closes_showtext(){document.getElementById("showtext").style.display='none';}
lastScrollY_SH=0; 
function heartBeat_SH(){ 
	var diffY_SH;
	if (document.documentElement && document.documentElement.scrollTop)
	    diffY_SH = document.documentElement.scrollTop;
	else if (document.body)
	    diffY_SH = document.body.scrollTop;
	else
	    {}
	percent_SH=.1*(diffY_SH-lastScrollY_SH); 
	if(percent_SH>0)percent_SH=Math.ceil(percent_SH); 
	else percent_SH=Math.floor(percent_SH); 
	lastScrollY_SH=lastScrollY_SH+percent_SH; 
} 
//
window.setInterval("heartBeat_SH()",2000);
//-->