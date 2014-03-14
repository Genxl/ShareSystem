package cn.lzs.share.web.controller.share;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.lzs.share.common.Constant;
import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.common.util.SessionUtil;
import cn.lzs.share.service.share.YAskService;
import cn.lzs.share.web.model.share.YAskModel;

@Controller
@RequestMapping(value="/share/yask")
@Scope("prototype")
public class YAskController {
	private String PATH="share/yask/";
	
	@Autowired
	private YAskService askService;
	
	/**
	 * 默认是查看自己的问答系统，这里查看自己的题库。
	 *	@param map
	 *	@param req
	 *	@return
	 *  @date :2012-4-11
	 */
	@RequestMapping(value="/")
	public String index(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			SessionUtil.bindPage(model, req);
			model.setRequest(req);
			askService.index(model);
			map.addAttribute("model", model);
			return PATH+"index";
		}catch(Exception e){
			e.printStackTrace();
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,尝试进入我的问答系统时出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	
	/**
	 * 申请题库
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@return
	 *  @date :2012-4-14
	 */
	@RequestMapping(value="/apply")
	public String apply(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			askService.myAsk(model);
			map.addAttribute("model", model);
			return PATH+"apply";
		}catch(Exception e){
			e.printStackTrace();
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,尝试进入我的问答系统时出错了！<br />"+e.getMessage());
			return "error_ajax";
		}
	}
	
