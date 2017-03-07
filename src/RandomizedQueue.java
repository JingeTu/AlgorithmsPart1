import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

/**
 * Created by jg on 26/01/2017.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> prev;
    }


    private Node<Item> first = null;
    private Node<Item> last = null;
    private int size = 0;

    public RandomizedQueue() {                 // construct an empty randomized queue

    }

    public boolean isEmpty() {                 // is the queue empty?
        return size == 0;
    }

    public int size() {                        // return the number of items on the queue
        return size;
    }

    public void enqueue(Item item) {           // add the item
        if (item == null)
            throw new java.lang.NullPointerException();
        if (size == 0) {
            first = new Node<Item>();
            first.item = item;
            first.next = null;
            first.prev = null;
            last = first;
        }
        else {
            Node<Item> oldFirst = first;
            first = new Node<Item>();
            first.item = item;
            first.prev = null;
            first.next = oldFirst;
            oldFirst.prev = first;
        }
        size++;
    }

    public Item dequeue() {                    // remove and return a random item
        int ran = StdRandom.uniform(0, size);
        int i;
        Node<Item> p;
        if (ran < size / 2) {
            p = first;
            i = 0;
            while (i < ran) {
                i++;
                p = p.next;
            }
        }
        else {
            p = last;
            i = size - 1;
            while (i > ran) {
                i--;
                p = p.prev;
            }
        }
        size--;
        if (p.prev != null)
            p.prev.next = p.next;
        else  // p == first
            first = first.next;
        if (p.next != null)
            p.next.prev = p.prev;
        else // p == last
            last = last.prev;
        if (size == 0) {
            first = null;
            last = null;
        }
        return p.item;
    }

    public Item sample() {                     // return (but do not remove) a random item
        int ran = StdRandom.uniform(0, size);
        int i = 0;
        Node<Item> p = first;
        while (i < ran) {
            i++;
            p = p.next;
        }
        return p.item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        int[] permutation = null;
        int index = 0;

        public ListIterator() {
            permutation = new int[size];
            for (int i = 0; i < size; i++) {
                permutation[i] = i;
            }
            for (int i = 0; i < size; i++) {
                int temp = permutation[i];
                int ran = StdRandom.uniform(0, i + 1);
                permutation[i] = permutation[ran];
                permutation[ran] = temp;
            }
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (index >= size)
                throw new java.util.NoSuchElementException();
            int i = 0;
            Node<Item> p = first;
            int ran = permutation[index];
            if (ran < size / 2) {
                p = first;
                i = 0;
                while (i < ran) {
                    i++;
                    p = p.next;
                }
            }
            else {
                p = last;
                i = size - 1;
                while (i > ran) {
                    i--;
                    p = p.prev;
                }
            }
            index++;
            return p.item;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> raq = new RandomizedQueue<String>();
        raq.enqueue("A");
        raq.enqueue("B");
        raq.enqueue("C");
        raq.enqueue("D");
        raq.enqueue("E");
        raq.enqueue("F");
        raq.enqueue("G");
        raq.enqueue("H");
        raq.enqueue("I");
        raq.enqueue("J");
        raq.enqueue("K");
        raq.enqueue("L");
        raq.enqueue("M");

        Iterator<String> iter = raq.iterator();
        while (iter.hasNext()) {
            System.out.println(iter.next());
        }

        System.out.println("***************************");

        int i = 20;
        while (i-- != 0) {
            System.out.println(raq.sample());
        }

        System.out.println("***************************");

        while (!raq.isEmpty()) {
            System.out.println(raq.dequeue());
        }
    }
}
