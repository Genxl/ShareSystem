package cn.lzs.share.domain.text;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.StringUtils;

import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.domain.BaseEntity;
import cn.lzs.share.domain.User;

@Entity
@Table(name="document")
@SuppressWarnings("serial")
public class Document extends BaseEntity{
	
	//排序
	public static final String DWONLOAD="download";
	public static final String TIME="time";
	public static final String PRICE="price";
	public static final String GOOD="good";
	
	private String title;
	private String tag;//标签
	private int downPrice;//下载需要的金额
	private boolean canReadOnLine;//是否可以在线阅读
	private boolean canComment;//是否允许用户评论
	private String information;//简介
	
	private long size;//文件大小
	private String fileType;//文件格式类型
	private String originName;//上传文件时的原始文件名
	private String originPath;//源文件路径
	private String swfPath;//swf在线阅读路径
	private int pageNumber;//pdf文件的页数，这个也就是  swf 中的页总数了
	private boolean ifUpload;//是否上传了文档
	
	private boolean ifPass;//是否通过审核
	private Date uploadTime;//上传时间
	private Date verifyTime;//审核通过时间
	private long processTime;//转换过程一共使用的时间，纯粹是个人记录用的
	private boolean ifLock;//是否锁定
	private int report;//举报数
	
	private int readNumber;//阅读次数
	private int downNumber;//下载次数
	private int supportNumber;//支持度
	private boolean ifGood;//是否推荐的文档
	
	private Category category;
	private User user;
	
	public Document(){
		this.canComment=true;
		this.canReadOnLine=true;
	}
	
	@Column(nullable=false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getDownPrice() {
		return downPrice;
	}
	public void setDownPrice(int downPrice) {
		this.downPrice = downPrice;
	}
	public boolean isCanReadOnLine() {
		return canReadOnLine;
	}
	public void setCanReadOnLine(boolean canReadOnLine) {
		this.canReadOnLine = canReadOnLine;
	}
	public boolean isCanComment() {
		return canComment;
	}
	public void setCanComment(boolean canComment) {
		this.canComment = canComment;
	}
	public String getInformation() {
		return information;
	}
	public void setInformation(String information) {
		this.information = information;
	}
	public String getOriginName() {
		return originName;
	}
	public void setOriginName(String originName) {
		this.originName = originName;
	}
	@Column
	public String getOriginPath() {
		return originPath;
	}
	public void setOriginPath(String originPath) {
		this.originPath = originPath;
	}
	public String getSwfPath() {
		return swfPath;
	}
	public void setSwfPath(String swfPath) {
		this.swfPath = swfPath;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Date getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}
	public Date getVerifyTime() {
		return verifyTime;
	}
	public void setVerifyTime(Date verifyTime) {
		this.verifyTime = verifyTime;
	}
	public long getProcessTime() {
		return processTime;
	}
	public void setProcessTime(long processTime) {
		this.processTime = processTime;
	}
	public int getReadNumber() {
		return readNumber;
	}
	public void setReadNumber(int readNumber) {
		this.readNumber = readNumber;
	}
	public int getDownNumber() {
		return downNumber;
	}
	public void setDownNumber(int downNumber) {
		this.downNumber = downNumber;
	}
	public int getSupportNumber() {
		return supportNumber;
	}
	public void setSupportNumber(int supportNumber) {
		this.supportNumber = supportNumber;
	}
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name="category_id",nullable=false)
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isIfUpload() {
		return ifUpload;
	}
	public void setIfUpload(boolean ifUpload) {
		this.ifUpload = ifUpload;
	}
	public boolean isIfGood() {
		return ifGood;
	}
	public void setIfGood(boolean ifGood) {
		this.ifGood = ifGood;
	}
	public boolean isIfPass() {
		return ifPass;
	}
	public void setIfPass(boolean ifPass) {
		this.ifPass = ifPass;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public boolean isIfLock() {
		return ifLock;
	}
	public void setIfLock(boolean ifLock) {
		this.ifLock = ifLock;
	}

	public int getReport() {
		return report;
	}

	public void setReport(int report) {
		this.report = report;
	}

	/**
	 * 获取标准格式的文件大小
	 *	@return
	 *  @date :2011-12-10
	 */
	@Transient
	public String getFileSize(){
		return FormatUtil.fileSize(getSize());
	}
	
	@Transient
	public String getSaveName(){
		if(originPath!=null)
			return getOriginPath().substring(getOriginPath().lastIndexOf("/")+1);
		return "";
	}
	
	/**
	 * 当设置了文件保存路径，就可以直接识别出文件的类型
	 *	
	 *  @date :2011-12-12
	 */
	public void initFileType(){
		if(originPath!=null){
			int index=originPath.lastIndexOf(".")+1;
			this.fileType=originPath.substring(index).toUpperCase();
			if(fileType.equals("DOCX"))
				fileType="DOC";
			if(fileType.equals("ZIP"))
				fileType="RAR";
			if(fileType.equals("CHM")||fileType.equals("HTM")||fileType.equals("HTML"))
				fileType="HTM";
		}
	}
	
	@Transient
	public String getSwf(){
		if(swfPath!=null){
			String path=swfPath.replace("1.swf","");
			return path;
		}
		return "";
	}
	
	@Transient
	public String getDownName(){
		String name=getSaveName();
		int index=name.lastIndexOf(".");
		if(index>-1)
			return name.substring(0, index);
		return name;
	}
	
	/**
	 * 获取页面上显示的说明信息，屏蔽 script 等内容
	 *	@return
	 *  @date :2012-1-2
	 */
	@Transient
	public String getHtmlInfomation(){
		String str=StringUtils.replace(getInformation(), "/script", "script");
		str=StringUtils.replace(str, "script", "-script-");
		return str;
	}
	
	/**
	 * 获取最新的时间格式
	 *	@return
	 *  @date :2012-1-3
	 */
	@Transient
	public String getNewTime(){
		Calendar c=Calendar.getInstance();
		c.setTime(getUploadTime());
		long timeLast=new Date().getTime()-c.getTimeInMillis();
		if(timeLast<0||timeLast/(24*60*60*1000)>0)
			return c.get(Calendar.MONTH)+1+"月"+c.get(Calendar.DAY_OF_MONTH)+"日";
		else 
			return c.get(Calendar.HOUR)+"时"+c.get(Calendar.MINUTE);
		/*
		long hour=timeLast/(60*60*1000);
		long min=timeLast/(60*1000)-hour*60;
		return (hour>0?hour+"小时":"")+(min+"分钟前");
		*/
	}
	
	@Override
	public String toString(){
		return " -name:"+getTitle()+
				" -category:"+getCategory().getTitle()+
				" -user:"+getUser().getNick();
	}
}