	/**
	 * 申请题库处理
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@return
	 *  @date :2012-4-14
	 */
	@RequestMapping(value="/applyDo")
	public void applyDo(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.apply(model);
			map.addAttribute("model", model);
			SessionUtil.json(response, 0, "题库申请成功！");
		}catch(Exception e){
			e.printStackTrace();
			Log.error("尝试申请题库时出错了!",e);
			Log.info(e.toString());
			SessionUtil.json(response, 1, "sorry,尝试申请题库时出错了！\r\n"+e.getMessage()+"\r\n"+e.toString());
		}
	}
	
	@RequestMapping(value="/topic/add",method=RequestMethod.GET)
	public String addTopic(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			askService.addTopic(model);
			map.addAttribute("model", model);
			return PATH+"topic_add";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,尝试申请题库时出错了！<br />"+e.getMessage());
			return "error_ajax";
		}
	}
	
	@RequestMapping(value="/topic/add",method=RequestMethod.POST)
	public void addTopicDo(YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.addTopicDo(model);
			SessionUtil.json(response, 0, "添加成功！");
		}catch(Exception e){
			e.printStackTrace();
			SessionUtil.json(response, 1, "添加题目时出错[题库id："+model.getId()+"]："+e.getMessage());
		}
	}
	
	/**
	 * 批量导入题目。
	 * 格式：
	 * [标题]
	 * [选项]
	 * ...
	 * [答案]
	 * -----》空行结束这一题
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@return
	 *  @date :2012-4-14
	 */
	@RequestMapping(value="/topic/add_batch",method=RequestMethod.GET)
	public String addTopicBatch(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			askService.addTopicBatch(model);
			map.addAttribute("model", model);
			return PATH+"topic_add_batch";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	@RequestMapping(value="/topic/add_batch",method=RequestMethod.POST)
	public String addTopicBatchDo(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			askService.addTopicBatchDo(model);
			map.addAttribute("model", model);
			SessionUtil.put(req, Constant.MESSAGES, model.getInfo());
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			map.addAttribute("model", model);
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return PATH+"topic_add_batch";
		}
	}
	
	/**
	 * 删除题目，model的ids保存了id串信息
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@param response
	 *  @date :2012-4-14
	 */
	@RequestMapping(value="/topic/del")
	public void delTopic(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.delTopic(model);
			SessionUtil.json(response, 0, "删除操作成功！");
		}catch(Exception e){
			String info="sorry,出错了！<br />"+e.getMessage();
			SessionUtil.json(response, 1, info);
		}
	}
	
	@RequestMapping(value="/topic/delFromPaper")
	public void delTopicFromPaper(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.delTopicFromPaper(model);
			SessionUtil.json(response, 0, "从试卷中删除题目成功！");
		}catch(Exception e){
			String info="sorry,出错了！<br />"+e.getMessage();
			SessionUtil.json(response, 1, info);
		}
	}
	
	@RequestMapping(value="/topic/addToPaper")
	public void addTopicToPaper(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.addTopicToPaper(model);
			SessionUtil.json(response, 0, "添加题目到'"+model.getPaper().getName()+"'成功！");
		}catch(Exception e){
			String info="sorry,出错了！<br />"+e.getMessage();
			SessionUtil.json(response, 1, info);
		}
	}
	
	/*
	 * ============================================================
	 * 卷子页面！
	 * ============================================================
	 */
	
	/**
	 * 转到卷子页面
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@return
	 *  @date :2012-4-14
	 */
	@RequestMapping(value="/paper")
	public String paper(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			SessionUtil.bindPage(model, req);
			model.setRequest(req);
			askService.myPaper(model);
			map.addAttribute("model", model);
			return PATH+"paper";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/paper/diy")
	public String paperDiy(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			askService.myDiyPaper(model);
			map.addAttribute("model", model);
			return PATH+"paper_diy";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error_ajax";
		}
	}
	
	/**
	 * 添加新卷子
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@return
	 *  @date :2012-4-14
	 */
	@RequestMapping(value="/paper/add",method=RequestMethod.GET)
	public String addPaper(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			askService.addPaper(model);
			map.addAttribute("model", model);
			
			return PATH+"paper_add";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	@RequestMapping(value="/paper/add",method=RequestMethod.POST)
	public String addPaperDo(ModelMap map,YAskModel model,HttpServletRequest req){
		String bd=req.getParameter("beforeDate");
		String ad=req.getParameter("afterDate");
		try{
			Date bDate=FormatUtil.date(bd);
			Date aDate=FormatUtil.date(ad);
			
			model.getPaper().setBeforeDate(bDate);
			model.getPaper().setAfterDate(aDate);
			
			model.setRequest(req);
			askService.addPaperDo(model);
			return "redirect:/share/yask/paper";
		}catch(Exception e){
			e.printStackTrace();
			map.addAttribute("beforeDate", ad);
			map.addAttribute("afterDate", ad);
			map.addAttribute("model", model);
			
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return PATH+"paper_add";
		}
	}
	@RequestMapping(value="/paper/edit",method=RequestMethod.GET)
	public String editPaper(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			askService.editPaper(model);
			
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm");
			map.addAttribute("beforeDate",df.format(model.getPaper().getBeforeDate()));
			map.addAttribute("afterDate", df.format(model.getPaper().getAfterDate()));
			map.addAttribute("model", model);
			
			return PATH+"paper_add";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/paper/randomTopic")
	public String randomTopic(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			askService.randomTopic(model);
			
			map.addAttribute("model", model);
			return PATH+"paper_randon_topic";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error_ajax";
		}
	}
	@RequestMapping(value="/paper/del")
	public void delPaper(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.delPaper(model);
			SessionUtil.json(response, 0, "删除操作成功！");
		}catch(Exception e){
			String info="sorry,出错了！<br />"+e.getMessage();
			SessionUtil.json(response, 1, info);
		}
	}
	
	/**
	 * 查看试卷
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@param response
	 *	@return
	 *  @date :2012-4-16
	 */
	@RequestMapping(value="/view")
	public String viewPaper(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.viewPaper(model);
			
			map.addAttribute("model", model);
			return PATH+"paper_view";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/start")
	public String startPaper(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.startPaper(model);
			
			map.addAttribute("model", model);
			return PATH+"start";
		}catch(Exception e){
			e.printStackTrace();
			req.getSession().removeAttribute(Constant.ERROR_MESSAGE);
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	
	/**
	 * 查看相应的统计信息
	 *	@param map
	 *	@param model
	 *	@param req
	 *	@param response
	 *	@return
	 *  @date :2012-4-18
	 */
	@RequestMapping(value="/resultList")
	public String resultList(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			SessionUtil.bindPage(model, req);
			model.setRequest(req);
			askService.resultList(model);
			
			map.addAttribute("model", model);
			return PATH+"result_list";
		}catch(Exception e){
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	
	/*
	 * ==================================================
	 * 答题
	 * ==================================================
	 */
	
	@RequestMapping(value="/checkTime")
	public void checkTime(ModelMap map,HttpServletRequest req,HttpServletResponse response){
		try{
			Integer id=Integer.valueOf(req.getParameter("id"));
			SessionUtil.put(req, "SHARE_TIME", System.currentTimeMillis());
			SessionUtil.put(req, "SHARE_ID",id);
			SessionUtil.json(response, 0, "ok");
		}catch(Exception e){
			SessionUtil.json(response, 1, e.getMessage());
		}
	}
	
	@RequestMapping(value="/result")
	public String viewResult(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			askService.viewResult(model);
			map.addAttribute("model", model);
			return PATH+"result";
		}catch(Exception e){
			e.printStackTrace();
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/post")
	public String post(ModelMap map,YAskModel model,HttpServletRequest req,HttpServletResponse response){
		try{
			model.setRequest(req);
			System.out.println(model.getId()+"-------->");
			askService.postPaper(model);
			map.addAttribute("model", model);
			return "redirect:/share/yask/result?id="+model.getResult().getId();
		}catch(Exception e){
			Log.error("交卷时出错！", e);
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
	
	@RequestMapping(value="/about")
	public String about(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			model.setRequest(req);
			map.addAttribute("model", model);
			return PATH+"about";
		}catch(Exception e){
			e.printStackTrace();
			map.addAttribute(Constant.ERROR_MESSAGE, "sorry,出错了！<br />"+e.getMessage());
			return "error";
		}
	}
}