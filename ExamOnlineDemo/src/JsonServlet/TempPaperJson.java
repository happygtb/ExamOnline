package JsonServlet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;
import org.json.JSONArray;
import org.json.JSONObject;

import myServlet.PackingDatabase;

/**
 * Servlet implementation class TempPaperJson
 */
@WebServlet("/TempPaperJson")
public class TempPaperJson extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TempPaperJson() {
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
               		request.setCharacterEncoding("utf-8");

		response.setContentType("text/json");
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache");

		String paperId = request.getParameter("paperId");
		/*String paperId = "21";*/
		String filename = "";
		String mysql = "select PaperPath from TempPaper where TempPaperId='" + paperId + "'";
		PackingDatabase p = new PackingDatabase();
		try {
			ResultSet rs = p.query(mysql);
			if (rs.next()) {
				filename = rs.getString("PaperPath");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("临时试卷查询异常" + e.getMessage());
		}
		String fileRealPathandName = getServletContext().getRealPath("/") + "file\\TempPaper\\" + filename;
		InputStream is = new FileInputStream(fileRealPathandName);
		/* XWPFDocument doc = new XWPFDocument(is); */
		WordExtractor extractor = new WordExtractor(is);
		//获取文档中的图片
		try {
			FileInputStream in = new FileInputStream(new File(fileRealPathandName));
			HWPFDocument doc = new HWPFDocument(in);
			PicturesTable pic = doc.getPicturesTable();

			List<Picture> pictureList = pic.getAllPictures();
			/*System.out.println(pictureList.size());*/
			BufferedOutputStream output = null;
			for (int i = 0; i < pictureList.size(); i++) {
				Picture picture= (Picture) pictureList.get(i);
				// System.out.println(p.get());
				output = new BufferedOutputStream(
						new FileOutputStream(getServletContext().getRealPath("/") + "file\\TempPaper\\" + filename.split(".doc")[0] +"_" + (i + 1) + ".jpg"));
				output.write(picture.getContent());
				output.flush();
				output.close();
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		// 获取各个段落的文本
		String paraTexts[] = extractor.getParagraphText();
		String PaperName=paraTexts[1].split("【注意：")[0].split("试卷名称：")[1];
		String ExamDescription="";
		//拼接试卷描述字段
		for (int i = 3; i < paraTexts.length; i++) {
			String Paragraph=paraTexts[i].split("【注意：")[0];
			if(Paragraph.indexOf("试卷限时：")!=-1){
				break;
			}else{
				ExamDescription+=Paragraph;
			}
		}
		PrintWriter out = response.getWriter();
		JSONArray ja = new JSONArray();
		JSONObject jo = new JSONObject();
		jo.put("PaperName",PaperName);
		jo.put("ExamDescription",ExamDescription);
		jo.put("filename", filename.split(".doc")[0]);
		//开始读取每部分的内容
		String partName="";
		String partDescription="";
		String perPoint="";
		int partCount=0;
		int j=0;
		int pictureCount=0;
		for (int i = 3; i < paraTexts.length; i++) {
			JSONObject part = new JSONObject();
			if(paraTexts[i].indexOf("试卷限时：")!=-1){
				jo.put("TimeLimited", paraTexts[i].split("【注意：")[0].substring(5));
				ja.put(jo);
				continue;
			}
			if(paraTexts[i].indexOf("【注意：必填项，请勿使用word自动编号")!=-1){
				/*System.out.println("第"+(i+1)+"段");*/
				partCount++;
				partName=paraTexts[i].split("【注意：")[0];
				part.put("partNum", partCount);
				part.put("partName", partName);
				/*System.out.println("partNum="+partCount);*/
				//读取部分的基本信息
				int questionNum=1;
				for(j=i+1;j<paraTexts.length; j++){
					/*System.out.println("第"+(j+1)+"段");*/
					if(paraTexts[j].indexOf("部分描述：")!=-1){
						partDescription=paraTexts[j].split("【注意：")[0].substring(5);
						part.put("partDescription", partDescription);
						continue;
					}else if(paraTexts[j].indexOf("每题分值：")!=-1){
						perPoint=paraTexts[j].split("【注意：")[0].substring(5);
						part.put("perPoint", perPoint);
						continue;
					}else if(paraTexts[j].indexOf("【注意：必填项，请勿使用word自动编号")!=-1){
						i=j-1;
						break;
					}else{
						//读取题目
						int optionNum=1;
						for(int q=j;q<paraTexts.length;q++){
							/*System.out.println("第"+(q+1)+"段");
							System.out.println("questionNum="+questionNum);*/
							if(paraTexts[q].indexOf(". [")!=-1){
								if(!(paraTexts[q+1].indexOf("选项")!=-1&&paraTexts[q+1].indexOf(". [")==-1)||paraTexts[q+1].indexOf("答案：")==-1){
									String quesDetails=paraTexts[q].split("【注意：")[0];
									for(int h=q+1;h<paraTexts.length;h++){
										if(paraTexts[h].indexOf("答案：")!=-1||(paraTexts[h].indexOf("选项")!=-1&&paraTexts[h].indexOf(". [")==-1)){
											q=h-1;
											break;
										}else{
											String str=paraTexts[h].split("【注意：")[0];
											/*str=str.split("[图片")[0];*/
											quesDetails+=str;
										}
									}
									part.put("questionDetail"+questionNum, quesDetails);
								}
								/*part.put("questionDetail"+questionNum, paraTexts[q].split("【注意：")[0].replace("\r\n", ""));*/
								continue;
							}else if(paraTexts[q].indexOf("答案：")!=-1){
								part.put("answer"+questionNum, paraTexts[q].split("【注意：")[0].substring(3).replace("\r\n", ""));
								questionNum++;
								j=q;
								/*optionNum=1;*/
								break;
							}else if(paraTexts[q].indexOf("选项")!=-1&&paraTexts[q].indexOf(". [")==-1){
								/*System.out.println("optionNum="+optionNum);*/
								part.put("option"+questionNum+optionNum, paraTexts[q].replace("\r\n", "").split("【注意：")[0].substring(2));
								optionNum++;
								continue;
							}else if(paraTexts[q].indexOf("[图片")!=-1){
								pictureCount++;
								part.put("picture"+pictureCount, filename.split(".doc")[0] +"_" + pictureCount + ".jpg");
								continue;
							}else if(paraTexts[q].indexOf("【注意：必填项，请勿使用word自动编号")!=-1){
								i=q;
								break;
							}else{
								continue;
							}
						}
					}
				}
				part.put("questionCount", questionNum);
				ja.put(part);
			}
		}
		out.print(ja.toString());
		out.close();
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
