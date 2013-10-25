package chapter04.naver;

import java.util.ArrayList;
import java.util.List;

import chapter04.searchengine.SearchEngine;

public class Naver implements SearchEngine {

	public List<String> search(String query) {
		List<String> result = new  ArrayList<String>();
		
		result.add("Naver text result1");
		result.add("Naver text result2");
		
		return result;
	}

	public List<String> searchImage(String query) {
		List<String> result = new  ArrayList<String>();
		
		result.add("Naver image result1");
		result.add("Naver image result2");
		return result;
	}

}
