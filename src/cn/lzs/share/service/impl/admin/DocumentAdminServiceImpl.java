package cn.lzs.share.service.impl.admin;

import java.io.File;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import cn.lzs.share.cache.Cache;
import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.core.ConvertHelper;
import cn.lzs.share.common.core.Converter;
import cn.lzs.share.common.util.Finder;
import cn.lzs.share.common.util.FormatUtil;
import cn.lzs.share.common.util.Log;
import cn.lzs.share.dao.text.DocumentDao;
import cn.lzs.share.dao.text.DownLogDao;
import cn.lzs.share.dao.text.ReportDao;
import cn.lzs.share.domain.User;
import cn.lzs.share.domain.text.Document;
import cn.lzs.share.domain.text.DownLog;
import cn.lzs.share.domain.text.Report;
import cn.lzs.share.service.admin.DocumentAdminService;
import cn.lzs.share.web.model.admin.DocAdminModel;

@Service("admin.documentService")
public class DocumentAdminServiceImpl implements DocumentAdminService {
	@Resource(name="document.documentDao")
	private DocumentDao docDao;
	@Resource(name="document.downLogDao")
	private DownLogDao downDao;
	@Resource(name="document.reportDao")
	private ReportDao reportDao;
	
	public void getDocumentList(DocAdminModel model){
		String sql="from Document where 1=1 ";
		if(model.getKey()!=null){
			String like=FormatUtil.toSQLLike(model.getKey());
			sql+="and title like '%"+like+"%' ";
		}
		if(model.getDoc_type()!=null&&model.getDoc_type().trim().length()>0){
			sql+="and fileType='"+model.getDoc_type()+"' ";
		}
		sql+=" order by uploadTime desc,ifPass desc";
		Finder finder=Finder.create(sql);
		finder.setPage(model.getPage());
		finder.setPageSize(model.getPageSize());
		
		model.setDocuments(docDao.pageFind(finder));
		model.setTotalCount(finder.getTotalSize());
	}
	
	/**
	 * 将指定文档的文件转换成 SWF
	 *	@param model
	 *	@throws Exception
	 *  @date :2011-12-11
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void convert(DocAdminModel model) throws Exception{
		long start=System.currentTimeMillis();
		Document doc=docDao.load(model.getDoc_id());
		
		//允许在线阅读时才会生成swf文件
		if(Boolean.valueOf(Config.getConfig(ConfigItem.CAN_READ_ONLINE))){
			//如果可以是可以转换的文件格式
			if(ConvertHelper.canConvert(doc.getFileType()))
				convertDo(doc);
		}else{
			System.out.println("没有开启阅读选项！不生成SWF");
		}
		
		doc.setIfPass(true);
		doc.setVerifyTime(new Date());
		doc.setProcessTime(System.currentTimeMillis()-start);
		
		model.setDocument(doc);
		
		Log.info("文档转换成功！ID："+doc.getId()+
				" 名称："+doc.getSaveName()+
				" 用时："+doc.getProcessTime());
	}
	
	public void convertDo(Document doc)throws Exception {
		User u=doc.getUser();
		
		String path=Config.getSingleConfig(ConfigItem.APP_HOME);
		File file=new File(path+doc.getOriginPath());
		
		String name=doc.getSaveName().substring(0,doc.getSaveName().lastIndexOf(".")+1);
		String pdfPath=makeDir(path, ConvertHelper.PDF, u.getId());
		String swfPath=makeDir(path, ConvertHelper.SWF, u.getId());
		String swfName=swfPath+doc.getSaveName();
		
		File pdfFile=null;
		
		pdfFile=new File(pdfPath+name+ConvertHelper.PDF);
		
		//txt的处理方法不一样
		if(file.getName().toLowerCase().contains(ConvertHelper.TXT)){
			File jpgFile=new File(pdfPath+name.substring(0, name.length()-1)+"_1."+ConvertHelper.JPG);
			txtToSwf(file, pdfFile,jpgFile);
			//删除JPG
			if(Config.getSingleConfig(ConfigItem.DEL_PDF_FILE).equals("1")&&jpgFile.exists())
				jpgFile.delete();
		}else{
			//如果已经存在了 PDF文件，跳过生成PDF的这一步！
			if(pdfFile.isFile()&&pdfFile.exists()){
				Log.error(pdfFile.getAbsolutePath()+" 已经存在，跳过生成PDF文件的这一步！ 文档ID："+doc.getId());
			}else{
				System.out.println("PDF文档00："+pdfFile.getAbsolutePath());
				Converter.toPDF(file, pdfFile);
			}
		}

		//转换成最终的swf
		Converter.PDFToSwf(pdfFile, swfName);
		//===========================
		//这里可以作一些处理，比如获取pDF文件的页数，这个是需要的
		//===========================
		int pageNumber=Converter.getPDFPageNumber(pdfFile);
		if(Config.getSingleConfig(ConfigItem.DEL_PDF_FILE).equals("1")&&pdfFile.exists())
			pdfFile.delete();
		
		//更新 Document 的信息
		doc.setPageNumber(pageNumber);
		doc.setSwfPath(ConvertHelper.getSwfPageName(swfName, false).replace(path, ""));
	}

	
	public void txtToSwf(File txtFile,File pdfFile,File jpgFile)throws Exception{
		/*
		 * 通过Jodc 的方法转换成PDF，再转换成SWF，需要先将 txt 转换成 UTF-8 编码
		 */
		if(Config.getSingleConfig(ConfigItem.TXT_CONVERT_METHOD).equalsIgnoreCase(Converter.JODC)){
			txtFile=Converter.txtToUTF8(txtFile);
			Converter.toPDF(txtFile, pdfFile);
		}else{
			Converter.txtToJPG(txtFile, jpgFile);
			Converter.jpgToPDFByItext(jpgFile, pdfFile);
		}
	}
	
	public String makeDir(String path,String type,int uid){
		String result=path+ConvertHelper.getDocPath(type)+"/"+uid+"/";
		File file=new File(result);
		if(!file.exists())
			file.mkdir();
		return result;
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void toGood(DocAdminModel model) throws Exception {
		Document d=docDao.get(model.getDoc_id());
		d.setIfGood(true);
		
		Cache.refresh();
		model.setDocument(d);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void lock(DocAdminModel model) throws Exception {
		Document d=docDao.get(model.getDoc_id());
		d.setIfLock(!d.isIfLock());
		if(!d.isIfLock())
			docDao.executeHsql("delete Report where document_id="+d.getId());
		model.setDocument(d);
	}

	public void getDownloadList(DocAdminModel model) {
		Finder finder=Finder.create("from DownLog order by downTime desc");
		finder.setPage(model.getPage());
		finder.setPageSize(model.getPageSize());
		
		model.setDownLogs(downDao.pageFind(finder));
		model.setTotalCount(finder.getTotalSize());
	}

	public void getReportList(DocAdminModel model) {
		Finder finder=Finder.create("from Report order by time desc");
		finder.setPage(model.getPage());
		finder.setPageSize(model.getPageSize());
		
		model.setReports(reportDao.pageFind(finder));
		model.setTotalCount(finder.getTotalSize());
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delDownload(DocAdminModel model) throws Exception {
		DownLog d=downDao.get(model.getDoc_id());
		downDao.delete(d);
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void delReport(DocAdminModel model) throws Exception {
		Report r=reportDao.get(model.getDoc_id());
		reportDao.delete(r);
	}
}