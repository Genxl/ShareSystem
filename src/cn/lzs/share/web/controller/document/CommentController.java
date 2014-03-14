package cn.lzs.share.web.controller.document;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.service.DocumentService;
import cn.lzs.share.web.model.text.CommentModel;

@Controller
@RequestMapping(value="/document/comment")
@Scope("prototype")
public class CommentController {
	private String PATH="document/ajax/";
	
	@Resource(name="documentService")
	private DocumentService documentService;
	
	@RequestMapping(value="/list")
	public ModelAndView list(CommentModel model,HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"comment_list");
		try{
			model.setPageSize(10);
			documentService.listComment(model);
			mav.addObject("model", model);
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject(Constant.ERROR_MESSAGE, e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/add")
	public void add(CommentModel model,HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		try{
			model.setRequest(request);
			documentService.addComment(model);
			SessionUtil.json(response, 0, "success");
		}catch(Exception e){
			e.printStackTrace();
			String info="添加评论出错-->"+e.getMessage();
			SessionUtil.json(response, 1, info);
		}
	}
	
	@RequestMapping(value="/del")
	public void del(CommentModel model,HttpServletRequest request,HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		try{
			documentService.delComment(model);
			SessionUtil.json(response, 0, "success");
		}catch(Exception e){
			e.printStackTrace();
			String info="添加评论出错-->"+e.getMessage();
			SessionUtil.json(response, 1, info);
		}
	}
}