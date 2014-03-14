package cn.lzs.share.domain.share;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;
import cn.lzs.share.domain.User;

@Entity
@Table(name="share_apply")
@SuppressWarnings("serial")
public class Apply extends BaseEntity{
	
	/*
	 * 定义状态常量
	 */
	public static final int WORKING=0;
	public static final int PASSED=1;
	public static final int FAIL=2;
	
	private User user;
	private Module module;
	private int state;//状态
	private String applyReason;//申请理由
	private Date applyDate;//申请时间
	private Date passDate;//审核通过时间
	private String passReason;//审核备注，如果不通过，可以在这里填写不通过的原因
	
	/**
	 * 这是一个可扩展的字段
	 * 比如在文章申请就可以保存所申请板块的名称。
	 */
	private String data;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="module_id",nullable=false)
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getApplyReason() {
		return applyReason;
	}
	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	public Date getPassDate() {
		return passDate;
	}
	public void setPassDate(Date passDate) {
		this.passDate = passDate;
	}
	public String getPassReason() {
		return passReason;
	}
	public void setPassReason(String passReason) {
		this.passReason = passReason;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
}
