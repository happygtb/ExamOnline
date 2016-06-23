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
 * Servlet implementation class MajorJson
 */
@WebServlet("/MajorJson")
public class MajorJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MajorJson() {
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
		
		String SchoolId=request.getParameter("SchoolId");
		
		String mySql = "select * from Major where SchoolId="+SchoolId;
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("MajorId", rs.getString("MajorId"));
				jo.put("MajorName", rs.getString("MajorName"));
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
