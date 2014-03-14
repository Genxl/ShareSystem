package cn.lzs.share.web.controller.document;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.service.DocumentService;
import cn.lzs.share.web.model.text.DocModel;
import cn.lzs.share.web.model.text.SearchModel;

@Controller("document.base")
@RequestMapping(value="/document")
@Scope("prototype")
public class DocumentController {
	
	@Resource(name="documentService")
	private DocumentService documentService;
	
	/**
	 * 转到上传文档的页面
	 *	@param model
	 *	@param request
	 *	@return
	 *  @date :2012-12-9
	 */
	@RequestMapping(value="/new",method=RequestMethod.GET)
	public String add(ModelMap model,DocModel docModel,HttpServletRequest request){
		try{
			docModel.setRequest(request);
			documentService.addDocument(docModel);
			
			model.put("allowExt",Config.getConfig(ConfigItem.CAN_UPLOAD));
			model.put("maxUploadSize", Integer.valueOf(Config.getConfig(ConfigItem.MAX_DOC_SIZE))*1024);
			model.put("model", docModel);
			
			return "document/new_doc";
		}catch(Exception e){
			Log.error("不能加载添加文档信息页面？？？！"+e.getMessage());
			model.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/new",method=RequestMethod.POST)
	public String addDo(ModelMap model,DocModel docModel,HttpServletRequest request){
		try{
			boolean isNew=docModel.getDoc().getId()==null||docModel.getDoc().getId()>=0;
			docModel.setRequest(request);
			System.out.println(docModel.getDoc().getDownPrice()+" -------------------------->");
			documentService.addDocumentDo(docModel);
			model.put("model", docModel);
			if(isNew)
				return "redirect:/user/doc/list";
			else
				return "redirect:upload?docId="+docModel.getDoc().getId();
		}catch(Exception e){
			e.printStackTrace();
			Log.error("添加新的文档信息时出错！"+e.getMessage());
			model.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "document/new_doc";
		}
	}
	
	@RequestMapping(value="/upload",method=RequestMethod.GET)
	public String upload(@RequestParam(value="docId",required=true)int docId,ModelMap model,HttpServletRequest request){
		try{
			model.put("doc", documentService.uploadDocument(request, docId));
			return "document/upload";
		}catch(Exception e){
			Log.error("请求上传文档页面失败！文档id："+docId+"  "+e.getMessage());
			model.put(Constant.ERROR_MESSAGE, e.getMessage());
			e.printStackTrace();
			return "error";
		}
	}
	
	@RequestMapping(value="/upload",method=RequestMethod.POST)
	public String uploadDo(@RequestParam(value="upload_file",required=false)MultipartFile file,
			ModelMap model,DocModel docModel,
			HttpServletRequest request,HttpServletResponse response){
		System.out.println("有文件："+file.getSize());
		System.out.println("-->"+docModel.getDoc_id());
		try{
			docModel.setRequest(request);
			documentService.uploadDocumentDo(docModel, file);
			SessionUtil.put(request, Constant.MESSAGES, getUploadResultMessage(docModel.getDoc()));
			
			if(docModel.getFlash()==0)
				return "redirect:/success";
			else{
				SessionUtil.json(response, 0, "success");
				return null;
			}
		}catch(Exception e){
			e.printStackTrace();
			Log.error("上传文件失败！"+e.getMessage());
			model.put(Constant.ERROR_MESSAGE, e.getMessage());
			
			if(docModel.getFlash()==0)
				return "redirect:upload?docId="+docModel.getDoc().getId();
			else{
				SessionUtil.json(response, 1,"上传文件失败！"+e.getMessage());
				return null;
			}
		}
	}
	public List<String> getUploadResultMessage(Document doc){
		List<String> list=new ArrayList<String>();
		list.add("文档：<font color='#92cf28'>"+doc.getOriginName()+"</font> 已经上传成功!");
		list.add("分类：<font color='#92cf28'>"+doc.getCategory().getTitle()+"</font>");
		list.add("服务器保存名：<font color='#92cf28'>"+doc.getOriginPath()+"</font>");
		list.add("通过管理员的审批后就会对外公开！");
		list.add("<br />感谢您的上传。");
		return list;
	}
	
	
	/*
	 * =========================================================================
	 * 查看/下载的控制器
	 * =========================================================================
	 */
	
	@RequestMapping(value="/view",method=RequestMethod.GET)
	public String view(@RequestParam(value="id",required=true)int id,ModelMap model,HttpServletRequest request){
		try{
			Document doc=documentService.view(id);
			model.put("doc", doc);
			model.put("canRead",doc.getSwfPath()!=null&&doc.isCanReadOnLine()
					&&Boolean.valueOf(Config.getConfig(ConfigItem.CAN_READ_ONLINE)));
			return "document/view";
		}catch(Exception e){
			Log.error("查看文档时出错了？！"+e.getMessage());
			request.getSession().removeAttribute(Constant.ERROR_MESSAGE);
			model.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/download/{name}/{id}")
	public String downLoad(@PathVariable("id")int id,@PathVariable("name")String name,HttpServletRequest request,HttpServletResponse response)throws Exception{
		BufferedInputStream bis=null;
		BufferedOutputStream bos=null;
		
		try{
			Document doc=documentService.downLoad(request,id,name);
			
			//设置 response 的头信息
			response.setContentType("text/html;charset=utf-8");  
	        request.setCharacterEncoding("UTF-8");
	        response.setContentType("application/x-msdownload;");  
            response.setHeader("Content-disposition", "attachment; filename="  
                   +doc.getSaveName());
            		//+ new String(doc.getOriginName().getBytes("utf-8"), "gkb"));
            
            //获取文件以及 输入输出流
            File file=new File(Config.getSingleConfig(ConfigItem.APP_HOME)+doc.getOriginPath());
	        bis=new BufferedInputStream(new FileInputStream(file));
	        bos=new BufferedOutputStream(response.getOutputStream());
	        
	        //将文件的 字节传递出去
	        byte[] buff = new byte[2048];
	        int bytesRead;  
            while ((bytesRead = bis.read(buff, 0, buff.length))!=-1) {  
                bos.write(buff, 0, bytesRead);  
            }
	        return null;
		}catch(Exception e){
			e.printStackTrace();
			String error="下载文件时出错了，ID:"+id+"!<br />"+e.getMessage();
			Log.error(error, e);
			request.setAttribute(Constant.ERROR_MESSAGE, error);
			return "error";
		}finally {  
            if (bis != null)  
                bis.close();  
            if (bos != null)  
                bos.close();  
        }
	}
	
	
	/*
	 * =========================================================================
	 * 分类对应的控制器
	 * =========================================================================
	 */
	@RequestMapping(value="/category",method=RequestMethod.GET)
	public String category(ModelMap map,SearchModel model,HttpServletRequest request){
		try{
			SessionUtil.bindPage(model, request);
			documentService.category(model);
			map.put("model", model);
			return "document/category";
		}catch(Exception e){
			e.printStackTrace();
			Log.error("分类查看时出错了？！"+e.getMessage());
			map.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/category/list")
	public String categoryTree(HttpServletRequest request,HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			String json=documentService.categoryZTree();
			response.getWriter().print(json);
		}catch(Exception e){
			e.printStackTrace();
			String info="获取文档分类列表时出错："+e.getMessage();
			Log.error(info);
			response.getWriter().print("[]");
		}
		return null;
	}
	
	/*
	 * =========================================================================
	 * 搜索的控制器
	 * =========================================================================
	 */
	@RequestMapping(value="/search")
	public String search(ModelMap map,SearchModel model,HttpServletRequest request){
		try{
			long start=System.currentTimeMillis();
			SessionUtil.bindPage(model, request);
			documentService.search(model);
			model.setUseTime(System.currentTimeMillis()-start);
			map.put("model", model);
			return "document/search";
		}catch(Exception e){
			e.printStackTrace();
			Log.error("搜索时出错了？！"+e.getMessage());
			map.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "error";
		}
	}
	
	/*
	 * =========================================================================
	 * 获取下载记录列表的控制器
	 * =========================================================================
	 */
	@RequestMapping(value="/downLogs")
	public String downLog(ModelMap map,DocModel model,HttpServletRequest request){
		try{
			if(model.getPageNumber()==0)
				model.setPageNumber(8);
			documentService.downLog(model);
			map.put("model", model);
			return "document/ajax/downlog_list";
		}catch(Exception e){
			e.printStackTrace();
			Log.error("获取文件的下载记录时出错了？！"+e.getMessage());
			map.put(Constant.ERROR_MESSAGE, e.getMessage());
			return "error";
		}
	}
}