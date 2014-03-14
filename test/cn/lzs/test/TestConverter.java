package cn.lzs.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ConnectException;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeException;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class TestConverter {
	public static final String PATH=System.getProperty("user.dir");//程序相对路径
	
	public static void convertToPDF(File sourceFile,File pdfFile){
		if(sourceFile.exists()){
			if(!pdfFile.exists()){
				try{
					convertPDFDo(sourceFile,pdfFile);
				}catch(ConnectException e){
					System.out.println("OpenOffice 无法链接，可能是你没有启动这个服务！！");
				}catch(OpenOfficeException ee){
					System.out.println("读取源文件出错了！！");
				}catch(Exception eee){
					eee.printStackTrace();
				}
			}else{
				System.out.println("PDF文件已经存在了！！");
			}
		}else{
			System.out.println("要转换的文件不存在");
		}
	}
	
	private static void convertPDFDo(File sourceFile,File pdfFile) throws OpenOfficeException,ConnectException,IOException{
		OpenOfficeConnection openOfficeConnection=new SocketOpenOfficeConnection(8100);//使用8100端口
		openOfficeConnection.connect();
		DocumentConverter converter=new OpenOfficeDocumentConverter(openOfficeConnection);
		converter.convert(sourceFile, pdfFile);
		pdfFile.createNewFile();
		openOfficeConnection.disconnect();
		System.out.println("PDF文件转换成功！！------>"+pdfFile.getAbsolutePath());
		
		//开始转换成 swf 文件
		File swfFile=new File(TestConverter.PATH+"/123.swf");
		TestConverter.convertSWF(pdfFile, swfFile);
	}
	
	/**
	 * 将PDF文件转换成swf文件
	 *	@param sourceFile
	 *	@param swfFile
	 *  @date :2011-11-30
	 */
	public static void convertSWF(File pdfFile,File swfFile){
		Runtime runTime=null;
		try{
			runTime=Runtime.getRuntime();
			Process p = runTime.exec("e:/Program Files/SWFTools/pdf2swf.exe " + pdfFile.getPath() + " -o 123_%.swf " + swfFile.getPath() + " -T 9 -t -s storeallcharacters");
			p.waitFor();
			swfFile.createNewFile();
			System.out.println("SWF文件生成成功：" + swfFile.getAbsolutePath());
			if(pdfFile.exists()) {
				pdfFile.delete();
			}
		}catch(FileNotFoundException e){
			System.out.println("找不到指定的文件："+e.getMessage());
		}catch(IOException eee){
			eee.printStackTrace();
		}catch(InterruptedException ee){
			ee.printStackTrace();
		}
	}
	
	public static void main(String a[]){
		File sourceFile=new File(TestConverter.PATH+"/123.doc");
		File pdfFile=new File(TestConverter.PATH+"/123.pdf");
		System.out.println(pdfFile.getAbsolutePath());
		TestConverter.convertToPDF(sourceFile, pdfFile);
	}
}