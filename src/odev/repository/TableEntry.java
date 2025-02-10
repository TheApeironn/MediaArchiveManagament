package odev.repository;


public class TableEntry<K, V> {
	private K key;
	private V value;
	private States state; // Flags whether this entry is in the hash table

	public enum States {
		CURRENT, REMOVED
	} // Possible values of state

	public TableEntry(K searchKey, V dataValue) {
		key = searchKey;
		value = dataValue;
		state = States.CURRENT;
	}

    public K getKey() { return key; }
    public V getValue() { return value; }
    public void setValue(V newValue) { value = newValue; }
    public boolean isIn() { return state == States.CURRENT; }
    public boolean isRemoved() { return state == States.REMOVED; }
    public void setToRemoved() { state = States.REMOVED; }
	
	
}