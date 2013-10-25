package chapter12.client;

import java.util.Iterator;
import java.util.List;

import chapter04.searchengine.SearchEngine;

public class MultiSearchClient {
	private List searchEngines;
	
	public void start() {	
		System.out.println("Start Querying SearchEngines..");
		
		
		for (Iterator iterator = searchEngines.iterator();iterator.hasNext();) {
			SearchEngine engine = (SearchEngine) iterator.next();
			
			List results = engine.search("query string");
			for(Object l:results){
				System.out.println(l);
			}
		}
		
		for(Object e:searchEngines) {
			SearchEngine engine = (SearchEngine) e;			
			List results = engine.search("query string");
			
			if (engine instanceof Loggable)
				((Loggable)engine).sendQueryResult();
		}
	}

	public void setSearchEngines(List searchEngines) {
		this.searchEngines = searchEngines;
	}
}
