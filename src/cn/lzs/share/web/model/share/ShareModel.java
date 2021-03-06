package cn.lzs.share.web.model.share;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.common.PageModel;
import cn.lzs.share.domain.share.Apply;

public class ShareModel extends PageModel{
	private HttpServletRequest request;
	
	private int id;
	private String ids;
	private String sort;
	
	private List<Apply> applys;

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
	public String getIds() {
		return ids;
	}
	public void setIds(String ids) {
		this.ids = ids;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public List<Apply> getApplys() {
		return applys;
	}
	public void setApplys(List<Apply> applys) {
		this.applys = applys;
	}
}
