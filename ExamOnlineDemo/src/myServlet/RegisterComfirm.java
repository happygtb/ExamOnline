package myServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import myServlet.PackingDatabase;

/**
 * Servlet implementation class Testing
 */
@WebServlet("/RegisterComfirm")
public class RegisterComfirm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterComfirm() {
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
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		String UserName = request.getParameter("username");
		boolean b = false;
		String p = "";
		PackingDatabase packing = new PackingDatabase();
		String Sql = "select * from [User] where UserName='" + UserName + "'";
		try {
			ResultSet rs = packing.query(Sql);
			while (rs.next()) {
				p = rs.getString("UserName");
				if (UserName.equals(p)) {
					b = true;
				}
			}
			if (b) {
				out.write("该账号已被注册！");
			} else {
				out.write("OK!");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
