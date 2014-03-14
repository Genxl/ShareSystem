package cn.lzs.share.web.controller.search;

import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.lzs.share.common.util.search.GetHttpPage;
import cn.lzs.share.service.search.SearchEngineService;
import cn.lzs.share.web.model.search.ContentModel;

@Controller
@RequestMapping(value="/search")
@Scope("prototype")
public class YSearchController {
	
	private String PATH="search/";
	
	/**
	 * 网络模式
	 * 1 为使用客户端本身网络 默认
	 * 其他则是使用 服务器网络
	 */
	private String type = "1";
	
	@Resource(name="search.searchService")
	private SearchEngineService searchService;
	
	@RequestMapping(value = "/s", method = RequestMethod.GET )
	public String s(ModelMap model, HttpServletRequest request) throws Exception {
		StringBuffer paramString = new StringBuffer();
		for(Object paramName : request.getParameterMap().keySet()){
			String name = (String)paramName;
			for(String value : request.getParameterValues(name)){
				if(paramString.length()>0)
					paramString.append("&");
				
				try{
					if("type".equalsIgnoreCase(name)){
						type = URLEncoder.encode(value,"ISO8859-1");
					}
					paramString.append(name + "=" + URLEncoder.encode(value,"ISO8859-1"));
				}catch (Exception e) {
					paramString.append(name + "=" + value);
				}
			}
		}
		
		ContentModel contentModel = searchService.listHtml(paramString.toString() , type);
		
		model.put("content", contentModel.getContent());
		model.put("type", type);
		model.put("key",new String(request.getParameter("wd").getBytes("ISO8859-1"),"utf-8"));
		
		return PATH+"result";
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET )
	public String view(ModelMap model, HttpServletRequest request){
		System.out.println(request.getContextPath()+ "have______________request....."+request.getParameter("url"));
		model.put("content", GetHttpPage.getHttpPage(request.getParameter("url"),false));
		return PATH+"view";
	}
	
	
	@RequestMapping(value = "/", method = RequestMethod.GET )
	public String index(){
		return PATH+"index";
	}
}