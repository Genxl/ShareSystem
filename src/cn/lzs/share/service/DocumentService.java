package cn.lzs.share.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import cn.lzs.share.domain.text.Document;
import cn.lzs.share.web.model.IndexModel;
import cn.lzs.share.web.model.text.CommentModel;
import cn.lzs.share.web.model.text.DocModel;
import cn.lzs.share.web.model.text.SearchModel;

public interface DocumentService {
	
	public Document uploadDocument(HttpServletRequest request,int id)throws Exception;
	/**
	 * 保存用户上传的原始文件
	 *	@param model
	 *  @date :2011-12-10
	 */
	public void uploadDocumentDo(DocModel model,MultipartFile file)throws Exception;
	
	/**
	 * 处理文档，生成相应的swf
	 *	@param model
	 *  @date :2012-12-2
	public void processDocuemt(DocModel model)throws Exception;
	*/
	
	public void addDocument(DocModel model)throws Exception;
	/**
	 * 保存文档信息
	 *	@param model
	 *	@throws Exception
	 *  @date :2012-12-10
	 */
	public void addDocumentDo(DocModel model)throws Exception;
	
	public Document view(int id)throws Exception;
	
	public Document downLoad(HttpServletRequest request,int id,String name)throws Exception;
	
	public List<Document> getAboutDocsByCategory(String type,int length,int id)throws Exception;
		
	/*
	 * -----------------------------------------------------
	 * 分类
	 * -----------------------------------------------------
	 */
	public String categoryZTree()throws Exception;
	
	/*
	 * -----------------------------------------------------
	 * 首页
	 * -----------------------------------------------------
	 */
	public void index(IndexModel model)throws Exception;
	
	public void category(SearchModel model)throws Exception;
	
	public void search(SearchModel model)throws Exception;
	
	/*
	 * -----------------------------------------------------
	 * 文档评论
	 * -----------------------------------------------------
	 */
	public void listComment(CommentModel model)throws Exception;
	public void addComment(CommentModel model)throws Exception;
	public void delComment(CommentModel model)throws Exception;
	
	/*
	 * -----------------------------------------------------
	 * 文档下载记录
	 * -----------------------------------------------------
	 */
	public void downLog(DocModel model)throws Exception;
}