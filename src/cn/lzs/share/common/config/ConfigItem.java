package cn.lzs.share.common.config;

import java.lang.reflect.Field;

public class ConfigItem {
	public static final String APP_NAME="app_name";
	public static final String APP_HOME="app_home";
	
	/**单一doc等文件的最大大小，如果超过这个值，将分分段保存这个文档，单位为 Kb*/
	public static final String MAX_DOC_SIZE="doc_single_maxsize";
	/**最高级别的在线阅读开头，如果这个没有开启，那么所有文件都不能在线阅读，即不会生成swf文件*/
	public static final String CAN_READ_ONLINE="can_read_online";
	/**可以转换成swf在线阅览的文件类型*/
	public static final String SWF_TYPE="doc_swf_type";
	/**原始文档的保存路径*/
	public static final String ORIGIN_PATH="doc_origin_path";
	/**PDF文档的保存路径*/
	public static final String PDF_PATH="doc_pdf_path";
	/**SWF文档的保存路径*/
	public static final String SWF_PATH="doc_swf_path";
	/**OPENOFFICE的端口*/
	public static final String OPENOFFICE_PORT="openOffice_port";
	/**文件随机名字的长度*/
	public static final String DOC_NAME_LENGTH="doc_name_length";
	/**txt文档转换方法设定，可以是 Jodc，也可以是Itext*/
	public static final String TXT_CONVERT_METHOD="txt_convert_method";
	/**是否在转换成功后将pdf文件删除*/
	public static final String DEL_PDF_FILE="del_pdf_file";
	/**swfTools 的路径*/
	public static final String SWFTOOLS_PATH="swfTools_path";
	/**指定了对同一个文件多次下载两次扣除积分的时间间隔，为0则是一次下载，永远不会两次扣除积分，单位为天*/
	public static final String DOWNLOAD_TIMEOUT="download_timeout";
	
	public static final String HEADER_PATH="header_path";
	public static final String HEADER_SIZE="header_size";
	
	public static final String CAN_UPLOAD="can_upload";
	public static final String CACHE_TIME="cache_time";
	
	/**当一个文档被举报一定次数时，会自动锁定*/
	public static final String NUMBER_TO_LOCK="number_to_lock";
	
	/**最小的密码长度*/
	public static final String PASSWORD_LENGTH="password_length";
	/**首页图片的个数*/
	public static final String INDEX_PIC_NUMBER="index_pic_number";
	
	public static final String INDEX_PIC_WIDTH="index_pic_width";
	public static final String INDEX_PIC_HEIGHT="index_pic_height";
	
	/**新用户注册可以获得的积分数*/
	public static final String NEW_USER_INTEGRAL="new_user_integral";
	
	/**搜索用到的目录*/
	public static final String SEARCH_BASE_URL="search_base_url";
	
	/**
	 * 获取全部的配置项的名称。
	 * 通过对 ConfigItem的反射获得
	 *	@return
	 *  @date :2012-12-18
	 */
	public static String[] getNames(){
		Field[] fields=ConfigItem.class.getFields();
		String name[]=new String[fields.length];
		for(int i=0;i<name.length;i++){
			try{
				name[i]=(String)(fields[i].get(null));
			}catch(Exception e){
				name[i]=fields[i].getName().toLowerCase();
			}
		}
		return name;
	}
}