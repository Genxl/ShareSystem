package cn.lzs.share.web.intercepter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.lzs.share.cache.Cache;
import cn.lzs.share.domain.text.Document;

public class CategoryIntercepter extends HandlerInterceptorAdapter{
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mav) throws Exception {
		if(mav.getViewName().contains("error"))
			return ;
		String id=request.getParameter("c_id");
		if(id==null){
			Document d=(Document)mav.getModel().get("doc");
			id=d.getCategory().getId()+"";
		}
		Integer i=Integer.valueOf(id);
		mav.addObject("category_hot", Cache.getCategoryHotList(i));
		mav.addObject("category_new", Cache.getCategoryNewList(i));
		super.postHandle(request, response, handler, mav);
	}
}