package cn.lzs.share.service.share;

import cn.lzs.share.domain.share.Apply;
import cn.lzs.share.web.model.share.ShareModel;

public interface ShareService {
	
	public void applyList(ShareModel model)throws Exception;
	
	public void apply(Apply a) throws Exception;
	
	public void applyPass(ShareModel model)throws Exception;
}