package chapter06.timelogger;

import java.util.Date;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	private ServiceTracker tracker;
	private LogService logger;
	
	private Thread t;
	
	public void start(BundleContext context) throws Exception {
		tracker = new ServiceTracker(context,org.osgi.service.log.LogService.class.getName(),null);
		tracker.open();
	
		logger = (LogService) tracker.getService();
		
		logger.log(LogService.LOG_INFO, "TimeLogger starting");
		
		t = new Thread() {
			public void run() {
				int count = 0;
				while (true) {					
					logger.log(LogService.LOG_INFO, "[" + count++ + "] " + (new Date()).toString());	            
					try {
						sleep(3000);
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		};

		t.start();		
		
	}

	public void stop(BundleContext context) throws Exception {
		
		t.interrupt();
		t.join();
		
		
		if(logger != null)
			logger.log(LogService.LOG_INFO, "TimeLogger stopped");
		
		tracker.close();
		tracker = null;		
		logger = null;
	}
}
