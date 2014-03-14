package cn.lzs.share.web.model;

import java.util.List;

import cn.lzs.share.domain.User;
import cn.lzs.share.domain.sys.Tips;
import cn.lzs.share.domain.text.Category;
import cn.lzs.share.domain.text.Document;

public class IndexModel {
	private List<Category> categorys;
	private List<Document> hotList;
	private List<Document> newList;
	private List<Document> goodList;
	private List<User> users;
	private List<Tips> tipsList;
	
	public List<Tips> getTipsList() {
		return tipsList;
	}
	public void setTipsList(List<Tips> tipsList) {
		this.tipsList = tipsList;
	}
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
	public List<Document> getHotList() {
		return hotList;
	}
	public void setHotList(List<Document> hotList) {
		this.hotList = hotList;
	}
	public List<Document> getNewList() {
		return newList;
	}
	public void setNewList(List<Document> newList) {
		this.newList = newList;
	}
	public List<Document> getGoodList() {
		return goodList;
	}
	public void setGoodList(List<Document> goodList) {
		this.goodList = goodList;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
