package myServlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class deletePaperClass
 */
@WebServlet("/deletePaperClass")
public class deletePaperClass extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deletePaperClass() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		String pcId = request.getParameter("pcId").substring(1);
		int count=Integer.parseInt(request.getParameter("count"));
		String[] paperClassId = new String[count];
		
		//将该分类下的试卷移动到试卷根目录
		PackingDatabase packing = new PackingDatabase();
		for(int i=0;i<count;i++){
			paperClassId[i]=pcId.split(",")[i];
			String mySql = "update Paper set PaperClassId=null where PaperClassId="+paperClassId[i]+";";
			mySql+="update TempPaper set PaperClassId=null where PaperClassId="+paperClassId[i];
			try {
				packing.update(mySql);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		//删除试卷分类
		for(int i=0;i<count;i++){
			String mySql = "delete PaperClass where PaperClassId="+paperClassId[i];
			try {
				packing.update(mySql);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		response.sendRedirect("ExamPapers/PaperClass.html");
	}

}
