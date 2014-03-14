package cn.lzs.share.service.admin;

import cn.lzs.share.web.model.share.YAskModel;

public interface ShareAdminService {

	public void apply(YAskModel model)throws Exception;
	public void applyPass(YAskModel model)throws Exception;
	
	public void store(YAskModel model)throws Exception;
	public void storeDel(YAskModel model)throws Exception;
	
	public void topic(YAskModel model)throws Exception;
	public void topicDel(YAskModel model)throws Exception;
	
	public void paper(YAskModel model)throws Exception;
	public void paperDo(YAskModel model)throws Exception;
	
	public void result(YAskModel model)throws Exception;
	public void resultDo(YAskModel model)throws Exception;
}
