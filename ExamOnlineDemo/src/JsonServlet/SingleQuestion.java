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
 * Servlet implementation class SingleQuestion
 */
@WebServlet("/SingleQuestion")
public class SingleQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleQuestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");		
		
		String QuestionId=request.getParameter("QuestionId");
		String mySql = "select TypeName,TipsName,LastEditTime,QuestionDetails,OptionDetails,isCorrect,Labels from Question,[Option],Tips,QuestionVersion,QuestionType where Tips.TipsId=Question.TipsId and QuestionType.QTypeId=Question.QTypeId and Question.QuestionId=[Option].QuestionId and QuestionVersion.VersionId=Question.VersionId and Question.QuestionId='"+QuestionId+"'";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("QType",rs.getString("TypeName"));        
				jo.put("Tips",rs.getString("TipsName"));
				jo.put("LastEditTime", rs.getString("LastEditTime").substring(0, 16));
				jo.put("QuestionDetails", rs.getString("QuestionDetails"));
				jo.put("Option", rs.getString("OptionDetails"));
				jo.put("isCorrect", rs.getString("isCorrect"));
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
