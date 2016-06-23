package myServlet;


import java.io.FileOutputStream;
import java.io.IOException;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.json.JSONObject;

import cc.CustomXWPFDocument;
import cc.IPTimeStamp;

/**
 * Servlet implementation class CreatrWordDoc
 */
@WebServlet("/CreateWordDoc")
public class CreateWordDoc extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CreateWordDoc() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		String method = request.getParameter("method");
		//request.getParameter("paperId");
		String paperId = "5034";
		String PaperName = "";
		CustomXWPFDocument docx = new CustomXWPFDocument();
		if ("imported".equals(method)) {// 导入的试卷
			
			
			
		} else if ("normal".equals(method)) {// 手动试卷
			String TotalTime = "";
			String TotalPoint = "";
			String ExamDescription = "";
			String mysql = "select * from Paper where PaperId='" + paperId + "'";
			PackingDatabase p = new PackingDatabase();
			try {// 获取内容
				ResultSet rs = p.query(mysql);
				if (rs.next()) {
					PaperName = rs.getString("PaperName");
					ExamDescription = rs.getString("ExamDescription");
					TotalPoint = rs.getString("TotalPoint");
					TotalTime = rs.getString("TotalTime");
				}
				rs.close();
			} catch (Exception e) {
				System.out.println("试卷查询异常" + e.getMessage());
			}
			// 试卷名称
			XWPFParagraph p1 = docx.createParagraph();
			p1.setAlignment(ParagraphAlignment.CENTER);// 对齐方式-居中
			p1.setBorderBottom(Borders.NONE);// 设置边框
			p1.setBorderTop(Borders.NONE);
			p1.setBorderRight(Borders.NONE);
			p1.setBorderLeft(Borders.NONE);
			p1.setBorderBetween(Borders.NONE);
			XWPFRun r1 = p1.createRun();
			r1.setBold(true);// 粗体
			r1.setFontSize(18);// 字号
			r1.setText(PaperName);
			r1.setFontFamily("仿宋");// 设置字体格式
			r1.setTextPosition(12);// 行距
			
			setParagraph(docx, "试卷描述:"+ExamDescription, false, 12);//试卷描述
			setParagraph(docx, "试卷总分:"+TotalPoint, false, 12);//试卷总分
			setParagraph(docx, "试卷限时:"+TotalTime, false, 12);//试卷限时
			// 开始读取每部分的内容
			List<String> Part = new ArrayList<String>();
			List<String> Question = new ArrayList<String>();
			List<String> Pquestion = new ArrayList<String>();
			List<String> Qoption = new ArrayList<String>();
			List<String> PartName = new ArrayList<String>();  
			List<String> Description = new ArrayList<String>(); 
			List<String> PartPoint = new ArrayList<String>(); 
			List<String> CountQuestion = new ArrayList<String>(); 
			List<String> perPoint = new ArrayList<String>(); 
			List<String> TypeName = new ArrayList<String>(); 
			List<String> questionDetail = new ArrayList<String>(); 
			List<String> option = new ArrayList<String>(); 
			List<String> isCorrect = new ArrayList<String>();
			try {
			String sql = "select * from PaperPart where PaperId='" + paperId + "'";
			//查询数据库，获得
			ResultSet rs0;
				rs0 = p.query(sql);
			while (rs0.next()) {                                             //这是第一部分的循环
				Part.add(rs0.getString("PartId"));	
				PartName.add(rs0.getString("PartName"));
				Description.add(rs0.getString("Description"));
				PartPoint.add(rs0.getString("PartPoint"));
				CountQuestion.add(rs0.getString("CountQuestion"));
				//部分ID，部分名称，部分描述，部分分数，题目数量
				String Sql = "select * from PaperPart_Question where PartId='" + rs0.getInt("PartId") + "'";
				//部分ID与题目ID结合，用来获取题目的ID
				int questionNum=1;
				ResultSet rs1 = p.query(Sql);
				while (rs1.next()){                                      //这是第一部分的第一道题的循环
					Pquestion.add(rs1.getString("PartId"));
					perPoint.add(rs1.getString("perPoint"));
					String sql1 = "select QuestionId,TypeName,QuestionDetails from Question,QuestionType where QuestionType.QTypeId=Question.QTypeId and  Question.QuestionId='"+rs1.getInt("QuestionId")+"'";
					//从数据库中获取题目ID对应的题目信息
					ResultSet rs2 = p.query(sql1);
					while (rs2.next()){  
						Question.add(rs2.getString("QuestionId")); 
						TypeName.add(rs2.getString("TypeName"));
						questionDetail.add(rs2.getString("QuestionDetails"));
						String sql2 = "select OptionDetails,isCorrect from Question,[Option] where Question.QuestionId=[Option].QuestionId and Question.QuestionId='"+rs1.getInt("QuestionId")+"'";
						//在获取的题目id下面操作
						int optionNum=1;
						ResultSet rs3 = p.query(sql2);
						while (rs3.next()){
							Qoption.add(rs2.getString("QuestionId"));
							option.add(rs3.getString("OptionDetails"));
							isCorrect.add(rs3.getString("isCorrect"));
							optionNum++;
						}//rs3
					}//rs2
					questionNum++;
				}//rs1 
			}//rs0
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for(int i=0;i<Part.size();i++) {
				String chinese=NumberTochinese(i+1);
				setParagraph(docx, "第"+chinese+"部分 "+PartName.get(i), true, 14);// 部分名称
				setParagraph(docx,  Description.get(i)+"(共"+CountQuestion.get(i)+"题，满分"+PartPoint.get(i)+"分)", false, 12);// 部分描述、部分题目总数和总分
				int v=1; 
				for (int j=0;j<Pquestion.size();j++) { 
					String P=Part.get(i);
					String Pq=Pquestion.get(j);	
					if(Integer.parseInt(Pq)==Integer.parseInt(P)){//Pquestion表示是第几部分里的question,此处判断是否为同一部分
						setParagraph(docx, v+". "+TypeName.get(j)+" "+questionDetail.get(j), false, 14);//题目detail
						v++;
						if(TypeName.get(j).indexOf("选题")!=-1){
							int w=1;
						for (int m=0;m<Qoption.size();m++) {	  
							String Q=Question.get(j);
							String Qo=Qoption.get(m); 
							if(Integer.parseInt(Qo)==Integer.parseInt(Q)){//Qoption表示是第几题里的option，此处判断是否为同一题目
								String letter=NumberToletter(w);
								setParagraph(docx, letter+"."+option.get(m), false, 14);//题目option
								w++;
							}else{
								continue; 
							}  
						}//判断题型
						}//for	
					}else{
						continue;
					}
				}//for
			 }//for	
		}else if("Auto".equals(method)){
			String TotalTime = "";
			String TotalPoint = "";
			String ExamDescription = "";
			String QuestionIds;//题目的id
			String mysql = "select * from Paper where PaperId='" + paperId + "'";
			PackingDatabase p = new PackingDatabase();
			try {// 获取内容
				ResultSet rs = p.query(mysql);
				if (rs.next()) {
					PaperName = rs.getString("PaperName");
					ExamDescription = rs.getString("ExamDescription");
					TotalPoint = rs.getString("TotalPoint");
					TotalTime = rs.getString("TotalTime");
				}
				rs.close();
			} catch (Exception e) {
				System.out.println("试卷查询异常" + e.getMessage());
			}
			XWPFParagraph p1 = docx.createParagraph();
			p1.setAlignment(ParagraphAlignment.CENTER);// 对齐方式-居中
			p1.setBorderBottom(Borders.NONE);// 设置边框
			p1.setBorderTop(Borders.NONE);
			p1.setBorderRight(Borders.NONE);
			p1.setBorderLeft(Borders.NONE);
			p1.setBorderBetween(Borders.NONE);
			XWPFRun r1 = p1.createRun();
			r1.setBold(true);// 粗体
			r1.setFontSize(18);// 字号
			r1.setText(PaperName);
			r1.setFontFamily("仿宋");// 设置字体格式
			r1.setTextPosition(12);// 行距
			
			setParagraph(docx, "试卷描述:"+ExamDescription, false, 12);//试卷描述
			setParagraph(docx, "试卷总分:"+TotalPoint, false, 12);//试卷总分
			setParagraph(docx, "试卷限时:"+TotalTime, false, 12);//试卷限时
			
			List<String> Part = new ArrayList<String>();//第几部分
			List<String> Question = new ArrayList<String>();//题目
			List<String> Pquestion = new ArrayList<String>();//哪一部分的题目
			List<String> Qoption = new ArrayList<String>();//哪道题的选项
			List<String> TypeName = new ArrayList<String>(); //类型名称
			List<String> questionDetail = new ArrayList<String>(); //题目细节
			List<String> option = new ArrayList<String>(); //选项
			List<String> isCorrect = new ArrayList<String>();//是否正确
			List<String> PartName = new ArrayList<String>();  
			List<String> Description = new ArrayList<String>(); 
			List<String> PartPoint = new ArrayList<String>(); 
			List<String> CountQuestion = new ArrayList<String>(); 
			List<String> perPoint = new ArrayList<String>(); 
		 
		try {
				
				//创建workbook
				Workbook workbook = Workbook.getWorkbook(new File("c:/test_第二份试卷.xls"));
				//获取第一个工作表sheet
				Sheet sheet = workbook.getSheet(0);
				//获取数据
				//for (int i = 0; i < sheet.getRows(); i++) {//行
					for (int j = 0; j < sheet.getColumns()/6; j++) {//列
						int PartPoint1=0;
						String chinese=NumberTochinese(j+1);
						Cell cell1 = sheet.getCell(j*6,0);
						//PartName=cell1.getContents();
						Cell cell2 = sheet.getCell(j*6+1,0);
						//Description=cell2.getContents();
						Cell cell3 = sheet.getCell(j*6+2,0);
						//perPoint=cell3.getContents();
						Cell cell5 = sheet.getCell(j*6+4,0);
						//QTypeId=cell5.getContents();
						Cell cell6=sheet.getCell(j*6+5,0);
						//CountQuestion=cell5.getContents();
						PartPoint1=Integer.parseInt(cell6.getContents()) * Integer.parseInt(cell3.getContents());
						Part.add(Integer.toString(j));	
						PartName.add(cell1.getContents());
						Description.add(cell2.getContents());
						PartPoint.add(Integer.toString(PartPoint1));
						CountQuestion.add(cell6.getContents());
						
						
					
						Cell cell4 = sheet.getCell(j*6+3,0);
						QuestionIds=cell4.getContents();
						int questionNum=1;
						 String a[] = QuestionIds.split("[+]");
						 for(int k=0;k<a.length-1;k++){
							 Pquestion.add(Integer.toString(j));
							perPoint.add(cell3.getContents());
							 String sql1 = "select QuestionId,TypeName,QuestionDetails from Question,QuestionType where QuestionType.QTypeId=Question.QTypeId and  Question.QuestionId='"+a[k]+"'";
							 ResultSet rs2 = p.query(sql1);
							// int questionNum=1;
							 while (rs2.next()){  
									Question.add(rs2.getString("QuestionId")); 
									TypeName.add(rs2.getString("TypeName"));
									questionDetail.add(rs2.getString("QuestionDetails"));
									String sql2 = "select OptionDetails,isCorrect from Question,[Option] where Question.QuestionId=[Option].QuestionId and Question.QuestionId='"+a[k]+"'";
									int optionNum=1;
									ResultSet rs3 = p.query(sql2);
									while (rs3.next()){
										Qoption.add(rs2.getString("QuestionId"));
										option.add(rs3.getString("OptionDetails"));
										isCorrect.add(rs3.getString("isCorrect"));
										optionNum++;
									}//rs3
									
							/* System.out.println(a[k]); */
							 }//rs2
							 questionNum++; 
						 }
					}
					workbook.close();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
		
		for(int i=0;i<Part.size();i++) {
			String chinese=NumberTochinese(i+1);
			setParagraph(docx, "第"+chinese+"部分 "+PartName.get(i), true, 14);// 部分名称
			setParagraph(docx,  Description.get(i)+"(共"+CountQuestion.get(i)+"题，满分"+PartPoint.get(i)+"分)", false, 12);// 部分描述、部分题目总数和总分
						 int v=1; 
							for (int l=0;l<Pquestion.size();l++) { 
								String P=Part.get(i);
								String Pq=Pquestion.get(l);	
								if(Integer.parseInt(Pq)==Integer.parseInt(P)){//Pquestion表示是第几部分里的question,此处判断是否为同一部分
									setParagraph(docx, v+". "+TypeName.get(l)+" "+questionDetail.get(l), false, 14);//题目detail
									v++;
									if(TypeName.get(l).indexOf("选题")!=-1){
										int w=1;
									for (int m=0;m<Qoption.size();m++) {	  
										String Q=Question.get(l);
										String Qo=Qoption.get(m); 
										if(Integer.parseInt(Qo)==Integer.parseInt(Q)){//Qoption表示是第几题里的option，此处判断是否为同一题目
											String letter=NumberToletter(w);
											setParagraph(docx, letter+"."+option.get(m), false, 14);//题目option
											w++;
										}else{
											continue; 
										}  
									}//判断题型
									}//for	
								}else{
									continue;
								}
							}//for
						 }//for				
		}

		IPTimeStamp its = new IPTimeStamp(request.getRemoteAddr());// 获取用户的ip地址，实例化不重复文件名生成类的对象。
		String outputFilename = its.getIPTimeStampRand() + ".docx";// 获取一个不重复文件名+文件后缀组成文件名。
		String outputPath = getServletContext().getRealPath("/") + "file\\PrintPapers\\" + outputFilename;
		FileOutputStream out = new FileOutputStream(outputPath);
		docx.write(out);
		out.close();

		// 将文件名写入json
		PrintWriter writer = response.getWriter();
		JSONObject jo = new JSONObject();
		jo.put("filename", outputFilename);
		jo.put("paperName", PaperName);
		writer.print(jo.toString());
		writer.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void setParagraph(CustomXWPFDocument docx, String str, boolean isBold, int fontSize) {
		XWPFParagraph p = docx.createParagraph();
		p.setAlignment(ParagraphAlignment.BOTH);// 对齐方式-居中
		p.setBorderBottom(Borders.NONE);// 设置边框
		p.setBorderTop(Borders.NONE);
		p.setBorderRight(Borders.NONE);
		p.setBorderLeft(Borders.NONE);
		p.setBorderBetween(Borders.NONE);
		XWPFRun r = p.createRun();
		r.setBold(isBold);// 粗体
		r.setFontSize(fontSize);// 字号
		r.setText(str);
		r.setFontFamily("仿宋");// 设置字体格式
		r.setTextPosition(12);// 行距
	}
	private String NumberTochinese(int number){
		String chinese = "";
		if(number==1){
			chinese="一";
		}else if(number==2){
			chinese="二";
		}
		else if(number==3){
			chinese="三";
		}
		else if(number==4){
			chinese="四";
		}
		else if(number==5){
			chinese="五";
		}
		else if(number==6){
			chinese="六";
		}
		else if(number==7){
			chinese="七";
		}
		else if(number==8){
			chinese="八";
		}
		else if(number==9){
			chinese="九";
		}
		else if(number==10){
			chinese="十";
		}
		return chinese;
	}
	private String NumberToletter(int num){
		String letter = "";
		if(num==1){
			letter="A";
		}else if(num==2){
			letter="B";
		}else if(num==3){
			letter="C";
		}else if(num==4){
			letter="D";
		}else if(num==5){
			letter="E";
		}else if(num==6){
			letter="F";
		}else if(num==7){
			letter="G";
		}else if(num==8){
			letter="H";
		}else if(num==9){
			letter="I";
		}else if(num==10){
			letter="J";
		}else if(num==11){
			letter="K";
		}else if(num==12){
			letter="L";
		}else if(num==13){
			letter="M";
		}else if(num==14){
			letter="N";
		}else if(num==15){
			letter="O";
		}else if(num==16){
			letter="P";
		}else if(num==17){
			letter="Q";
		}else if(num==18){
			letter="R";
		}else if(num==19){
			letter="S";
		}else if(num==20){
			letter="T";
		}else if(num==21){
			letter="U";
		}else if(num==22){
			letter="V";
		}else if(num==23){
			letter="W";
		}else if(num==24){
			letter="X";
		}else if(num==25){
			letter="Y";
		}else if(num==26){
			letter="Z";
		}
		return letter;
	}
}
