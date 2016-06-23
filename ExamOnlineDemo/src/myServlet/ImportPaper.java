package myServlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImportPaper
 */
@WebServlet("/ImportPaper")
public class ImportPaper extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImportPaper() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		String filename=request.getParameter("filename");
		/*String [] strs = filename.split("[.]");
		String filetype=strs[1];*/
		String fileRealPathandName= getServletContext().getRealPath("/")+"file\\TempPaper\\"+filename;
		System.out.println(fileRealPathandName);
		String paperClass=(String) request.getSession().getAttribute("paperClass");
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
		String time=format.format(date);
		String TempPaperId="";
		String insertTempPaper = "insert into TempPaper (PaperClassId,CreateTime,PaperPath) values('"+paperClass+"','"+time+"','"+filename+"')";
		PackingDatabase p = new PackingDatabase();
		try {
			p.update(insertTempPaper);
			String selectTempPaperId="select TempPaperId from TempPaper where CreateTime='"+time+"' and PaperPath='"+filename+"'";
			ResultSet rs = p.query(selectTempPaperId);
			if (rs.next()) {
				TempPaperId=rs.getString("TempPaperId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("试卷导入异常" + e.getMessage());
		}
		response.sendRedirect("ExamPapers/TempPaperPreview.html?paperId="+TempPaperId);
	}

}
