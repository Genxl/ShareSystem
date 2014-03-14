package cn.lzs.share.domain.share.yask;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;

@Entity
@Table(name="share_yask_paper")
@SuppressWarnings("serial")
public class Paper extends BaseEntity{
	private YStore ystore;
	
	private String name;
	private int topicNum;
	private String information;
	private String elements;//组成
	private int joinType;//用户参与方式
	private int bornType;//卷子生成方式
	private int exeType;//答题方式
	private long allowTime;//可用时间，单位为秒
	private Date beforeDate;
	private Date afterDate;
	private Date date;
	
	private int hitNum;//参与人次统计,这个只是点击的统计，
	private int maxJoinNum;//最大参与次数，为0则是无限
	private String data;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="store_id",nullable=false)
	public YStore getYstore() {
		return ystore;
	}
	public void setYstore(YStore ystore) {
		this.ystore = ystore;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getElements() {
		return elements;
	}

	public void setElements(String elements) {
		this.elements = elements;
	}

	public int getJoinType() {
		return joinType;
	}

	public void setJoinType(int joinType) {
		this.joinType = joinType;
	}

	public int getBornType() {
		return bornType;
	}

	public void setBornType(int bornType) {
		this.bornType = bornType;
	}

	public int getExeType() {
		return exeType;
	}

	public void setExeType(int exeType) {
		this.exeType = exeType;
	}

	public long getAllowTime() {
		return allowTime;
	}

	public void setAllowTime(long allowTime) {
		this.allowTime = allowTime;
	}

	public Date getBeforeDate() {
		return beforeDate;
	}

	public void setBeforeDate(Date beforeDate) {
		this.beforeDate = beforeDate;
	}

	public Date getAfterDate() {
		return afterDate;
	}

	public void setAfterDate(Date afterDate) {
		this.afterDate = afterDate;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	public int getTopicNum() {
		return topicNum;
	}
	public void setTopicNum(int topicNum) {
		this.topicNum = topicNum;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getHitNum() {
		return hitNum;
	}
	public void setHitNum(int hitNum) {
		this.hitNum = hitNum;
	}
	public int getMaxJoinNum() {
		return maxJoinNum;
	}
	public void setMaxJoinNum(int maxJoinNum) {
		this.maxJoinNum = maxJoinNum;
	}
}
