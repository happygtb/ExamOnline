package myServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class deleteQuestion
 */
@WebServlet("/deleteQuestion")
public class deleteQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteQuestion() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		String ids = request.getParameter("ids");
		//ids = ids.substring(0, ids.length() - 1);
		//String[] idArray = ids.split(";");
		
//for (int i = 0; i < idArray.length; i++) {
			
			String mySql = "delete Question where QuestionId='"+ids+"'";
			
			
			PackingDatabase packing = new PackingDatabase();
			
			try {
			
				packing.update(mySql);
				System.out.println(mySql);
			
			}
		   
			catch (Exception e) {
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
