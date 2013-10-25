package chapter09.helloworldweb;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
    private ServiceTracker tracker;
    private HttpService service;
	
	public void start(BundleContext context) throws Exception {
		tracker = new ServiceTracker(context, HttpService.class.getName(), null);
        tracker.open();
        service = (HttpService) tracker.getService();

        service.registerServlet("/hello", new HelloWorldServlet(), null, null);    
	}
	
	public void stop(BundleContext context) throws Exception {
	}

}
