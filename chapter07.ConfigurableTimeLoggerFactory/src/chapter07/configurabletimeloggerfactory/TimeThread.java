package chapter07.configurabletimeloggerfactory;

import java.util.Date;

import org.osgi.service.log.LogService;

public class TimeThread extends Thread {

	private LogService logger;
	private int timeInterval;	
	private String pid;
	
	public TimeThread(LogService logger, String pid, int timeInterval) {
		this.logger = logger;
		this.pid = pid;
		this.timeInterval = timeInterval;		
	}
	
	public void setTimeInterval(int timeInterval) {
		this.timeInterval = timeInterval;
	}
	
	public void run() {
        int count = 0;
        while (true) {                   
            logger.log(LogService.LOG_INFO, "[" + count++ + "] [" + pid + "] " + (new Date()).toString());               
            try {
                sleep(timeInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }	
}
