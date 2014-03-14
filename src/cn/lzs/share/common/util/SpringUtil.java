package cn.lzs.share.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtil implements ApplicationContextAware {

	private static ApplicationContext wac=null;
	
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		SpringUtil.wac=arg0;
		System.out.println("ApplicationContext 注入成功！！！！！！！！！！！！！！！！！");
	}
	
	public static ApplicationContext getContext(){
		return wac;
	}
	
	public static Object getBean(String name){
		return getContext().getBean(name);
	}
}