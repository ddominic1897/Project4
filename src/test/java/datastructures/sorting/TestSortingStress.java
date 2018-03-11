/*
 * Diana Dominic
 * CSE 373 Project 3 Part 1
 */

package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;

import static org.junit.Assert.assertTrue;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout=10*SECOND)
    public void testArrayHeapEfficiency() {
         IPriorityQueue<Integer> heap = this.makeInstance();
         int cap = 100000;
         for (int i = cap; i >= 0; i--) {
             heap.insert(i);
             assertEquals(i, heap.peekMin());
             }
        
         for (int i = 0; i <= cap; i++) {
             heap.insert(i);
             assertEquals(0, heap.peekMin());
             }        
    }

    @Test(timeout=10*SECOND)
    public void testSimpleTopKSort() {
        IList<Integer> list = new DoubleLinkedList<>();
        int cap = 100000;
        for (int i = 0; i < cap; i++) {
            list.add(i);
        }
        IList<Integer> top = Searcher.topKSort(cap, list);
        assertEquals(cap, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=10 * SECOND)
    public void testTopKSortKBiggerThanSize() {
        int cap = 100000;
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < cap; i++) {
            list.add(i);
        }
            
        IList<Integer> top = Searcher.topKSort(cap + 1, list);
        assertEquals(cap, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
}
