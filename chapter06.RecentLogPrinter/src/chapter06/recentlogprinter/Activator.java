package chapter06.recentlogprinter;

import java.util.Enumeration;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	public void start(BundleContext context) throws Exception {
		ServiceTracker tracker = new ServiceTracker(context,org.osgi.service.log.LogReaderService.class.getName(),null);
		tracker.open();
	
		LogReaderService reader = (LogReaderService) tracker.getService();		
				
		for (Enumeration<LogEntry> en = reader.getLog(); en.hasMoreElements();) {
			LogEntry entry = en.nextElement();

			System.out.println( "[" +getLevelName(entry.getLevel()) + 
		            "] [" + entry.getBundle().getSymbolicName() + 
		            "] " + entry.getMessage());
		}		
	}

	public void stop(BundleContext context) throws Exception {
	}
	
	protected static String getLevelName(int type) {
		switch (type) {
		case LogService.LOG_DEBUG:
			return "DEBUG";		
		case LogService.LOG_INFO:
			return "INFO ";		
		case LogService.LOG_ERROR:
			return "ERROR";		
		case LogService.LOG_WARNING:
			return "WARN ";					
		}
		return "";
	}
}
