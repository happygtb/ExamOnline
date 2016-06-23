package JsonServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import myServlet.PackingDatabase;

import java.io.File;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * Servlet implementation class ShowPaperJson
 */
@WebServlet("/showAutopaper")
public class showAutopaper extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public showAutopaper() {
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
		// request.getParameter("paperId");
		String paperId = "5034";
		String mySql = "select * from Paper where PaperId='" + paperId + "'";
		PackingDatabase packing = new PackingDatabase();

		String ExcelPath = "";
		String PartName;
		String Description;
		String perPoint;
		String QuestionIds;
		String QTypeId;
		int CountQuestion;
		int PartPoint;
		JSONArray ja = new JSONArray();
		try {
			ResultSet rs = packing.query(mySql);
			PrintWriter out = response.getWriter();
			int partCount = 0;
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("PaperId", paperId);
				jo.put("PaperName", rs.getString("PaperName"));
				jo.put("ExamDescription", rs.getString("ExamDescription"));
				jo.put("CountQuestions", rs.getInt("CountQuestions"));
				jo.put("TotalPoint", rs.getInt("TotalPoint"));
				jo.put("TotalTime", rs.getInt("TotalTime"));
				jo.put("Difficulty", rs.getString("Difficulty"));
				ja.put(jo);
				/*System.out.println(ja.toString());*/
			}
			

			String mysql = "select * from Autopaper where PaperId='" + paperId + "'";
			PackingDatabase packing1 = new PackingDatabase();
			try {
				ResultSet rs0 = packing1.query(mysql);
				while (rs0.next()) {
					ExcelPath = rs0.getString("ExcelPath");
				}
			} catch (Exception e) {
				System.out.println("试卷查询异常" + e.getMessage());
			}
			try {

				// 创建workbook
				Workbook workbook = Workbook.getWorkbook(new File("c:/test_第二份试卷.xls"));
				// 获取第一个工作表sheet
				Sheet sheet = workbook.getSheet(0);
				// 获取数据
				// for (int i = 0; i < sheet.getRows(); i++) {//行
				for (int j = 0; j < sheet.getColumns() / 5; j++) {// 列
					JSONObject part = new JSONObject();
					partCount++;
					Cell cell1 = sheet.getCell(j * 6, 0);
					PartName = cell1.getContents();
					Cell cell2 = sheet.getCell(j * 6 + 1, 0);
					Description = cell2.getContents();
					Cell cell3 = sheet.getCell(j * 6 + 2, 0);
					perPoint = cell3.getContents();
					Cell cell4 = sheet.getCell(j * 6 + 3, 0);
					QuestionIds = cell4.getContents();
					Cell cell5 = sheet.getCell(j * 6 + 4, 0);
					String a[] = QuestionIds.split("[+]");
					QTypeId = cell5.getContents();
					// Cell cell6=sheet.getCell(j*6+5,0);
					CountQuestion = a.length;
					/*System.out.println("CountQuestion=" + CountQuestion);*/
					PartPoint = Integer.parseInt(perPoint) * CountQuestion;
					/*System.out.println("PartPoint=" + PartPoint);*/

					part.put("PartId", j);
					part.put("Partnum", partCount);
					part.put("PartName", PartName);
					part.put("Description", Description);
					part.put("CountQuestion", CountQuestion + 1);
					part.put("PartPoint", PartPoint);

					int questionNum = 1;
					for (int k = 0; k < a.length; k++) {
						part.put("QuestionId", Integer.parseInt(a[k]));
						part.put("perPoint" + questionNum, perPoint);
						String sql = "select TypeName,QuestionDetails,OptionDetails,isCorrect from Question,[Option],QuestionType where QuestionType.QTypeId=Question.QTypeId and Question.QuestionId=[Option].QuestionId and Question.QuestionId='"
								+ Integer.parseInt(a[k]) + "'";
						int optionNum = 1;
						ResultSet rs2 = packing.query(sql);
						while (rs2.next()) {
							String quesDetails = rs2.getString("QuestionDetails");
							part.put("TypeName" + questionNum, rs2.getString("TypeName"));
							part.put("questionDetail" + questionNum, quesDetails);
							part.put("option" + questionNum + optionNum, rs2.getString("OptionDetails"));
							part.put("isCorrect" + questionNum + optionNum, rs2.getString("isCorrect"));
							optionNum++;
						} // rs2
						questionNum++;
						/* System.out.println(a[k]); */
					}
					ja.put(part);
				}
				rs.close();
				out.print(ja.toString());
				out.close();
				workbook.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			System.out.println("试卷查询异常" + e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
