package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_CAPACITY = 10;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private int size;
    
    public ArrayHeap() {
       heap = this.makeArrayOfT(DEFAULT_CAPACITY);
       size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int newSize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[newSize]);
    }

    /**
     * Removes and return the smallest element in the queue.
     *
     * If two elements within the queue are considered "equal"
     * according to their compareTo method, this method may break
     * the tie arbitrarily and return either one.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    @Override
    public T removeMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        T result = heap[0];
        size--;
        heap[0] = heap[size];
        heap[size] = null;
        removeMinHelper(0);
        return result;
        
    }

    private void removeMinHelper(int index) {
        int min = index;
        int child;
        for (int i = 1; i <= NUM_CHILDREN; i++) {
            child = NUM_CHILDREN * index + i;
            if (child < size && (heap[child].compareTo(heap[min]) < 0)) {
                min = child;
            }
        }
        
        //recurse to percolate down
        if (min != index) {
            T temp = heap[index];
            heap[index] = heap[min];
            heap[min] = temp;
            removeMinHelper(min);               
        }        
    }

    @Override
    public T peekMin() {
        if (size == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    /**
     * Inserts the given item into the queue.
     *
     * @throws IllegalArgumentException  if the item is null
     */
    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        checkCapacity();
        heap[size] = item;
        insertHelper(size);
        size++;
    }
    
    private void checkCapacity() {
        if (size == heap.length) {
            T[] newHeap = makeArrayOfT(size * 2);
            for (int i = 0; i < size; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
    }
    private void insertHelper(int index) {
        int parent = (index - 1) / NUM_CHILDREN;
        if (heap[parent].compareTo(heap[index]) > 0) {
            T temp = heap[parent];
            heap[parent] = heap[index];
            heap[index] = temp;
            insertHelper(parent);
        }
    }

    @Override
    public int size() {
        return this.size;
    }
    
    public void remove(T item) {
        throw new UnsupportedOperationException();
    }
}
