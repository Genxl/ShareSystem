package cn.lzs.share.common.core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.util.Log;

public class ConvertHelper {
	
	public static final String TXT="txt";
	public static final String PDF="pdf";
	public static final String SWF="swf";
	public static final String JPG="jpg";
	
	/**A4纸的大小 595, 842*/
	public static final int A4_HEIGHT=842;
	
	
	/**
	 * 检查文件类型能不能转换成 PDF，如果不要设置的文件列表中，那么是不能在线阅读的
	 *	@param type
	 *	@return
	 *	@throws Exception
	 *  @date :2012-12-13
	 */
	public static boolean canConvert(String type)throws Exception{
		String types=Config.getSingleConfig(ConfigItem.SWF_TYPE);
		return types.contains(type);
	}
	
	/**
	 * 检测file是不是一个TXT文件，同时这个文件的编码应该是非UTF8的
	 *	@param file
	 *	@return
	 *  @date :2011-12-2
	 */
	public static boolean txtAndNotUTF8(File file){
		String name=file.getName().toLowerCase();
		boolean result=name.endsWith(TXT);
		Charset c=new Charset();
		try{
			String encode=c.getEncoding(file).toUpperCase();
			Log.error(file.getAbsolutePath()+" 的编码是："+encode);
			return result&!encode.contains("UTF");
		}catch(Exception e){
			Log.error("获取编码时出错！"+file.getAbsolutePath(), e);
			return result;
		}
	}
	
	public static String getDocPath(String type){
		try{
			if(type.equals(PDF)){
				return Config.getConfig(ConfigItem.PDF_PATH);
			}else if(type.equals(SWF)){
				return Config.getConfig(ConfigItem.SWF_PATH);
			}
			return Config.getConfig(ConfigItem.ORIGIN_PATH);
		}catch(Exception e){
			e.printStackTrace();
			return "/resource";
		}
	}
	
	/**
	 * 获取指定文件类型的保存目录。
	 *	@param request
	 *	@param type
	 *	@return
	 *  @date :2012-12-2
	 */
	public static String getDocPath(HttpServletRequest request,String type){
		String base=request.getSession().getServletContext().getRealPath("");
		return base+getDocPath(type);
	}
	
	public static String getSwfPageName(String name,boolean isPage){
		int index=name.lastIndexOf(".");
		String start=name.substring(0, index);
		if(isPage)
			return start+"_%."+SWF;
		else
			return start+"_1."+SWF;
	}
	
	/**
	 * 将指定的String列表生成一个jpg，并保存到 jpgFile中
	 *	@param list
	 *	@param jpgFile
	 *	@throws Exception
	 *  @date :2011-12-6
	 */
	public static void getImageByTxt(List<String> list,File jpgFile) throws Exception{
		int width=600;
		int height=list.size()*20;
		BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics(); // 创建Graphics类的对象
		Font mFont = new Font("宋体", Font.PLAIN, 13); // 通过Font构造字体
		g.setFont(mFont);
		g.setColor(new Color(202,234,207)); // 改变图形的当前颜色为随机生成的颜色
		g.fillRect(0, 0, width, height); // 绘制一个填色矩形
		g.setColor(Color.BLACK);
		for(int i=0;i<list.size();i++){
			String text=list.get(i);
			g.drawString(text, 5, 20*i+10);
		}
		ImageIO.write(image, "jpg", jpgFile);
	}
	
	public static List<String> txtToList(File file,String encode) throws Exception{
		List<String> list=new ArrayList<String>();
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file),encode);
		BufferedReader br = new BufferedReader(isr);
		String temp=null;
		while((temp=br.readLine())!=null){
			list.add(temp);
		}
		br.close();
		isr.close();
		return list;
	}
}
