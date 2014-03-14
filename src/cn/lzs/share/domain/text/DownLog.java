package cn.lzs.share.domain.text;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import cn.lzs.share.domain.BaseEntity;
import cn.lzs.share.domain.User;

@Entity
@Table(name="document_downlog")
@SuppressWarnings("serial")
public class DownLog extends BaseEntity{
	private Date downTime;
	
	private User user;
	private Document document;
	
	public Date getDownTime() {
		return downTime;
	}
	public void setDownTime(Date downTime) {
		this.downTime = downTime;
	}
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=false)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne( fetch = FetchType.LAZY)
	@Cascade(value={CascadeType.MERGE})
	@JoinColumn(name="document_id",nullable=false)
	public Document getDocument() {
		return document;
	}
	public void setDocument(Document document) {
		this.document = document;
	}
	
	@Transient
	public boolean isTimeout(String day){
		long now=System.currentTimeMillis();
		long before=this.downTime.getTime();
		now=now-before;
		System.out.println("已经过"+now+"   可以的时间："+Long.valueOf(day)*24*60*60*1000);
		return now>Long.valueOf(day)*24*60*60*1000;
	}
}