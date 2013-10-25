package chapter05.event2;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator, EventHandler {
	final static String[] topics = new String[] { "*" };
	
	public void start(BundleContext context) throws Exception {
		Dictionary<String,Object> prop = new Hashtable<String,Object>();		
		prop.put(EventConstants.EVENT_TOPIC, topics);
		//prop.put(EventConstants.EVENT_FILTER, "(!(bundle.symbolicName=*eclipse*))");
		
		context.registerService(EventHandler.class.getName(), this, prop);
				
		
		ServiceTracker tracker = new ServiceTracker(context,EventAdmin.class.getName(),null);       
		tracker.open();
		
		Properties props = new Properties();
		props.put("key", "value");

		EventAdmin ea = (EventAdmin) tracker.getService();		
		if (ea != null ) ea.sendEvent( new Event( "com/foo/bar" ,props)); 
		
		System.out.println("sent");
	}
	
	public void stop(BundleContext context) throws Exception {
	}
	
	public void handleEvent(Event event) {		
		System.out.println("\r\n[Event Received] Topic : " + event.getTopic());
		
		for (String key : event.getPropertyNames())
			System.out.println("\t[Property] " + key + " : " + event.getProperty(key ));
		
	}

}
