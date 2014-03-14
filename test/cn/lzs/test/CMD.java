package cn.lzs.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cn.lzs.share.common.util.Log;

public class CMD {

	public static void main(String a[])throws Exception{
		Process pc=Runtime.getRuntime().exec("cmd /c ipconfig");
		BufferedReader br=new BufferedReader(new InputStreamReader(pc.getInputStream(),"GBK"));
		String result=null;
		while((result=br.readLine())!=null){
			System.out.println(result);
		}
		Log.error("这个是异常！");
		Log.info("这个是信息！");
		throw new Exception("sasasasasa");
	}
}
