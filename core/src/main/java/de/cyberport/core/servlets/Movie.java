package de.cyberport.core.servlets;

public class Movie {

	private String title;
	private String year;
	private int awards;
	private int nominations;
	private Boolean isBestPicture;
	private int numberOfReferences;


	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getAwards() {
		return awards;
	}
	public void setAwards(int awards) {
		this.awards = awards;
	}
	public int getNominations() {
		return nominations;
	}
	public void setNominations(int nominations) {
		this.nominations = nominations;
	}
	public Boolean getIsBestPicture() {
		return isBestPicture;
	}
	public void setIsBestPicture(Boolean isBestPicture) {
		this.isBestPicture = isBestPicture;
	}
	public int getNumberOfReferences() {
		return numberOfReferences;
	}
	public void setNumberOfReferences(int numberOfReferences) {
		this.numberOfReferences = numberOfReferences;
	}
	
	public Movie() {
	}
	
	@Override
	public String toString() {

		return title+":"+year+":"+awards+":"+nominations+":"+isBestPicture+":"+numberOfReferences;
	}

}
