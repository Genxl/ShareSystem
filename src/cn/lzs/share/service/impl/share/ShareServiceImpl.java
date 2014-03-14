package cn.lzs.share.service.impl.share;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.lzs.share.common.util.Finder;
import cn.lzs.share.common.util.SpringUtil;
import cn.lzs.share.dao.share.ApplyDao;
import cn.lzs.share.domain.share.Apply;
import cn.lzs.share.service.share.ApplyListener;
import cn.lzs.share.service.share.ShareService;
import cn.lzs.share.web.model.share.ShareModel;

@Service("share.shareService")
public class ShareServiceImpl implements ShareService{
	@Resource(name="share.applyDao")
	private ApplyDao applyD;
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void apply(Apply a) throws Exception {
		a.setApplyDate(new Date());
		applyD.save(a);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void applyPass(ShareModel model) throws Exception {
		Apply a=applyD.get(model.getId());
		if(a.getState()==Apply.WORKING){
			a.setState(Apply.PASSED);
		}
		
		String name=a.getModule().getBeanName();
		ApplyListener listener=(ApplyListener)SpringUtil.getBean(name);
		listener.applyPass(a);
		
		a.setPassDate(new Date());
	}

	public void applyList(ShareModel model) throws Exception {
		Finder f=new Finder("from Apply as t order by t.applyDate desc");
		f.setPage(model.getPage());
		f.setPageSize(model.getPageSize());
		
		model.setApplys(applyD.pageFind(f));
		model.setTotalCount(f.getTotalSize());
	}
}
