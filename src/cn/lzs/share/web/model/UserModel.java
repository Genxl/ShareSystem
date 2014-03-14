package cn.lzs.share.web.model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.common.PageModel;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.text.Document;

public class UserModel extends PageModel{
	private HttpServletRequest request;
	
	private int id;
	private String ids;
	private User user;
	private List<User> users;
	private List<Document> documents;
	
	private String key;
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	public List<Document> getDocuments() {
		return documents;
	}
	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}