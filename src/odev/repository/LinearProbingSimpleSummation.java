package odev.repository;

public class LinearProbingSimpleSummation<K, V> extends BaseHashDictionary<K, V> {

	
	@Override
    protected int hash(K key) {
        int sum = 0;
        for (char c : key.toString().toCharArray()) {
            sum += c; // Basit toplama (Simple Summation)
        }
        return sum % getHashTable().length;
    }
	
	@Override
	protected int probe(int index, K key) {// yeni değer eklemek için
	    // hashTable getter'ı ile erişim sağlanabilir
		int collision = 0;
	    while (getHashTable()[index] != null && getHashTable()[index].isIn() && !getHashTable()[index].getKey().equals(key)) {
	        index = (index + 1) % getHashTable().length;
	        collision++;
	    }
	    if(collision > 0) {
	    	setNumberOfCollision( getNumberOfCollision()+collision);
	    }
	    return index;
	}

	@Override
	protected int locate(int index, K key) {// remove or getvalue için
	    // hashTable getter'ı ile erişim sağlanabilir
		 // Loop to find the key (if present) or an empty slot
	    int i = index;
	    while (getHashTable()[i] != null) {
	    	if (getHashTable()[i].getKey().equals(key) && getHashTable()[i].isIn()) {
	            return i; // Key found
	        }
	        i = (i + 1) % getHashTable().length; // Move to the next index
	    }
	    return -1;
	}
	
//	The private method locate. Before we look at add, let’s implement the method locate that both
//	getValue and remove invoke. The method looks for the given search key along the probe sequence
//	that begins at hashTable[index], where index is the key’s hash index. Recall that the search must
//	ignore entries that are in the removed state. The search continues until it locates either key or null.
//	To follow the probe sequence, locate must implement a particular open addressing scheme
//	to resolve collisions. For simplicity, we will implement linear probing. The following algorithm
//	summarizes our approach:
//	Algorithm locate(index, key)
//	// Returns either the index of the entry containing key or -1 if no such entry is found.
//	while (key is not found and hashTable[index] is not null)
//	{
//	 if (hashTable[index] references an entry that is in the dictionary and contains key)
//	 Exit loop
//	 else
//	 index = next probe index
//	}
//	if (key is found)
//	 return index
//	else
//	 return -1
//
//	A Dictionary Implementation That Uses Hashing 661
//	The implementation of locate now follows from this pseudocode:
//	// Precondition: checkInitialization has been called.
//	private int locate(int index, K key)
//	{
//	 boolean found = false;
//	 while ( !found && (hashTable[index] != null) )
//	 {
//	 if ( hashTable[index].isIn() &&
//	 key.equals(hashTable[index].getKey()) )
//	 found = true; // Key found
//	 else // Follow probe sequence
//	 index = (index + 1) % hashTable.length; // Linear probing
//	 } // end while
//	 // Assertion: Either key or null is found at hashTable[index]
//	 int result = -1;
//	 if (found)
//	 result = index;
//	 return result;
//	} 
//	private int probe(int index, K key) {
//	    int removedStateIndex = -1; // İlk silinmiş konumu saklamak için -1 başlangıç değeri
//
//	    while (hashTable[index] != null) { // Boş olmayan bir konum olduğu sürece aramaya devam et
//	        if (hashTable[index].isIn()) { // Geçerli bir sözlük girişi
//	            if (hashTable[index].getKey().equals(key)) { // Eğer anahtar eşleşiyorsa
//	                return index; // Aranan anahtar bulundu
//	            }
//	            // Eğer anahtar farklı ise bir sonraki prob konumuna geç
//	            index = (index + 1) % hashTable.length;
//	        } else { // Silinmiş bir konum
//	            if (removedStateIndex == -1) { // İlk silinmiş konum
//	                removedStateIndex = index; // removedStateIndex bu konum olarak ayarlanır
//	            }
//	            index = (index + 1) % hashTable.length;
//	        }
//	    }
//
//	    // Eğer anahtar bulunmadıysa, ilk silinmiş konum varsa orayı, yoksa mevcut index'i döndür
//	    return (removedStateIndex != -1) ? removedStateIndex : index;
//	}


}
