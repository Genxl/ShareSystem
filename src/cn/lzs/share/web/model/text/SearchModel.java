package cn.lzs.share.web.model.text;

import java.util.List;

import cn.lzs.share.common.PageModel;
import cn.lzs.share.domain.text.Category;
import cn.lzs.share.domain.text.Document;

public class SearchModel extends PageModel{
	private long useTime;//搜索用时
	private String key;
	private int c_id;
	private String sort;//
	private boolean desc=true;//是否降序
	private String doc_type;//文档类型，比如TXT，DOC
	
	private Category category;
	
	private List<Document> documents;
	private List<Category> categorys;
	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getC_id() {
		return c_id;
	}
	public void setC_id(int cId) {
		c_id = cId;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public boolean isDesc() {
		return desc;
	}
	public void setDesc(boolean desc) {
		this.desc = desc;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public List<Document> getDocuments() {
		return documents;
	}
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
	public long getUseTime() {
		return useTime;
	}
	public void setUseTime(long useTime) {
		this.useTime = useTime;
	}
	public String getDoc_type() {
		return doc_type;
	}
	public void setDoc_type(String docType) {
		doc_type = docType;
	}
}