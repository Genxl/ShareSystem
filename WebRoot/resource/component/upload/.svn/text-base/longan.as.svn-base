package
{
	import fl.controls.Button;
	import fl.controls.Label;
	import fl.controls.ProgressBar;
	import fl.controls.ProgressBarMode;
	import fl.controls.progressBarClasses.IndeterminateBar;
	import fl.managers.StyleManager;
	
	import flash.display.Sprite;
	import flash.events.DataEvent;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.MouseEvent;
	import flash.events.ProgressEvent;
	import flash.external.ExternalInterface;
	import flash.net.FileFilter;
	import flash.net.FileReference;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLVariables;
	import flash.text.TextFormat;
	
	import mx.events.ModuleEvent;
	
	/**
	 * <script type="text/javascript" src="js/file.longan.1.0.js"></script><div><a href="#" onclick="File.toAS();">12465</a></div><div id="debug"></div>
	 * 
	 * 
	 * longan上传工具（flash版）
	 * 带进度条的上传工具哈。
	 * 2011-12-27 by 集成显卡
	 */
	[SWF(width="500", height="65", backgroundColor="#fdf7f7")]//定义场景大小
	public class longan extends Sprite{
		/*
		外部设置的参数
		*/
		//上传地址
		private var url:String="";
		private var fileTag:String="select file";
		private var fileExt:String="*.*";
		//上传文件的name
		private var fileName:String="fileArea";
		private var maxFileNumber:int=1;//最大文件数
		private var debug:Boolean=false;//是否开启debug模式
		private var maxSize:uint=10*1024*1024;
		private var logoText:String="welcome! by:集成显卡";//初始时显示的文字
		private var params:String="version=1.0";
		private var response:String="";//返回数据时的函数
		
		/*
		内置参数
		 * */
		private var L:Object={select:"选择文件",send:"上传"};
		private var loading:Boolean=false;
		private var fileSize:uint=0;//文件大小
		private var curSize:uint=0;//当前上传的大小
		private var curFile:int=0;
		private var data:URLVariables = new URLVariables();
		private var timeout:Number;
		
		/*
		组件
		 * */
		private var request:URLRequest;
		private var button:Button;
		private var sendButton:Button;
		//进度条
		private var progressBar:ProgressBar;
		private var label:Label;
		private var infoLabel:Label;
		//上传主要
		private var file:FileReference;
		
		public function longan(){
			init();
		}

		public function init():void{
			initStyle();
			initUI();
			initJS();
		}

		/*
		===============================================================================
		JS 交互
		===============================================================================
		*/
		public function initJS():void{
			ExternalInterface.addCallback("setParams",initParams);
		}
		public function initParams(s:Object):void{
			this.url=s.url;
			this.fileExt=s.fileExt;
			this.fileName=s.fileName;
			this.fileTag=s.fileTag;
			this.maxFileNumber=s.maxFileNumber;
			this.maxSize=s.maxSize;
			this.debug=s.debug;
			this.logoText=s.logoText;
			this.params=s.params;
			this.response=s.response;
			
			if(this.logoText)
				this.label.text=this.logoText;
			
			if(this.debug){
				toJSDebug(getParamsInfo());
			}
		}
		
		public function toJSDebug(s:String):void{
			if(ExternalInterface.available){
				ExternalInterface.call("longan_debug",s);
			}
		}
		
		public function getParamsInfo():String{
			var s:String="参数设置成功！<br />";
			s+="URL:"+url+"<br />";
			s+="文件格式:"+fileExt+"<br />";
			s+="文件数:"+maxFileNumber+"<br />";
			s+="文件大小限制："+maxSize+"<br />";
			s+="debug模式："+debug+"<br />";
			s+="post参数："+params+"<br />";
			s+="server response function："+response+"<br />";
			return s+"<br />";
		}
		/*
		===============================================================================
		end    JS 交互
		===============================================================================
		*/

		/**
		 * 设置字体
		 * */
		public function initStyle():void{
			var txt:TextFormat=new TextFormat();
			txt.size=13;
			StyleManager.setStyle("textFormat",txt);
		}
		
		/**
		 * 设置界面
		 * */
		public function initUI():void{
			this.button=new Button();
			this.button.label=L.select;
			this.button.width=80;
			this.button.x=340;
			this.button.y=5;
			this.button.addEventListener(MouseEvent.CLICK,selectFile);
				
			this.sendButton=new Button();
			this.sendButton.label=L.send;
			this.sendButton.width=60;
			this.sendButton.x=430;
			this.sendButton.y=5;
			this.sendButton.addEventListener(MouseEvent.CLICK,sendFile);
			this.sendButton.enabled=loading;
			
			this.label=new Label();
			this.label.text="";
			this.label.x=5;
			this.label.y=5;
			this.label.height=30;
			this.label.width=310;
			
			this.infoLabel=new Label();
			this.infoLabel.text="";
			this.infoLabel.x=5;this.infoLabel.y=45;
			this.infoLabel.width=480;
			
			this.progressBar=new ProgressBar();
			this.progressBar.x=5;
			this.progressBar.y=30;
			this.progressBar.setSize(480,15);
			this.progressBar.mode=ProgressBarMode.MANUAL;
			this.progressBar.indeterminate=false;
			
			addChild(label);
			addChild(button);
			addChild(sendButton);
			addChild(this.infoLabel);
		}
		
		/**
		 * 选择文件
		 * */
		public function selectFile(e:MouseEvent):void{
			if(!canUpload()){
				error("已经达到了上传数量限制！不能再上传文件了");
				return ;		
			}
			if(!loading){
				file = new FileReference();
				file.addEventListener(ProgressEvent.PROGRESS, onProgress);  
				file.addEventListener(Event.SELECT, onSelect);  
				file.addEventListener(Event.COMPLETE, completeHandle);
				file.addEventListener(IOErrorEvent.IO_ERROR,onLoadError);
				file.addEventListener(DataEvent.UPLOAD_COMPLETE_DATA,returnData);
				this.file.browse(new Array(new FileFilter(fileTag,fileExt)));
			}
		}
		
		/**是否可以上传*/
		public function canUpload():Boolean{
			return curFile<maxFileNumber;
		}
		
		public function error(s:String):void{
			infoLabel.htmlText="<font color='#fd5555'>"+s+"</font>";
		}
		
		public function onProgress(e:ProgressEvent):void{
			curSize=e.bytesLoaded;
			infoLabel.text = "已上传 "+Math.round(e.bytesLoaded/1024) + " / "+Math.round(e.bytesTotal/1024)+" KB";
			progressBar.setProgress(curSize,fileSize);
		}
		
		public function onLoadError(e:IOErrorEvent):void{
			this.button.enabled=true;
			this.loading=false;
			error("上传出错！"+e);
		}
		
		public function onSelect(e:Event):void{
			var name:String=file.name;
			this.fileSize=file.size;
			this.label.text=name+"    大小:"+Math.round(this.fileSize/1024)+" KB";
			if(allowSize()){
				this.sendButton.enabled=true;
			}else{
				error("选择的文件太大，最大的文件大小为："+this.maxSize/1024+" Kb");
				return ;
			}
		}
		
		public function completeHandle(e:Event):void{
			this.button.enabled=true;
			this.loading=false;
			error("完成！用时:"+(new Date().time-timeout)+" 毫秒");
			this.curFile++;
		}
		
		public function returnData(e:DataEvent):void{
			trace(e.data);
			if(this.debug){
				toJSDebug("upload success!"+e.data);
			}
			if(ExternalInterface.available){
				ExternalInterface.call(response,e.data);
			}
		}
		
		/**
		 * 上传文件
		 * */
		public function sendFile(e:MouseEvent):void{
			if(this.url==""){
				error("上传的URL不明确！");
				return ;
			}
			data.decode(this.params);
			request = new URLRequest(this.url);
			request.method=URLRequestMethod.POST;
			request.data=data;
			this.timeout=new Date().time;
			this.file.upload(request,this.fileName);
			addProgress();
			this.loading=true;
			this.sendButton.enabled=false;
			this.button.enabled=false;
		}
		public function addProgress():void{
			if(curFile==0){
				addChild(progressBar);
			}
		}
		public function allowSize():Boolean{
			return this.fileSize<=this.maxSize;
		}
		
	}
}