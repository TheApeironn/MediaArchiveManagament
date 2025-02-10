package odev.business;
import odev.entities.*;

public interface MediaService {
	
	public boolean isExist(String ImdbID);// media var mı yok mu onu kontrol eder
	public void add(String  imdbId, Media media);//yeni media ekler
	public Media get(String imdbId);// sorgulanan id nin media sını verir
	public void remove(String imdbId);// verilen id'li bir media varsa onu siler
	public void print(String imdbId);// verilen id'nin bilgilerini yazdırır
	public void listTopN(int N);// listenin en üstündeki N mediayı sıralar
	public void StreamsInCountry(String country);// istenilen ülkedeki yayınları getirir
	public void listMediaOnAllPlatforms(int N);//verilen sayı kadar platformda listelenen filmleri getirir
	
	
}
