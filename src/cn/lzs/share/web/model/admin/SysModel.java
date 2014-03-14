package cn.lzs.share.web.model.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.common.PageModel;
import cn.lzs.share.domain.sys.Tips;

public class SysModel extends PageModel{
	private HttpServletRequest request;
	
	private int id;
	
	private Tips tips;
	private List<Tips> tipsList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public List<Tips> getTipsList() {
		return tipsList;
	}

	public void setTipsList(List<Tips> tipsList) {
		this.tipsList = tipsList;
	}

	public Tips getTips() {
		return tips;
	}

	public void setTips(Tips tips) {
		this.tips = tips;
	}
}