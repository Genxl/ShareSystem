package cn.lzs.share.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.lzs.share.cache.Cache;
import cn.lzs.share.common.Constant;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.domain.User;
import cn.lzs.share.service.DocumentService;
import cn.lzs.share.service.UserService;
import cn.lzs.share.web.model.IndexModel;

@Controller
public class CoreController {
	@Resource(name="documentService")
	private DocumentService documentService;
	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 首页
	 *  @date :2012-12-13
	 */
	@RequestMapping(value="")
	public String index(ModelMap model,IndexModel indexModel,HttpServletRequest request){
		try{
			documentService.index(indexModel);
			model.put("model", indexModel);
			model.put("tipsList", indexModel.getTipsList());
			return "index";
		}catch(Exception e){
			e.printStackTrace();
			Log.error("加载首页时出错："+e.getMessage());
			model.put(Constant.ERROR_MESSAGE, "sorry,加载首页时出错了！<br />"+e.getMessage());
			return "error";
		}
		
	}
	
	@RequestMapping(value="/register",method=RequestMethod.GET)
	public String register(ModelMap map){
		map.put("tipsList", Cache.getTips());
		return "user/register";
	}
	
	@RequestMapping(value="/register",method=RequestMethod.POST)
	public String reg(@RequestParam("name")String name,
			 @RequestParam("password")String password,@RequestParam("nick")String nick,HttpServletRequest request){
		try{
			userService.register(request, name, password, nick);
			return "redirect:/login";
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String login(ModelMap map){
		map.put("tipsList", Cache.getTips());
		return "user/login";
	}
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	public String login(@RequestParam("name")String name,
						 @RequestParam("password")String password,ModelMap model,HttpServletRequest request){
		try{
			User user=userService.loign(request,name, password);
			System.out.println("登陆成功！");
			Log.info("登录成功："+user.getLastLoginTime()+" ,已经登录过："+user.getLoginTimes()+" 次.");
			return "redirect:"+SessionUtil.getLoginUrl(request);//"redirect:"+
		}catch(Exception e){
			e.printStackTrace();
			SessionUtil.put(request, Constant.ERROR_MESSAGE,e.getMessage()+"请重新登录！");
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	public String logout(HttpServletRequest request){
		SessionUtil.clearUser(request);
		return "redirect:/login";
	}
	
	@RequestMapping(value="/success")
	public String success(HttpServletRequest request){
		return "success";
	}
}