/*
 * Diana Dominic, Anna Lim
 * CSE 373 Project 4
 */

package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private IDictionary<T, Integer> theNodes;
    private int index;
    private static final int DEFAULT_CAP = 10;

    public ArrayDisjointSet() {
        index = -1;
        pointers = new int[DEFAULT_CAP];
        theNodes = new ChainedHashDictionary<T, Integer>();
    }

    @Override
    /**
     * Creates a new set containing just the given item.
     * The item is internally assigned an integer id (a 'representative').
     *
     * @throws IllegalArgumentException  if the item is already a part of this disjoint set somewhere
     */
    public void makeSet(T item) {
        if (theNodes.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        index++;
        int length = pointers.length;
        //update size if necessary
        if (index >= length) {
            int[] bigger = new int[index * 2]; //pointers.length or index * 2
            for (int i = 0; i < length; i++) {
                bigger[i] = pointers[i];
            }
            pointers = bigger;            
        }
        theNodes.put(item,  index);
        pointers[index] = -1;        
    }

    /**
     * Returns the integer id (the 'representative') associated with the given item.
     *
     * @throws IllegalArgumentException  if the item is not contained inside this disjoint set
     */
    @Override
    public int findSet(T item) {
        //item not in disjoint set
        if (!theNodes.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int targetIndex = theNodes.get(item);
        while(pointers[targetIndex] != -1) { 
            targetIndex = pointers[targetIndex];
        }
        return targetIndex;        
    }
    
    /**
     * Finds the two sets associated with the given items, and combines the two sets together.
     *
     * @throws IllegalArgumentException  if either item1 or item2 is not contained inside this disjoint set
     * @throws IllegalArgumentException  if item1 and item2 are already a part of the same set
     */
    @Override
    public void union(T item1, T item2) {
        //set1 or set2 not contained in disjoint set
        if ((!theNodes.containsKey(item1)) || (!theNodes.containsKey(item2))) {
            throw new IllegalArgumentException();
        }
        int set1 = findSet(item1);
        int set2 = findSet(item2);
        //set1 and set2 part of same set
        if (set1 == set2) {
            throw new IllegalArgumentException();
        }
        //find most efficient order of union
        unionHelper(set1, set2);        
    }
    
    private void unionHelper(int set1, int set2) {
        int set1Rank = Math.abs(pointers[set1]) - 1;
        int set2Rank = Math.abs(pointers[set2]) - 1;
        if (set1Rank >= set2Rank) {
            pointers[set2] = set1;
        } else {
            pointers[set1] = set2;
        }
    }
}