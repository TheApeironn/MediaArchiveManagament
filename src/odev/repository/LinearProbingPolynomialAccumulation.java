package odev.repository;

public class LinearProbingPolynomialAccumulation<K, V> extends BaseHashDictionary<K, V>{


	protected int hash(K key) {
	    int hashCode = 0;
	  
	    String keyString = key.toString();
	    int prime = 31; // A prime number used for polynomial accumulatio
	    // horner rule not to cause for stack overflow error
	    for (int i = 0; i < keyString.length(); i++) {
	    	hashCode = (( hashCode * prime ) + keyString.charAt(i)) % getHashTable().length;
		}
	    return hashCode;
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
	    	setNumberOfCollision(getNumberOfCollision()+collision);
	    }
	    return index;
	}
	
	
	@Override
	protected int locate(int index, K key) {// remove or getvalue için
	    // Loop to find the key (if present) or an empty slot
	    int i = index;
	    while (getHashTable()[i] != null) {
	        if (getHashTable()[i].getKey().equals(key) && getHashTable()[i].isIn()) {
	            return i; // Key found
	        }
	        i = (i + 1) % getHashTable().length; // Move to the next index
	    }
	    return -1; // Key not found
	}

	

}
