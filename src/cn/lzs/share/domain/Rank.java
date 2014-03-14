package cn.lzs.share.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.lzs.share.common.util.FormatUtil;

@Entity
@Table(name="rank")
@SuppressWarnings("serial")
public class Rank extends BaseEntity{
	
	private String name;
	private int integral;
	private long size;
	private Date date;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Transient
	public String getSizeDisplay(){
		return FormatUtil.fileSize(size);
	}
}