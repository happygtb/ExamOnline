package myServlet;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class addSubject
 */
@WebServlet("/AddQType")
public class AddQType extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddQType() {
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
		request.setCharacterEncoding("utf-8");
		String name = request.getParameter("QTypeName");
		int num = Integer.parseInt(request.getParameter("num"));

		String qtype[] = new String[num];
		String amount[] = new String[num];

		String typeId = "";
		PackingDatabase packing1 = new PackingDatabase();
		String mySql1 = "insert into QuestionType (TypeName) values ('" + name + "')";
		try {
			packing1.update(mySql1);
			String sql = "select QTypeId from QuestionType where TypeName='" + name + "'";
			ResultSet rs = packing1.query(sql);
			if (rs.next()) {
				typeId = rs.getString("QTypeId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		for (int i = 0; i < num; i++) {
			qtype[i] = request.getParameter("QType" + (i + 1));
			amount[i] = request.getParameter("amount" + (i + 1));

			PackingDatabase packing2 = new PackingDatabase();
			String mySql2 = "insert into QtypeTemplate (QTypeId,SubQTypeId,amount) values ('" + typeId + "','"
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
