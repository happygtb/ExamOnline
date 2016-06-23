package myServlet;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class Batdelete
 */
@WebServlet("/Batdelete")
public class Batdelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Batdelete() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter();
		response.setContentType("text/html,charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String value="";
		String[] chkid = request.getParameterValues("question");
		for(int i=0;i<chkid.length;i++){
			value=chkid[i];
			PackingDatabase pack = new PackingDatabase();
			String mySql1 = "select RoteId from Question where QuestionId='"+value+"'";
			try {
				pack.update(mySql1);
				ResultSet rs = pack.query(mySql1);
				if (rs.next()) {
					rs.getString("RoteId");
				}
				rs.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			PackingDatabase pack1 = new PackingDatabase();
			String mySql2 = "select VersionId from Question where QuestionId='"+value+"'";
			try {
				pack1.update(mySql2);
				ResultSet rs = pack1.query(mySql2);
				if (rs.next()) {
					rs.getString("VersionId");
				}
				rs.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			PackingDatabase packing = new PackingDatabase();
			String mySql = "delete [Option] where QuestionId='"+value+"';delete Question where QuestionId='"+value+"';delete QuestionVersion where VersionId='"+value+"';delete RoteOfQuestion where RoteId='"+value+"'";
			
			try {
			
				packing.update(mySql);
			
			}catch (Exception e) {
				System.out.println(e.getMessage());
			
		
		
			}
			}
		
			response.sendRedirect("QuestionBank/batDelete.html");
			}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
