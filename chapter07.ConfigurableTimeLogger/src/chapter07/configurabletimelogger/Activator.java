package chapter07.configurabletimelogger;

import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, ManagedService {
	private ServiceTracker tracker;
	private LogService logger;
	
	private TimeThread timeThread = null;
	
	static int timeInterval = 3000;
	
	private static final String TIMELOGGER_PID = "chapter07.ConfigurableTimeLogger";
	
	public void start(BundleContext context) throws Exception {
		tracker = new ServiceTracker(context,org.osgi.service.log.LogService.class.getName(),null);
		tracker.open();
		
		logger = (LogService) tracker.getService();		
		logger.log(LogService.LOG_INFO, "Starting");
		
		
		// Managed Service µî·Ï
		Dictionary<String, Object> props = new Hashtable<String, Object>();  
		props.put(Constants.SERVICE_PID, TIMELOGGER_PID);	
		context.registerService(ManagedService.class.getName(), this, props);	
		
	}		

	public void stop(BundleContext context) throws Exception {
	}

	public void updated(Dictionary properties) throws ConfigurationException {
		logger.log(LogService.LOG_INFO, "Updated with Properties");
		
		if (timeThread != null) {
			logger.log(LogService.LOG_INFO, "Terminating Timer Thread");
			try {
				timeThread.interrupt();
				timeThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if (properties == null) {
			logger.log(LogService.LOG_ERROR, "Properties is null");
			return;
		}
		
		if (properties.get("timeInterval") != null)
			timeInterval = (Integer) properties.get("timeInterval");
		else {
			logger.log(LogService.LOG_ERROR, "timeInterval is not set. Can't start time thread");
			return;
		}			
		
		timeThread = new TimeThread(logger,timeInterval);
		
		logger.log(LogService.LOG_INFO, "Starting Timer Thread with Interval : " + timeInterval);
		timeThread.start();		
	}
}
