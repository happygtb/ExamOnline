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
 * Servlet implementation class SingleQTypeJson
 */
@WebServlet("/SingleQTypeJson")
public class SingleQTypeJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleQTypeJson() {
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
		response.setHeader("Cache-Control", "no-cache");
		String QTypeId=request.getParameter("QTypeId");
		String mySql = "select * from QtypeTemplate,QuestionType where QtypeTemplate.QTypeId=QuestionType.QTypeId and QtypeTemplate.QTypeId='"+QTypeId+"'";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("TypeName", rs.getString("TypeName"));
				jo.put("SubQTypeId", rs.getString("SubQTypeId"));
				jo.put("amount", rs.getString("amount"));
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
