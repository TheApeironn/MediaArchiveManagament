package odev.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;



public abstract class BaseHashDictionary<K, V> implements DictionaryInterface<K, V>  {

	private int numberOfEntries;
	private static final int DEFAULT_CAPACITY = 5; // Must be prime
	private static final int MAX_CAPACITY = 10000;
	private TableEntry<K, V>[] hashTable;
	private int tableSize; // Must be prime
	private static final int MAX_SIZE = 2 * MAX_CAPACITY;
	private boolean initialized = false;
	private static final double MAX_LOAD_FACTOR = 0.5;
	private static int numberOfCollision = 0;

	public BaseHashDictionary() {
		this(DEFAULT_CAPACITY);
	}

	public BaseHashDictionary(int initialCapacity) {
		checkCapacity(initialCapacity);
		numberOfEntries = 0; // Dictionary is empty

		// Set up hash table:
		// Initial size of hash table is same as initialCapacity if it is prime;
		// otherwise increase it until it is prime size
		int tableSize = getNextPrime(initialCapacity);
		checkSize(tableSize); // Check for max array size

		// The cast is safe because the new array contains null entries
		@SuppressWarnings("unchecked")
		TableEntry<K, V>[] temp = (TableEntry<K, V>[]) new TableEntry[tableSize];
		hashTable = temp;
		initialized = true;
	}


