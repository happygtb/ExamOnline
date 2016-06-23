package myServlet;

//import java.io.FileInputStream;
import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

//import java.io.InputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.*;

/**
 * Servlet implementation class AutoGetpapers
 */
@WebServlet("/AutoGetpapers")
public class AutoGetpapers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AutoGetpapers() {
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

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");

		
		
		int Qnum = 0;

		String PaperVersionId = "";
		int Difficulty = 3;
		// 获取试卷难度
		// int PaperClassId =
		// Integer.parseInt(request.getParameter("PaperClass"));//前台js尚未解决
		int PaperClassId = 1;
		// 获取试卷分类
		String PaperName = request.getParameter("PaperName"); // 获取试卷名称
		String ExamDescription = request.getParameter("ExamDescription");// 获取试卷描述
		String TotalTime = request.getParameter("TotalTime");// 获取考试时间
		String CountQuestions = request.getParameter("CountQuestions");// 获取试卷总题数
		int TotalPoint = Integer.parseInt(request.getParameter("TotalPoint"));// 获取试卷总分
		int subjectid = Integer.parseInt(request.getParameter("subject"));// 获取科目id
		int Tnum = Integer.parseInt(request.getParameter("Tnum"));// 获取试卷总数
		int num = Integer.parseInt(request.getParameter("num"));// 获取部分的数量，用来循环
		
		File file = new File("c:/test_" + PaperName + ".xls");

		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = format.format(date);
		String insertVersion = "insert into PaperVersion (PaperMakerId,AssemblyTime,EditTime) values('1','" + time
				+ "','" + time + "')";
		PackingDatabase p1 = new PackingDatabase();
		try {
			p1.update(insertVersion);
			String sql = "select PaperVersionId from PaperVersion where PaperMakerId='1' and AssemblyTime='" + time
					+ "'";
			ResultSet rs = p1.query(sql);
			if (rs.next()) {
				PaperVersionId = rs.getString("PaperVersionId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("试卷版本信息插入错误" + e.getMessage());
		}

		PackingDatabase packing1 = new PackingDatabase();
		String sql1 = "insert into Paper (PaperName,ExamDescription,CountQuestions,TotalPoint,TotalTime,PaperVersionId,Difficulty,PaperClassId) values ('"
				+ PaperName + "','" + ExamDescription + "','" + CountQuestions + "','" + TotalPoint + "','" + TotalTime
				+ "','" + PaperVersionId + "','" + Difficulty + "','" + PaperClassId + "')";
		try {
			packing1.update(sql1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		PackingDatabase packing2 = new PackingDatabase();
		String sql2 = "select PaperId from Paper where PaperName='"+ PaperName +"' and ExamDescription='" + ExamDescription
				+ "'";
		int	PaperId=0;
		String ExcelPath="excel路径";
		try{
			ResultSet rs = packing2.query(sql2);
			if(rs.next()){
			PaperId=Integer.parseInt(rs.getString("PaperId"));
			}
			rs.close();
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		PackingDatabase packing3 = new PackingDatabase();
		String sql3 ="insert into Autopaper (PaperId,ExcelPath) values('" + PaperId + "','" + ExcelPath + "')";
		try {
			packing3.update(sql3);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		

		try {
			file.createNewFile();
			// 创建工作簿
			WritableWorkbook workbook = Workbook.createWorkbook(file);
			// 创建sheet
			WritableSheet sheet = workbook.createSheet("sheet1", 0);
			Label label = null;

			for (int n = 0; n < Tnum; n++) {
				int truerow = 0;// 实际列，也就是实际的部分数
				for (int k = 1; k < num + 1; k++) {// 执行部分循环
					String PartName = request.getParameter("PartName" + k);// 部分名称
					int f = 0;// 部分循环测试

					if (PartName != null) {
						String QuestionId = "";
						label = new Label(6 * truerow, n, PartName);// 列/行/内容
						sheet.addCell(label);
						String Description = request.getParameter("Description" + k);// 部分描述
						label = new Label(6 * truerow + 1, n, Description);// 列/行/内容
						sheet.addCell(label);
						String PartPoint = request.getParameter("Point" + k);// 部分每题分数
						label = new Label(6 * truerow + 2, n, PartPoint);// 列/行/内容
						sheet.addCell(label);
						String questiontypeid = request.getParameter("QType" + k);// 题目类型id
						label = new Label(6 * truerow + 4, n, questiontypeid);// 列/行/内容
						sheet.addCell(label);

						int Tipsnum = Integer.parseInt(request.getParameter("Tipsnumb" + k));// 获取该部分知识点的数量
						for (int p = 1; p < Tipsnum + 1; p++) {// 执行知识点循环
							int Tipsid = Integer.parseInt(request.getParameter("Tips" + k + p));// 获取知识点类型id
							if (Integer.toString(Tipsid) != null) {

								int simple = Integer.parseInt(request.getParameter("simple" + k + p));// 简单题目的数量
								
								PackingDatabase simp = new PackingDatabase();
								String sqls = "select top " + simple + " * from Question where SubjectId='" + subjectid
										+ "' and TipsId='" + Tipsid + "'and QTypeId='" + questiontypeid
										+ "'and ( Difficulty='1'or Difficulty='2')  ORDER BY NEWID()";
								try {
									ResultSet rs = simp.query(sqls);
									while (rs.next()) {
										QuestionId += rs.getString("QuestionId")+"+";
										f = f + 1;// 部分循环测试
										
									}
									rs.close();
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}

								int ordinary = Integer.parseInt(request.getParameter("ordinary" + k + p));// 普通题目的数量
								PackingDatabase ord = new PackingDatabase();
								String sqlo = "select top " + ordinary + " * from Question where SubjectId='"
										+ subjectid + "' and TipsId='" + Tipsid + "'and QTypeId='" + questiontypeid
										+ "'and Difficulty='3' ORDER BY NEWID()";
								try {
									ResultSet rs = ord.query(sqlo);								
									while (rs.next()) {

										QuestionId += rs.getString("QuestionId")+"+";
										f = f + 1;// 部分循环测试
										
									}
									rs.close();
								} catch (Exception e) {
									System.out.println(e.getMessage());
								}

								int hard = Integer.parseInt(request.getParameter("hard" + k + p));// 困难题目的数量
								PackingDatabase har = new PackingDatabase();
								String sqlh = "select top " + hard + " * from Question where SubjectId='" + subjectid
										+ "' and TipsId='" + Tipsid + "'and QTypeId='" + questiontypeid
										+ "'and ( Difficulty='4'or Difficulty='5') ORDER BY NEWID()";
								
								try {
									ResultSet rs = har.query(sqlh);									
									while (rs.next()) {
										QuestionId += rs.getString("QuestionId")+"+";
										f = f + 1;// 部分循环测试
										Qnum++;

									}
									rs.close();

								} catch (Exception e) {
									System.out.println(e.getMessage());
								}

								

							} else {
								continue;
							}
						}
						label = new Label(6 * truerow + 3, n, QuestionId);// 列/行/内容
						sheet.addCell(label);
						label = new Label(6 * truerow + 5, n, Integer.toString(f));// 列/行/内容
						sheet.addCell(label);
						truerow++;

					} else {
						continue;
					}
System.out.println(f);


				}

			}
			workbook.write();
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
