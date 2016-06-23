package myServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateSubject
 */
@WebServlet("/UpdateSubject")
public class UpdateSubject extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateSubject() {
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

		response.setContentType("text/html,charset=utf-8");
		request.setCharacterEncoding("utf-8");

		String oldsubject = request.getParameter("SubjectId");
		String newsubject = request.getParameter("newsubject");
		PackingDatabase packing = new PackingDatabase();
		String Sql = "update Subject set SubjectName='" + newsubject + "' where SubjectId='" + oldsubject + "'";
		try {
			packing.update(Sql);
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
