import edu.princeton.cs.algs4.StdIn;

import java.util.Iterator;

/**
 * Created by jg on 26/01/2017.
 */
public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> raq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            raq.enqueue(StdIn.readString());
        }
        Iterator<String> iter = raq.iterator();
        for (int i = 0; i < k; ++i) {
            System.out.println(iter.next());
        }
        System.out.println(Permutation.class.toString());
    }
}
