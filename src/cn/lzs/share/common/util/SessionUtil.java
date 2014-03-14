package cn.lzs.share.common.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.PageModel;
import cn.lzs.share.common.exception.Exceptions;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.admin.Admin;

public class SessionUtil {

	public static void put(HttpServletRequest request,String key,Object value){
		HttpSession session=request.getSession();
		session.setAttribute(key, value);
	}
	
	public static Object get(HttpServletRequest request,String key){
		HttpSession session=request.getSession();
		return session.getAttribute(key);
	}
	
	/**
	 * 获取登录后的用户
	 *	@param request
	 *	@return
	 *  @date :2011-12-7
	 */
	public static User getUser(HttpServletRequest request){
		return (User)get(request,Constant.LOGIN_USER);
	}
	
	public static User checkLogin(HttpServletRequest request)throws Exception{
		User u=getUser(request);
		if(u==null)
			throw new Exception(Exceptions.NOT_LOGIN);
		return u;
	}
	
	public static void clearUser(HttpServletRequest request){
		HttpSession session=request.getSession();
		session.removeAttribute(Constant.LOGIN_USER);
	}
	
	/**
	 * 获取登录的管理员
	 *	@param request
	 *	@return
	 *  @date :2012-1-4
	 */
	public static Admin getAdmin(HttpServletRequest request){
		return (Admin)get(request,Constant.ADMIN);
	}
	public static Admin checkAdmin(HttpServletRequest request)throws Exception{
		Admin u=getAdmin(request);
		if(u==null)
			throw new Exception(Exceptions.NOT_LOGIN);
		return u;
	}
	public static void clearAdmin(HttpServletRequest request){
		HttpSession session=request.getSession();
		session.removeAttribute(Constant.ADMIN);
	}
	
	@SuppressWarnings("unchecked")
	public static void bindPage(PageModel model,HttpServletRequest request){
		model.setActionName(request.getRequestURI());
		model.setParamMap(request.getParameterMap());
	}
	
	public static void initResponse(HttpServletResponse response){
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Cache-Control", "no-cache");
	}
	
	@SuppressWarnings("unchecked")
	public static void json(HttpServletResponse response,int isError,String info){
		initResponse(response);
		PrintWriter writer = null;
		try{
			writer=response.getWriter();
			JSONObject obj = new JSONObject();
			obj.put("error", isError);
			obj.put("message",info);
			writer.write(obj.toJSONString());
		} catch (IOException e) {
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
	
	public static void bindLoginUrl(HttpServletRequest request){
		String url=request.getRequestURL()+"?"+request.getQueryString();
		put(request, Constant.BEFORE_LOGIN, url);
		System.out.println("已经标记！---------------》"+url);
	}
	
	public static String getLoginUrl(HttpServletRequest request){
		HttpSession session=request.getSession();
		String url=(String)session.getAttribute(Constant.BEFORE_LOGIN);
		if(url==null)
			url="/";
		else{
			session.removeAttribute(Constant.BEFORE_LOGIN);
		}
		if(url.indexOf("/longansss")>-1)
			url=url.replace("/longan", "");
		return url;
	}
	
}
