package de.cyberport.core.utils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import de.cyberport.core.servlets.Movie;

public class MovieSort
{
	public static List<Movie> byTitle(List<Movie> movies)
	{
		return movies.stream()
				.sorted(Comparator.comparing(Movie::getTitle))
				.collect(Collectors.toList());
	}

	public static List<Movie> byYear(List<Movie> movies)
	{
		return movies.stream()
				.sorted(Comparator.comparing(Movie::getYear))
				.collect(Collectors.toList());

	}
	public static List<Movie> byAwards(List<Movie> movies)
	{
		return movies.stream()
				.sorted(Comparator.comparing(Movie::getAwards))
				.collect(Collectors.toList());

	}
	public static List<Movie> byNominations(List<Movie> movies)
	{
		return movies.stream()
				.sorted(Comparator.comparing(Movie::getNominations))
				.collect(Collectors.toList());
	}
}