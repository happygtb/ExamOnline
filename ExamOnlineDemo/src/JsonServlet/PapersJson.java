package JsonServlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.json.JSONArray;
import org.json.JSONObject;

import myServlet.PackingDatabase;

/**
 * Servlet implementation class PapersJson
 */
@WebServlet("/PapersJson")
public class PapersJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PapersJson() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		
		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");
		
		String PaperClassId=request.getParameter("PaperClassId");
		String method=request.getParameter("method");
		System.out.println(PaperClassId.length());
		if(PaperClassId.length()==0){
			if("normal".equals(method)){
				String mySql = "select * from Paper,PaperClass where Paper.PaperClassId=PaperClass.PaperClassId and PaperClass.UserId='2'";
				paperQuery(request,response,mySql);
			}else{
				String mySql = "select * from TempPaper,PaperClass where TempPaper.PaperClassId=PaperClass.PaperClassId and PaperClass.UserId='2'";
				paperImportedQuery(request,response,mySql);
			}
		}else{
			if("normal".equals(method)){
				String mySql = "select * from Paper,PaperClass where Paper.PaperClassId=PaperClass.PaperClassId and Paper.PaperClassId='"+PaperClassId+"' and PaperClass.UserId='2'";
				paperQuery(request,response,mySql);
			}else{
				String mySql = "select * from TempPaper,PaperClass where TempPaper.PaperClassId=PaperClass.PaperClassId and PaperClass.PaperClassId='"+PaperClassId+"' and PaperClass.UserId='2'";
				paperImportedQuery(request,response,mySql);
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
	
	private void paperQuery(HttpServletRequest request, HttpServletResponse response,String SQL){
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(SQL);
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("PaperId", rs.getString("PaperId"));
				jo.put("PaperName", rs.getString("PaperName"));
				jo.put("PaperClassName", rs.getString("PaperClassName"));
				ja.put(jo);
			}
			rs.close();
			out.print(ja.toString());
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private void paperImportedQuery(HttpServletRequest request, HttpServletResponse response,String SQL){
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(SQL);
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray();
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("PaperId", rs.getString("TempPaperId"));
				//从文件读取试卷名称
				String filename=rs.getString("PaperPath");
				String fileRealPathandName = getServletContext().getRealPath("/") + "file\\TempPaper\\" + filename;
				InputStream is = new FileInputStream(fileRealPathandName);
				WordExtractor extractor = new WordExtractor(is);
				String paraTexts[] = extractor.getParagraphText();
				String PaperName=paraTexts[1].split("【注意：")[0].split("试卷名称：")[1];
				//写入json
				jo.put("PaperName", PaperName);
				jo.put("PaperClassName", rs.getString("PaperClassName"));
				ja.put(jo);
			}
			rs.close();
			out.print(ja.toString());
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