	public int getNumberOfEntries() {
		return numberOfEntries;
	}

	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}

	public TableEntry<K, V>[] getHashTable() {
		return hashTable;
	}

	public void setHashTable(TableEntry<K, V>[] hashTable) {
		this.hashTable = hashTable;
	}

	public int getTableSize() {
		return tableSize;
	}

	public void setTableSize(int tableSize) {
		this.tableSize = tableSize;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}
	
	public int getNumberOfCollision() {
		return numberOfCollision;
	}
	public void setNumberOfCollision(int numberOfCOllision) {
		this.numberOfCollision = numberOfCOllision;
	}
	
	
	
	
	 protected abstract int hash(K key);
	 protected abstract int probe(int index, K key);
	 protected abstract int locate(int index, K key);
	 
	 public V getValue(K key) {
			checkInitialization();
			V result = null;
			int index = hash(key); // Get the initial hash index
			index = locate(index, key);
			if (index != -1)
				result = hashTable[index].getValue(); // Key found; get value
			// Else key not found; return null
			return result;
		}
	 
	 
	 public V remove(K key) {
			checkInitialization();
			V removedValue = null;
			int index = hash(key); // Get the initial hash index																	// resolution
			index = locate(index, key);
			if (index != -1) { // Key found; flag entry as removed and return its value
				removedValue = hashTable[index].getValue();
				hashTable[index].setToRemoved();
				numberOfEntries--;
			}
			return removedValue;
		}
	 
	 
	 public V add(K key, V value) {
			checkInitialization();
			if ((key == null) || (value == null))
				throw new IllegalArgumentException();
			else {
				V oldValue = null; // Value to return
				int index = hash(key); // Get the initial hash index
				index = probe(index, key); // Check for and resolve collision
				// Assertion: index is within legal range for hashTable
				assert (index >= 0) && (index < hashTable.length);
				if ((hashTable[index] == null) || hashTable[index].isRemoved()) { // Key not found, so insert new entry
					hashTable[index] = new TableEntry<>(key, value);
					numberOfEntries++;
					oldValue = null;
				}
				else { // Key found; get old value for return and then replace it
					oldValue = hashTable[index].getValue();
					hashTable[index].setValue(value);
				} // end if
					// Ensure that hash table is large enough for another add
				if (isHashTableTooFull())
					enlargeHashTable();
				return oldValue;
			}
		}
	 
	 
	 @Override
	    public boolean isEmpty() {
	        return numberOfEntries == 0;
	    }

	    @Override
	    public int getSize() {
	        return numberOfEntries;
	    }

	    @Override
	    public void clear() {
	        // Clear işlemi
	        for (int i = 0; i < hashTable.length; i++) {
	            hashTable[i] = null;
	        }
	        numberOfEntries = 0;
	    }

	    @Override
	    public boolean contains(K key) {
	        return getValue(key) != null;
	    }

	    @Override
	    public Iterator<K> getKeyIterator() {
	        List<K> keys = new ArrayList<>();
	        for (TableEntry<K, V> entry : hashTable) {
	            if (entry != null && entry.isIn()) {
	                keys.add(entry.getKey());
	            }
	        }
	        return keys.iterator();
	    }

	    @Override
	    public Iterator<V> getValueIterator() {
	        List<V> values = new ArrayList<>();
	        for (TableEntry<K, V> entry : hashTable) {
	            if (entry != null && entry.isIn()) {
	                values.add(entry.getValue());
	            }
	        }
	        return values.iterator();
	    }
	 
	 
	 public void enlargeHashTable()
	 {
	  TableEntry<K, V>[] oldTable = hashTable;
	  int oldSize = hashTable.length;
	  tableSize = getNextPrime(oldSize + oldSize);
	  // The cast is safe because the new array contains null entries
	  @SuppressWarnings("unchecked")
	  TableEntry<K, V>[] temp = (TableEntry<K, V>[])new TableEntry[tableSize];
	  hashTable = temp;
	  numberOfEntries = 0; // Reset number of dictionary entries, since
	  // it will be incremented by add during rehash
	  // Rehash dictionary entries from old array to the new and bigger
	  // array; skip both null locations and removed entries
	  for (int index = 0; index < oldSize; index++)
	  {
	  if ( (oldTable[index] != null) && oldTable[index].isIn() )
	  add(oldTable[index].getKey(), oldTable[index].getValue());
	  } // end for
	 }
	 
	 public boolean isHashTableTooFull() {
			return numberOfEntries > (hashTable.length * MAX_LOAD_FACTOR);
		}

		public void checkCapacity(int capacity) {
			if (capacity < 1 || capacity > MAX_CAPACITY)
				throw new IllegalArgumentException("Invalid capacity: " + capacity);
		}

		public void checkSize(int size) {
			if (size > MAX_SIZE)
				throw new IllegalArgumentException("Size exceeds maximum: " + size);
		}

		public void checkInitialization() {
			if (!initialized)
				throw new IllegalStateException("Dictionary is not initialized.");
		}

		public int getNextPrime(int value) {
			int nextPrime = value;
			boolean found = false;
			while (!found) {
				nextPrime++;
				if (isPrime(nextPrime))
					found = true;
			}
			return nextPrime;
		}

		public boolean isPrime(int number) {

			boolean prime = true;
			for (int divisor = 2; divisor < number; divisor++) {
				if (number % divisor == 0) {
					prime = false;
					break;
				}
			}
			return prime;
		}
		
		private class KeyIterator implements Iterator<K> {
	        private int currentIndex = 0;

	        @Override
	        public boolean hasNext() {
	            while (currentIndex < hashTable.length) {
	                if (hashTable[currentIndex] != null && hashTable[currentIndex].isIn()) {
	                    return true;
	                }
	                currentIndex++;
	            }
	            return false;
	        }

	        @Override
	        public K next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            return hashTable[currentIndex++].getKey();
	        }

	        @Override
	        public void remove() {
	            throw new UnsupportedOperationException();
	        }
	    }

	    private class ValueIterator implements Iterator<V> {
	        private int currentIndex = 0;

	        @Override
	        public boolean hasNext() {
	            while (currentIndex < hashTable.length) {
	                if (hashTable[currentIndex] != null && hashTable[currentIndex].isIn()) {
	                    return true;
	                }
	                currentIndex++;
	            }
	            return false;
	        }

	        @Override
	        public V next() {
	            if (!hasNext()) throw new NoSuchElementException();
	            return hashTable[currentIndex++].getValue();
	        }

	        @Override
	        public void remove() {
	            throw new UnsupportedOperationException();
	        }
	    }
}







	