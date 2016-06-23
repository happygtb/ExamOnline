package myServlet;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
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
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("UTF-8");
		String userName = request.getParameter("username");
		String auto = request.getParameter("check");
		String pw = request.getParameter("password");
		String role = request.getParameter("role");
		String p = "";
		String id = "";
		String roleid="";
		PackingDatabase packing = new PackingDatabase();
		String Sql = "select * from [User] where Username='" + userName + "'";
		try {
			ResultSet rs = packing.query(Sql);
			while (rs.next()) {
				p = rs.getString("password");
				id = rs.getString("UserId");
				roleid=rs.getString("RoleId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		if (pw.equals(p)&&role.equals(roleid)) {
			// login successfully
			HttpSession session = request.getSession();
			session.setAttribute("role", role);
			session.setAttribute("UserId", id);
			session.setAttribute("login", "logined");
			session.setAttribute("username", userName);
			if (auto != null && auto=="on") {
				Cookie cookie = new Cookie("UserName", userName);
				//cookie.setMaxAge(7*24*60*60);
				cookie.setMaxAge(5*60 * 60);
				response.addCookie(cookie);
			} else {
				Cookie cookie = new Cookie("username", null);
				cookie.setMaxAge(0);
			}
			response.sendRedirect("index.html");
		} else {
			System.out.print("验证失败");
			response.sendRedirect("Login.html");
		}

	}

}
