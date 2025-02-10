package odev.business;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import odev.entities.Media;
import odev.entities.Node;


import odev.repository.BaseHashDictionary;
import odev.repository.DoubleHashingPolynomialAccumulation;
import odev.repository.DoubleHashingSimpleSummation;
import odev.repository.LinearProbingPolynomialAccumulation;
import odev.repository.LinearProbingSimpleSummation;
import odev.repository.TableEntry;

public class MediaManager implements MediaService {

	// hash algoritmaları arasında kolay geçiş için temel bir hash sınıfı
	BaseHashDictionary<String, Media> mediaRepository = null;

	public MediaManager() {
		this.mediaRepository = new DoubleHashingPolynomialAccumulation<String, Media>();
	}

//	public MediaManager() {
//        this.mediaRepository = new DoubleHashingSimpleSummation<String, Media>();
//  }
	
	public BaseHashDictionary<String, Media> getMediaRepository() {
		return mediaRepository;
	}

//	public void setMediaRepository(BaseHashDictionary<String, Media> mediaRepository) {
//		this.mediaRepository = mediaRepository;
//	}

//	public MediaManager() {
//		this.mediaRepository = new LinearProbingSimpleSummation<String, Media>();
//	}

//	public MediaManager() {
//	this.mediaRepository = new LinearProbingPolynomialAccumulation<String, Media>();
//	}

	


		
	
	// media repository i çağırıp film ekleme işleme
	public void add(String imdbId, Media media) {
		mediaRepository.add(imdbId, media);
	}

	public void print(String imdbId) {
		Media media = mediaRepository.getValue(imdbId);

		if (media == null) {
			System.out.println("Warning: Media with IMDb ID " + imdbId + " not found.");
		} else {
			List<String> genreList = (List<String>) media.getGenres();
			System.out.println("Type: " + media.getType());
			System.out.print("Genre: ");
			for (String genre : genreList) {
				System.out.print(genre + " ");
			}
			System.out.println("\nRelease Year: " + media.getReleaseYear());
			System.out.println("Rating: " + media.getImdbAverageRating());
			System.out.println("Number of Votes: " + media.getImdbNumVotes());

			List<Node> platformsAndCountries = (List<Node>) media.getPlatformsAndCountries();
			if (platformsAndCountries.isEmpty()) {
				System.out.println("No platforms found for " + media.getTitle());
			} else {
				System.out.println(
						"\n" + platformsAndCountries.size() + " platforms found for " + media.getTitle() + "\n");
				for (Node node : platformsAndCountries) {
					System.out.print(node.getPlatform() + " - ");
					for (String country : node.getCountries()) {
						System.out.print(country + " ");
					}
					System.out.println();
				}
			}
		}
	}

	// media repository'den film kaldırma işlemi
	@Override
	public void remove(String imdbId) {
		Media media = mediaRepository.getValue(imdbId);
		if (media == null) {
			System.out.println("Warning: Media with IMDb ID " + imdbId + " not found.");
		} else {
			String name = mediaRepository.getValue(imdbId).getTitle();
			mediaRepository.remove(imdbId);
			System.out.println(name + " is removed");
		}

	}

	@Override
	public boolean isExist(String ImdbID) {
		return mediaRepository.getValue(ImdbID) != null;
	}

	@Override
	public Media get(String imdbId) {
		return mediaRepository.getValue(imdbId);

	}

	public void listTopN(int N) {
		Media temp = new Media();
		Media t1 = new Media();
		// tüm media reposu
		TableEntry<String, Media>[] hashTable = mediaRepository.getHashTable();
		// tüm medialar için geçici bir list repo
		List<TableEntry<String, Media>> tempHashTable =new ArrayList<TableEntry<String,Media>>(Arrays.asList(hashTable));
		Double max = 0.0;
		int oldIndex = 0;
		TableEntry<String, Media> oldEntry = null;
		int count = 0;
		for (int j = 0; j < N; j++) {
			t1 = new Media();
			for (int i = 0; i < hashTable.length; i++) {
				
				if (tempHashTable.get(i) != null) {
					
					temp = tempHashTable.get(i).getValue();
					// her değer için kıyaslama
					if (temp.getImdbAverageRating() > max) {
						if (count > 1) {
							// ilk karşılaştırma sonrasındaki karşılaştırmalarda, eski büyük değerin yerine yerleştirilmesi
							tempHashTable.set(oldIndex, oldEntry);
						}
						oldIndex = i;
						oldEntry = tempHashTable.get(i);
						// en büyük puanlı media listeden çıkarılır
						tempHashTable.set(i, null);
						t1 = temp;
						max = t1.getImdbAverageRating();
						count++;
					}
				}
			}
			// en büyük puanlı diğer mediayı bulmak amacıyla değerler sıfırlanır
			count = 0;
			oldEntry = null;
			oldIndex = 0;
			max = 0.0;
			System.out.println(t1.getTitle() + " " + t1.getImdbAverageRating());
		}

	}
	
	public void StreamsInCountry(String country) {
		// mediaları tutan asıl array
		TableEntry<String, Media>[] hashTable = mediaRepository.getHashTable();
		// üstünde işlem yapılacak olan templist
		List<TableEntry<String, Media>> tempHashTable =new ArrayList<TableEntry<String,Media>>(Arrays.asList(hashTable));
		Iterator<TableEntry<String, Media>> iterator = tempHashTable.iterator();
		int counter = 0;
		Media media = null;
		List<Node> platformsAndCountries = (List<Node>) new LinkedList<Node>();
		
		while (iterator.hasNext()) {
			TableEntry<String, Media> entry = iterator.next(); 
			if (entry != null) {  //entry(imdbId, Media) boşsa geç
				media = entry.getValue();  	
				platformsAndCountries = (List<Node>) media.getPlatformsAndCountries(); // medianın tüm platformları ve ülkeleri
				for (Node node : platformsAndCountries) {   // her node bir platform ve onun ülkelerini temsil eder
					if(node.getCountries().contains(country)) {  // sorgulanan ülke o medianın ülke listesinde var mı?
						System.out.println(media.getTitle());
						counter++;
						break; // diğer platformlarda da aynı film varsa, film adının sadece 1 kez yazılması sağlanır
					}
				}
			}
		}
		System.out.println("Total media count is: " + counter);
	}
	
	
	public void listMediaOnAllPlatforms(int N){
		// mediaları tutan asıl array
		TableEntry<String, Media>[] hashTable = mediaRepository.getHashTable();
		// üstünde işlem yapılacak olan templist
		List<TableEntry<String, Media>> tempHashTable =new ArrayList<TableEntry<String,Media>>(Arrays.asList(hashTable));
		Iterator<TableEntry<String, Media>> iterator = tempHashTable.iterator();
		TableEntry<String, Media> entry = null;
		Media media = null;
		List<Node> platformsAndCountries = (List<Node>) new LinkedList<Node>();
		int counter = 0;
		for (int i = 0; i < tempHashTable.size(); i++) {
			if (tempHashTable.get(i) != null) {
				entry = tempHashTable.get(i);
				media = entry.getValue();
				platformsAndCountries = (List<Node>) media.getPlatformsAndCountries();
				if (platformsAndCountries.size() >= N) {
					System.out.println(media.getTitle());
					counter++;		
				}		
			}
		}
		System.out.println("Total media number that is listed on " + N + " platform  " + counter);
	}	
}
