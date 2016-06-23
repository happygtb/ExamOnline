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
 * Servlet implementation class ShowPaperJson
 */
@WebServlet("/ShowPaperJson")
public class ShowPaperJson extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowPaperJson() {
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
		String paperId = request.getParameter("paperId");
		String mySql = "select * from Paper where Paper.PaperId='" + paperId + "'";
		PackingDatabase packing = new PackingDatabase();
		try {
			ResultSet rs = packing.query(mySql);
			PrintWriter out = response.getWriter();
			JSONArray ja = new JSONArray();
			int partCount=0;
			while (rs.next()) {
				JSONObject jo = new JSONObject();
				jo.put("PaperId",paperId);
				jo.put("PaperName", rs.getString("PaperName"));
				jo.put("ExamDescription", rs.getString("ExamDescription"));
				jo.put("CountQuestions", rs.getInt("CountQuestions"));
				jo.put("TotalPoint", rs.getInt("TotalPoint"));
				jo.put("TotalTime", rs.getInt("TotalTime"));
				jo.put("Difficulty", rs.getString("Difficulty"));
				ja.put(jo);
			}
			
			
			
			
			String mysql = "select * from PaperPart where PaperId='" + paperId + "'";
			ResultSet rs0 = packing.query(mysql);
			while (rs0.next()) {
				JSONObject part = new JSONObject();
				partCount++;
				part.put("PartId", rs0.getInt("PartId"));
				part.put("Partnum", partCount);
				part.put("PartName", rs0.getString("PartName"));
				part.put("Description", rs0.getString("Description"));
				part.put("CountQuestion", rs0.getInt("CountQuestion")+1);
				part.put("PartPoint", rs0.getInt("PartPoint"));
				String Sql = "select * from PaperPart_Question where PartId='" + rs0.getInt("PartId") + "'";
				int questionNum=1;
				ResultSet rs1 = packing.query(Sql);
				while (rs1.next()){
					part.put("QuestionId", rs1.getInt("QuestionId"));
					part.put("perPoint"+questionNum, rs1.getInt("perPoint"));
					String sql = "select TypeName,QuestionDetails,OptionDetails,isCorrect from Question,[Option],QuestionType where QuestionType.QTypeId=Question.QTypeId and Question.QuestionId=[Option].QuestionId and Question.QuestionId='"+rs1.getInt("QuestionId")+"'";
					int optionNum=1;
					ResultSet rs2 = packing.query(sql);
					while (rs2.next()){
						String quesDetails=rs2.getString("QuestionDetails");        
						part.put("TypeName"+questionNum, rs2.getString("TypeName"));
						part.put("questionDetail"+questionNum, quesDetails);					
						part.put("option"+questionNum+optionNum, rs2.getString("OptionDetails"));
						part.put("isCorrect"+questionNum+optionNum, rs2.getString("isCorrect"));
						optionNum++;
					}
					questionNum++;
				}
				ja.put(part);
			}
				
			rs.close();
			out.print(ja.toString());
			out.close();
		} catch (Exception e) {
			System.out.println("试卷查询异常" + e.getMessage());
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}		
		
					



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
