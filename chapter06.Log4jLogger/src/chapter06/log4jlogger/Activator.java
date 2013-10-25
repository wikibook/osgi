package chapter06.log4jlogger;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Activator implements BundleActivator,LogListener {
	ServiceTracker tracker;
	LogReaderService reader;
	
	static Logger logger;
	
	public void start(BundleContext context) throws Exception {
		tracker = new ServiceTracker(context,org.osgi.service.log.LogReaderService.class.getName(),null);
		tracker.open();
	
		reader = (LogReaderService) tracker.getService();		
		reader.addLogListener(this);
		
		logger = Logger.getLogger(this.getClass());
	}

	public void stop(BundleContext context) throws Exception {
	}

	public void logged(LogEntry entry) {
		logger.log(getLog4jLevel(entry.getLevel()), 
	                "[" + entry.getBundle().getSymbolicName() + 
	                "] " + entry.getMessage());
	}
	
	protected static Level getLog4jLevel(int type) {
		switch (type) {
		case LogService.LOG_DEBUG:
			return Level.DEBUG;		
		case LogService.LOG_INFO:
			return Level.INFO;		
		case LogService.LOG_ERROR:
			return Level.ERROR;		
		case LogService.LOG_WARNING:
			return Level.WARN;					
		}
		return Level.TRACE;
	}
}

