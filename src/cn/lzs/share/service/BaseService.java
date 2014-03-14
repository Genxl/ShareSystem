package cn.lzs.share.service;

import java.io.Serializable;

public interface BaseService<T extends Serializable> {

	public T get(Serializable id);
	
	/**
	 * 根据ID删除记录
	 * 
	 * @param id
	 *            记录ID
	 */
	public T deleteById(Serializable id);
	
}
