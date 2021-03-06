package cn.lzs.share.web.controller.admin;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.domain.Rank;
import cn.lzs.share.service.admin.UserAdminService;
import cn.lzs.share.web.model.UserModel;
import cn.lzs.share.web.model.admin.RankAdminModel;

@Controller
@Scope("prototype")
@RequestMapping("/ad_min/user")
public class UserAdminController {
	private String PATH="admin/";
	
	@Resource(name="admin.userService")
	private UserAdminService userService;
	
	/*
	 * -----------------------------------------------------------------------
	 * 这个是用户等级的
	 * -----------------------------------------------------------------------
	 */
	@RequestMapping(value="/rank/list")
	public ModelAndView rankList(RankAdminModel model,HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"rank_list");
		try{
			userService.listRank(model);
			mav.addObject("model", model);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "获取用户等级列表时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/rank/add")
	public ModelAndView addRank(ModelMap map,RankAdminModel model){
		ModelAndView mav=new ModelAndView(PATH+"add_rank");
		try{
			Rank r=new Rank();
			r.setId(0);
			model.setRank(r);
			mav.addObject("model", model);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "添加用户等级时出错时出错："+e.getMessage());
			mav.setViewName("error");
			Log.error(e);
		}
		return mav;
	}
	@RequestMapping(value="/rank/addDo")
	public void addRankDo(RankAdminModel model,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		try{
			userService.addRank(model);
			response.getWriter().write(FormatUtil.dwzResult("用户等级 "+model.getRank().getName()+" 创建成功", "rank_list", "user/rank/list"));
		}catch(Exception e){
			e.printStackTrace();
			String info="创建用户等级时出错，-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
	
	@RequestMapping(value="/rank/edit")
	public ModelAndView editRank(RankAdminModel model){
		ModelAndView mav=new ModelAndView(PATH+"add_rank");
		try{
			userService.editRank(model);
			mav.addObject("model", model);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "获取用户等级列表时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	@RequestMapping(value="/rank/editDo")
	public void editRankDo(RankAdminModel model,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			userService.editRankDo(model);
			response.getWriter().write("{\"statusCode\":\"200\", \"message\":\"用户等级 "+model.getRank().getName()+" 修改成功\", \"navTabId\":\"rank_list\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"user/rank/list\"}");
		}catch(Exception e){
			e.printStackTrace();
			String info="修改用户等级时出错，id:"+model.getRank().getId()+"-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
	
	@RequestMapping(value="/rank/del")
	public void delRank(RankAdminModel model,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			userService.delRank(model);
			response.getWriter().write(FormatUtil.dwzResult("等级删除成功！", "rank_list","user/rank/list"));
		}catch(Exception e){
			e.printStackTrace();
			String info="删除用户等级时出错，id:"+model.getId()+"-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
	
	/*
	 * -----------------------------------------------------------------------
	 * 下面的是用户的
	 * -----------------------------------------------------------------------
	 */
	@RequestMapping(value="/list")
	public ModelAndView list(UserModel model,HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"user_list");
		try{
			userService.listUser(model);
			mav.addObject("model", model);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "获取用户列表时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	@RequestMapping(value="/lock")
	public void userLock(UserModel model,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			userService.lockUser(model);
			response.getWriter().write(FormatUtil.dwzResult("用户"+model.getUser().getNick()+"锁定/解锁成功！", "user_list","user/list"));
		}catch(Exception e){
			e.printStackTrace();
			String info="用户锁定/解锁时出错，id:"+model.getId()+"-->"+e.getMessage();
			response.getWriter().write(FormatUtil.dwzResult(info, "user_list","user/list"));
		}
	}
	
	@RequestMapping(value="/edit")
	public ModelAndView userEdit(UserModel model,HttpServletResponse response) throws Exception{
		ModelAndView mav=new ModelAndView(PATH+"edit_user");
		try{
			userService.editUser(model);
			mav.addObject("model", model);
		}catch(Exception e){
			e.printStackTrace();
			String info="用户请求编辑时出错，id:"+model.getId()+"-->"+e.getMessage();
			mav.addObject(Constant.ERROR_MESSAGE, info);
			mav.setViewName("error");
		}
		return mav;
	}
	@RequestMapping(value="/editDo")
	public void editUserDo(UserModel model,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			userService.editUserDo(model);
			response.getWriter().write("{\"statusCode\":\"200\", \"message\":\"用户 "+model.getUser().getName()+" 修改成功\", \"navTabId\":\"user_list\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"user/list\"}");
		}catch(Exception e){
			e.printStackTrace();
			String info="修改用户资料时出错，id:"+model.getId()+"-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
}