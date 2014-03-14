package cn.lzs.share.domain.admin;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.lzs.share.domain.BaseEntity;

@Entity
@Table(name="admin_upload")
@SuppressWarnings("serial")
public class UploadItem extends BaseEntity{
	public static final String INDEX="/resource/images/picture/";
	
	private Date time;
	private String savePath;
	
	private Admin admin;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getSavePath() {
		return savePath;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	@ManyToOne( fetch = FetchType.LAZY)
	@JoinColumn(name="admin_id",nullable=false)
	public Admin getAdmin() {
		return admin;
	}

	public void setAdmin(Admin admin) {
		this.admin = admin;
	}
}