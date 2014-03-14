package cn.lzs.share.common.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import cn.lzs.share.common.config.Config;
import cn.lzs.share.common.config.ConfigItem;
import cn.lzs.share.common.exception.ConvertException;
import cn.lzs.share.common.util.Log;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

public class Converter {
	public static final String JODC="jodc";
	
	public static File txtToJPG(File sourceFile,File jpgFile)throws Exception{
		sourceFile=txtToUTF8(sourceFile);//先转换TXT格式的编码,使得全部的txt都是 utf-8 编码的
		List<String> list=ConvertHelper.txtToList(sourceFile, "UTF-8");
		ConvertHelper.getImageByTxt(list,jpgFile);
		return jpgFile;
	}
	
	/**
	 * 将一个 文件 转换成 PDF 格式的， 这个文件应该是COnfig中定义的可以转换成PDF的文件格式。<br />
	 * 返回 转换后的 PDF 文件，方便 Service 层调用数据。<br />
	 * 添加  pdfFIle 这个参数可以加大程序的灵活性，调用者可以自定义 pdf文件的保存路径<br />
	 * 
	 *	@param sourceFile
	 *	@param pdfFile
	 *	@return
	 *  @date :2011-12-2
	 */
	public static File toPDF(File sourceFile,File pdfFile)throws Exception{
		System.out.println("PDF文档11："+pdfFile.getAbsolutePath());
		//一定是一个文件，而不能是一个文件夹
		if(sourceFile.isFile()&&sourceFile.exists()){
			if(pdfFile.exists())
				throw new Exception(ConvertException.PDF_READY_EXIST+"--->"+pdfFile.getAbsolutePath());
			
			pdfFile=toPDFDo(sourceFile,pdfFile);
		}else{
			throw new Exception(ConvertException.ORIGIN_FILE_NOT_FOUND+"--->"+sourceFile.getAbsolutePath());
		}
		return pdfFile;
	}
	
	/**
	 * 真正的转换方法，指定了 OPENOFFICE的端口
	 *	@param sourceFile
	 *	@param pdfFile
	 *	@param port
	 *	@return
	 *	@throws Exception
	 *  @date :2011-12-2
	 */
	private static File toPDFDo(File sourceFile,File pdfFile)throws Exception{
		int port=Integer.valueOf(Config.getSingleConfig(ConfigItem.OPENOFFICE_PORT));
		pdfFile=toPdfByJodc(sourceFile,pdfFile,port);
		return pdfFile;
	}
	
	/**
	 * 通过Jdoc的方法转换成PDF。
	 *	@param sourceFile
	 *	@param pdfFile
	 *	@param port
	 *	@return
	 *	@throws Exception
	 *  @date :2011-12-6
	 */
	public static File toPdfByJodc(File sourceFile,File pdfFile,int port)throws Exception{
		try{
			System.out.println("PDF文档："+pdfFile.getAbsolutePath());
			OpenOfficeConnection openOfficeConnection=new SocketOpenOfficeConnection(port);
			openOfficeConnection.connect();
			DocumentConverter converter=new OpenOfficeDocumentConverter(openOfficeConnection);
			converter.convert(sourceFile, pdfFile);
			pdfFile.createNewFile();
			openOfficeConnection.disconnect();
			System.out.println("PDF文件转换成功！！------>"+pdfFile.getAbsolutePath());
		}catch(ConnectException e){
			throw new Exception(ConvertException.OPENOFFICE_NOT_CONNECT+"--->"+"当前使用的端口："+port);
		}catch(OpenOfficeException ee){
			System.out.println("读取源文件出错了！！");
			throw new Exception("读取源文件出错了！！----->"+ee.getMessage());
		}
		return pdfFile;
	}
	
	/**
	 * 在window环境下，纯文本转换成PDF时出现中文乱码。<br />
	 * 这种情况只是在txt为gbk 编码时出现，可能是OpenOffice能gbk支持不好。<br />
	 * 如果文档是utf-8编码，就中文正常，所以，需要检测是不是UTF<br />
	 * 
	 *	@param file
	 *	@return
	 *	@throws Exception
	 *  @date :2011-12-2
	 */
	public static File txtToUTF8(File file) throws Exception{
		if(!ConvertHelper.txtAndNotUTF8(file))
			return file;
		
		System.out.println(file.getAbsolutePath()+"  是一个TXT并是非UTF8的文件！！");
		Log.error(file.getAbsolutePath()+"  是一个TXT并是非UTF8的文件！！");
		List<String> list=new ArrayList<String>();
		InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "GBK");
		BufferedReader br = new BufferedReader(isr);
		String temp=null;
		while((temp=br.readLine())!=null)
			list.add(temp);
		
