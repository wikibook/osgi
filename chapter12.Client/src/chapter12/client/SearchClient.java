package chapter12.client;

import java.util.List;
import chapter04.searchengine.SearchEngine;

public class SearchClient {

	private SearchEngine searchEngine;
	
	public void start() {	
		System.out.println("Start Querying SearchEngine..");
		
		List results = searchEngine.search("query string");
			
		for(Object l:results){
			System.out.println(l);
		}
	}

	public void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}
}
