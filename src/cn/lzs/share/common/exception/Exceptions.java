package cn.lzs.share.common.exception;

import cn.lzs.share.common.util.ValidateUtil;

public class Exceptions {
	public static final String USER_LOCK="对不起，此用户已经被锁定，不能正常登录，请联系管理员！";
	
	public static final String LOGIN_NEED="对不起，请先登录！";
	
	public static final String USER_LOGIN_ERROR="用户名或者密码错误！";
	
	public static final String UPLOAD_ILLEGAL_USER="文档不是你创建的，你没有权利对其操作";
	
	public static final String NOT_LOGIN="对不起，你还没有登录，不能执行这个操作！";
	
	public static final String NOT_UPLOAD="对不起，因为这个文档的文件已经通过审核，不能再修改其中的文件！如有需要，请重新创建文档";
	
	public static final String DOC_NOT_PASS="对不起，这个文档还没有通过审核，不能阅读或者下载";
	
	public static final String DOC_NOT_UPLOAD="对不起，这个文档还没有上传文件，还不能下载文件！";
	
	public static final String NOT_ENOUGH_MONEY="对不起，你的积分不足以支付本次下载！";
	
	public static final String DOWNLOG_ERROR="下载链接出错了，找不到对应的文件！这个错误一般是下载地址出错了。";
	
	public static final String CATEGORY_NOT_EXIST="分类不存在！";
	
	public static final String DOC_NOT_EXIST="文档不存在!";
	
	public static final String BBS_USER_ERROR="同步论坛用户信息出错，可能是您输入的用户名或者密码有误，请确认后再试";
	
	public static final String USER_EXIST="对不起，此用户已经存在！";
	
	public static final String FACE_EXT_ERROR="头像格式不对！只能是"+ValidateUtil.allowExt;
	
	public static final String FACE_SIZE_ERROR="头像大小不符合要求！";
	
	public static final String FILE_NOT_ALLOW="上传文件出错：文件大小太大或者文件格式不被允许！";
	
	public static final String NOT_ENOUGH_SPACE="您没有足够的服务器空间保存这个文件。";
	
	public static final String REPORT_EXIST="由于你已经举报过这个文档，这次操作没有成功";
	
	public static final String DOC_LOCK="对不起，此文档已经被锁定！";
	
	public static final String OLD_PASSWORD_ERROR="输入的用户密码不正确";
	
	public static final String PASSWORD_LENGTH="新密码长度不符合要求！";
	
	public static final String INDEX_PIC_SIZE_ERROR="首页图片大小不符合要求！";
}
