package chapter06.consolelogger;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator,LogListener {
	ServiceTracker tracker;
	LogReaderService reader;
	
	public void start(BundleContext context) throws Exception {
		tracker = new ServiceTracker(context,org.osgi.service.log.LogReaderService.class.getName(),null);
		tracker.open();
	
		reader = (LogReaderService) tracker.getService();		
		reader.addLogListener(this);
	}

	public void stop(BundleContext context) throws Exception {
	}

	public void logged(LogEntry entry) {
		
		System.out.println( "[" +getLevelName(entry.getLevel()) + 
				            "] [" + entry.getBundle().getSymbolicName() + 
				            "] " + entry.getMessage());
		
		/*
		ServiceReference sr = entry.getServiceReference();
		if (sr != null) {
			System.out.println("Service Name : " + sr);
			for (String key : sr.getPropertyKeys())
				System.out.println("\t[Svc Prop] " + key + " : " + sr.getProperty(key ));
		}*/
		
		
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
