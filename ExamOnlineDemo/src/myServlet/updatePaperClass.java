package myServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet implementation class updatePaperClass
 */
@WebServlet("/updatePaperClass")
public class updatePaperClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updatePaperClass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String name = request.getParameter("newPaperClass");
		String id = request.getParameter("id");
		if(id.length()>0){
			String mySql = "update PaperClass set PaperClassName='"+name+"' where PaperClassId="+id;
			PackingDatabase packing = new PackingDatabase();
			try {
				packing.update(mySql);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}else{
			PackingDatabase packing1 = new PackingDatabase();
			String mySql1 = "insert into PaperClass (PaperClassName,UserId) values ('" + name + "','2')";
			try {
				packing1.update(mySql1);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		response.sendRedirect("ExamPapers/PaperClass.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
