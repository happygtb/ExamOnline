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
 * Servlet implementation class OptionJson
 */
@WebServlet("/OptionJson")
public class OptionJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OptionJson() {
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
		String id =request.getParameter("QuestionId");
		int QuestionId=Integer.parseInt(id); 
		String Sql = "select * from [Option] where QuestionId='"+QuestionId+"'";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(Sql);
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray(); 
			while (rs.next()) {				
				JSONObject jo = new JSONObject();
				jo.put("OptionId", rs.getString("OptionId"));
				jo.put("OptionDetails", rs.getString("OptionDetails"));
				jo.put("IsCorrect", rs.getString("IsCorrect"));
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
