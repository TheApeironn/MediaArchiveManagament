package odev;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import odev.business.MediaManager;
import odev.entities.Media;
import odev.entities.Node;

public class Main {

	public static void main(String[] args) {


		System.out.println("Menu\nChose a value:");
		System.out.println(
				"1- Load Dataset\n2- Run 1000 search test\n3- Search for a media item with the ImdbId.\n4- List the top N media according to user votes\n5- List all the media streams in a given country\n6- List the media items that are streaming on all 5 platforms");
		Scanner scanner = new Scanner(System.in);
		MediaManager mediaManager = null;
		Thread thread = new Thread();

		int choice = -1;
		while (choice != 0) {
			try {
				System.out.print("Enter your choice: ");
				choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {
				case 1:// kullanılacak arrayı hazırlayan kısım
					System.out.println("Loading dataset...");
					mediaManager = InitializeTable();
					long totaltime = 0; // Toplam süreyi hesaplamak için değişken
					long time1, time2;

					for (int i = 0; i < 100; i++) {
					    time1 = System.currentTimeMillis(); // Her sorgu başlangıç zamanını al
					    mediaManager.get("tt33081933");    // Belirtilen ID'ye göre veri sorgusu
					    time2 = System.currentTimeMillis(); // Her sorgu bitiş zamanını al
					    totaltime += (time2 - time1);       // Süre farkını toplam süreye ekle
					}

					System.out.println("Total Time for 100 Queries: " + totaltime + " ms");
					break;
				case 2:// verilen giridiye göre var mı yok mu sorgulayan kısım
					System.out.println("Running 1000 search tests...");
					getQuery(mediaManager);
					
					break;
				case 3:// verilen imdbid ye göre tek tek arama kısmı
					System.out.println("Enter IMDb ID to search for a media item:");
					String imdbId = scanner.next(); // Get IMDb ID input
					System.out.println("Searching for media item with IMDb ID: " + imdbId);
					Search(mediaManager, imdbId);
					break;
				case 4:// en üstteki n elemanı gösteren kısım
					System.out.println("How many movies would you like to see at the top:");
					int N = scanner.nextInt();
					scanner.nextLine();
					System.out.println("Searching for top " + N + " film sorted by Imdb Point");
					listTopN(mediaManager, N);
					break;
				case 5:
					System.out.println("Enter country to list media streams:");
					String country = scanner.nextLine(); // Get country input
					System.out.println("Listing media streams in " + country);
					StreamsInCountry(mediaManager, country.toUpperCase());
					break;
				case 6:
					System.out.println("Listing media items streaming on all 5 platforms...");
					int C = scanner.nextInt(); // Get country input
					scanner.nextLine();
					System.out.println("Listing media streams in " + C);
					NumberOfPlatforms(mediaManager, C);
					break;
				case 0:
					System.out.println("Exiting the program...");
					break;
				default:
					System.out.println("Invalid choice. Please choose a valid option.");
					break;
				}

				// Adding a delay before re-displaying the menu
				thread.sleep(2000);
			} catch (InterruptedException e) {
				System.out.println("An error occurred while waiting: " + e.getMessage());
			} catch (Exception e) {
				System.out.println("An error occurred: " + e.getMessage());
				scanner.nextLine(); // Enter tuşunu temizler.
				String country = scanner.nextLine();
				
			}

			System.out.println("Menu\nChose a value:");
			System.out.println(
					"1- Load Dataset\n2- Run 1000 search test\n3- Search for a media item with the ImdbId.\n4- List the top N media according to user votes\n5- List all the media streams in a given country\n6- List the media items that are streaming on all 5 platforms");
		}
	}

