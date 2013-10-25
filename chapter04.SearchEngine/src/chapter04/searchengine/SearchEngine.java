package chapter04.searchengine;

import java.util.List;

public interface SearchEngine {	
	List<String> search(String query);
	List<String> searchImage(String query);		
}
