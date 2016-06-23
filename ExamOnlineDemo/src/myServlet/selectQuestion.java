package myServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class selectQuestion
 */
@WebServlet("/selectQuestion")
public class selectQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public selectQuestion() {
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
		
		String mySql = "select * from Question where QuestionId='"+QuestionId+"'";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			PrintWriter out = response.getWriter();
			JSONObject jo = new JSONObject();
			if (rs.next()) {
				jo.put("QuestionId", rs.getString("QuestionId"));
				jo.put("QTypeId", rs.getString("QTypeId"));
				jo.put("SubjectId", rs.getString("SubjectId"));
				jo.put("TipsId", rs.getString("TipsId"));
				jo.put("Difficulty", rs.getString("Difficulty"));
				jo.put("QuestionDetails", rs.getString("QuestionDetails"));
				jo.put("Labels", rs.getString("Labels"));
			}
			rs.close();
			out.print(jo.toString());
			out.close();
		} catch (Exception e) {
			System.out.println("akjshk"+e.getMessage());
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