		br.close();
		//重新建立文件，这次以UTF-8的编码写入信息
		file.createNewFile();
		PrintWriter pw=new PrintWriter(file,"UTF-8");
		for(String s:list)
			pw.println(s);
		pw.close();
		Log.error(file.getAbsolutePath()+"  --->转换成UTF8 成功！");
		return file;
	}
	
	/**
	 * 直接通过 Itext 写入到 PDF 文件中。
	 *	@param sourceFIle
	 *	@param pdfFile
	 *  @date :2011-12-6
	 */
	public static File toPdfByItext(File sourceFile,File pdfFile)throws Exception{
		List<String> list=new ArrayList<String>();
		InputStreamReader isr = new InputStreamReader(new FileInputStream(sourceFile), "UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String temp=null;
		while((temp=br.readLine())!=null){
			list.add(temp);
		}
		br.close();
		isr.close();
		
		return toPdfByItext(list, pdfFile);
		
	}
	
	/**
	 * 通过 Itext 输入PDF，虽然文件可以生成，但是不能通过SWFTools转成 SWF，这里暂不使用
	 *	@param list
	 *	@param pdfFile
	 *	@return
	 *	@throws Exception
	 *  @date :2011-12-6
	 */
	public static File toPdfByItext(List<String> list,File pdfFile)throws Exception{
		Document doc=new Document(PageSize.A4,10,0,0,0);
		PdfWriter.getInstance(doc, new FileOutputStream(pdfFile.getAbsoluteFile()));	
		doc.open();
				
		addLogo(doc);
		
		BaseFont baseFontChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese =  new  Font(baseFontChinese , 12 , Font.NORMAL);
        for(String text:list)
        	doc.add(new Paragraph(text,fontChinese));
        	
		doc.close();
		
		return pdfFile;
	}
	
	/**
	 * 通过 Itext 将jpg 生成一个PDF
	 *	@param jpgFile
	 *	@param pdfFile
	 *	@return
	 *	@throws Exception
	 *  @date :2011-12-7
	 */
	public static File jpgToPDFByItext(File jpgFile,File pdfFile) throws Exception{
		Document doc=new Document(PageSize.A4,10,0,0,0);
		PdfWriter.getInstance(doc, new FileOutputStream(pdfFile.getAbsoluteFile()));	
		doc.open();
        
		addImageContent(doc, jpgFile.getAbsolutePath());
		
		doc.close();
		return pdfFile;
	}
	
	/**
	 * 添加一个logo水印
	 *	@param doc
	 *	@throws Exception
	 *  @date :2011-12-6
	 */
	public static void addLogo(Document doc)throws Exception{
		String logoPath=null;
		try{
			logoPath=Thread.currentThread().getContextClassLoader().getResource("/config/").toURI().getPath() + "logo.jpg";
		}catch(Exception e){
			//window下
			logoPath=Thread.currentThread().getContextClassLoader().getResource("\\config\\").toURI().getPath() + "logo.jpg";
		}
		Image i=Image.getInstance(logoPath);
		i.setAlignment(Image.RIGHT|Image.UNDERLYING);
		doc.add(i);
		doc.addCreationDate();
        doc.addTitle("雨无声网站--www.newgxu.cn");
        doc.addCreator("集成显卡");
	}
	
	/**
	 * 添加一个图片
	 *	@param doc
	 *	@param path
	 *	@throws Exception
	 *  @date :2011-12-6
	 */
	public static void addImageContent(Document doc,String path)throws Exception{
		Image i=Image.getInstance(path);
		i.setAlignment(Image.LEFT|Image.UNDERLYING);
		doc.add(i);
	}
	
	
	public static File PDFToSwf(File pdfFile,String swfFileName)throws Exception{
		Runtime runTime=Runtime.getRuntime();
		swfFileName=ConvertHelper.getSwfPageName(swfFileName,true);
		String command=Config.getSingleConfig(ConfigItem.SWFTOOLS_PATH)+" -t " + pdfFile.getPath() + 
			" -o " + swfFileName + 
			" -s drawonlyshapes -s flashversion=9";
		System.out.println("需要转换的swf路径："+swfFileName);
		System.out.println(command);
		Process p = runTime.exec(command);
		
		//p.waitFor();
		BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
		String result=null;
		while((result=br.readLine())!=null){
			System.out.println(result);
		}
		System.out.println("SWF文件生成成功：" + swfFileName);
		
		return pdfFile;
	}
	
	public static File JPGToSWF(File jpgFile,String swfFileName)throws Exception{
		Runtime runTime=Runtime.getRuntime();
		swfFileName=ConvertHelper.getSwfPageName(swfFileName,false);
		//Process p = runTime.exec("e:/Program Files/SWFTools/pdf2swf.exe " + pdfFile.getPath() + " -o " + swfName + " -s drawonlyshapes -s flashversion=9");
		String command="e:/Program Files/SWFTools/jpeg2swf.exe " + jpgFile.getPath() + 
			" -o " + swfFileName+
			" -s flashversion=9 -s drawonlyshapes";
		System.out.println("需要转换的swf路径："+swfFileName);
		System.out.println("cmd /c "+command);
		Process p = runTime.exec(command);
		
		//p.waitFor();
		BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream(),"GBK"));
		String result=null;
		while((result=br.readLine())!=null){
			System.out.println(result);
		}
		System.out.println("JPG---->SWF文件生成成功：" + swfFileName);
		
		return jpgFile;
	}
	
	/**
	 * 获取一个pdf文件的页数
	 *	@param file
	 *	@return
	 *	@throws Exception
	 *  @date :2011-12-7
	 */
	public static int getPDFPageNumber(File file)throws Exception{
		if(file.getName().toLowerCase().endsWith(ConvertHelper.PDF)){
			PdfReader reader=new PdfReader(file.getAbsolutePath());
			return reader.getNumberOfPages();
		}
		return -1;
	}
	
	public static void main(String a[]) throws Exception{
		//System.out.println(ConvertHelper.canToPDF(new File("c:/ZendOptimizer_errors.txt")));
		//toPDF(new File("c:/ZendOptimizer_errors.txt"), new File("c:/ZendOptimizer_errors.pdf"));
		//toPDF(new File("c:/ssss.xls"), new File("c:/ssss.pdf"));
		File pdfFile=new File("c:/s.pdf");
		toPDFDo(new File("c:/s.txt"), pdfFile);
		//String swfName="c:/s.swf";
		//Converter.PDFToSwf(pdfFile, swfName);
	}
}