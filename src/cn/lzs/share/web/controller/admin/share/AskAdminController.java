package cn.lzs.share.web.controller.admin.share;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.lzs.share.web.model.share.YAskModel;

@Controller
@Scope("prototype")
@RequestMapping(value="/ad_min/share/yask")
public class AskAdminController {
	private String PATH="admin/share/yask";
	private String V(String a){
		return PATH+a;
	}
	
	@RequestMapping(value="/store")
	public String store(ModelMap map,YAskModel model,HttpServletRequest req){
		try{
			map.addAttribute("model", model);
			return V("apply_list");
		}catch(Exception e){
			return "";
		}
	}
}
