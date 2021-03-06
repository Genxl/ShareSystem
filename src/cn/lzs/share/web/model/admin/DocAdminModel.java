package cn.lzs.share.web.model.admin;

import java.util.List;

import cn.lzs.share.common.PageModel;
import cn.lzs.share.domain.text.Category;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.domain.text.DownLog;
import cn.lzs.share.domain.text.Report;

public class DocAdminModel extends PageModel{
	
	private int category_id;
	private int doc_id;
	private String key;
	private String doc_type;

	private Category category;
	private Document document;
	
	private List<Category> categorys;
	private List<Document> documents;
	private List<DownLog> downLogs;
	private List<Report> reports;
	
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
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
	public List<Document> getDocuments() {
		return documents;
	}
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	public List<DownLog> getDownLogs() {
		return downLogs;
	}
	public void setDownLogs(List<DownLog> downLogs) {
		this.downLogs = downLogs;
	}
	public List<Report> getReports() {
		return reports;
	}
	public void setReports(List<Report> reports) {
		this.reports = reports;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(String docType) {
		doc_type = docType;
	}
}