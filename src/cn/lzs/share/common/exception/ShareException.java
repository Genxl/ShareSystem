package cn.lzs.share.common.exception;

public class ShareException {
	public static final String NO_STORE="没有找到你的题库！你还没有申请或者没有通过审核，请先开通题库再操作";
	
	public static final String TOPIC_NO_NAME="题目没有标题!";
	
	public static final String TOPIC_NO_ANSWER="题目没有答案！";
	
	public static final String TOPIC_JUDGE_ANSWER="判断题的答案只能是 true 或者 false 这两个值";
	
	public static final String TOPIC_SINGLE_ANSWER="单选题的答案范围必须在选项的个数以内，且一定是数字形式，比如 1，2，3";
	
	public static final String NO_SHARE_RULE="你没有权限进行这个操作，只能由创建者操作！";
}
