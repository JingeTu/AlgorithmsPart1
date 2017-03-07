import java.util.Iterator;

/**
 * Created by jg on 26/01/2017.
 */
public class Deque<Item> implements Iterable<Item>  {

    private class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> prev;
    }

    private Node<Item> first = null;
    private Node<Item> last = null;
    private int size = 0;

    public Deque() {                          // construct an empty deque

    }

    public boolean isEmpty() {                // is the deque empty?
        return size == 0;
    }

    public int size() {                       // return the number of items on the deque
        return size;
    }

    public void addFirst(Item item) {         // add the item to the front
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

    public void addLast(Item item) {          // add the item to the end
        if (item == null)
            throw new java.lang.NullPointerException();
        if (size == 0) {
            last = new Node<Item>();
            last.item = item;
            last.next = null;
            last.prev = null;
            first = last;
        }
        else {
            Node<Item> oldLast = last;
            last = new Node<Item>();
            last.item = item;
            last.next = null;
            last.prev = oldLast;
            oldLast.next = last;
        }
        size++;
    }

    public Item removeFirst() {               // remove and return the item from the front
        if (size == 0)
            throw new java.util.NoSuchElementException();
        Node<Item> oldFirst = first;
        first = first.next;
        if (first != null)
            first.prev = null;
        size--;
        if (size == 0) {
            last = null;
        }
        return oldFirst.item;
    }

    public Item removeLast() {                // remove and return the item from the end
        if (size == 0)
            throw new java.util.NoSuchElementException();
        Node<Item> oldLast = last;
        last = oldLast.prev;
        if (last != null)
            last.next = null;
        size--;
        if (size == 0) {
            first = null;
        }
        return oldLast.item;
    }

    @Override
    public Iterator<Item> iterator() {

        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node<Item> current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        @Override
        public Item next() {
            if (current == null)
                throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        Deque<String> deqStr = new Deque<String>();
        deqStr.addLast("time");
        deqStr.addLast("no");
        deqStr.addLast("see");
        deqStr.addFirst("long");
        deqStr.removeFirst();
        deqStr.removeFirst();
        deqStr.removeFirst();
        deqStr.removeFirst();

        Iterator<String> iter = deqStr.iterator();
        while(iter.hasNext()) {
            System.out.println(iter.next());
        }
    }
}
