package cc;

import java.text.SimpleDateFormat;
import java.util.Random;

public class IPTimeStamp {
	private String ip;
	public IPTimeStamp(String ip) {
		this.ip = ip; 
	}
	
	private  String getTimeStamp() {
		String temp = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		temp = sdf.format(new java.util.Date());
		return temp;
	}
	
	
	public String getIPTimeStampRand() {
		StringBuffer buf = new StringBuffer();
		buf.append(this.getTimeStamp());
		Random rand = new Random();
		for (int i = 0; i < 3; i++) {
			buf.append(rand.nextInt(10)) ;
		}
		return buf.toString() ;
	}
}
