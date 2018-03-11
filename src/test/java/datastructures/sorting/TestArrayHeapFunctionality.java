package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        assertEquals(1, heap.removeMin());  
        assertEquals(2, heap.size());
        assertEquals(2,  heap.removeMin());
    }
    
    @Test(timeout = SECOND)
    public void testRemoveMinThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            //do nothing
        }
    }
    
    
    @Test(timeout=SECOND)
    public void testRemoveAndPeek() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.removeMin();
        assertEquals(2, heap.peekMin());
        assertEquals(2, heap.size());
    }
    
    @Test(timeout=SECOND)
    public void testPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(2);
        heap.insert(3);
        assertEquals(2, heap.peekMin());
        heap.insert(2);
        assertEquals(2, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testPeekMinThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }
    
    @Test(timeout=SECOND)
    public void testInsert() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(2);
        assertEquals(2, heap.size());
        assertEquals(1, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertNegatives() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(-2);
        heap.insert(5);
        assertEquals(3, heap.size());
        assertEquals(-2, heap.peekMin());
    }
    
    @Test(timeout=SECOND)
    public void testInsertSame() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(1);
        assertEquals(2, heap.size());
        assertEquals(1, heap.removeMin());
        assertEquals(1, heap.removeMin());
    }
   
    @Test(timeout=SECOND)
    public void testInsertAndRemove() {
         IPriorityQueue<Integer> heap = this.makeInstance();
         for (int i = 100; i > 0; i--) {
             heap.insert(i);
             }
         assertEquals(100, heap.size());
         for (int i = 1; i <= 100; i++) {
             assertEquals(i, heap.removeMin());
             }
         assertTrue(heap.isEmpty());
    }
    
    @Test(timeout=SECOND)
    public void testInsertThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            //do nothing
        }
    }
    
    @Test(timeout = SECOND)
    public void testIsEmpty() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertTrue(heap.isEmpty());
    } 
}
