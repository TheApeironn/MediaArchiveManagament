package odev.repository;

public class DoubleHashingSimpleSummation<K, V> extends BaseHashDictionary<K, V> {

	@Override
	protected int hash(K key) {
		int hashCode = 0;
		String keyString = key.toString();
		for (int i = 0; i < keyString.length(); i++) {
			hashCode += keyString.charAt(i);
		}
		return hashCode % getHashTable().length;
	}

	protected int hash2(int key) {
		int prime = 13007; // 7.001 11.117 13.007
		int result = prime - (key % prime);

		if (result == 0) {
			prime = getNextPrime(prime);
			result = prime - (key % prime);
		}
		return result;
	}

	@Override
	protected int probe(int index, K key) {// yeni değer eklemek için

		int hash1 = index;
		int doubleHashCounter = 0;
		// index null mu, içinde deger var mı,
		// içindeki değer aynı mı (daha önceden eklenmiş mi ) diye bakılır
		while (getHashTable()[index] != null && getHashTable()[index].isIn()
				&& !getHashTable()[index].getKey().equals(key)) {
			index = (hash1 + doubleHashCounter * hash2(hash1)) % getHashTable().length;
			// doubleHashCounter aşırı büyük olduğu için overflow oluyor ve negatif değer
			// döndürmeye başlıyor
			// bunu tespit etmek için kullanıldı
			// yukarıda bahsedilen taşmayı önlemek amacıyla tablo yeniden boyutlandırılır
			if (doubleHashCounter >= getHashTable().length) {
//				System.out.println("taşma tespit edildi" + doubleHashCounter);
				enlargeHashTable();
				return probe(hash(key), key); // Yeniden dene
			}
			doubleHashCounter++;
		}
		setNumberOfCollision(getNumberOfCollision() + doubleHashCounter);
		return index;// bulunan index boşsa // available durumda ise
		// daha önceden aynı değer eklenmişse direkt orayı döndür
	}

	@Override
	protected int locate(int index, K key) {// remove or getvalue için
		int hash1 = index;
		int doubleHashCounter = 0;
		while (getHashTable()[hash1] != null) {
			if (getHashTable()[hash1].isIn() && key.equals(getHashTable()[hash1].getKey())) {
				return hash1;
			}
			hash1 = (index + doubleHashCounter * hash2(index)) % getHashTable().length;
			if (doubleHashCounter >= getHashTable().length) {
				enlargeHashTable();
			}
			doubleHashCounter++;
		}

		return -1; // not found, return -1
	}
}
