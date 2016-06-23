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
 * Servlet implementation class UpdateQuestion
 */
@WebServlet("/UpdateQuestion")
public class UpdateQuestion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateQuestion() {
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
		String QType=request.getParameter("Qtype");//�ڵ��ҳ��ȡ��
		String OptionNum=request.getParameter("num");
		String subject=request.getParameter("subject");
		String tips=request.getParameter("tips");
		String difficulty=request.getParameter("Difficulty");;
		String QuestionStem=request.getParameter("details");
		String questionLabel=request.getParameter("labels"); 
		String QuestionId=request.getParameter("questionid");//�ǵö�questionid��ֵ��������
		System.out.println(QuestionId);
		int VersionId=0;
		int EditTime=0;
		String correct="";
		//д��汾��Ϣ
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
		String time=format.format(date);
		PackingDatabase p7 = new PackingDatabase();
		String VIDsql="select VersionId from Question where QuestionId='"+QuestionId+"'";
		try {
			ResultSet rs = p7.query(VIDsql);
			while (rs.next()) {
				VersionId=rs.getInt("VersionId");
			}
			rs.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql="select EditTime from QuestionVersion where VersionId='"+VersionId+"'";
		PackingDatabase p1 = new PackingDatabase();
		try {
			ResultSet rs = p1.query(sql);
			while (rs.next()) {
				EditTime=rs.getInt("EditTime");
				EditTime=EditTime+1;
			}
			rs.close();
			String updateVersion = "update  QuestionVersion set EditTime='"+EditTime+"',LastEditTime='"+time+"',LastEditor='��Ŀ�޸���' where VersionId='"+VersionId+"'";
			p1.update(updateVersion);
		} catch (Exception e) {
			System.out.println("�汾��Ϣ�����쳣" + e.getMessage());
		}
		//�޸���Ŀ
		String Sql = "update  Question  set QuestionDetails='"+QuestionStem+"',SubjectId='"+subject+"',TipsId='"+tips+"',Difficulty='"+difficulty+"',Labels='"+questionLabel+"' where QuestionId='"+QuestionId+"'";
		PackingDatabase p2 = new PackingDatabase();
		try {
			p2.update(Sql);
		}  catch (Exception e) {
			System.out.println("��Ŀ�޸��쳣" + e.getMessage());
		}
		//��ѡ
		if("SingleChoice".equals(QType)){
			//��ȡ��ѡ��ѡ����Ϣ
			String SCoptions=request.getParameter("SCoptions");
			System.out.println(SCoptions);
			String options[]=new String[Integer.parseInt(OptionNum)];
			String deleteOption="delete [Option] where QuestionId='"+QuestionId+"'";
			PackingDatabase p3 = new PackingDatabase();
			try {
				p3.update(deleteOption);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i=0;i<Integer.parseInt(OptionNum);i++){
				options[i]=request.getParameter("SCTipsDetail"+(i+1));
				System.out.println(options[i]);
				if(i+1==Integer.parseInt(SCoptions)){
					correct="True";
				}		
				else{
					correct="False";
				}
			
				try {
					
					String updateSCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+options[i]+"','"+correct+"')";
					p3.update(updateSCOption);
				} catch (Exception e) {
					System.out.println("��ѡ��ѡ������쳣" + e.getMessage());
				}
			}
		}
		//��ѡ
		if("MultipleChoice".equals(QType)){
			String options[]=new String[Integer.parseInt(OptionNum)];
			String deleteOption="delete [Option] where QuestionId='"+QuestionId+"'";
			PackingDatabase p4 = new PackingDatabase();
			try {
				p4.update(deleteOption);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			for(int i=0;i<Integer.parseInt(OptionNum);i++){
				options[i]=request.getParameter("MCTipsDetail"+(i+1));
				if(request.getParameter("MCoptions"+(i+1))!=null){
					correct="True";
				}else{
					correct="False";
				}			
				try {
					String updateMCOption = "insert into [Option] (QuestionId,OptionDetails,IsCorrect) values('"+QuestionId+"','"+options[i]+"','"+correct+"')";
					p4.update(updateMCOption);
				} catch (Exception e) {
					System.out.println("��ѡ��ѡ������쳣" + e.getMessage());
				}
			}
		}
		//�ж�
		if("TFChoice".equals(QType)){
			String TFoptions=request.getParameter("TFoptions");
			try {
				String updateTFOption = "update [Option] set IsCorrect='"+TFoptions+"'";
				PackingDatabase p5 = new PackingDatabase();
				p5.update(updateTFOption);
			} catch (Exception e) {
				System.out.println("�ж�������쳣" + e.getMessage());
			}
		}
		//���
		if("ShortAnswerQuestion".equals(QType)){
			String ShortAnswer=request.getParameter("ShortAnswer");
			String updateShortAnswer = "update [Option] set OptionDetails='"+ShortAnswer+"' where QuestionId='"+QuestionId+"'";
			PackingDatabase p6 = new PackingDatabase();
			try {
				p6.update(updateShortAnswer);
			} catch (Exception e) {
				System.out.println("���������쳣" + e.getMessage());
			}
		}
		
		
		
	}

}