package chapter07.configurabletimeloggerfactory;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedServiceFactory;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, ManagedServiceFactory {
	private ServiceTracker tracker;
	private LogService logger;
	
	private Map<String,TimeThread> timeThreads = new HashMap<String,TimeThread>();
	
	static int timeInterval = 3000;
	
	private static final String TIMELOGGER_PID = "MyFactory";
	
	public void start(BundleContext context) throws Exception {
		tracker = new ServiceTracker(context,org.osgi.service.log.LogService.class.getName(),null);
		tracker.open();
		
		logger = (LogService) tracker.getService();		
		logger.log(LogService.LOG_INFO, "Starting");
		
		
		// Managed Service µî·Ï
		Dictionary<String, Object> props = new Hashtable<String, Object>();  
		props.put(Constants.SERVICE_PID, TIMELOGGER_PID);	
		context.registerService(ManagedServiceFactory.class.getName(), this, props);	
		
	}		

	public void stop(BundleContext context) throws Exception {
	}

	public String getName() {		
		return "My Configurable Time Logger Factory";
	}

	public void updated(String pid, Dictionary properties) throws ConfigurationException {
		logger.log(LogService.LOG_INFO, "Updated for PID : " + pid);
		
		Enumeration<String> en = properties.keys();		
		while (en.hasMoreElements()) {
			String key = en.nextElement();
		    logger.log(LogService.LOG_INFO,  key + " = " + properties.get(key));
		}

		if (properties == null) {
			logger.log(LogService.LOG_ERROR, "Properties is null");
			return;
		}
		
		if (properties.get("timeInterval") != null)
			timeInterval = (Integer) properties.get("timeInterval");
		else {
			logger.log(LogService.LOG_ERROR, "timeInterval is not set. Can't update time thread");
			return;
		}
		
		TimeThread t = timeThreads.get(pid);
		
		if (t == null) {
			t = new TimeThread(logger,pid,timeInterval);
			logger.log(LogService.LOG_INFO, "Starting Timer Thread [" + pid + "] with Interval : " + timeInterval);
			t.start();
			
			timeThreads.put(pid,t);
		} else {
			logger.log(LogService.LOG_INFO, "Updating Timer Thread [" + pid + "] with Interval : " + timeInterval);
			t.setTimeInterval(timeInterval);
		}			
	}
	
	public void deleted(String pid) {
		logger.log(LogService.LOG_INFO, "Deleted for PID : " + pid);
		
		TimeThread t = timeThreads.remove(pid);
		
		if (t != null) {
			logger.log(LogService.LOG_INFO, "Terminating Timer Thread for PID : " + pid);
			try {
				t.interrupt();
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}			
		}
		
	}
}

