package cn.lzs.share.common.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SQLUtil {
	/*
	 * 写入数据
	 */
	public static void writeData(){
		Connection connect=null;
		PreparedStatement ps=null;
		
		try{
			//因为是没有通过servlet操作，要使用传统的方法链接数据库
			Class.forName("com.mysql.jdbc.Driver");
			String url="jdbc:mysql://localhost:3306/wild?useUnicode=true&characterEncoding=UTF-8";
			//取得链接
			connect=DriverManager.getConnection(url,"root","root");
			
			//sql语句
			//String name[]={"class","question","A","B","C","D","answer"};
			String sql="insert into vote_student (xuehao,name,idcard) values (?,?,?);";
			ps=connect.prepareStatement(sql);
			ResultSet rs=connect.prepareStatement("select xuehao,name,idcard from vote_graduate;").executeQuery();
			System.out.println(rs.getRow()+"   "+connect);
			while(rs.next()){
				ps.setString(1, rs.getString("xuehao"));
				ps.setString(2, rs.getString("name"));
				ps.setString(3, rs.getString("idcard"));
				//System.out.println(ps.);
				ps.execute();
			}
			rs.close();
			ps.close();
			System.out.println("writeData---->OK");
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(Exception r){
			r.printStackTrace();
		}
		finally{
			try{
				if(connect!=null)
					connect.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String a[]){
		SQLUtil.writeData();
	}
}
