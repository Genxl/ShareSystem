package cn.lzs.share.web.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.exception.Exceptions;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.domain.admin.Admin;

public class AdminIntercepter extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(request.getRequestURI().endsWith("/login"))
			return true;
		
		Admin admin=SessionUtil.getAdmin(request);
		if(admin==null){
			SessionUtil.put(request, "admin_"+Constant.ERROR_MESSAGE, Exceptions.NOT_LOGIN);
			response.sendRedirect(request.getContextPath() + "/ad_min/login");
			return false;
		}
		return true;
	}
}