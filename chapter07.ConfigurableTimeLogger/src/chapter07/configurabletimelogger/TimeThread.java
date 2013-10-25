package chapter07.configurabletimelogger;

import java.util.Date;

import org.osgi.service.log.LogService;

public class TimeThread extends Thread {

	private LogService logger;
	int timeInterval;
	
	public TimeThread(LogService logger,int timeInterval) {
		this.logger = logger;
		this.timeInterval = timeInterval;
	}
	
	public void run() {
        int count = 0;
        while (true) {                   
            logger.log(LogService.LOG_INFO, "[" + count++ + "] " + (new Date()).toString());               
            try {
                sleep(timeInterval);
            } catch (InterruptedException e) {
                break;
            }
        }
    }	
}
