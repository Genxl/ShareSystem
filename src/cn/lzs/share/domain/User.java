package cn.lzs.share.domain;

import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import cn.lzs.share.common.util.FormatUtil;

@Entity
@Table(name="user")
@SuppressWarnings("serial")
public class User extends BaseEntity{
	
	public static final int MONEY=0;
	public static final int LIVE=1;
	public static final int DOC_NUMBER=2;

	private String name;
	private String password;
	private String nick;
	private String face;//头像
	private int integral;//积分
	private Date joinTime;
	private int loginTimes;
	private Date lastLoginTime;
	private int docNumber;//文档数
	private long fileSize;//一共使用了多少的文件空间 
	private int state;
	private String IP;//最后一次登录的IP
	
	private double used=-1;//使用的空间百分比
	
	private Rank rank;//官阶
	
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	@Column
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	@Column
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	@Column(name="add_time")
	public Date getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}
	@Column
	public int getLoginTimes() {
		return loginTimes;
	}
	public void setLoginTimes(int loginTimes) {
		this.loginTimes = loginTimes;
	}
	@Column
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	@Column(name="document_number")
	public int getDocNumber() {
		return docNumber;
	}
	public void setDocNumber(int docNumber) {
		this.docNumber = docNumber;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	
	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name="rank_id",nullable=false)
	public Rank getRank() {
		return rank;
	}
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	
	@Transient
	public boolean canDownLoad(int price){
		return this.integral>=price;
	}
	
	/**
	 * 添加/删除文件
	 *	
	 *  @date :2012-12-23
	 */
	public void changeNumber(int a){
		this.docNumber+=a;
		if(docNumber<0)
			docNumber=0;
	}
	
	/**
	 * 修改用户的文件大小
	 *	@param size
	 *  @date :2012-12-23
	 */
	@Transient
	public void changeFileSize(long size){
		fileSize+=size;
		if(fileSize<0)
			fileSize=0;
	}
	
	/**
	 * 获取标准格式的文件大小
	 *	@return
	 *  @date :2012-12-10
	 */
	@Transient
	public String getFileSizeDisplay(){
		return FormatUtil.fileSize(getFileSize());
	}
	
	@Transient
	public String getUsedSize(){
		if(used==-1){
			used=(double)this.fileSize/(double)rank.getSize();
		}
		return new DecimalFormat("#0.00%").format(used);
	}
}