package cn.lzs.share.web.controller.share;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.common.util.Share;
import cn.lzs.share.common.util.SpringUtil;
import cn.lzs.share.common.util.Share.JoinType;
import cn.lzs.share.domain.User;
import cn.lzs.share.service.UserService;

@Controller
@RequestMapping(value="/share")
@Scope("prototype")
public class ShareController {
	
	/**
	 * 分享模块登录
	 * 支持3种登录模式！
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@return
	 *  @date :2012-12-19
	 */
	@RequestMapping(value="/login")
	public String index(ModelMap map,HttpServletRequest req){
		String errorPage=req.getParameter("errorPage");//错误页面
		String successPage=req.getParameter("successPage");//成功后跳转的页面
		
		//如果没有值，我们使用默认的值
		if(errorPage==null||errorPage.length()==0)
			errorPage="/error";
		if(successPage==null||successPage.length()==0)
			successPage="/";
		
		try{
			//获取登录类型
			Integer type=Integer.valueOf(req.getParameter("loginType"));
			String name=req.getParameter("name");
			String password=req.getParameter("password");
			
			User user=new User();
			
			switch(type){
			//使用学院帐号密码登录
/*			case JoinType.STUDENT:{
				if(RemoteUtil.checkRemoteStudent(name, password)){
					user.setName(name);
					user.setPassword(password);
				}else{
					throw new Exception("无法登录，请确认信息再重试！");
				}
				break;
			}*/
			case JoinType.STU_NAME:{
				//暂时不考虑
				throw new Exception("对不起，目前还没有开放此登录模式！请选择其他登录模式");
			}
			default:{
				UserService userService=(UserService)SpringUtil.getBean("userService");
				user=userService.loign(req, name, password);
			}
			}
			//将登录模式保存到用户state属性中
			user.setState(type);
			SessionUtil.put(req, Share.SHARE_SESSION+type, user);
			
			return "redirect:"+successPage;
		}catch(Exception e){
			e.printStackTrace();
			//map.addAttribute(Constant.ERROR_MESSAGE, "sorry,登录出错！<br />"+e.getMessage());
			String info="sorry,登录出错！<br />"+e.getMessage();
			SessionUtil.put(req, "SHARE_MESSAGE", info);
			System.out.println(errorPage);
			return "redirect:"+errorPage;
		}
	}
}