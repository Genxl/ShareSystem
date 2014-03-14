package cn.lzs.share.service.impl.admin;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.lzs.share.dao.share.yask.PaperDao;
import cn.lzs.share.dao.share.yask.ResultDao;
import cn.lzs.share.dao.share.yask.TopicDao;
import cn.lzs.share.dao.share.yask.YStoreDao;
import cn.lzs.share.service.admin.ShareAdminService;
import cn.lzs.share.web.model.share.YAskModel;

@Service("admin.shareService")
public class ShareAdminControllerImpl implements ShareAdminService{

	@SuppressWarnings("unused")
	@Resource(name="share.yask.storeDao")
	private YStoreDao storeD;
	@SuppressWarnings("unused")
	@Resource(name="share.yask.topicDao")
	private TopicDao topicD;
	@SuppressWarnings("unused")
	@Resource(name="share.yask.paperDao")
	private PaperDao paperD;
	@SuppressWarnings("unused")
	@Resource(name="share.yask.resultDao")
	private ResultDao resultD;
	
	public void apply(YAskModel model) throws Exception {
	}

	public void applyPass(YAskModel model) throws Exception {
	}

	public void paper(YAskModel model) throws Exception {
	}

	public void paperDo(YAskModel model) throws Exception {
	}

	public void result(YAskModel model) throws Exception {
	}

	public void resultDo(YAskModel model) throws Exception {
	}

	public void store(YAskModel model) throws Exception {
	}

	public void storeDel(YAskModel model) throws Exception {
	}

	public void topic(YAskModel model) throws Exception {
	}

	public void topicDel(YAskModel model) throws Exception {
	}
}