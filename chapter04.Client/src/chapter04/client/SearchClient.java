package chapter04.client;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import chapter04.searchengine.SearchEngine;

public class SearchClient extends Thread {
	BundleContext context;
	
	public SearchClient(BundleContext context) {
		this.context = context;
	}
	
	public void run() {
		ServiceTracker tracker = new ServiceTracker(context,SearchEngine.class.getName(),null);		
		tracker.open();
		
		for(Object service:tracker.getServices()) {
			SearchEngine engine = (SearchEngine) service;
			List results = engine.search("some text");
			
			for(Object l:results){
				System.out.println(l);
			}		
		}		
		tracker.close();
	}		
}
