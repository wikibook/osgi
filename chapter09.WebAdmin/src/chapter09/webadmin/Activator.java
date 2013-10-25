package chapter09.webadmin;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.util.tracker.ServiceTracker;

public class Activator implements BundleActivator {
	private ServiceTracker tracker;
	private HttpService service;

	private final String CONTEXT_PATH = "/admin";
	
	public void start(BundleContext context) throws Exception {
		System.out.println("Starting WebAdmin");
		tracker = new ServiceTracker(context, HttpService.class.getName(), null);
		tracker.open();
		service = (HttpService) tracker.getService();
		
		
		service.registerServlet("/admin", new WebAdminServlet(context , CONTEXT_PATH ), null, null);
		service.registerResources("/admin/res/admin.css", "/res/admin.css", null);
	}
	public void stop(BundleContext context) throws Exception {
		System.out.println("Stop WebAdmin");
	}

}
