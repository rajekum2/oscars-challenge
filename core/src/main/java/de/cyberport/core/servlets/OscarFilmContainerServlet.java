package de.cyberport.core.servlets;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import de.cyberport.core.constants.Constants;
import de.cyberport.core.utils.MovieUtils;

/**
 * Servlet that writes information about the Oscar films in json format into the response.
 * It is mounted for all resources of a specific Sling resource type.
 *
 * Based on the request parameters, a filtering and sorting should be applied.
 *
 * For cases when there is no supported request parameter provided in the request,
 * the servlet should return all the films below the requested container.
 *
 * The Servlet must support following request parameters:
 * 1. title - String. The exact film title
 * 2. year - Integer. The exact year when the film was nominated
 * 3. minYear - Integer. The minimum value of the year for the nominated film
 * 4. maxYear - Integer. The maximum value of the year for the nominated film
 * 5. minAwards - Integer. The minimum value for number of awards
 * 6. maxAwards - Integer. The maximum value for number of awards
 * 7. nominations - Integer. The exact number of nominations
 * 8. isBestPicture - Boolean. True to return only the winners of the best picture nomination.
 * 9. sortBy - Enumeration. Sorting in ascending order, supported values are: title, year, awards, nominations.
 *
 * Please note:
 * More then 1 filter must be supported.
 * The resulting JSON must not contain "jcr:primaryType" and "sling:resourceType" properties
 *
 * Examples based on the data stored in oscars.json in resources directory.
 *
 * 1. Request parameters: year=2019&minAwards=4
 *
 * Sample response:
 * {
 *   "result": [
 *     {
 *       "title": "Parasite",
 *       "year": "2019",
 *       "awards": 4,
 *       "nominations": 6,
 *       "isBestPicture": true,
 *       "numberOfReferences": 8855
 *     }
 *   ]
 * }
 *
 * 2. Request parameters: minYear=2018&minAwards=3&sortBy=nominations&limit=4
 *
 * Sample response:
 * {
 *   "result": [
 *     {
 *       "title": "Bohemian Rhapsody",
 *       "year": "2018",
 *       "awards": 4,
 *       "nominations": 5,
 *       "isBestPicture": false,
 *       "numberOfReferences": 387
 *     },
 *     {
 *       "title": "Green Book",
 *       "year": "2018",
 *       "awards": 3,
 *       "nominations": 5,
 *       "isBestPicture": true,
 *       "numberOfReferences": 2945
 *     },
 *     {
 *       "title": "Parasite",
 *       "year": "2019",
 *       "awards": 4,
 *       "nominations": 6,
 *       "isBestPicture": true,
 *       "numberOfReferences": 8855
 *     },
 *     {
 *       "title": "Black Panther",
 *       "year": "2018",
 *       "awards": 3,
 *       "nominations": 7,
 *       "isBestPicture": false,
 *       "numberOfReferences": 770
 *     }
 *   ]
 * }
 *
 * @author Vitalii Afonin
 */

@Component(service = Servlet.class,immediate = true, property = {
		"sling.servlet.methods=" + HttpConstants.METHOD_GET,
		"sling.servlet.paths=" + "/bin/test/filmEntryContainer",
		"sling.servlet.extensions="+"json"
})
@ServiceDescription("Oscar Film Container Servlet")
public class OscarFilmContainerServlet extends SlingSafeMethodsServlet {

	private static final long serialVersionUID = 2598426539166789515L;
	protected final Logger log = LoggerFactory.getLogger(this.getClass());
	private PrintWriter out = null;
	List<Movie> fliteredmovielist=null;
	List<Movie> movielist=null;
	ArrayList<String> paramsList=null;
	private JSONObject resultObj;

	@Override
	protected void doGet(final SlingHttpServletRequest req, final SlingHttpServletResponse resp) throws IOException {	
		try {
			resp.setContentType("application/json");
			resultObj=new JSONObject();
			paramsList=new ArrayList<String>();
			out = resp.getWriter();
			String data="";
			
			Enumeration<?> paramEnum =req.getParameterNames();
			while(paramEnum.hasMoreElements()) {
				paramsList.add(paramEnum.nextElement().toString());
			}
		    data=loadDataFromResource(req,resp); 
			data=MovieUtils.formatDataString(data);
			movielist=MovieUtils.createMovieList(data);
			fliteredmovielist=MovieUtils.filterMovies(req,paramsList,movielist);
			
			if(paramsList.contains(Constants.SORT_BY)) {
				fliteredmovielist=MovieUtils.sortMovies(req,fliteredmovielist);
			}
			if(paramsList.contains(Constants.LIMIT)) {
				fliteredmovielist=MovieUtils.limitMovies(req,fliteredmovielist);
			}
			if(!fliteredmovielist.isEmpty()) {
				resultObj.put(Constants.RESULT_OBJ, fliteredmovielist);
				out.println(resultObj);
			}else {
				out.println("No match found ! ");
			}			

		}catch(Exception e) {
			log.error(e.getMessage());
		}

	}    
	/** Load JSON file as String from resourse folder
    *
    * @param SlingHttpServletRequest req request
    * @param SlingHttpServletResponse resp response
    * @return JSON String */
	public String loadDataFromResource(SlingHttpServletRequest req,SlingHttpServletResponse resp) throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("oscars.json");
		return MovieUtils.readFromInputStream(in);
	}
}
