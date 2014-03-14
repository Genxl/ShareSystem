package cn.lzs.share.web.controller.admin.share;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.service.share.ShareService;
import cn.lzs.share.web.model.share.ShareModel;

@Controller
@Scope("prototype")
@RequestMapping(value="/ad_min/share")
public class ShareAdminController {
	private String PATH="admin/share/";
	
	@Resource(name="share.shareService")
	private ShareService shareService;
	
	@RequestMapping(value="/apply/list")
	public String applyList(ModelMap map,ShareModel model,HttpServletRequest req){
		try{
			shareService.applyList(model);
			map.addAttribute("model", model);
			return V("apply_list");
		}catch(Exception e){
			e.printStackTrace();
			return "error";
			//response.getWriter().write(FormatUtil.dwzError(e.getMessage()));
		}
	}
	
	@RequestMapping(value="/apply/pass")
	public void applyPass(ModelMap map,ShareModel model,HttpServletRequest req,HttpServletResponse response)throws Exception{
		response.setCharacterEncoding("utf-8");
		try{
			shareService.applyPass(model);
			response.getWriter().write(FormatUtil.dwzResult("题库申请已经通过！", "share_apply_list", "share/apply/list"));
		}catch(Exception e){
			String info="通过题库申请时出错：";
			Log.error(info, e);
			response.getWriter().write(FormatUtil.dwzError(info));
		}
	}
	
	private String V(String a){
		return PATH+a;
	}
}
