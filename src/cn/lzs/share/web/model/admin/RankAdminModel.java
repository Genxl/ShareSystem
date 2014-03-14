package cn.lzs.share.web.model.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.domain.Rank;

public class RankAdminModel {
	private HttpServletRequest request;
	
	private int id;
	private Rank rank;
	private List<Rank> ranks;
	
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
	public Rank getRank() {
		return rank;
	}
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	public List<Rank> getRanks() {
		return ranks;
	}
	public void setRanks(List<Rank> ranks) {
		this.ranks = ranks;
	}
}