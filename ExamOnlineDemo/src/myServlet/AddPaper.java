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
 * Servlet implementation class AddPaper
 */
@WebServlet("/AddPaper")
public class AddPaper extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddPaper() {
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
		// TODO Auto-generated method stub
				request.setCharacterEncoding("utf-8");
				String PaperName=request.getParameter("PaperName");
				String ExamDescription=request.getParameter("ExamDescription");
				String TotalTime=request.getParameter("TotalTime");
				String TotalPoint=request.getParameter("TotalPoint");
				String CountQuestions=request.getParameter("CountQuestions");
				String difficulty=request.getParameter("Difficulty");
				int Difficulty=0;	
				String PartId="";
				String PaperId="";
				String PaperVersionId="";
				

				//试卷版本信息
				Date date=new Date();
				DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String time=format.format(date);
				String insertVersion = "insert into PaperVersion (PaperMakerId,AssemblyTime,EditTime) values('1','"+time+"','"+time+"')";
				PackingDatabase p1 = new PackingDatabase();
				try {
					p1.update(insertVersion);
					String sql="select PaperVersionId from PaperVersion where PaperMakerId='1' and AssemblyTime='"+time+"'";
					ResultSet rs = p1.query(sql);
					if (rs.next()) {
						PaperVersionId=rs.getString("PaperVersionId");
					}
					rs.close();
				} catch (Exception e) {
					System.out.println("试卷版本信息插入错误" + e.getMessage());
				}
				//试卷信息
				String Sql = "insert into Paper (PaperName,ExamDescription,CountQuestions,TotalPoint,TotalTime,PaperVersionId,Difficulty) values('"+PaperName+"','"+ExamDescription+"','"+CountQuestions+"','"+TotalPoint+"','"+TotalTime+"','"+PaperVersionId+"','"+Difficulty+"')";
				PackingDatabase p2 = new PackingDatabase();
				try {
					p2.update(Sql);
					String sql="select PaperId from Paper where PaperName='"+PaperName+"' and PaperVersionId='"+PaperVersionId+"'";
					ResultSet rs = p2.query(sql);
					if (rs.next()) {
						PaperId=rs.getString("PaperId");
					}
					rs.close();
				} catch (Exception e) {
					System.out.println("试卷信息插入错误" + e.getMessage());
				}
				
				String num=request.getParameter("num");		
				int n=Integer.parseInt(num);
				String PartName[]=new String[n]; 
				String Description[]=new String[n]; 
				String PartPoint[]=new String[n]; 
				String CountQuestion[]=new String[n];
				for(int i=0;i<n;i++){
					PartName[i]=request.getParameter("PartName"+(i+1));			 
					Description[i]=request.getParameter("Description"+(i+1));
					PartPoint[i]=request.getParameter("PartPoint"+(i+1));
					CountQuestion[i]=request.getParameter("PartCount"+(i+1));
					//试卷部分信息
					String Sql1 = "insert into PaperPart (PartName,CountQuestion,Description,PartPoint,PaperId) values('"+PartName[i]+"','"+CountQuestion[i]+"','"+Description[i]+"','"+PartPoint[i]+"','"+PaperId+"')";
					PackingDatabase p3 = new PackingDatabase();
					try {
						p3.update(Sql1);
						String sql="select PartId from PaperPart where PartName='"+PartName[i]+"' and PartPoint='"+PartPoint[i]+"' and PaperId='"+PaperId+"'";
						ResultSet rs = p3.query(sql);
						if (rs.next()) {
							PartId=rs.getString("PartId");
						}
						rs.close();
					} catch (Exception e) {
						System.out.println("试卷部分信息插入错误" + e.getMessage());
					}
					String count=request.getParameter("PartCount"+(i+1));		
					int c=Integer.parseInt(count);
					String QuestionId[]=new String[c]; 
					String perPoint[]=new String[c]; 
					for(int j=0;j<c;j++){
						QuestionId[j]=request.getParameter("QuestionId"+(i+1)+j);
						System.out.println(QuestionId[j]); 
						perPoint[j]=request.getParameter("perPoint"+(i+1)+j);
						System.out.println(perPoint[j]); 
					
						//部分题目信息
						String Sql2 = "insert into PaperPart_Question(PartId,QuestionId,perPoint) values('"+PartId+"','"+QuestionId[j]+"','"+perPoint[j]+"')";
						PackingDatabase p4 = new PackingDatabase();
						try {
							p4.update(Sql2);		
						} catch (Exception e) {
							System.out.println("部分题目信息插入错误" + e.getMessage());
						}
					}//第二个for循环
				}//for循环
				/*response.sendRedirect("QuestionBank/ShowPapers.html");*/
				
				/*response.sendRedirect("QuestionBank/SingleQuestion.html?PaperId=");*/
				
			}

		}
