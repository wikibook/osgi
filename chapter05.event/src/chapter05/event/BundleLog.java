package chapter05.event;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class BundleLog implements BundleListener {

	public void bundleChanged(BundleEvent event) {	
		System.out.println("Bundle [" + event.getBundle().getSymbolicName() + "] " + getStateName(event.getType()));
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
