package cn.lzs.share.service.admin;

import cn.lzs.share.web.model.admin.DocAdminModel;

public interface DocumentAdminService {
	public void getDocumentList(DocAdminModel model);
	
	public void convert(DocAdminModel model) throws Exception;
	
	public void toGood(DocAdminModel model)throws Exception;
	
	public void lock(DocAdminModel model)throws Exception;
	
	public void getDownloadList(DocAdminModel model);
	public void delDownload(DocAdminModel model)throws Exception;
	public void getReportList(DocAdminModel model);
	public void delReport(DocAdminModel model)throws Exception;
}