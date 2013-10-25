package chapter04.client;

import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import chapter04.searchengine.SearchEngine;

public class Activator implements BundleActivator {

	SearchClient client;
	
	public void start(BundleContext context) throws Exception {		
		client = new SearchClient(context);
        new Thread(client).start();		
	}
	
	public void stop(BundleContext context) throws Exception {	
	}
}
