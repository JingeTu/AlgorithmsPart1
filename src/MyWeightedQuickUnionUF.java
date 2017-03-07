/**
 * Created by jg on 24/01/2017.
 */
public class MyWeightedQuickUnionUF {
    private int N;
    private int[] id;
    private int[] sz;

    public MyWeightedQuickUnionUF(int N) {
        this.N = N;
        id = new int[N];
        sz = new int[N];
        for (int i = 0; i < N; ++i) {
            id[i] = i;
            sz[i] = 1;
        }
    }

    public void union(int p, int q) {
        int root_p = root(p);
        int root_q = root(q);
        if (root_p == root_q)
            return;
        if (sz[root_p] < sz[root_q]) {
            id[root_p] = root_q;
            sz[root_q] += sz[root_p];
        }
        else {
            id[root_q] = root_p;
            sz[root_p] += sz[root_q];
        }
    }

    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    private int root(int p) {
        while (id[p] != p) {
            p = id[p];
        }
        return p;
    }
}
