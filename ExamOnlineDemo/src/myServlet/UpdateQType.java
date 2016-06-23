package myServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateQType
 */
@WebServlet("/UpdateQType")
public class UpdateQType extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateQType() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html,charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		String oldtype = request.getParameter("QTypeId");
		String questiontype = request.getParameter("newQType");
		int num=Integer.parseInt(request.getParameter("num"));
		String qtype[] = new String[num];
		String amount[] = new String[num];
		
		PackingDatabase packing = new PackingDatabase();
		String Sql = "update QuestionType set TypeName='"+questiontype+"' where QTypeId='"+oldtype+"';delete QtypeTemplate where QTypeId='"+oldtype+"'";
		try {
			packing.update(Sql);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	
		for (int i = 0; i < num; i++) {
			qtype[i] = request.getParameter("QType" + (i + 1));
			amount[i] = request.getParameter("amount" + (i + 1));
			PackingDatabase packing2 = new PackingDatabase();
			String mySql2 = "insert into QtypeTemplate (QTypeId,SubQTypeId,amount) values ('" + oldtype + "','"
					+ qtype[i] + "','" + amount[i] + "')";
			try {
				packing2.update(mySql2);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}
		response.sendRedirect("QuestionBank/checkQuestionType.html");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
