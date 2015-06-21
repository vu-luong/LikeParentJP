package com.likeparentjp.utils;
/**
 * class <tt>ArrayBasedStack</tt> extends abstract class <tt>Stack</tt> of generic items
 * <p>
 * It supports the usual <em>push</em> and <em>pop</em> operations
 * <p>
 * This implement uses and Node to store data, each node have an reference to next node in linked
 * list  
 * @author NhanTQD
 * @author VuLA
 *
 * @param <Item> type of item store on stack
 */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Stack<Item> implements Iterable<Item>{
    private Node<Item> first;
    private int size;
    
    private static class Node<Item> {
        Item item;
        Node<Item> next;
    }
    
    public Stack() {
        //just clear the stack, set first Node to null
        clear();
    }

    public void clear() {
        //garbage collector will free memory after set first to null
        size = 0;
        first = null;
    }
    

    public void push(Item item) {
        Node<Item> oldFirst = first;    //save oldFirst 
        first = new Node<Item>();
        first.item = item;              //store new item into new first node
        first.next = oldFirst;          //point to next node
        size++;                         //increase size
    }

    public Item pop() {
        if (size == 0) throw new NoSuchElementException("stack under flow");
        Item item = first.item;         //save item to return
        first = first.next;             //point to next node
        size--;                         //decrease size
        
        return item;
    }

    public Item topValue() {
        if (size == 0) throw new NoSuchElementException("stack under flow");
        return first.item;
    }

    public int length() {
        return size;
    }

    @Override
    public Iterator<Item> iterator() {
        // TODO Auto-generated method stub
        return new StackIterator<Item>(first);
    }
    
    @SuppressWarnings("hiding")
    private class StackIterator<Item> implements Iterator<Item> {
        private Node<Item> current;
        
        public StackIterator(Node<Item> first) {
            current = first;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() { throw new UnsupportedOperationException(); }
        
    }
    
}
