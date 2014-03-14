package cn.lzs.share.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.domain.Rank;
import cn.lzs.share.domain.User;
import cn.lzs.share.service.UserService;
import cn.lzs.share.web.model.UserModel;

@Controller
@RequestMapping(value="/user")
@Scope("prototype")
public class UserController {
	
	@Resource(name="userService")
	private UserService userService;
	
	/*
	 * ------------------------------------------------------------------------
	 * 个人信息
	 * ------------------------------------------------------------------------
	 */
	
	/**
	 * 查看个人信息
	 *	@param map
	 *	@param model
	 *	@return
	 *  @date :2012-12-23
	 */
	@RequestMapping(value="/personal")
	public String personal(ModelMap map,UserModel model,HttpServletRequest request){
		try{
			model.setRequest(request);
			userService.personal(model);
			map.put("model", model);
			Rank r=userService.getNextRank(request);
			
			map.put("next_integral",r==null?"不能再升级":r.getIntegral());
			return "user/personal";
		}catch(Exception e){
			e.printStackTrace();
			map.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/doc/list")
	public String docList(ModelMap map,UserModel model,HttpServletRequest request){
		try{
			model.setRequest(request);
			userService.docList(model);
			map.put("model", model);
			return "user/doc_list";
		}catch(Exception e){
			e.printStackTrace();
			map.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/doc/del")
	public String delDoc(UserModel model,HttpServletRequest request,HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			model.setRequest(request);
			userService.delDoc(model);
			SessionUtil.json(response, 0, "success");
		}catch(Exception e){
			e.printStackTrace();
			String info="用户删除文档时出错，id："+model.getIds()+"-->"+e.getMessage();
			Log.error(info);
			SessionUtil.json(response, 1, info);
		}
		return null;
	}
	
	@RequestMapping(value="/header")
	public String uploadHeader(ModelMap map,HttpServletRequest request)throws Exception{
		User user=SessionUtil.getUser(request);
		map.put("user", user);
		map.put("size", Config.getConfig(ConfigItem.HEADER_SIZE));
		return "user/header";
	}
	@RequestMapping(value="/header/upload",method=RequestMethod.POST)
	public String uploadDo(@RequestParam(value="upload_file",required=false)MultipartFile file,ModelMap model,
			UserModel userModel,HttpServletRequest request){
		try{
			userModel.setRequest(request);
			userService.uploadHeader(userModel, file);
			SessionUtil.put(request,"FACE_INFO", "头像上传成功!");
		}catch(Exception e){
			e.printStackTrace();
			Log.error("上传文件失败！"+e.getMessage());
			SessionUtil.put(request, "FACE_INFO", e.getMessage());
		}
		return "redirect:/user/header";
	}
	
	/**
	 * 修改用户密码
	 *	@param model
	 *	@param request
	 *	@param response
	 *	@throws Exception
	 *  @date :2013-1-3
	 */
	@RequestMapping(value="/changePass")
	public void changePass(HttpServletRequest request,HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			userService.changePass(request);
			SessionUtil.json(response, 0, "密码修改成功！");
		}catch(Exception e){
			e.printStackTrace();
			String info="修改密码时出错:"+e.getMessage();
			Log.error(info);
			SessionUtil.json(response, 1, info);
		}
	}
	
	/**
	 * 用户升级
	 *	@param request
	 *	@param response
	 *	@throws Exception
	 *  @date :2012-12-31
	 */
	@RequestMapping(value="/upgrade")
	public void upgrade(HttpServletRequest request,HttpServletResponse response)throws Exception{
		try{
			userService.upgrade(request);
			SessionUtil.json(response, 0, "升级成功！");
		}catch(Exception e){
			String info="升级用户时出错:"+e.getMessage();
			Log.error(info);
			SessionUtil.json(response, 1, info);
		}
	}
	
	/*
	 * ------------------------------------------------------------------------
	 *	查看用户信息
	 * ------------------------------------------------------------------------
	 */
	@RequestMapping(value="/info")
	public String userInfo(ModelMap map,UserModel model,HttpServletRequest request){
		try{
			userService.userInfo(model);
			map.put("model", model);
			return "user/info";
		}catch(Exception e){
			e.printStackTrace();
			map.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "error";
		}
	}
	
	
	/*
	 * ------------------------------------------------------------------------
	 *	举报文档
	 * ------------------------------------------------------------------------
	 */
	@RequestMapping(value="/report")
	public void report(UserModel model,HttpServletRequest request,HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			model.setRequest(request);
			userService.report(model);
			SessionUtil.json(response, 0, "举报文档成功！");
		}catch(Exception e){
			e.printStackTrace();
			String info="用户举报文档时出错，id："+model.getId()+"-->"+(e.getMessage()==null?"请先登录":e.getMessage());
			Log.error(info);
			SessionUtil.json(response, 1, info);
		}
	}
}