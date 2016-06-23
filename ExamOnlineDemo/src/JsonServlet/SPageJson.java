package JsonServlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import myServlet.PackingDatabase;

/**
 * Servlet implementation class SPageJson
 */
@WebServlet("/SPageJson")
public class SPageJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SPageJson() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");	
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String Qtype=request.getParameter("Qtype");	
		String searchcontent=request.getParameter("searchcontent");
		int rowCount=0;//��ֵ�����ݿ��в�ѯ
		int pageCount=0;//��ֵ��ͨ��pageSize��rowCount
		int pageSize=2; 
		
		 
		try {
			PackingDatabase p0 = new PackingDatabase();
			String sql0="";
			if(Qtype==""){
				sql0="select count(*) from Question where QuestionDetails like '%"+searchcontent+"%'or Labels like '%"+searchcontent+"%'";
			}else{
				sql0="select count(*) from Question where QTypeId="+Qtype+" and QuestionDetails like '%"+searchcontent+"%'or Labels like '%"+searchcontent+"%'";
			}
			ResultSet rs0;
			rs0 = p0.query(sql0);
			if(rs0.next()){
				rowCount=rs0.getInt(1);
			}						
			//����pageCount
			if(rowCount%pageSize==0){
			pageCount=rowCount/pageSize;
			}else{
			pageCount=rowCount/pageSize+1;
			}
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray();
			JSONObject jo = new JSONObject();
			jo.put("pageCount", pageCount);
			ja.put(jo);
			out.print(ja.toString());
			out.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
