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
 * Servlet implementation class AddQuestion
 */
@WebServlet("/AddQuestion")
public class AddQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddQuestion() {
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
		request.setCharacterEncoding("utf-8");
		String QType=request.getParameter("Qtype");
		String OptionNum=request.getParameter("num");
		String subject=request.getParameter("subject");
		String tips=request.getParameter("Tips");
		String difficulty=request.getParameter("Difficulty");
		String RoteName=request.getParameter("RoteName");
		String RoteTel=request.getParameter("RoteTel");
		String RoteEmail=request.getParameter("RoteEmail");
		String College=request.getParameter("College");
		String School=request.getParameter("School");
		String Major=request.getParameter("Major");
		String QuestionStem=request.getParameter("QuestionStem");
		String RoteId="";
		String questionLabel=request.getParameter("questionLabel");
		int QtypeId=Integer.parseInt(request.getParameter("qtid"));
		String QuestionId="";
		String VersionId="";
		String correct="";
		//д���������Ϣ
		String mySql = "insert into RoteOfQuestion (RoteName,RoteTel,RoteEmail,CollegeId,SchoolId,MajorId) values('"+RoteName+"','"+RoteTel+"','"+RoteEmail+"','"+College+"','"+School+"','"+Major+"')";
		PackingDatabase p1 = new PackingDatabase();
		try {
			p1.update(mySql);
			String sql="select RoteId from RoteOfQuestion where RoteName='"+RoteName+"' and RoteTel='"+RoteTel+"' and MajorId='"+Major+"'";
			ResultSet rs = p1.query(sql);
			if (rs.next()) {
				RoteId=rs.getString("RoteId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("��������Ϣ�����쳣" + e.getMessage());
		}
		//д��汾��Ϣ
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time=format.format(date);
		String insertVersion = "insert into QuestionVersion (EditTime,LastEditTime,LastEditor) values('0','"+time+"','"+RoteName+"')";
		PackingDatabase p7 = new PackingDatabase();
		try {
			p7.update(insertVersion);
			String sql="select VersionId from QuestionVersion where EditTime='0' and LastEditTime='"+time+"' and LastEditor='"+RoteName+"'";
			ResultSet rs = p7.query(sql);
			if (rs.next()) {
				VersionId=rs.getString("VersionId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("�汾��Ϣ�����쳣" + e.getMessage());
		}
		//������Ŀ
		String Sql = "insert into Question (QTypeId,QuestionDetails,SubjectId,TipsId,Difficulty,VersionId,Labels,IsBound,RoteId) values('"+QtypeId+"','"+QuestionStem+"','"+subject+"','"+tips+"','"+difficulty+"','"+VersionId+"','"+questionLabel+"','False','"+RoteId+"')";
		PackingDatabase p2 = new PackingDatabase();
		try {
			p2.update(Sql);
			String sql="select QuestionId from Question where QTypeId='"+QtypeId+"' and RoteId='"+RoteId+"' and TipsId='"+tips+"'";
			ResultSet rs = p2.query(sql);
			if (rs.next()) {
				QuestionId=rs.getString("QuestionId");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("��Ŀ�����쳣" + e.getMessage());
		}
		//��ѡ
		if("SingleChoice".equals(QType)){
			//��ȡ��ѡ��ѡ����Ϣ
			String SCoptions=request.getParameter("SCoptions");
			String options[]=new String[Integer.parseInt(OptionNum)];
			for(int i=0;i<Integer.parseInt(OptionNum);i++){
				options[i]=request.getParameter("SCTipsDetail"+(i+1));
				if(i+1==Integer.parseInt(SCoptions)){
					correct="True";
				}
				else{
					correct="False";
				}
				if(options[i]!=null){
					String insertSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+options[i]+"','"+correct+"')";
					PackingDatabase p3 = new PackingDatabase();
					try {
						p3.update(insertSCOption);
					} catch (Exception e) {
						System.out.println("��ѡ��ѡ������쳣" + e.getMessage());
					}
				}
			}
		}
		//��ѡ
		if("MultipleChoice".equals(QType)){
			String options[]=new String[Integer.parseInt(OptionNum)];
			for(int i=0;i<Integer.parseInt(OptionNum);i++){
				options[i]=request.getParameter("MCTipsDetail"+(i+1));
				if(request.getParameter("MCoptions"+(i+1))!=null){
					correct="True";
				}else{
					correct="False";
				}
				if(options[i]!=null){
					String insertMCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+options[i]+"','"+correct+"')";
					PackingDatabase p4 = new PackingDatabase();
					try {
						p4.update(insertMCOption);
					} catch (Exception e) {
						System.out.println("��ѡ��ѡ������쳣" + e.getMessage());
					}
				}
			}
		}
		//�ж�
		if("TFChoice".equals(QType)){
			String TFoptions=request.getParameter("TFoptions");
			String insertTFOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','1','"+TFoptions+"')";
			PackingDatabase p5 = new PackingDatabase();
			try {
				p5.update(insertTFOption);
			} catch (Exception e) {
				System.out.println("�ж�������쳣" + e.getMessage());
			}
		}
		//���
		if("ShortAnswerQuestion".equals(QType)){
			String ShortAnswer=request.getParameter("ShortAnswer");
			String insertShortAnswer = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+ShortAnswer+"','True')";
			PackingDatabase p6 = new PackingDatabase();
			try {
				p6.update(insertShortAnswer);
			} catch (Exception e) {
				System.out.println("���������쳣" + e.getMessage());
			}
		}
		
		response.sendRedirect("QuestionBank/SingleQuestion.html?QuestionId="+QuestionId);
		
	}

}
