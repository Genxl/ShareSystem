package cn.lzs.share.web.model.text;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.domain.text.Category;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.domain.text.DownLog;

public class DocModel {
	private HttpServletRequest request;
	
	private int flash=0;//是否使用的Flash上传
	
	private Document doc;
	private int doc_id;
	private List<Category> categorys;
	private Category category;
	private int category_id;
	
	private List<DownLog> downLogs;
	
	private File file;
	private String fileName;
	/**记录了需要查看的swf文件的名称*/
	private String swfName;//
	/**pdf的页数，就对应了swf的个数*/
	private int pageNumber;
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getSwfName() {
		return swfName;
	}
	public void setSwfName(String swfName) {
		this.swfName = swfName;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Document getDoc() {
		return doc;
	}
	public void setDoc(Document doc) {
		this.doc = doc;
	}
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int categoryId) {
		category_id = categoryId;
	}
	public int getDoc_id() {
		return doc_id;
	}
	public void setDoc_id(int docId) {
		doc_id = docId;
	}
	public int getFlash() {
		return flash;
	}
	public void setFlash(int flash) {
		this.flash = flash;
	}
	public List<DownLog> getDownLogs() {
		return downLogs;
	}
	public void setDownLogs(List<DownLog> downLogs) {
		this.downLogs = downLogs;
	}
}