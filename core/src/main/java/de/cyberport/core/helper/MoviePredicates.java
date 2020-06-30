package de.cyberport.core.helper;


import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.drew.lang.annotations.NotNull;

import de.cyberport.core.servlets.Movie;
 
public class MoviePredicates 
{
    public static Predicate<Movie> byTitle(@NotNull String title) {
    	return p -> p.getTitle().equalsIgnoreCase(title);
    }
    public static Predicate<Movie> byYear(@NotNull String year) {
        return p -> p.getYear().equalsIgnoreCase(year);
    }
    public static Predicate<Movie> byminYear (@NotNull String year) {
        return p -> Integer.parseInt(p.getYear()) >= Integer.parseInt(year);
    }
    public static Predicate<Movie> bymaxYear (@NotNull String year) {
        return p -> Integer.parseInt(p.getYear()) <= Integer.parseInt(year);
    }
    public static Predicate<Movie> byminAwards  (@NotNull int awards) {
        return p -> p.getAwards() >= awards;
    }
    public static Predicate<Movie> bymaxAwards  (@NotNull int awards) {
        return p -> p.getAwards() <= awards;
    }
    public static Predicate<Movie> byNominations(@NotNull Integer nominations) {
        return p -> p.getNominations()==nominations;
    }
    public static Predicate<Movie> byisBestPicture (@NotNull Boolean isBestPicture ) {
        return p -> p.getIsBestPicture()==isBestPicture ;
    }
   
     
    public static List<Movie> filterMovies(List<Movie> movies, 
                                                Predicate<Movie> predicate) 
    {
        return movies.stream()
                    .filter( predicate )
                    .collect(Collectors.<Movie>toList());
    }
}   
