package chapter04.google;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import chapter04.searchengine.SearchEngine;


public class Activator implements BundleActivator {

	private SearchEngine service;
	private ServiceRegistration registration;
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {		

		Dictionary props = new Properties();		
		props.put("searchable", "Text,Image");
		props.put(org.osgi.framework.Constants.SERVICE_VENDOR, "Google");
		props.put(org.osgi.framework.Constants.SERVICE_RANKING, 5);
		
		service = new Google();
		// register the service
		registration = context.registerService(SearchEngine.class.getName(), service, props);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {	
		registration.unregister(); //optional		
	}

}
