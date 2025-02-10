package odev.entities;

import java.util.List;

public class Media {
	//url,title,type,genres,releaseYear,imdbId,imdbAverageRating,imdbNumVotes,platform,availableCountries
    private String url;
	private String title;
	private String type;
	private List<String> genres;
	private int releaseYear;
	private String imbdId;
	private double imdbAverageRating;
	private int imdbNumVotes;
	private List<Node> platformsAndCountries;
	
	
	public Media(String url, String title, String type, List<String> genres, int releaseYear, String imbdId,
			double imdbAverageRating, int imdbNumVotes, List<Node> platformsAndCountries) {
		super();
		this.url = url;
		this.title = title;
		this.type = type;
		this.genres = genres;
		this.releaseYear = releaseYear;
		this.imbdId = imbdId;
		this.imdbAverageRating = imdbAverageRating;
		this.imdbNumVotes = imdbNumVotes;
		this.platformsAndCountries = platformsAndCountries;
	}
	
	public Media() {
		
	}
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getGenres() {
		return genres;
	}
	public void setGenres(List<String> genres) {
		this.genres = genres;
	}
	public int getReleaseYear() {
		return releaseYear;
	}
	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}
	public String getImbdId() {
		return imbdId;
	}
	public void setImbdId(String imbdId) {
		this.imbdId = imbdId;
	}
	public double getImdbAverageRating() {
		return imdbAverageRating;
	}
	public void setImdbAverageRating(double imdbAverageRating) {
		this.imdbAverageRating = imdbAverageRating;
	}
	public int getImdbNumVotes() {
		return imdbNumVotes;
	}
	public void setImdbNumVotes(int imdbNumVotes) {
		this.imdbNumVotes = imdbNumVotes;
	}

	public List<Node> getPlatformsAndCountries() {
		return platformsAndCountries;
	}

	public void setPlatformsAndCountries(List<Node> platformsAndCountries) {
		this.platformsAndCountries = platformsAndCountries;
	}
	
	
//	
//	public void listTop100() {
//	Media temp = new Media();
//	Media t1 = new Media();
//	Media t2 = new Media();
//	Media t3 = new Media();
//	Media t4 = new Media();
//	Media t5 = new Media();
//	Media t6 = new Media();
//	Media t7 = new Media();
//	Media t8 = new Media();
//	Media t9 = new Media();
//	Media t10 = new Media();
//	TableEntry<String, Media>[] hashTable = mediaRepository.getHashTable();
//	Double max = 0.0;
//	int oldIndex = 0;
//	TableEntry<String, Media> oldEntry = null;
//	int count = 0;
//
//	for (int j = 0; j < 10; j++) {
//		for (int i = 0; i < hashTable.length; i++) {
//			if (hashTable[i] != null) {
//				temp = hashTable[i].getValue();
//				if (temp.getImdbAverageRating() > max) {
//					if (count > 1) {
//						hashTable[oldIndex] = oldEntry;
//					}
//					oldIndex = i;
//					oldEntry = hashTable[i];
//					hashTable[i] = null;
//					t1 = temp;
//					max = t1.getImdbAverageRating();
//					count++;
//				}
//			}
//		}
//		count = 0;
//		oldEntry = null;
//		oldIndex = 0;
//		max = 0.0;
//		System.out.println(t1.getTitle() + " " + t1.getImdbAverageRating());
//		t1 = null;
//	}
	
	
	
	
	

}