	public static MediaManager InitializeTable() {

		// list which will store the films in text
		MediaManager mediaManager = new MediaManager();
		String path = "datasetconverted.txt";
		File file = new File(path);
		BufferedReader bufferedReader = null;
		long firstTime = 0;
		long lastTime = 0;
		// every line in text
		String line = null;
		int count = 1;
		List<Node> platformsAndCountries = new LinkedList<Node>();// platformları ve ülkeleri tutacak olan linked list
		List<String> availableCountries = new ArrayList<String>();// bir medianin o platformdaki ülke listesi
		Media media = new Media();

		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			firstTime = System.currentTimeMillis();
			line = bufferedReader.readLine();
			while ((line = bufferedReader.readLine()) != null) {
				String[] data = line.split("\t\t");
				if (data.length == 10) {
					// okunan satırın verilerini media repository' e kayıt etme işlemi
					String url = data[0];
					String title = data[1];
					String type = data[2];
					List<String> genres = Arrays.asList(data[3].split(", "));
					// yıl değeri atanmayan verileri kontrol etme
					int releaseYear = 0;
					if (data[4] != null && !data[4].isEmpty()) {
						releaseYear = Integer.parseInt(data[4]);
					} else {
						releaseYear = 0;
					}
					String imdbId = data[5];
					// ortalaması olmayan değerler kotrol ediliyor
					double imdbAverageRating = 0.0;
					if (data[6] != null && !data[6].isEmpty()) {
						imdbAverageRating = Double.parseDouble(data[6].replace(",", "."));
					} else {
						imdbAverageRating = 0.0;
					}
					// imdbNumVotes içinin boş olup olmadığını kontrol et
					int imdbNumVotes = 0;//////
					if (data[7] != null && !data[7].isEmpty()) {
						imdbNumVotes = Integer.parseInt(data[7]);
					}
					if (data[9] != null && !data[9].isEmpty()) {
						availableCountries = new ArrayList<>(Arrays.asList(data[9].split(", ")));
					} else {
						availableCountries = new ArrayList<>();
					}
					String platform = data[8];
					Node newNode = new Node(platform, availableCountries);

					if (mediaManager.isExist(imdbId)) {
						Media existMedia = mediaManager.get(imdbId);
						existMedia.getPlatformsAndCountries().add(newNode);
					} else {
						platformsAndCountries = new LinkedList<>();
						platformsAndCountries.add(newNode);

						Media newMedia = new Media(url, title, type, genres, releaseYear, imdbId, imdbAverageRating,
								imdbNumVotes, platformsAndCountries);
						mediaManager.add(imdbId, newMedia);
					}
					media.setUrl(url);
					media.setTitle(title);
					media.setType(type);
					media.setGenres(genres);
					media.setReleaseYear(releaseYear);
					media.setImbdId(imdbId);
					media.setImdbAverageRating(imdbAverageRating);
					media.setImdbNumVotes(imdbNumVotes);
					media.setPlatformsAndCountries(platformsAndCountries);

					count++;
				} else {
					count++;
				}
			}
			lastTime = System.currentTimeMillis();

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e2) {
				System.out.println("error closing file: " + e2.getMessage());
			}
		}
		System.out.println("Media count in dataset: " + count);
		System.out.println("Collision: " + mediaManager.getMediaRepository().getNumberOfCollision());
		System.out.println("süre " + (lastTime - firstTime));

		count = 1;
		mediaManager.getMediaRepository().setNumberOfCollision(0);
		return mediaManager;
	}

	public static void Search(MediaManager mediaManager, String imdbID) {
		mediaManager.print(imdbID);
	}

	public static void listTopN(MediaManager mediaManager, int N) {
		mediaManager.listTopN(N);
	}

	public static void StreamsInCountry(MediaManager mediaManager, String country) {
		mediaManager.StreamsInCountry(country);
	}

	public static void NumberOfPlatforms(MediaManager mediaManager, int NPlatform) {
		mediaManager.listMediaOnAllPlatforms(NPlatform);
	}

	public static void getQuery(MediaManager mediaManager) {
		BufferedReader bufferedReader = null;
		String path = "search.txt";
		File file = new File(path);
		String singleQuery = null;
		int existMedia = 0;
		int allMedia = 0;

		try {
			bufferedReader = new BufferedReader(new FileReader(file));
			while ((singleQuery = bufferedReader.readLine()) != null) {
				if (mediaManager.isExist(singleQuery)) {
//					mediaManager.print(singleQuery);
					existMedia++;
					allMedia++;
				} else {
					allMedia++;
				}

			}
			
			System.out.println(existMedia + " of " + allMedia + " is exist in the list");

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (Exception e2) {
				System.out.println("error closing file: " + e2.getMessage());
			}
		}
	}

}
