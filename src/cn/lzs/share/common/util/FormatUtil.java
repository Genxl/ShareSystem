package cn.lzs.share.common.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;

public class FormatUtil {
	
	/**
	 * 检查一个字符串是不是全是数字组成，使用的是 Integer.valueOf(String ) 方法<br />
	 * 接收多个参数
	 *	@param origin
	 *	@return
	 *  @date :2011-10-23
	 */
	public static boolean isAllDigital(String... origin){
		try{
			for(String s:origin)
				Double.valueOf(s);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 获取request的ip<br>
	 * 这个可以得到代理后的真正ip<br>
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	/**
	 * 将一个 key 关键字中的空格都变成 %
	 *	@param key
	 *	@return
	 *  @date :2011-12-13
	 */
	public static String toSQLLike(String key){
		return "%"+key.trim().replaceAll(" ", "%")+"%";
	}
	
	/**
	 * 获取文件大小的 单位表示格式
	 *	@param size
	 *	@return
	 *  @date :2011-12-23
	 */
	public static String fileSize(long size){
		long k=size/1024;
		if(k<1024)
			return k+"K";
		k=k/1024;
		long kb=(size-k*1024*1024)/1024;
		
		return k+"M"+((kb==0)?"":kb+"K");
	}
	
	/**
	 * 将关键字转换成字符串的数组，默认是使用空格分隔
	 *	@param key
	 *	@return
	 *  @date :2011-12-23
	 */
	public static List<String> keysToArray(String key){
		String result[]=key.split(" ");
		List<String> list=new ArrayList<String>();
		for(String s:result){
			if(s.trim().length()>0)
				list.add(s);
		}
		return list;
	}
	
	/**
	 * 加亮源字符串中的所有关键字
	 *	@param origin
	 *	@param keys
	 *	@return
	 *  @date :2011-12-23
	 */
	public static String lightKey(String origin,List<String> keys){
		if(origin!=null){
			for(String a:keys){
				origin=origin.replaceAll(a, "<span style='color:red;font-weight:bold;'>"+a+"</span>");
			}
		}
		return origin;
	}
	
	public static String dwzResult(String message,String navId,String url){
		return "{\"statusCode\":\"200\", \"message\":\""+message+"\", \"navTabId\":\""+navId+"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\""+url+"\"}";
	}
	
	public static String dwzError(String massage){
		return "{\"statusCode\":\"500\", \"message\":\""+massage+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}";
	}
	
	/**
	 * 获取文件后缀
	 *	@param name
	 *	@return
	 *  @date :2011-12-2
	 */
	public static String getFileSuffix(String name){
		System.out.println("得到的文件名："+name);
		return name.substring(name.lastIndexOf(".")+1).toLowerCase();
	}
	
	/**
	 * 得到一个随机的由数字与字母组成的文件名
	 *	@return
	 *  @date :2011-12-2
	 */
	public static String getRandomName(){
		String name="";
		Random random = new Random(); // 实例化一个Random对象
		int itmp=0;
		int length=20;//默认的文件长度是20
		try{
			length=Integer.valueOf(Config.getSingleConfig(ConfigItem.DOC_NAME_LENGTH));
		}catch(Exception e){}
		for (int i = 0; i <length ; i++) {
			if (random.nextInt(2) == 1) {
				itmp = random.nextInt(26) + 65; // 生成A~Z的字母
			} else {
				itmp = random.nextInt(10) + 48; // 生成0~9的数字
			}
			char ctmp = (char) itmp;
			name += String.valueOf(ctmp);
		}
		return name;
	}
	
	public static boolean isEmpty(String str){
		return str==null||str.trim().length()==0;
	}
	
	/**
	 * 去除字符串的全部空格
	 *	@param str
	 *	@return
	 *  @date :2012-4-14
	 */
	public static String killBlank(String str){
		return str.replaceAll(" {1,}", "");
	}
	
	public static Date date(String str)throws Exception{
		try{
			return new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(str);
		}catch(Exception r){
			throw new Exception("日期格式转换出错！日期格式为：2012-4-14 11:30");
		}
	}
	
	public static void main(String a[]){
		System.out.println(isAllDigital("123","111","221a021.21"));
	}
}
