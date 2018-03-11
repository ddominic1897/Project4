/*
 * Diana Dominic
 * CSE 373 
 * Project 1 Part 1
*/
package datastructures.concrete.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

/**
 * See IDictionary for more details on what this class should do
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private Pair<K, V>[] pairs;

    // You're encouraged to add extra fields (and helper methods) though!
    private int size;
    
    public ArrayDictionary() {
        size = 0;
        pairs = makeArrayOfPairs(10);
    }
    
    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);

    }
    /**
     * Returns the value corresponding to the given key.
     *
     * @throws NoSuchKeyException if the dictionary does not contain the given key.
     */
    @Override
    public V get(K key) {
        return pairs[indexOf(key)].value;
    }

    private int indexOf(K key) {
        for (int i = 0; i < size; i++) {
            if (pairs[i].key == key || pairs[i].key.equals(key)) {
                return i;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        if (containsKey(key)) { //existing key
            pairs[indexOf(key)].value = value;
        } else { //new key
            Pair<K, V> newPair = new Pair<K, V>(key, value); 
            if (size >= pairs.length) { 
                Pair<K, V>[] expandPairs = makeArrayOfPairs(pairs.length * 2); 
                System.arraycopy(pairs, 0, expandPairs, 0, pairs.length); 
                pairs = expandPairs; 
            }
            pairs[size] = newPair;
            size++;
        }
    }

    @Override
    public V remove(K key) {
        int index = indexOf(key);
        V value = pairs[index].value;
        for (int i  = index; i < size - 1; i++) {
            pairs[i] = pairs[i+1];
        }
        pairs[size - 1] = null;
        size--;
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        try {
            indexOf(key);
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
    
    /**
     * Returns an iterator that, when used, will yield all key-value pairs
     * contained within this dict.
     */
    @Override
    /**
     * Returns an iterator that, when used, will yield all key-value pairs
     * contained within this dict.
     */
    public Iterator<KVPair<K, V>> iterator() {
        return new DictionaryIterator<KVPair<K, V>>(pairs);
    }
    
    public class DictionaryIterator<T> implements Iterator<KVPair<K, V>> {
        private int currentIndex;
        private Pair<K, V>[] pairs;
        
        public DictionaryIterator(Pair<K, V>[] pairs) {
            this.pairs = pairs;
            currentIndex = 0;
        }
        
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }
        
        public KVPair<K, V> next() {
            if (hasNext()) {
                int oldIndex = currentIndex;
                currentIndex++;
                return new KVPair<K, V>(pairs[oldIndex].key, pairs[oldIndex].value);
            }
            throw new NoSuchElementException();
        }
        
    }
}