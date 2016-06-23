package myListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Application Lifecycle Listener implementation class TempPaperDelete
 *
 */
@WebListener
public class TempPaperDelete implements ServletContextListener {
	private Timer timer = null;
    /**
     * Default constructor. 
     */
    public TempPaperDelete() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	timer.cancel(); 
    	sce.getServletContext().log("定时器销毁");
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    @Override
    public void contextInitialized(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	/*SimpleDateFormat df = new SimpleDateFormat("HH:mm");*/
    	Calendar calendar = Calendar.getInstance(); 

    	calendar.set(Calendar.HOUR_OF_DAY, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);

    	Date date=calendar.getTime(); //第一次执行定时任务的时间
    	if (date.before(new Date())) {
    		date = this.addDay(date, 1);
    	}
    	timer = new Timer(true);
    	sce.getServletContext().log("定时器已启动");
    	timer.schedule(new ListenerAction(),date,24*60*60*1000);//每天执行一次
    	sce.getServletContext().log("已经添加任务调度表");
    }
    
    public Date addDay(Date date, int num) {
    	Calendar startDT = Calendar.getInstance();
    	startDT.setTime(date);
    	startDT.add(Calendar.DAY_OF_MONTH, num);
    	return startDT.getTime();
    }
}
