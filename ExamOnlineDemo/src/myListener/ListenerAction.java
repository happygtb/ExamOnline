package myListener;

import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.Picture;

import myServlet.PackingDatabase;

public class ListenerAction extends TimerTask {
	private static boolean isrunning = false;
	private static long dotaskmillis = 0l;

	@Override
	public void run() {
		System.out.println("dotaskmillis:" + dotaskmillis);
		if (!isrunning) {
			isrunning = true;
			Date d=new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String TimeDecount=sdf.format(new Date(d.getTime()-15 * 24 * 60 * 60 * 1000));
			String mySql = "select PaperPath from TempPaper where PaperPath like '"+TimeDecount+"%'";
			PackingDatabase packing = new PackingDatabase();
			String path=this.getClass().getClassLoader().getResource("../../").getPath()+"file/TempPaper/";
			path=path.substring(1).replace("/", "\\");
			String filePath="";
			try {
				ResultSet rs = packing.query(mySql);
				while (rs.next()) {
					filePath=path+rs.getString("PaperPath");
					try {
						FileInputStream in = new FileInputStream(new File(filePath));
						HWPFDocument doc = new HWPFDocument(in);
						PicturesTable pic = doc.getPicturesTable();
						List<Picture> pictureList = pic.getAllPictures();
						for(int i=0;i<pictureList.size();i++){
							String picture=path+rs.getString("PaperPath").split(".doc")[0]+"_"+(i+1)+".jpg";
							deleteFile(picture);
						}
					}catch (Exception ex) {
						ex.printStackTrace();
					}
					deleteFile(filePath);
					String delete="delete TempPaper where PaperPath='"+rs.getString("PaperPath")+"'";
					packing.update(delete);
				}
				rs.close();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			isrunning = false;
		} else {
			System.out.println("系统正忙，请稍后");
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