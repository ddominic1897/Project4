/*
 /*
 * Diana Dominic
 * CSE 373 
 * Project 1 Part 1
*/

package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (isEmpty()) {
            front = new Node<T>(item);
            back = front;
        } else {
            Node<T> newNode = new Node<T>(back, item, null);
            back.next = newNode;
            back = newNode;          
        }
       size++;
    }
    
    @Override
    public T remove() {
        if (isEmpty()) {
            throw new EmptyContainerException();
        } 
        T item = back.data;
        back = back.prev;
        
        if (back == null) { 
            front = null;
        } else {
            back.next = null;
        }
        size--;
        return item;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        } 
        return getNode(index).data; 
        }
    
    private Node<T> getNode(int index) {
        if (index < size / 2) { //traverse from front
            Node<T> temp = front;
            for (int i = 0; i < index; i++) {
                temp = temp.next;
            }
            return temp;
        } else { //traverse from back
            Node<T> temp = back;
            for (int i = size - 1; i > index; i--) {
                temp = temp.prev;
            }
            return temp;
        }
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
       delete(index); //shift nodes left
       insert(index, item); //change data and shift back
        
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= this.size() + 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size) { //insert end
            add(item);
        } else if (index == 0) { //insert front
            Node<T> temp = new Node<T>(null, item, front);
            front.prev = temp;  
            front = temp;
            size++;
        } else { //middle
            Node<T> temp = getNode(index); 
            Node<T> newNode = new Node<T>(temp.prev, item, temp);
            temp.prev.next = newNode;
            temp.prev = newNode;
            size++;
        }
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        if (index == (this.size - 1)) { 
            return remove();
        } else if (index == 0) { 
           T temp = front.data;
           front = front.next;
           front.prev = null;
           size--;
           return temp;
        } else {
            Node<T> temp = getNode(index);
            temp.prev.next = temp.next;
            temp.next.prev = temp.prev;
            size--;
            return temp.data;
        }
    }

    @Override
    public int indexOf(T item) {
        int index = 0;
        Node<T> temp = front;
        while (temp != null) { 
            if (temp.data == item || temp.data.equals(item)) {
                return index;
            }
            temp = temp.next;
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
       return (indexOf(other) != -1); 
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return (current != null);
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T temp = current.data;  
            current = current.next;
            return temp;
        }
    }
}