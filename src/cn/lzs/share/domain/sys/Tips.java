package cn.lzs.share.domain.sys;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;

@Entity
@Table(name="sys_tips")
@SuppressWarnings("serial")
public class Tips extends BaseEntity{
	
	private String name;
	private String imgPath;
	private String url;//链接到的地址
	private boolean ifDie;//是否无效
	private int sort;
	private String tips;//说明信息
	private Date addTime;
	
	public Tips(){
		setId(0);
		setAddTime(new Date());
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getTips() {
		return tips;
	}
	public void setTips(String tips) {
		this.tips = tips;
	}
	public boolean isIfDie() {
		return ifDie;
	}
	public void setIfDie(boolean ifDie) {
		this.ifDie = ifDie;
	}
}