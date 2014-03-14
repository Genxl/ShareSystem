package cn.lzs.share.service.impl.share;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.lzs.share.common.exception.ShareException;
import cn.lzs.share.common.util.Finder;
import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.common.util.Share;
import cn.lzs.share.common.util.SpringUtil;
import cn.lzs.share.common.util.Share.BornType;
import cn.lzs.share.common.util.Share.JoinType;
import cn.lzs.share.dao.share.ModuleDao;
import cn.lzs.share.dao.share.yask.PaperDao;
import cn.lzs.share.dao.share.yask.ResultDao;
import cn.lzs.share.dao.share.yask.TopicDao;
import cn.lzs.share.dao.share.yask.YStoreDao;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.share.Apply;
import cn.lzs.share.domain.share.yask.Paper;
import cn.lzs.share.domain.share.yask.Result;
import cn.lzs.share.domain.share.yask.Topic;
import cn.lzs.share.domain.share.yask.YStore;
import cn.lzs.share.service.share.ApplyListener;
import cn.lzs.share.service.share.ShareService;
import cn.lzs.share.service.share.YAskService;
import cn.lzs.share.web.model.share.YAskModel;

@Service("share.yaskService")
public class YAskServiceImpl implements YAskService,ApplyListener{
	
	@Resource(name="share.yask.storeDao")
	private YStoreDao storeD;
	@Resource(name="share.yask.topicDao")
	private TopicDao topicD;
	@Resource(name="share.yask.paperDao")
	private PaperDao paperD;
	@Resource(name="share.yask.resultDao")
	private ResultDao resultD;
	@Resource(name="share.moduleDao")
	private ModuleDao moduleD;
	
	public void index(YAskModel model) throws Exception {
		myAsk(model);
		/*
		 * 如果指定了ID，则只显示这个id所对应的试卷的题目。
		 * 只有生成模式是 DIY，才能看到相应的题目列表
		 */
		if(model.getId()>0){
			Paper p=paperD.get(model.getId());
			if(p!=null&&p.getBornType()==BornType.DIY){
				List<Topic> list=new ArrayList<Topic>();
				String[] t=p.getElements().split(",");
				
				for(String is:t){
					if(is.trim().length()>0){
						Topic topic=topicD.get(Integer.valueOf(is));
						list.add(topic);
					}
				}
				model.setPaper(p);
				model.setTopics(list);
				model.setTotalCount(list.size());
				return ;
			}
		}
		
		Finder f=new Finder("from Topic as t where t.ystore=:store order by t.date desc");
		f.setPage(model.getPage());
		f.setPageSize(model.getPageSize());
		f.setParam("store", model.getYstore());
		
		model.setTopics(topicD.pageFind(f));
		model.setTotalCount(f.getTotalSize());
	}
	
	public void myAsk(YAskModel model) throws Exception{
		User user=SessionUtil.getUser(model.getRequest());
		YStore myStroe=storeD.get(user);
		
		model.setYstore(myStroe);
	}
	
