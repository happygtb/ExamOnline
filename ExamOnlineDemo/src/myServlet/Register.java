package myServlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");  
		request.setCharacterEncoding("UTF-8");
		String UserName = request.getParameter("username");
		String pw = request.getParameter("password");
		String Email = request.getParameter("Email");
		String Tel = request.getParameter("Tel");
		PackingDatabase packing1 = new PackingDatabase();
		String mySql1 = "insert into [User] (UserName,Password,RoleId,Tel,Email) values ('" + UserName + "','" + pw + "','2','" + Tel + "','" + Email + "')";
		try {
			packing1.update(mySql1);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		response.sendRedirect("Login.html");
	}

}
