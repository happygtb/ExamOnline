package myServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Servlet implementation class ImportQuestion
 */
@WebServlet("/ImportQuestion")
public class ImportQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportQuestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		String filename=request.getParameter("filename");
		String [] strs = filename.split("[.]");
		String filetype=strs[1];
		String fileRealPathandName= getServletContext().getRealPath("/")+"file\\"+filename;
		String subject=(String) request.getSession().getAttribute("subject");
		String tips=(String) request.getSession().getAttribute("tips");
		
		if(filetype.equals("xlsx")||filetype.equals("XLSX")){
			try {
        		FileInputStream is=new FileInputStream(fileRealPathandName);
        		XSSFWorkbook wb = new XSSFWorkbook(is);
        		for(int i=0;i<4;i++){
	       			XSSFSheet sheet = wb.getSheetAt(i);
	       			//读取行
	       			for(int j=1;j<=sheet.getLastRowNum();j++){
	       				XSSFRow row = sheet.getRow(j);
	       				if(row.getCell(0)==null||row.getCell(0).equals("")){
	       					continue;
	       				}else{
		       				//读取单元格
		       				String questionDetails[]=new String[row.getLastCellNum()+1];
		       				for(int p=0;p<=row.getLastCellNum();p++){
		       					XSSFCell cell = row.getCell(p);
		       					if (cell != null) {
		       						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		       						questionDetails[p]= cell.getStringCellValue();
		       					}else{
		       						continue;
		       					}
		       				}
		       				//写入数据库
	       					if(i==0){//单选
	       						//获取出题人学校等信息ID
	    	       				String CollegeId=getCollegeId(questionDetails[11]);
	    	       				String SchoolId=getSchoolId(questionDetails[12]);
	    	       				String MajorId=getMajorId(questionDetails[13]);
	       						//写入出题人信息
	    	       				String RoteId=getRoteId(questionDetails[8],questionDetails[9],questionDetails[10],CollegeId,SchoolId,MajorId);
	    	       				//写入版本信息
	    	       				String VersionId=getVersionId(questionDetails[8]);
	    	       				//写入题目
	    	       				String QuestionId=getQuestionId("1",questionDetails[0],subject,tips,questionDetails[7],VersionId,questionDetails[14],RoteId);
	    	       				//获取正确选项
	    	       				String correct[]=new String[5];
	    	       				for(int c=0;c<5;c++){
	    	       					correct[c]="False";
	    	       				}
	    	       				String crct=questionDetails[6];
	    	       				if(crct.equals("A")){
	    	       					correct[0]="True";
	    	       				}else if(crct.equals("B")){
	    	       					correct[1]="True";
	    	       				}else if(crct.equals("C")){
	    	       					correct[2]="True";
	    	       				}else if(crct.equals("D")){
	    	       					correct[3]="True";
	    	       				}else{
	    	       					correct[4]="True";
	    	       				}
	    	       				//写入选项
	       						String options[]=new String[5];
	       						for(int q=0;q<5;q++){
	       							options[q]=questionDetails[q+1];
	       							if(options[q]!=null){
	           							String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+options[q]+"','"+correct[q]+"')";
	           							PackingDatabase p3 = new PackingDatabase();
	           							try {
	           								p3.update(insertSCOption);
	           							} catch (Exception e) {
	           								System.out.println("单选题选项插入异常" + e.getMessage());
	           							}
	       							}
	       						}
	       					}else if(i==1){//多选
	       						//获取出题人学校等信息ID
	    	       				String CollegeId=getCollegeId(questionDetails[11]);
	    	       				String SchoolId=getSchoolId(questionDetails[12]);
	    	       				String MajorId=getMajorId(questionDetails[13]);
	       						//写入出题人信息
	    	       				String RoteId=getRoteId(questionDetails[8],questionDetails[9],questionDetails[10],CollegeId,SchoolId,MajorId);
	    	       				//写入版本信息
	    	       				String VersionId=getVersionId(questionDetails[8]);
	    	       				//写入题目
	    	       				String QuestionId=getQuestionId("2",questionDetails[0],subject,tips,questionDetails[7],VersionId,questionDetails[14],RoteId);
	    	       				//获取正确选项
	    	       				String correct[]=new String[5];
	    	       				for(int c=0;c<5;c++){
	    	       					correct[c]="False";
	    	       				}
	    	       				String crct=questionDetails[6];
	    	       				for(int c=0;c<5;c++){
	    	       					correct[c]="False";
	    	       				}
	    	       				String s[]=new String[crct.length()];
	    	       				for(int d=0;d<crct.length();d++){
	    	       					s[d]=crct.substring(d,d+1);
	    	       					if(s[d].equals("A")){
	        	       					correct[0]="True";
	        	       				}else if(s[d].equals("B")){
	        	       					correct[1]="True";
	        	       				}else if(s[d].equals("C")){
	        	       					correct[2]="True";
	        	       				}else if(s[d].equals("D")){
	        	       					correct[3]="True";
	        	       				}else{
	        	       					correct[4]="True";
	        	       				}
	    	       				}
	    	       				String options[]=new String[5];
	       						for(int q=0;q<5;q++){
	       							options[q]=questionDetails[q+1];
	       							if(options[q]!=null){
	           							String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+options[q]+"','"+correct[q]+"')";
	           							PackingDatabase p3 = new PackingDatabase();
	           							try {
	           								p3.update(insertSCOption);
	           							} catch (Exception e) {
	           								System.out.println("多选题选项插入异常" + e.getMessage());
	           							}
	       							}
	       						}
	       					}else if(i==2){//判断
	       						//获取出题人学校等信息ID
	    	       				String CollegeId=getCollegeId(questionDetails[6]);
	    	       				String SchoolId=getSchoolId(questionDetails[7]);
	    	       				String MajorId=getMajorId(questionDetails[8]);
	       						//写入出题人信息
	    	       				String RoteId=getRoteId(questionDetails[3],questionDetails[4],questionDetails[5],CollegeId,SchoolId,MajorId);
	    	       				//写入版本信息
	    	       				String VersionId=getVersionId(questionDetails[3]);
	    	       				//获取正确选项
	    	       				String correct;
	    	       				if(questionDetails[1].equals("T")||questionDetails[1].equals("t")){
	    	       					correct="True";
	    	       				}else{
	    	       					correct="False";
	    	       				}
	    	       				//写入题目
	    	       				String QuestionId=getQuestionId("3",questionDetails[0],subject,tips,questionDetails[2],VersionId,questionDetails[9],RoteId);
	       						String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','1','"+correct+"')";
	       						PackingDatabase p3 = new PackingDatabase();
          						try {
           							p3.update(insertSCOption);
           						} catch (Exception e) {
           							System.out.println("判断题选项插入异常" + e.getMessage());
           						}      							
	       					}else{//简答
	       						//获取出题人学校等信息ID
	    	       				String CollegeId=getCollegeId(questionDetails[6]);
	    	       				String SchoolId=getSchoolId(questionDetails[7]);
	    	       				String MajorId=getMajorId(questionDetails[8]);
	       						//写入出题人信息
	    	       				String RoteId=getRoteId(questionDetails[3],questionDetails[4],questionDetails[5],CollegeId,SchoolId,MajorId);
	    	       				//写入版本信息
	    	       				String VersionId=getVersionId(questionDetails[3]);	    	       				
	    	       				//写入题目
	    	       				String QuestionId=getQuestionId("4",questionDetails[0],subject,tips,questionDetails[2],VersionId,questionDetails[9],RoteId);
	       						String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+questionDetails[1]+"','True')";
	       						PackingDatabase p3 = new PackingDatabase();
          						try {
           							p3.update(insertSCOption);
           						} catch (Exception e) {
           							System.out.println("简答题答案插入异常" + e.getMessage());
           						}   
	       					}
	       				}
	       			}
        		}
			} catch (Exception e) {
                e.printStackTrace();
			}
		}
		if(filetype.equals("xls")||filetype.equals("XLS")){
			try {
        		FileInputStream is=new FileInputStream(fileRealPathandName);
        		HSSFWorkbook wb = new HSSFWorkbook(is);
        		for(int i=0;i<4;i++){
	       			HSSFSheet sheet = wb.getSheetAt(i);
	       			//读取行
	       			for(int j=1;j<=sheet.getLastRowNum();j++){
	       				HSSFRow row = sheet.getRow(j);
	       				if(row.getCell(0)==null||row.getCell(0).equals("")){
	       					continue;
	       				}else{
		       				//读取单元格
		       				String questionDetails[]=new String[row.getLastCellNum()+1];
		       				for(int p=0;p<=row.getLastCellNum();p++){
		       					HSSFCell cell = row.getCell(p);
		       					if (cell != null) {
		       						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
		       						questionDetails[p]= cell.getStringCellValue();
		       					}else{
		       						continue;
		       					}
		       				}
		       					//写入数据库
		       					if(i==0){//单选
	       						//获取出题人学校等信息ID
	    	       				String CollegeId=getCollegeId(questionDetails[11]);
	    	       				String SchoolId=getSchoolId(questionDetails[12]);
	    	       				String MajorId=getMajorId(questionDetails[13]);
	       						//写入出题人信息
	    	       				String RoteId=getRoteId(questionDetails[8],questionDetails[9],questionDetails[10],CollegeId,SchoolId,MajorId);
	    	       				//写入版本信息
	    	       				String VersionId=getVersionId(questionDetails[8]);
	    	       				//写入题目
	    	       				String QuestionId=getQuestionId("1",questionDetails[0],subject,tips,questionDetails[7],VersionId,questionDetails[14],RoteId);
	    	       				//获取正确选项
	    	       				String correct[]=new String[5];
	    	       				for(int c=0;c<5;c++){
	    	       					correct[c]="False";
	    	       				}
	    	       				String crct=questionDetails[6];
	    	       				if(crct.equals("A")){
	    	       					correct[0]="True";
	    	       				}else if(crct.equals("B")){
	    	       					correct[1]="True";
	    	       				}else if(crct.equals("C")){
	    	       					correct[2]="True";
	    	       				}else if(crct.equals("D")){
	    	       					correct[3]="True";
	    	       				}else{
	    	       					correct[4]="True";
	    	       				}
	    	       				//写入选项
	       						String options[]=new String[5];
	       						for(int q=0;q<5;q++){
	       							options[q]=questionDetails[q+1];
	       							if(options[q]!=null){
	           							String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+options[q]+"','"+correct[q]+"')";
	           							PackingDatabase p3 = new PackingDatabase();
	           							try {
	           								p3.update(insertSCOption);
	           							} catch (Exception e) {
	           								System.out.println("单选题选项插入异常" + e.getMessage());
	           							}
	       							}
	       						}
	       					}else if(i==1){//多选
	       						//获取出题人学校等信息ID
	    	       				String CollegeId=getCollegeId(questionDetails[11]);
	    	       				String SchoolId=getSchoolId(questionDetails[12]);
	    	       				String MajorId=getMajorId(questionDetails[13]);
	       						//写入出题人信息
	    	       				String RoteId=getRoteId(questionDetails[8],questionDetails[9],questionDetails[10],CollegeId,SchoolId,MajorId);
	    	       				//写入版本信息
	    	       				String VersionId=getVersionId(questionDetails[8]);
	    	       				//写入题目
	    	       				String QuestionId=getQuestionId("2",questionDetails[0],subject,tips,questionDetails[7],VersionId,questionDetails[14],RoteId);
	    	       				//获取正确选项
	    	       				String correct[]=new String[5];
	    	       				for(int c=0;c<5;c++){
	    	       					correct[c]="False";
	    	       				}
	    	       				String crct=questionDetails[6];
	    	       				for(int c=0;c<5;c++){
	    	       					correct[c]="False";
	    	       				}
	    	       				String s[]=new String[crct.length()];
	    	       				for(int d=0;d<crct.length();d++){
	    	       					s[d]=crct.substring(d,d+1);
	    	       					if(s[d].equals("A")){
	        	       					correct[0]="True";
	        	       				}else if(s[d].equals("B")){
	        	       					correct[1]="True";
	        	       				}else if(s[d].equals("C")){
	        	       					correct[2]="True";
	        	       				}else if(s[d].equals("D")){
	        	       					correct[3]="True";
	        	       				}else{
	        	       					correct[4]="True";
	        	       				}
	    	       				}
	    	       				String options[]=new String[5];
	       						for(int q=0;q<5;q++){
	       							options[q]=questionDetails[q+1];
	       							if(options[q]!=null){
	           							String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+options[q]+"','"+correct[q]+"')";
	           							PackingDatabase p3 = new PackingDatabase();
	           							try {
	           								p3.update(insertSCOption);
	           							} catch (Exception e) {
	           								System.out.println("多选题选项插入异常" + e.getMessage());
	           							}
	       							}
	       						}
	       					}else if(i==2){//判断
	       						//获取出题人学校等信息ID
	    	       				String CollegeId=getCollegeId(questionDetails[6]);
	    	       				String SchoolId=getSchoolId(questionDetails[7]);
	    	       				String MajorId=getMajorId(questionDetails[8]);
	       						//写入出题人信息
	    	       				String RoteId=getRoteId(questionDetails[3],questionDetails[4],questionDetails[5],CollegeId,SchoolId,MajorId);
	    	       				//写入版本信息
	    	       				String VersionId=getVersionId(questionDetails[3]);
	    	       				//获取正确选项
	    	       				String correct;
	    	       				if(questionDetails[1].equals("T")||questionDetails[1].equals("t")){
	    	       					correct="True";
	    	       				}else{
	    	       					correct="False";
	    	       				}
	    	       				//写入题目
	    	       				String QuestionId=getQuestionId("3",questionDetails[0],subject,tips,questionDetails[2],VersionId,questionDetails[9],RoteId);
	       						String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','1','"+correct+"')";
	       						PackingDatabase p3 = new PackingDatabase();
          						try {
           							p3.update(insertSCOption);
           						} catch (Exception e) {
           							System.out.println("判断题选项插入异常" + e.getMessage());
           						}      							
	       					}else{//简答
	       						//获取出题人学校等信息ID
	    	       				String CollegeId=getCollegeId(questionDetails[6]);
	    	       				String SchoolId=getSchoolId(questionDetails[7]);
	    	       				String MajorId=getMajorId(questionDetails[8]);
	       						//写入出题人信息
	    	       				String RoteId=getRoteId(questionDetails[3],questionDetails[4],questionDetails[5],CollegeId,SchoolId,MajorId);
	    	       				//写入版本信息
	    	       				String VersionId=getVersionId(questionDetails[3]);	    	       				
	    	       				//写入题目
	    	       				String QuestionId=getQuestionId("4",questionDetails[0],subject,tips,questionDetails[2],VersionId,questionDetails[9],RoteId);
	       						String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+questionDetails[1]+"','True')";
	       						PackingDatabase p3 = new PackingDatabase();
          						try {
           							p3.update(insertSCOption);
           						} catch (Exception e) {
           							System.out.println("简答题答案插入异常" + e.getMessage());
           						}  
	       					}
	       				}
	       			}
        		}
			} catch (Exception e) {
                e.printStackTrace();
			}
		}
		//删除文件
		deleteFile(fileRealPathandName);
		
		response.sendRedirect("QuestionBank/addQuestions.html");
	}  
	
	public String getCollegeId(String CollegeName){
		String mySql = "select CollegeId from College where CollegeName='"+CollegeName+"'";
		String CollegeId="";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			if (rs.next()) {
				CollegeId=rs.getString("CollegeId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return CollegeId;
	}
	
	public String getSchoolId(String SchoolName){
		String mySql = "select SchoolId from School where SchoolName='"+SchoolName+"'";
		String SchoolId="";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			if (rs.next()) {
				SchoolId=rs.getString("SchoolId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return SchoolId;
	}
	
	public String getMajorId(String MajorName){
		String mySql = "select MajorId from Major where MajorName='"+MajorName+"'";
		String MajorId="";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			if (rs.next()) {
				MajorId=rs.getString("MajorId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return MajorId;
	}
	
	public String getRoteId(String RoteName,String RoteTel,String RoteEmail,String CollegeId,String SchoolId,String MajorId){
		String mySql1 = "insert into RoteOfQuestion (RoteName,RoteTel,RoteEmail,CollegeId,SchoolId,MajorId) values('"+RoteName+"','"+RoteTel+"','"+RoteEmail+"','"+CollegeId+"','"+SchoolId+"','"+MajorId+"')";
		String RoteId="";
		PackingDatabase p1 = new PackingDatabase();
		try {
			p1.update(mySql1);
			String sql="select RoteId from RoteOfQuestion where RoteName='"+RoteName+"' and RoteTel='"+RoteTel+"' and MajorId='"+MajorId+"'";
			ResultSet rs = p1.query(sql);
			if (rs.next()) {
				RoteId=rs.getString("RoteId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("出题人信息插入异常" + e.getMessage());
		}	
		return RoteId;
	}
	
	public String getVersionId(String RoteName){
		//写入版本信息
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		String time=format.format(date);
		String insertVersion = "insert into QuestionVersion (EditTime,LastEditTime,LastEditor) values('0','"+time+"','"+RoteName+"')";
		String VersionId="";
		PackingDatabase p7 = new PackingDatabase();
		try {
			p7.update(insertVersion);
			String sql="select VersionId from QuestionVersion where EditTime='0' and LastEditTime='"+time+"' and LastEditor='"+RoteName+"'";
			ResultSet rs = p7.query(sql);
			if (rs.next()) {
				VersionId=rs.getString("VersionId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("版本信息插入异常" + e.getMessage());
		}
		return VersionId;
	}
	
	public String getQuestionId(String QTypeId,String QuestionDetails,String SubjectId,String TipsId,String Difficulty,String VersionId,String Labels,String RoteId){
		//插入题目
		String Sql = "insert into Question (QTypeId,QuestionDetails,SubjectId,TipsId,Difficulty,VersionId,Labels,IsBound,RoteId) values('"+QTypeId+"','"+QuestionDetails+"','"+SubjectId+"','"+TipsId+"','"+Difficulty+"','"+VersionId+"','"+Labels+"','False','"+RoteId+"')";
		String QuestionId="";
		PackingDatabase p2 = new PackingDatabase();
		try {
			p2.update(Sql);
			String sql="select QuestionId from Question where RoteId='"+RoteId+"' and VersionId='"+VersionId+"'";
			ResultSet rs = p2.query(sql);
			if (rs.next()) {
				QuestionId=rs.getString("QuestionId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("题目插入异常" + e.getMessage());
		}
		return QuestionId;
	}
	

    public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
