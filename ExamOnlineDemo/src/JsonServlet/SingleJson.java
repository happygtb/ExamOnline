package JsonServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import myServlet.PackingDatabase;

/**
 * Servlet implementation class SingleQtypeJson
 */
@WebServlet("/SingleJson")
public class SingleJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleJson() {
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
		
		String value=request.getParameter("value");
		String table=request.getParameter("table");
		String keyword=request.getParameter("keyword");

		String mySql = "select "+keyword+" from "+table+" where "+keyword+"='"+value+"'";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			PrintWriter out = response.getWriter();
			JSONObject jo = new JSONObject();
			if (rs.next()) {
				jo.put("exist","1");
			}else{
				jo.put("exist","0");
			}
			rs.close();
			out.print(jo.toString());
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