	public void addTopic(YAskModel model)throws Exception{
		checkUser(model);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void addTopicDo(YAskModel model)throws Exception{
		checkUser(model);
		Topic t=model.getTopic();
		//将前端传来的数据 中的空格转换成 # 这个分隔符
		String option=t.getItems();
		t.setItems(option);
		System.out.println("--------------->"+option+"  |  "+t.getAnswer()+"  |  "+t.getName()+" storeID:"+model.getYstore().getId());
		
		Share.checkTopic(t);
		t.setYstore(model.getYstore());
		t.setDate(new Date());
		
		topicD.save(t);
	}
	
	public void checkUser(YAskModel model)throws Exception{
		myAsk(model);
		if(model.getYstore()==null)
			throw new Exception(ShareException.NO_STORE);
	}

	public void addTopicBatch(YAskModel model) throws Exception {
		checkUser(model);
	}
	
	/**
	 * 为了做到人性化，这里会记录错误信息，让用户即时修改
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void addTopicBatchDo(YAskModel model) throws Exception {
		checkBatchData(model.getData());
		
		checkUser(model);
		List<Topic> list=Share.toTopicList(model.getData());
		
		String info="扫描到的题目数："+list.size();
		List<String> errors=new ArrayList<String>();//记录错误信息
		
		int num=0;
		for (Topic topic : list) {
			topic.setYstore(model.getYstore());
			topic.setDate(new Date());
			
			try{
				Share.checkTopic(topic);
				topicD.save(topic);
				num++;
			}catch(Exception e){
				//如果没有通过checkTopic，那么就不要保存这个Topic
				errors.add("题目:"+topic.getName()+" 保存出错--><span class='ERROR'>"+e.getMessage()+"</span>");
			}
		}
		
		info+="<br />成功保存了 "+num+" 个题目。";
		if(errors.size()>0){
			info+="<br />过程中出现以下错误：<br />";
			for(String s:errors)
				info+=s+"<br />";
		}
		
		model.setInfo(info);
	}
	
	/**
	 * 从用户那里接收到的 data 的最少长度是：
	 *	@param data
	 *  @date :2012-4-14
	 */
	public void checkBatchData(String data){
	}

	
	public void addPaper(YAskModel model) throws Exception {
		checkUser(model);
		model.setPaper(new Paper());
	}
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void addPaperDo(YAskModel model) throws Exception {
		checkUser(model);
		Paper p=model.getPaper();
		p.setYstore(model.getYstore());
		p.setDate(new Date());
		
		if(p.getId()==null||p.getId()==0)
			paperD.save(p);
		else
			paperD.update(p);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delTopic(YAskModel model) throws Exception {
		checkUser(model);
		if(model.getIds()==null||model.getIds().length()==0){
			Topic t=topicD.get(model.getId());
			if(t==null)
				throw new Exception("想要操作的对象不存在！");
			if(t.getYstore().getId()!=model.getYstore().getId())
				throw new Exception(ShareException.NO_SHARE_RULE);
			topicD.delete(t);
		}
		else{
			String temp[]=model.getIds().split("@");
			for(String a:temp){
				try{
					Topic t=topicD.get(Integer.valueOf(a));
					if(t==null)
						throw new Exception("想要操作的对象不存在！");
					if(t.getYstore().getId()!=model.getYstore().getId())
						throw new Exception(ShareException.NO_SHARE_RULE);
					topicD.delete(t);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void myPaper(YAskModel model) throws Exception {
		checkUser(model);
		Finder f=new Finder("from Paper as t where t.ystore=:store order by t.date desc");
		f.setPage(model.getPage());
		f.setPageSize(model.getPageSize());
		f.setParam("store", model.getYstore());
		
		model.setPapers(paperD.pageFind(f));
		model.setTotalCount(f.getTotalSize());
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delPaper(YAskModel model) throws Exception {
		checkUser(model);
		Paper p=paperD.get(model.getId());
		if(p==null)
			throw new Exception("想要操作的对象不存在！");
		if(p.getYstore().getId()!=model.getYstore().getId())
			throw new Exception(ShareException.NO_SHARE_RULE);
		paperD.delete(p);
	}

	public void editPaper(YAskModel model) throws Exception {
		checkUser(model);
		Paper p=paperD.get(model.getId());
		if(p==null)
			throw new Exception("想要操作的对象不存在！");
		if(p.getYstore().getId()!=model.getYstore().getId())
			throw new Exception(ShareException.NO_SHARE_RULE);
		model.setPaper(p);
	}

	public void randomTopic(YAskModel model) throws Exception {
		checkUser(model);
		if(model.getId()<=0)
			throw new Exception("随机的题目数必须大于0！");
		
		List<Topic> list=topicD.find("from Topic where store_id="+model.getYstore().getId());
		if(model.getId()>list.size())
			throw new Exception("您的题库中没有足够的题目！只有"+list.size()+"道题目，而您却要抽取"+model.getId()+"条=.=");
		
		List<Topic> list2=new ArrayList<Topic>();
		int index=0;
		Random r=new Random();
		String elements="";
		for(int i=0;i<model.getId();i++){
			index=r.nextInt(list.size());
			Topic t=list.remove(index);
			elements+=t.getId()+",";
			list2.add(t);
		}
		model.setData(elements);
		model.setTopics(list2);
		list=null;
		System.gc();
	}
	
	public List<Topic> getTopicList(YStore s){
		return null;
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void startPaper(YAskModel model) throws Exception {
		Paper p=paperD.get(model.getId());
		
		checkPaper(p);
		
		User u=Share.checkShareLogin(model.getRequest(),p.getJoinType());
		checkResultCount(p, u);
		
		p.setHitNum(p.getHitNum()+1);//点击加1
		
		List<Topic> list=new ArrayList<Topic>();
		
		if(p.getBornType()==BornType.RANDOM_ALL){
			List<Topic> tempL=topicD.find("from Topic where store_id="+p.getYstore().getId());
			Random r=new Random();
			for(int i=0;i<p.getTopicNum();i++){
				list.add(tempL.remove(r.nextInt(tempL.size())));
			}
		}else{
			String[] t=p.getElements().split(",");
			
			for(String is:t){
				if(is.trim().length()>0){
					Topic topic=topicD.get(Integer.valueOf(is));
					list.add(topic);
				}
				//只取一定数的题目
				if(list.size()>=p.getTopicNum())
					break;
			}
		}
		
		model.setPaper(p);
		model.setTopics(list);
		
		System.out.println("获取题目："+list.size());
	}

	public void viewPaper(YAskModel model) throws Exception {
		Paper p=paperD.get(model.getId());
		
		model.setPaper(p);
	}
	/**
	 * 检查试卷是否可以被使用，看时间
	 *	@param p
	 *	@throws Exception
	 *  @date :2012-4-17
	 */
	private void checkPaper(Paper p)throws Exception{
		Date d=new Date();
		if(d.after(p.getBeforeDate())&&d.before(p.getAfterDate()))
			return ;
		else{
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			throw new Exception("对不起，你查看的问卷还没有开始或者已经停止，不能访问！该问卷的开放时间是："+df.format(p.getBeforeDate())+" 至 "+df.format(p.getAfterDate()));
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void postPaper(YAskModel model) throws Exception {
		long endTime=System.currentTimeMillis();
		
		long startTime=0;
		try{
			startTime=(Long)SessionUtil.get(model.getRequest(), "SHARE_TIME");
		}catch(Exception e){
			throw new Exception("无法匹配开始时间，此次测试无效！");
		}
		
		Integer id=(Integer)SessionUtil.get(model.getRequest(), "SHARE_ID");
		
		Paper p=paperD.get(id);
		
		if(endTime-startTime>=p.getAllowTime()*1000){
			throw new Exception("对不起，时间已经超出问卷时间！请重新答题");
		}
		
		checkPaper(p);
		User u=Share.checkShareLogin(model.getRequest(),p.getJoinType());
		
		checkResultCount(p, u);
		
		Result result=processResult(model);
		result.setUsedTime((endTime-startTime)/1000);
		result.setRate(result.getRightCount()*100/p.getTopicNum());
		
		if(p.getJoinType()!=JoinType.UNLIMITED){
			result.setName(u.getName());
			result.setNick(u.getNick());
		}else{
			//没有用户名是，使用时间戳
			result.setName(startTime+"");
		}
		
		result.setDate(new Date());
		result.setPaper(p);
		
		resultD.save(result);
		
		model.setResult(result);
	}
	
	/**
	 * 批卷
	 *	@param model
	 *	@return
	 *	@throws Exception
	 *  @date :2012-4-17
	 */
	private Result processResult(YAskModel model)throws Exception{
		Result result=new Result();
		
		//如果为空
		//在服务器中测试，发现item没有被绑定数据
		//原因不明
		//现在就传一个data数据
		if(model.getItem()==null){
			if(model.getData()!=null){
				model.setItem(model.getData().split("@"));
			}
		}
		
		int count=0;
		for(String s:model.getItem()){
			String i[]=s.split("-");
			if(i.length==2){
				Topic t=topicD.get(Integer.valueOf(i[0]));
				if(i[1].trim().equals(t.getAnswer()))
					count++;
			}
		}
		
		result.setRightCount(count);
		
		return result;
	}

	public void viewResult(YAskModel model) throws Exception {
		Result r=resultD.get(model.getId());
		model.setResult(r);
		model.setPaper(r.getPaper());
	}
	
	/**
	 * 检查用户参与问卷的次数是否符合标准
	 *	@param p
	 *	@param u
	 *	@throws Exception
	 *  @date :2012-4-17
	 */
	public void checkResultCount(Paper p,User u)throws Exception{
		if(p.getJoinType()==JoinType.UNLIMITED)
			return ;
		if(p.getMaxJoinNum()==0)
			return ;
		
		int count=resultD.count(p, u.getName());
		if(count>=p.getMaxJoinNum())
			throw new Exception("对不起，‘"+p.getName()+"’这个问卷只允许同一个用户参与"+p.getMaxJoinNum()+"次，而你已经参与了"+count+"次。");
	}

	
	public void resultList(YAskModel model) throws Exception {
		//检查用户并注入试卷列表
		myPaper(model);
		
		String where="where 1=1 ";
		if(model.getId()>0){
			//有卷子就注入Paper对象
			model.setPaper(paperD.get(model.getId()));
			where+=" and paper_id="+model.getId();
		}
		
		boolean iskey=model.getInfo()!=null&&model.getInfo().trim().length()>0;
		if(iskey){
			String like=FormatUtil.toSQLLike(model.getInfo());
			where +=" and name like '"+like+"' or nick like '"+like+"'";
		}
		if(model.getSort()==null)
			model.setSort("rate");
		
		Finder f=new Finder("from Result as t "+where+" order by "+model.getSort()+" desc,t.date desc");
		f.setPage(model.getPage());
		f.setPageSize(model.getPageSize());
		
		model.setResults(resultD.pageFind(f));
		
		if(iskey){
			List<String> list=new ArrayList<String>();
			list.add(model.getInfo());
			for(Result r:model.getResults()){
				r.setName(FormatUtil.lightKey(r.getName(),list));
				r.setNick(FormatUtil.lightKey(r.getNick(),list));
			}
		}
			
		model.setTotalCount(f.getTotalSize());
	}

	public void myDiyPaper(YAskModel model) throws Exception {
		checkUser(model);
		Finder f=new Finder("from Paper as t where t.ystore=:store and t.bornType=:bt order by t.date desc");
		f.setParam("store", model.getYstore());
		f.setParam("bt", BornType.DIY);
		
		model.setPapers(paperD.pageFind(f));
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delTopicFromPaper(YAskModel model) throws Exception {
		checkUser(model);
		
		Paper p=paperD.get(model.getId());
		
		System.out.println("del:"+model.getIds());
	
		String ele=p.getElements();
		String temp[]=model.getIds().split("@");
		for(String a:temp){
			String item=a+",";
			ele=ele.replaceAll(item, "");
			System.out.println("del:"+item+"-->"+ele);
		}
		p.setElements(ele);
		paperD.save(p);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void addTopicToPaper(YAskModel model) throws Exception {
		checkUser(model);
		
		Paper p=paperD.get(model.getId());
		String info=p.getElements();
		
		String temp[]=model.getIds().split("@");
		for(String a:temp){
			info+=(a+",");
		}
		p.setElements(info);
		paperD.save(p);
		
		model.setPaper(p);
	}

	public void apply(YAskModel model) throws Exception {
		User user=SessionUtil.getUser(model.getRequest());
		YStore myStroe=storeD.get(user);
		if(myStroe!=null){
			throw new Exception("对不起，检测到你已经开通了题库，一个用户只能开通一个题库！");
		}
		
		Apply a=new Apply();
		a.setUser(user);
		a.setApplyReason(model.getData());
		a.setData(model.getInfo());
		a.setModule(moduleD.get(1));
		
		ShareService service=(ShareService)SpringUtil.getBean("share.shareService");
		service.apply(a);
	}

	public void applyPass(Apply a) throws Exception {
		System.out.println("接收到Apply："+a.getData());
		YStore store=new YStore();
		store.setUser(a.getUser());
		store.setName(a.getData());
		store.setDate(new Date());
		
		storeD.save(store);
	}
}