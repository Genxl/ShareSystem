package cn.lzs.share.web.controller.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.domain.admin.Admin;
import cn.lzs.share.domain.sys.Tips;
import cn.lzs.share.service.admin.UserAdminService;
import cn.lzs.share.web.model.admin.SysModel;

@Controller
@Scope("prototype")
@RequestMapping(value="/ad_min/sys")
public class SystemController {
	private String PATH="admin/sys/";
	
	@Resource(name="admin.userService")
	private UserAdminService userService;
	
	/*
	 * -----------------------------------------------------------------------
	 * 首页大图的管理
	 * -----------------------------------------------------------------------
	 */
	@RequestMapping(value="/tips/list")
	public ModelAndView tipsList(SysModel model,HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"tips_list");
		try{
			userService.listTips(model);
			mav.addObject("model", model);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "获取Tips列表时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/tips/add")
	public ModelAndView tipsAdd(SysModel model,HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"tips_add");
		try{
			Tips t=new Tips();
			mav.addObject("t", t);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "尝试请求添加大图片页面时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/tips/addDo")
	public void tipsAddDo(SysModel model,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		try{
			userService.addTips(model);
			response.getWriter().write(FormatUtil.dwzResult("首页大图 "+model.getTips().getName()+" 创建成功", "tips_list", "sys/tips/list"));
		}catch(Exception e){
			e.printStackTrace();
			String info="创建首页大图时出错，-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
	
	@RequestMapping(value="/tips/edit")
	public ModelAndView editRank(SysModel model){
		ModelAndView mav=new ModelAndView(PATH+"tips_add");
		try{
			userService.editTips(model);
			mav.addObject("t", model.getTips());
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "请求Tips编辑页面时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/tips/del")
	public void delRank(SysModel model,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			userService.delTips(model);
			response.getWriter().write(FormatUtil.dwzResult("首页大图删除成功！", "tips_list","sys/tips/list"));
		}catch(Exception e){
			e.printStackTrace();
			String info="删除用户等级时出错，id:"+model.getId()+"-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
	
	/*
	 * -----------------------------------------------------------------------
	 * 系统配置
	 * -----------------------------------------------------------------------
	 */
	@RequestMapping(value="/config/list")
	public ModelAndView config(HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"config");
		try{
			Map<String,String> map=new HashMap<String,String>();
			String names[]=ConfigItem.getNames();
			for(String n:names){
				map.put(n, Config.getConfig(n));
			}
			mav.addObject("map", map);
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject(Constant.ERROR_MESSAGE, "获取系统配置时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	@RequestMapping(value="/config/save")
	public void configSave(HttpServletRequest request,HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			Config.back();
			userService.saveConfig(request);
			response.getWriter().write(FormatUtil.dwzResult("配置文件重写成功！", "",""));
		}catch(Exception e){
			Config.saveBack();
			Admin admin=SessionUtil.getAdmin(request);
			String info="保存配置文件时出错，Admin:"+admin.getTrueName()+"-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
}