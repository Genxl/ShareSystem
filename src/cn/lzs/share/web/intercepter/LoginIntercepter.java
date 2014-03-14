package cn.lzs.share.web.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.exception.Exceptions;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.domain.User;

public class LoginIntercepter extends HandlerInterceptorAdapter{
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		User user=SessionUtil.getUser(request);
		if(user==null){
			//添加到 记录URL
			SessionUtil.bindLoginUrl(request);
			request.setAttribute(Constant.ERROR_MESSAGE, Exceptions.LOGIN_NEED);
			//response.sendRedirect(request.getContextPath() + "/user/login");
			request.getRequestDispatcher("/login").forward(request, response);
			return false;
		}
		return true;
	}
}