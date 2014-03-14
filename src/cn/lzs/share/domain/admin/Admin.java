package cn.lzs.share.domain.admin;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;

@Entity
@Table(name="admin_")
@SuppressWarnings("serial")
public class Admin extends BaseEntity{
	
	private String name;
	private String password;
	private String trueName;
	
	private int state;
	private Date addTime;
	private int loginTimes;
	private Date lastLogin;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	public int getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
}