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

/**
 * Servlet implementation class QuestionJson
 */
@WebServlet("/QuestionJson")
public class QuestionJson extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public QuestionJson() {
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
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String Qtype = request.getParameter("Qtype");
		String s_pageNow = request.getParameter("pageNow");
		int pageNow = Integer.parseInt(s_pageNow);
		int pageSize = 5;
		int rowCount = 0;// ��ֵ�����ݿ��в�ѯ
		int pageCount = 0;// ��ֵ��ͨ��pageSize��rowCount
		PackingDatabase p0 = new PackingDatabase();
		String sql0 = "";
		String mySql = "";
		try {
			if (Qtype == "") {
				sql0 = "select count(*) from Question";
				mySql = "select top " + pageSize + " * from Question where QuestionId not in(select top "
						+ pageSize * (pageNow - 1) + " QuestionId from Question) ";
			} else {
				sql0 = "select count(*) from Question where QTypeId=" + Qtype;
				mySql = "select top " + pageSize + " * from Question where QTypeId=" + Qtype
						+ " and QuestionId not in(select top " + pageSize * (pageNow - 1)
						+ " QuestionId from Question where QTypeId=" + Qtype + ") ";
			}
			ResultSet rs0;
			rs0 = p0.query(sql0);
			if (rs0.next()) {
				rowCount = rs0.getInt(1);
			}
			// ����pageCount
			if (rowCount % pageSize == 0) {
				pageCount = rowCount / pageSize;
			} else {
				pageCount = rowCount / pageSize + 1;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ��ѯ����Ҫ��ʾ�ļ�¼

		PackingDatabase packing = new PackingDatabase();

		try {
			ResultSet rs = packing.query(mySql);
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("QuestionID", rs.getInt("QuestionID"));
				int SubjectId = rs.getInt("SubjectId");
				String Sql1 = "select SubjectName from Subject where SubjectId='" + SubjectId + "'";
				PackingDatabase p1 = new PackingDatabase();
				ResultSet rs1 = p1.query(Sql1);
				while (rs1.next()) {
					jo.put("SubjectName", rs1.getString("SubjectName"));
				}
				rs1.close();
				int QTypeId = rs.getInt("QTypeId");
				String Sql2 = "select TypeName from QuestionType where QTypeId='" + QTypeId + "'";
				PackingDatabase p2 = new PackingDatabase();
				ResultSet rs2 = p2.query(Sql2);
				while (rs2.next()) {
					jo.put("TypeName", rs2.getString("TypeName"));
				}
				rs2.close();
				int TipsId = rs.getInt("TipsId");
				String Sql3 = "select TipsName from Tips where TipsId='" + TipsId + "'";
				PackingDatabase p3 = new PackingDatabase();
				ResultSet rs3 = p3.query(Sql3);
				while (rs3.next()) {
					jo.put("TipsName", rs3.getString("TipsName"));
				}
				rs3.close();
				jo.put("Difficulty", rs.getInt("Difficulty"));
				jo.put("QuestionDetails", rs.getString("QuestionDetails"));
				jo.put("Labels", rs.getString("Labels"));
				
				ja.put(jo);
			}

			rs.close();
			out.print(ja.toString());
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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

}
