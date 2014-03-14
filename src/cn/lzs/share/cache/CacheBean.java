package cn.lzs.share.cache;

import java.util.Date;

import cn.lzs.share.common.config.Config;

public class CacheBean {
	private String name;
	private Date date;
	private long timeout;
	private Object object;
	private long hits;//缓存被调用次数
	
	public CacheBean(){
		this.date=new Date();
		this.timeout=Config.getCacheTime();
	}
	
	/**
	 * 检查缓存是否需要更新
	 */
	public boolean isNeedUpdate(){
		hits++;//每次都会加1
		if(timeout==0)
			return false;
		long newTime=System.currentTimeMillis();
		boolean result=(newTime-date.getTime())>=timeout;
		return result;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTimeout() {
		return timeout;
	}
	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}
	public long getHits() {
		return hits;
	}
	public void setHits(long hits) {
		this.hits = hits;
	}
}