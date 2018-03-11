/*
 * Diana Dominic
 * CSE 373 Project 3 Part 1
 */

package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Searcher.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }
    
    //sort negative input    
    @Test(timeout=SECOND)
    public void testTopKSortThrowsException() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        try {
            Searcher.topKSort(-1, list);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            //do nothing
        }
    }
    
    @Test(timeout=SECOND)
    public void testZeroK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        IList<Integer> result = Searcher.topKSort(0, list);
        assertTrue(result.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testTopKSortString() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("b");
        list.add("a");
        list.add("d");
        list.add("c");
        IList<String> result =  Searcher.topKSort(2, list);
        assertEquals("c", result.get(0));
        assertEquals("d", result.get(1));
    }
    
    @Test(timeout=SECOND)
    public void testKBiggerThanSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 50; i++) {
            list.add(i);
        }
            
        IList<Integer> top = Searcher.topKSort(100, list);
        assertEquals(50, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testTopKSortDuplicates() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 100; i++) {
            list.add(8);
        }

        IList<Integer> top = Searcher.topKSort(50, list);
        assertEquals(50, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(8, top.get(i));
        }
    }
    
    @Test(timeout=SECOND)
    public void testEmptyList() {
        IList<Integer> list = new DoubleLinkedList<>();
            
        IList<Integer> top = Searcher.topKSort(1, list);
        assertTrue(top.isEmpty());
    }
  
}
