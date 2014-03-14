package cn.lzs.share.service.share;

import cn.lzs.share.web.model.share.YAskModel;

public interface YAskService {
	public void index(YAskModel model)throws Exception;
	public void myAsk(YAskModel model) throws Exception;
	
	
	public void apply(YAskModel model)throws Exception;
	
	/**
	 * 添加题目
	 */
	public void addTopic(YAskModel model) throws Exception;
	public void addTopicDo(YAskModel model) throws Exception;
	public void addTopicBatch(YAskModel model)throws Exception;
	public void addTopicBatchDo(YAskModel model)throws Exception;
	/**
	 * 删除题目
	 */
	public void delTopic(YAskModel model)throws Exception;
	/**
	 * 从DIy试卷中删除题目
	 *	@param model
	 *	@throws Exception
	 *  @date :2012-4-18
	 */
	public void delTopicFromPaper(YAskModel model)throws Exception;
	
	public void addTopicToPaper(YAskModel model)throws Exception;
	
	public void resultList(YAskModel model)throws Exception;
	
	/*
	 * ============================================
	 * 卷子
	 * ============================================
	 */
	public void myPaper(YAskModel model)throws Exception;
	/**
	 * 得到我的自定义试卷列表
	 *	@param model
	 *	@throws Exception
	 *  @date :2012-4-18
	 */
	public void myDiyPaper(YAskModel model)throws Exception;
	public void addPaper(YAskModel model)throws Exception;
	public void addPaperDo(YAskModel model)throws Exception;
	public void editPaper(YAskModel model)throws Exception;
	public void delPaper(YAskModel model)throws Exception;
	public void randomTopic(YAskModel model)throws Exception;
	public void viewPaper(YAskModel model)throws Exception;
	/**
	 * 开始测试试卷
	 *	@param model
	 *	@throws Exception
	 *  @date :2012-4-15
	 */
	public void startPaper(YAskModel model)throws Exception;
	public void postPaper(YAskModel model)throws Exception;
	public void viewResult(YAskModel model)throws Exception;
	
}