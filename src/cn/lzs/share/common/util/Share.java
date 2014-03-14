package cn.lzs.share.common.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.lzs.share.common.exception.ShareException;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.share.yask.Topic;

public class Share {
	public static final String SHARE_SESSION="share_session_name";
	
	/**
	 * 题目类型
	 */
	public class TopicType{
		public static final int JUDGE=0;
		public static final int SINGLE=1;
	}
	
	/**
	 * 用户参与的类型：
	 *  分享用户登陆<br />
		选课系统学号/密码登陆<br />
		无需登陆<br />
		学号/姓名/身份证登陆<br />
	 */
	public class JoinType{
		public static final int LONGAN=0;
		public static final int STUDENT=1;
		public static final int UNLIMITED=2;
		public static final int STU_NAME=3;
	}
	
	/**
	 * 卷子生成的方式。
	 * 				每次测试随机抽取一定数量题目<br />
					手动添加题目<br />
					3.随机生成卷子<br />
	 */
	public class BornType{
		public static final int RANDOM_ONE=0;
		public static final int RANDOM_ALL=1;
		public static final int DIY=2;
	}
	
	/**
	 * 卷子答题的选项显示方式。
	 * 有标准方式，还有使用 roundabout 这个jquery UI 框架的特效
	 */
	public enum ExeType{
		NORMAL,ROUNDABOUT
	}
	
	/**
	 * 检查一个题目是否符合要求。要有答案，选项最少2个。
	 *	@param topic
	 *	@throws Exception
	 *  @date :2012-4-13
	 */
	public static void checkTopic(Topic topic)throws Exception{
		if(FormatUtil.isEmpty(topic.getName()))
			throw new Exception(ShareException.TOPIC_NO_NAME);
		if(FormatUtil.isEmpty(topic.getAnswer()))
			throw new Exception(ShareException.TOPIC_NO_ANSWER);
		
		//判断题的答案必须是treu或者false
		if(topic.getType()==TopicType.JUDGE){
			try{
				Boolean.valueOf(topic.getAnswer().trim());
			}catch(Exception e){
				throw new Exception(ShareException.TOPIC_JUDGE_ANSWER);
			}
		}
		//单选的答案范围必须在选项个数以内
		else if(topic.getType()==TopicType.SINGLE){
			String[] temp=topic.getItems().split(" ");
			try{
				Integer i=Integer.valueOf(topic.getAnswer().trim());
				if(i>=temp.length)
					throw new Exception();
			}catch(Exception e){
				throw new Exception(ShareException.TOPIC_SINGLE_ANSWER);
			}
		}
	}
	
	/**
	 * 先清理data中的空格，再将data分解成数组，然后根据空行将数组分隔
	 * 形成start，end点，从这两个点中的元素组合成一个Topic。
	 * 
	 *	@param data
	 *	@return
	 *  @date :2012-4-14
	 */
	public static List<Topic> toTopicList(String data){
		//处理字符串
		data=FormatUtil.killBlank(data);
		data=data.replaceAll("\r", "").replaceAll("\n", "<br#>");
		
		String[] temp=data.split("<br#>");
		List<Topic> list=new ArrayList<Topic>();
		System.out.println(temp.length+" ----个");
		
		
		int start=0;
		int end=0;
		for(int i=0;i<temp.length;i++){
			System.out.println("--->"+temp[i]);
			boolean canRun=false;
			if(temp[i].trim().length()==0){
				System.out.println("=====>"+i);
				end=i;
				canRun=true;
			}else if(i==temp.length-1){
				//如果只有一个题目，那么start点就不需要更改，直接为0
				if(start!=0)
					start=end+1;
				end=temp.length;
				canRun=true;
			}
			
			if(canRun){
				System.out.println(start+"-->"+end);
				Topic t=makeTopic(temp, start, end);
				if(t!=null)
					list.add(t);
				start=i+1;
			}
		}
		
		
		return list;
	}
	
	/**
	 * 从数组t中的start点到end点构造一个Topic 题目，
	 * end点是空行
	 *	@param t
	 *	@param start
	 *	@param end
	 *	@return
	 *  @date :2012-4-14
	 */
	public static Topic makeTopic(String[] t,int start,int end){
		if(t.length<2||end>t.length||end==0||end==start)
			return null;
		
		Topic tp=new Topic();
		tp.setName(t[start]);
		tp.setAnswer(t[end-1]);
		System.out.println("答案："+tp.getAnswer());
		if(end-start==2){
			tp.setType(TopicType.JUDGE);
		}else{
			tp.setType(TopicType.SINGLE);
			String items="";
			for(int i=start+1;i<end-1;i++){
				items+=" "+t[i];
			}
			tp.setItems(items);
		}
		return tp;
	}
	
	/**
	 * 检查是否可以正常通过
	 *	@param type
	 *	@throws Exception
	 *  @date :2012-4-16
	 */
	public static User checkShareLogin(HttpServletRequest req,int type)throws Exception{
		if(type==JoinType.UNLIMITED)
			return null;
		User u=null;
		//如果分享平台登录，则直接读取默认的session信息
		if(type==JoinType.LONGAN)
			u=SessionUtil.checkLogin(req);
		else	
			u=(User)SessionUtil.get(req,SHARE_SESSION+type);
		if(u==null)
			throw new Exception("在进行操作之前，请按照要求登录！");
		return u;
	}
}