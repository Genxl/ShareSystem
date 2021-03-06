package cn.lzs.share.common.util.search;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class GetHttpPage {
	//判断html图片的正则表达式
	public static final String  IMG_VIEW="(<img.[^\\[]*/>)";
	
	//替换后的img标签
	private static final String IMG_TAG="[屏蔽图片--share.newgxu.cn]";
	
	public static String getHttpPage(String URL,boolean isKillImg){
		String result="";
		try {
			URL=SearchHelper.decrypt(URL);
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(URL);//http://www.newgxu.cn/html/2011-10/106535.htm
			HttpResponse response = httpclient.execute(httpget);
			InputStream is=response.getEntity().getContent();
			String charSet=getCharset(response);
			BufferedReader br=new BufferedReader(new InputStreamReader(is,charSet));
			StringBuffer sbf = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null){
			  sbf.append(line);
			}
			br.close();
			result=sbf.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(isKillImg)
			result=killImg(result);
		return result;
	}
	
	/**
	 * 尝试获取页面的编码，异常时，返回 UTF-8
	 *	@param response
	 *	@return
	 *  @date :2011-10-18
	 */
	public static String getCharset(HttpResponse response){
		try{
			Header contentHeader=response.getHeaders("Content-Type")[0];
			String contentType=contentHeader.getValue();
			if(contentType.toLowerCase().contains("charset=")){
				return contentType.substring(contentType.indexOf("charset=")+8);
			}
			return "gb2312";
		}catch(Exception e){
			e.printStackTrace();
		}
		return "UTF-8";
	}
	
	public static String killImg(String str){
		if(str==null||str.length()==0)
			return "";
		Pattern pattern=Pattern.compile(IMG_VIEW);
		Matcher matcher=pattern.matcher(str);
		return matcher.replaceAll(IMG_TAG);
	}
	
	public static void main(String a[]){
		String str="><img src='../img/image_xlsk/icoe3.jpg' width='25' height='30'/></td>                  <td width='64' class='font14_blue'>心理测试</td> ";
		//System.out.println(FindImg.hasImg(str));
		System.out.println(killImg(str));
	}
}
