package cn.lzs.share.common.util;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;

public class ValidateUtil {
	/**可以使用的头像后缀*/
	public static String allowExt="@jpg@gif@png";
	
	/**
	 * 检查是否符合头像格式
	 *	@param ext
	 *	@return
	 *  @date :2011-12-26
	 */
	public static boolean checkFaceExt(String ext){
		return allowExt.contains("@"+ext.toLowerCase());
	}
	
	/**
	 * 验证图片的大小是否在规定的范围内<br />
	 * 只有当图片大小与设置的大小相同才会返回false<br />
	 *	@param file
	 *	@return
	 *  @date :2011-12-26
	 */
	public static boolean checkFaceSize(File file){
		int w=0;
		int h=0;
		int size=1;
		try{
			size=Integer.valueOf(Config.getConfig(ConfigItem.HEADER_SIZE));
			Image i=ImageIO.read(file);
			w=i.getWidth(null);
			h=i.getHeight(null);
		}catch(Exception e){
			e.printStackTrace();
		}
		System.out.println(w+"   -----   "+h);
		return w!=size&&h!=size;
	}
	
	/**
	 * 检查一个文件是否合法。从大小及后缀
	 *	@param file
	 *	@return
	 *  @date :2011-12-28
	 */
	public static boolean checkFile(File file){
		return checkFile(file.getName(),file.length());
	}
	
	public static boolean checkFile(String name,long size){
		try{
			long maxLength=Long.valueOf(Config.getConfig(ConfigItem.MAX_DOC_SIZE));
			String exts=Config.getConfig(ConfigItem.CAN_UPLOAD);
			return size<=maxLength*1024&&exts.contains(FormatUtil.getFileSuffix(name));
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * 检测首页大图的大小是否符合要求
	 * 不符合时返回真
	 *	@param file
	 *	@return
	 *  @date :2012-1-5
	 */
	public static boolean checkIndexImg(File file){
		try{
			int w=Integer.valueOf(Config.getConfig(ConfigItem.INDEX_PIC_WIDTH));
			int h=Integer.valueOf(Config.getConfig(ConfigItem.INDEX_PIC_HEIGHT));
			
			Image i=ImageIO.read(file);
			int iw=i.getWidth(null);
			int ih=i.getHeight(null);
			
			System.out.println(w+" "+h+" "+iw+" "+ih);
			
			return w!=iw||h!=ih;
		}catch(Exception e){return true;}
	}
}
