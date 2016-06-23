package myServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UpdateTips
 */
@WebServlet("/UpdateTips")
public class UpdateTips extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateTips() {
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

		String oldtips = request.getParameter("TipsId");
		String newtips = request.getParameter("TipsName");
		PackingDatabase packing = new PackingDatabase();
		String Sql = "update Tips set TipsName='" + newtips + "' where TipsId='" + oldtips + "'";
		try {
			packing.update(Sql);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		response.sendRedirect("QuestionBank/checkTips.html");

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
