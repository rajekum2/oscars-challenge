package de.cyberport.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.cyberport.core.constants.Constants;
import de.cyberport.core.helper.MoviePredicates;
import de.cyberport.core.servlets.Movie;

public class MovieUtils {

	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private static List<Movie> movielistByTitle=null;
	private static List<Movie> movielistByYear=null;
	private static List<Movie> movielistByminYear=null;
	private static List<Movie> movielistBymaxYear=null;
	private static List<Movie> movielistByminAwards=null;
	private static List<Movie> movielistBymaxAwards=null;
	private static List<Movie> movielistByNominations=null;
	private static List<Movie> movielistByisBestPicture=null;

	public static List<Movie> createMovieList(String data) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();    	
		HashMap <String,Movie> movies = objectMapper.readValue(data, new TypeReference<HashMap <String,Movie>>() {});
		ArrayList<Movie> arrList= new ArrayList<Movie>();
		movies.forEach((k, v) -> {
			arrList.add(v);
		});
		return arrList;
	}

	public static String formatDataString(String data) {
		data=data.replace("\"sling:resourceType\": \"test/filmEntryContainer\",", "");
		data=data.replace(",\n" + 
				"    \"jcr:primaryType\": \"nt:unstructured\",", "");
		data=data.replace("\"jcr:primaryType\": \"nt:unstructured\",", "");
		data=data.replace("\"sling:resourceType\": \"test/filmEntry\"","");
		return data;
	}

	public static String readFromInputStream(InputStream inputStream)
			throws IOException {
		StringBuilder resultStringBuilder = new StringBuilder();
		try (BufferedReader br
				= new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				resultStringBuilder.append(line).append("\n");
			}
		}
		return resultStringBuilder.toString();
	}   

	public static List<Movie> filterMovies(SlingHttpServletRequest req,ArrayList<String> paramsList, List<Movie> movielist) throws JSONException {
		Integer nominations=-1;
		Integer minAwards=-1;
		Integer maxAwards=-1;
		Boolean yearFlag = true;

		List<Movie> countermovielist=new ArrayList<Movie>();
		countermovielist=movielist;
		if(paramsList.contains(Constants.TITLE)) {
			String title=req.getParameter(Constants.TITLE);
			movielistByTitle=MoviePredicates.filterMovies(movielist, MoviePredicates.byTitle(title));
			countermovielist=movielistByTitle;
		}
		if (countermovielist.size()>1) {		

			if(paramsList.contains(Constants.YEAR)) {
				yearFlag=false;
				String year=req.getParameter(Constants.YEAR);
				movielistByYear=MoviePredicates.filterMovies(countermovielist, MoviePredicates.byYear(year));
				if (movielistByYear !=null || !movielistByYear.isEmpty() ) {
					countermovielist=movielistByYear;
				}
			}
			if(paramsList.contains(Constants.IS_BEST_PICTURE)) {	
				Boolean isBestPicture  =Boolean.valueOf(req.getParameter(Constants.IS_BEST_PICTURE));
				movielistByisBestPicture=MoviePredicates.filterMovies(countermovielist, MoviePredicates.byisBestPicture(isBestPicture));
				if (movielistByisBestPicture !=null || !movielistByisBestPicture.isEmpty()) {
					countermovielist=movielistByisBestPicture;
				}
			}
			if(paramsList.contains(Constants.NOMINATIONS)) {	
				nominations=Integer.parseInt(req.getParameter(Constants.NOMINATIONS));
				movielistByNominations=MoviePredicates.filterMovies(countermovielist, MoviePredicates.byNominations(nominations));
				if (movielistByNominations !=null || !movielistByNominations.isEmpty()) {
					countermovielist=movielistByNominations;
				}
			}
			if(paramsList.contains(Constants.MIN_YEAR) && yearFlag) {	
				String minYear=req.getParameter(Constants.MIN_YEAR);
				movielistByminYear=MoviePredicates.filterMovies(countermovielist, MoviePredicates.byminYear(minYear));
				if (movielistByminYear !=null || !movielistByminYear.isEmpty()) {
					countermovielist=movielistByminYear;
				}
			}
			if(paramsList.contains(Constants.MIN_AWARDS)) {	
				minAwards=Integer.parseInt(req.getParameter(Constants.MIN_AWARDS));
				movielistByminAwards=MoviePredicates.filterMovies(countermovielist, MoviePredicates.byminAwards(minAwards));
				if (movielistByminAwards !=null || !movielistByminAwards.isEmpty()) {
					countermovielist=movielistByminAwards;
				}
			}
			if(paramsList.contains(Constants.MAX_YEAR) && yearFlag) {	
				String maxYear =req.getParameter(Constants.MAX_YEAR); 
				movielistBymaxYear=MoviePredicates.filterMovies(countermovielist, MoviePredicates.bymaxYear(maxYear));
				if (movielistBymaxYear !=null || !movielistBymaxYear.isEmpty()) {
					countermovielist=movielistBymaxYear;
				}
			}		
			if(paramsList.contains(Constants.MAX_AWARDS)) {	
				maxAwards=Integer.parseInt(req.getParameter(Constants.MAX_AWARDS));
				movielistBymaxAwards=MoviePredicates.filterMovies(countermovielist, MoviePredicates.bymaxAwards(maxAwards));
				if (movielistBymaxAwards !=null || !movielistBymaxAwards.isEmpty()) {
					countermovielist=movielistBymaxAwards;
				}
			}
		}
		return countermovielist; 		 
	}

	public static List<Movie> sortMovies(SlingHttpServletRequest req,List<Movie> countermovielist) {

		if(req.getParameter(Constants.SORT_BY).equalsIgnoreCase(Constants.TITLE)) {
			countermovielist=MovieSort.byTitle(countermovielist);
		}
		if(req.getParameter(Constants.SORT_BY).equalsIgnoreCase(Constants.YEAR)) {
		}
		if(req.getParameter(Constants.SORT_BY).equalsIgnoreCase(Constants.AWARDS)) {
			countermovielist=MovieSort.byAwards(countermovielist);
		}
		if(req.getParameter(Constants.SORT_BY).equalsIgnoreCase(Constants.NOMINATIONS)) {
			countermovielist=MovieSort.byNominations(countermovielist);
		}
		return countermovielist;
	}

	public static List<Movie> limitMovies(SlingHttpServletRequest req,List<Movie> countermovielist) {		

		Integer limit=-1;
		limit=Integer.parseInt(req.getParameter(Constants.LIMIT));
		countermovielist=countermovielist.subList(0, limit);	

		return countermovielist;
	}
}
