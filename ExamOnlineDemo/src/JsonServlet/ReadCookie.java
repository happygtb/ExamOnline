package JsonServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import myServlet.PackingDatabase;

/**
 * Servlet implementation class Cookie
 */
@WebServlet("/ReadCookie")
public class ReadCookie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReadCookie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		JSONObject jo = new JSONObject();
		String password = "";
		String roleid="";
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				if (name.equals("UserName")) {
					String value = cookie.getValue();
					String mySql = "select * from [User] where UserName='"+ value + "'";
					System.out.println("value" + value);
					PackingDatabase select = new PackingDatabase();
					try {
						ResultSet rs = select.query(mySql);
						if (rs.next()) {
							jo.put("auto", "true");
							password = rs.getString("Password");
							roleid=rs.getString("RoleId");
							jo.put("username", cookie.getValue());
							jo.put("password", password);
							jo.put("roleid", roleid);
							out.print(jo.toString());
							out.close();
							break;
						} else {
							jo.put("auto", "");
							jo.put("username", "");
							jo.put("password", "");
							jo.put("roleid", roleid);
							out.print(jo.toString());
							out.close();
						}
						rs.close();
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			}
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
