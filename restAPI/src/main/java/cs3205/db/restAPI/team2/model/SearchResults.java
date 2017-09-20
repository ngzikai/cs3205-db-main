package cs3205.db.restAPI.team2.model;

import java.util.ArrayList;

public class SearchResults {
	private ArrayList<ResultLine> results;
	
	public SearchResults() {
		results = new ArrayList<ResultLine>();
	}
	
	public void addResult(ResultLine rl) {
		results.add(rl);
	}
	
	public ArrayList<ResultLine> getSearchResults(){
		return results;
	}
}
