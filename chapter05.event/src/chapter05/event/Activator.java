package chapter05.event;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.FrameworkEvent;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.service.startlevel.StartLevel;

public class Activator implements BundleActivator,BundleListener,ServiceListener,FrameworkListener{
	
	StartLevel sl;
	
	public void start(BundleContext context) throws Exception {				
		context.addBundleListener(this);
		context.addFrameworkListener(this);
		context.addServiceListener(this);
		
		sl = (StartLevel) context.getService(context.getServiceReference(org.osgi.service.startlevel.StartLevel.class.getName()));
	}

	public void stop(BundleContext context) throws Exception {
		context.removeServiceListener(this);
	}
	
	public void bundleChanged(BundleEvent event) {	
		System.out.println("Bundle [" + event.getBundle().getSymbolicName() + "] " + getStateName(event.getType()));
	}	

	public void serviceChanged(ServiceEvent event) {
		ServiceReference sr = event.getServiceReference();
		String serviceVendor = sr.getProperty(org.osgi.framework.Constants.SERVICE_VENDOR).toString();		
		String serviceClass = ((String[]) sr.getProperty("objectClass"))[0];
		String serviceID = sr.getProperty(org.osgi.framework.Constants.SERVICE_ID).toString();
        
		System.out.println("Service [" + serviceClass + "|" + serviceVendor + "|" + serviceID + "] " 
				+ getServiceStateName(event.getType()));		
	}	
	

	private String getServiceStateName(int state) {
		switch (state) {			
		case ServiceEvent.REGISTERED :
			return "REGISTERED";
		case ServiceEvent.MODIFIED :
			return "MODIFIED";
		case ServiceEvent.UNREGISTERING:
			return "UNREGISTERING";				
		default :
			return Integer.toHexString(state);
		}
	}

	public void frameworkEvent(FrameworkEvent event) {		
		System.out.println("Framework " + getFWStateName(event.getType()));
		
		if (event.getType() == FrameworkEvent.STARTLEVEL_CHANGED) {
			System.out.println("SL : " + sl.getStartLevel());
		}
		
		if (event.getType() == FrameworkEvent.ERROR)
			System.out.println(event.getThrowable().toString());
	}
	
	private String getFWStateName(int state) {		
		switch (state) {			
			case FrameworkEvent.STARTED :
				return "STARTED";
			case FrameworkEvent.ERROR :
				return "ERROR";
			case FrameworkEvent.PACKAGES_REFRESHED :
				return "PACKAGES_REFRESHED";
			case FrameworkEvent.STARTLEVEL_CHANGED :
				return "STARTLEVEL_CHANGED";
			case FrameworkEvent.WARNING:
				return "WARNING";				
			default :
				return Integer.toHexString(state);
		}
	}
	
	private String getStateName(int state) {		
		switch (state) {			
			case BundleEvent.INSTALLED :
				return "INSTALLED"; 

			case BundleEvent.STARTED :
				return "STARTED";			

			case BundleEvent.STOPPED :
				return "STOPPED";
				
			case BundleEvent.UPDATED:
				return "UPDATED";				
				
			case BundleEvent.UNINSTALLED:
				return "UNINSTALLED";
				
			case BundleEvent.RESOLVED :
				return "RESOLVED";
				
			case BundleEvent.UNRESOLVED :
				return "RESOLVED"; 				

			case BundleEvent.STARTING :				
				return "STARTING"; 
				
			case BundleEvent.STOPPING :
				return "STOPPING"; 			 

				
			default :
				return Integer.toHexString(state);
		}
	}



}
