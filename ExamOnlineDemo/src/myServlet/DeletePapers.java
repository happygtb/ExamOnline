package myServlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;

/**
 * Servlet implementation class DeletePapers
 */
@WebServlet("/DeletePapers")
public class DeletePapers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeletePapers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		request.setCharacterEncoding("utf-8");
		
		String method=request.getParameter("method");
		String filename="";
		
		if("batch".equals(method)){
			int count=Integer.parseInt(request.getParameter("count"));
			String paperId=request.getParameter("paperId");
			String normalPapers[]=new String[count];
			String importedPapers[]=new String[count];
			int normalCount=0;
			int importedCount=0;
			
			for(int i=0;i<count;i++){
				String str=paperId.split(",")[i+1];
				if(str.indexOf("normal")!=-1){
					normalCount++;
					System.out.println(normalCount);
					System.out.println(str);
					normalPapers[normalCount-1]=str.substring(7);
					String PaperVersionId="";
					String query="select PaperVersionId from Paper where PaperId='"+normalPapers[normalCount-1]+"'";
					PackingDatabase p = new PackingDatabase();
					try {
						ResultSet rs = p.query(query);
						if (rs.next()) {
							PaperVersionId=rs.getString("PaperVersionId");
						}
						rs.close();
					}catch (Exception ex) {
						ex.printStackTrace();
					}
					String delete="delete Paper where PaperId='"+normalPapers[normalCount-1]+"';delete PaperVersion where PaperVersionId='"+PaperVersionId+"'";
					batchDelete(delete);
				}else{
					importedCount++;
					importedPapers[importedCount-1]=str.substring(9);
					String mysql = "select PaperPath from TempPaper where TempPaperId='" + importedPapers[importedCount-1] + "'";
					PackingDatabase p = new PackingDatabase();
					try {
						ResultSet rs = p.query(mysql);
						if (rs.next()) {
							filename = rs.getString("PaperPath");
							String fileRealPathandName = getServletContext().getRealPath("/") + "file\\TempPaper\\" + filename;
							try {
								FileInputStream in = new FileInputStream(new File(fileRealPathandName));
								HWPFDocument doc = new HWPFDocument(in);
								PicturesTable pic = doc.getPicturesTable();
								List<Picture> pictureList = pic.getAllPictures();
								for(int j=0;j<pictureList.size();j++){
									String picture=getServletContext().getRealPath("/") + "file\\TempPaper\\"+rs.getString("PaperPath").split(".doc")[0]+"_"+(j+1)+".jpg";
									deleteFile(picture);
								}
							}catch (Exception ex) {
								ex.printStackTrace();
							}
							deleteFile(fileRealPathandName);
						}
						rs.close();
					} catch (Exception e) {
						System.out.println("临时试卷查询异常" + e.getMessage());
					}
					String delete1="delete TempPaper where TempPaperId='"+importedPapers[importedCount-1]+"'";
					batchDelete(delete1);
				}	
			}
		}else{
			String paperId=request.getParameter("paperId");
			String type=request.getParameter("type");
			if("normal".equals(type)){
				String query="select PaperVersionId from Paper where PaperId='"+paperId+"'";
				String PaperVersionId="";
				PackingDatabase p = new PackingDatabase();
				try {
					ResultSet rs = p.query(query);
					if (rs.next()) {
						PaperVersionId=rs.getString("PaperVersionId");
					}
					rs.close();
				}catch (Exception ex) {
					ex.printStackTrace();
				}
				String delete="delete Paper where PaperId='"+paperId+"';delete PaperVersion where PaperVersionId='"+PaperVersionId+"'";
				batchDelete(delete);
			}else{
				String mysql = "select PaperPath from TempPaper where TempPaperId='" + paperId + "'";
				PackingDatabase p = new PackingDatabase();
				try {
					ResultSet rs = p.query(mysql);
					if (rs.next()) {
						filename = rs.getString("PaperPath");
						String fileRealPathandName = getServletContext().getRealPath("/") + "file\\TempPaper\\" + filename;
						try {
							FileInputStream in = new FileInputStream(new File(fileRealPathandName));
							HWPFDocument doc = new HWPFDocument(in);
							PicturesTable pic = doc.getPicturesTable();
							List<Picture> pictureList = pic.getAllPictures();
							for(int j=0;j<pictureList.size();j++){
								String picture=getServletContext().getRealPath("/") + "file\\TempPaper\\"+rs.getString("PaperPath").split(".doc")[0]+"_"+(j+1)+".jpg";
								deleteFile(picture);
							}
						}catch (Exception ex) {
							ex.printStackTrace();
						}
						deleteFile(fileRealPathandName);
					}
					rs.close();
				} catch (Exception e) {
					System.out.println("临时试卷查询异常" + e.getMessage());
				}
				String delete1="delete TempPaper where TempPaperId='"+paperId+"'";
				batchDelete(delete1);
			}
		}
		response.sendRedirect("ExamPapers/PaperIndex.html");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	private void batchDelete(String SQL){
		PackingDatabase packing = new PackingDatabase();
		try {
			packing.update(SQL);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
