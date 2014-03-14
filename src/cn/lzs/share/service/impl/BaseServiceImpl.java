package cn.lzs.share.service.impl;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import cn.lzs.share.service.BaseService;

/**
 * @className:BaseServiceImpl.java
 * @classDescription:基本业务类
 * @author:余影
 * @createTime:2011-3-23
 */
@Transactional
public abstract class BaseServiceImpl<T extends Serializable> implements BaseService<T> {
	protected Logger log = LoggerFactory.getLogger(getClass());
	
}
