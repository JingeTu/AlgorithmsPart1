/**
 * Created by jg on 25/01/2017.
 */
public class MyBitonicSearch {
    public static int BitonicMax(int[] array) {
        int l = 0;
        int r = array.length - 1;
        while (l < r) {
            int i = (l + r) / 2;
            int iM = i - 1;
            int iP = i + 1;
            int a = array[i];
            int aM = array[iM];
            int aP = array[iP];
            if (array[i] > array[iM] && array[i] < array[iP]) { // accending
                l = i;
            }
            else if (array[i] < array[iM] && array[i] > array[iP]) {
                r = i;
            }
            else {
                return array[i];
            }
        }
        return array[l];
    }

    public static int BinarySearchAccending(int[] array, int key, int l, int r) {
        while (l < r) {
            int m = (l + r) / 2;
            if (array[m] < key) {
                l = m + 1;
            }
            else if (array[m] > key) {
                r = m - 1;
            }
            else {
                return m;
            }
        }
        if (array[l] == key) {
            return l;
        }
        else {
            return -1;
        }
    }

    public static int BinarySearchDecending(int[] array, int key, int l, int r) {
        while (l < r) {
            int m = (l + r) / 2;
            if (array[m] > key) {
                l = m + 1;
            }
            else if (array[m] < key) {
                r = m - 1;
            }
            else {
                return m;
            }
        }
        if (array[l] == key) {
            return l;
        }
        else {
            return -1;
        }
    }

    public static int BitonicSearch(int[] array, int key, int l, int r) {
        if (l > r) {
            return -1;
        }
        int m = (l + r) / 2;
        if (array[m] < array[m + 1]) { // middle in accending part
            if (array[m] > key) {
                int i = BinarySearchAccending(array, key, l, m - 1);
                int j = BitonicSearch(array, key, m + 1, r);
                return (i == -1) ? j : i;
            }
            else if (array[m] == key) {
                return m;
            }
            else { // array[m] < key
                int i = BitonicSearch(array, key, m + 1, r);
                return i;
            }
        }
        else if (array[m] > array[m + 1]) { // middle in decending part
            if (array[m] > key) {
                int i = BinarySearchDecending(array, key, m + 1, r);
                int j = BitonicSearch(array, key, l, m - 1);
                return (i == -1) ? j : i;
            }
            else if (array[m] == key) {
                return m;
            }
            else {
                int i = BitonicSearch(array, key, l, m - 1);
                return i;
            }
        }
        else { // middle in the top
            if (array[m] == key) {
                return m;
            }
            else if (array[m] < key) {
                return -1;
            }
            else {
                int i = BinarySearchAccending(array, key, l, m - 1);
                int j = BinarySearchDecending(array, key, m + 1, r);
                return (i == -1) ? j : i;
            }
        }
    }

    public static void main(String[] args) {
        int[] array = {11,22,32,34,38,45,47,51,54,55,56,53,51,42,41,39,33,27,24,12,9};
        int key = 51;
        System.out.println(BitonicSearch(array, key, 0, array.length - 1));
    }


}
