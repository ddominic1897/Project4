package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;

    // You're encouraged to add extra fields (and helper methods) though!
    private int totalSize;

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(10);
        totalSize = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    @Override
    public V get(K key) {
        if (this.containsKey(key)) {
            return chains[getHashCode(key)].get(key);
        }
        throw new NoSuchKeyException();
    }

    @Override
    public void put(K key, V value) {
        int hashCode = getHashCode(key);
        if (chains[hashCode] == null) {
            chains[hashCode] = new ArrayDictionary<K, V>();
        }
        
        if (!chains[hashCode].containsKey(key)) {
            totalSize++;
        }
        chains[hashCode].put(key, value);
        checkEfficiency();
        
    }

    private void checkEfficiency() {
        if (totalSize > 10 * chains.length) {
            IDictionary<K, V>[] newChain = this.makeArrayOfChains(chains.length * 2);
            for (KVPair<K, V> pair : this) {
                int index = getHashCode(pair.getKey(), newChain.length);
                if (newChain[index] == null) {
                    newChain[index] = new ArrayDictionary<K, V>();
                }
                newChain[index].put(pair.getKey(), pair.getValue());
            }
            this.chains = newChain;
        }
    }
    
    @Override
    public V remove(K key) {
        if (this.containsKey(key)) {
            totalSize--;
            return chains[getHashCode(key)].remove(key);
        }
        throw new NoSuchKeyException();
    }

    @Override
    public boolean containsKey(K key) {
        int index = getHashCode(key);
        if (chains[index] != null) {
            return chains[index].containsKey(key);
        }
        return false;
    }
    
    private int getHashCode(K key) {
        return getHashCode(key, chains.length);
    }
    
    private int getHashCode(K key, int length) {
        if (key != null) {
            return Math.abs(key.hashCode() % length);
        }
        return 0;
    }

    @Override
    public int size() {
        return totalSize;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Think about what exactly your *invariants* are. Once you've
     *    decided, write them down in a comment somewhere to help you
     *    remember.
     *
     * 3. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 4. Think about what exactly your *invariants* are. As a 
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int current;
        private Iterator<KVPair<K, V>> iter;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.current = 0;
            if (this.chains[current] != null) {
                this.iter = this.chains[current].iterator();
            }
        }

        @Override
        public boolean hasNext() {
            for (int i = current; i < chains.length; i++) {
               if (iter != null && iter.hasNext()) {
                   return true;
               }
           
               if (current == chains.length - 1) {
               return false;
               }
               current++;
               if (chains[current] != null) {
                   iter = chains[current].iterator();
               } else {
                   iter = null;
               }
           }
           return false;
           }
        
        @Override
        public KVPair<K, V> next() {
            if (this.hasNext()) {
                return iter.next();
            }
            throw new NoSuchElementException();
        }
    }
}