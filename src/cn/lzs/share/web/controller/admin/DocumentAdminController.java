package cn.lzs.share.web.controller.admin;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import cn.lzs.share.cache.Cache;
import cn.lzs.share.common.Constant;
import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.service.admin.DocumentAdminService;
import cn.lzs.share.web.model.admin.DocAdminModel;

@Controller
@Scope("prototype")
@RequestMapping(value="/ad_min/document/")
public class DocumentAdminController {
	private String PATH="admin/doc/";
	
	@Resource(name="admin.documentService")
	private DocumentAdminService docService;
	
	@RequestMapping(value="/list")
	public ModelAndView document(DocAdminModel model,HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"doc_list");
		try{
			docService.getDocumentList(model);
			mav.addObject("model", model);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "获取文档列表时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/convert")
	public void convert(DocAdminModel model ,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		try{
			docService.convert(model);
			Cache.refresh();
			response.getWriter().write("{\"statusCode\":\"200\", \"message\":\"文档 "+model.getDocument().getSaveName()+" 转换成功\", \"navTabId\":\"doc_list\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"document/list\"}");
		}catch(Exception e){
			e.printStackTrace();
			String info="转换文档时出错，id："+model.getDoc_id()+"-->"+e.getMessage();
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
	
	@RequestMapping(value="/toGood")
	public void toGood(DocAdminModel model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		try{
			docService.toGood(model);
			String result=FormatUtil.dwzResult(model.getDocument().getTitle()+" 推荐成功！","doc_list", "document/list");
			response.getWriter().write(result);
		}catch(Exception e){
			e.printStackTrace();
			String info="推荐文档时出错，id："+model.getDoc_id()+"-->"+e.getMessage();
			SessionUtil.json(response, 1, info);
		}
	}
	
	@RequestMapping(value="/lock")
	public void lock(DocAdminModel model,HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setCharacterEncoding("utf-8");
		try{
			docService.lock(model);
			String result=FormatUtil.dwzResult(model.getDocument().getTitle()+" 锁定/解锁操作成功！","doc_list", "document/list");
			response.getWriter().write(result);
		}catch(Exception e){
			e.printStackTrace();
			String info="推荐文档时出错，id："+model.getDoc_id()+"-->"+e.getMessage();
			SessionUtil.json(response, 1, info);
		}
	}
	
	/*
	 *----------------------------------------------------- 
	 *下载记录
	 *----------------------------------------------------- 
	 */
	@RequestMapping(value="/download/list")
	public ModelAndView downloadList(DocAdminModel model,HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"download_list");
		try{
			docService.getDownloadList(model);
			mav.addObject("model", model);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "获取下载记录列表时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/download/del")
	public void delDownload(DocAdminModel model,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			docService.delDownload(model);
			response.getWriter().write(FormatUtil.dwzResult("下载记录删除成功！", "",""));
		}catch(Exception e){
			e.printStackTrace();
			String info="删除下载记录时出错，id:"+model.getDoc_id()+"-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
	
	/*
	 *----------------------------------------------------- 
	 *举报记录
	 *----------------------------------------------------- 
	 */
	@RequestMapping(value="/report/list")
	public ModelAndView reportList(DocAdminModel model,HttpServletRequest request){
		ModelAndView mav=new ModelAndView(PATH+"report_list");
		try{
			docService.getReportList(model);
			mav.addObject("model", model);
		}catch(Exception e){
			mav.addObject(Constant.ERROR_MESSAGE, "获取举报列表时出错："+e.getMessage());
			mav.setViewName("error");
		}
		return mav;
	}
	
	@RequestMapping(value="/report/del")
	public void delReport(DocAdminModel model,HttpServletResponse response) throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			docService.delReport(model);
			response.getWriter().write(FormatUtil.dwzResult("举报记录删除成功！", "",""));
		}catch(Exception e){
			e.printStackTrace();
			String info="举报下载记录时出错，id:"+model.getDoc_id()+"-->"+e.getMessage();
			Log.error(info, e);
			response.getWriter().write("{\"statusCode\":\"500\", \"message\":\""+info+"\", \"navTabId\":\"\",\"rel\":\"\", \"callbackType\":\"\",\"forwardUrl\":\"\"}");
		}
	}
}