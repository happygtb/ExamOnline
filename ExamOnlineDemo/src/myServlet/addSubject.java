package myServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class addSubject
 */
@WebServlet("/addSubject")
public class addSubject extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public addSubject() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType("text/html;charset=utf-8");
		/* response.setCharacterEncoding("utf-8"); */
		request.setCharacterEncoding("utf-8");
		String name = request.getParameter("subjectname");
		
		PackingDatabase packing1 = new PackingDatabase();
		String mySql1 = "insert into Subject (SubjectName) values ('" + name + "')";
		try {
			packing1.update(mySql1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		response.sendRedirect("QuestionBank/checkSubject.html");

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
