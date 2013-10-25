package chapter04.google;

import java.util.ArrayList;
import java.util.List;

import chapter04.searchengine.SearchEngine;

public class Google implements SearchEngine {

	public List<String> search(String query) {
		List<String> result = new  ArrayList<String>();
		
		result.add("Google text result1");
		result.add("Google text result2");
		
		return result;
	}

	public List<String> searchImage(String query) {
		List<String> result = new  ArrayList<String>();
		
		result.add("Google image result1");
		result.add("Google image result2");
		return result;
	}

}
