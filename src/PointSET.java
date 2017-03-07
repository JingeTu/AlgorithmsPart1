import edu.princeton.cs.algs4.*;

import java.util.ArrayList;

/**
 * Created by jg on 07/03/2017.
 */
public class PointSET {

    private SET<Point2D> root;
    private int pointsNum;

    public PointSET() {
        root = new SET<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return root.isEmpty();
    }                     // is the set empty?

    public int size() {
        return root.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        root.add(p);
    }             // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {

        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        return root.contains(p);
    }           // does the set contain point p?

    public void draw() {
        for (Point2D point :
                root) {
            point.draw();
        }
    }                        // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        ArrayList<Point2D> arrayList = new ArrayList<Point2D>();
        for (Point2D point :
                root) {
            if (rect.contains(point)) {
                arrayList.add(point);
            }
        }
        return arrayList;
    }            // all points that are inside the rectangle

    public Point2D nearest(Point2D p) {

        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        double minDist2 = Double.MAX_VALUE;
        Point2D minPoint = null;

        for (Point2D point :
                root) {
            if (p.distanceSquaredTo(point) < minDist2) {
                minDist2 = p.distanceSquaredTo(point);
                minPoint = point;
            }
        }

        return minPoint;
    }            // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {


    }                 // unit testing of the methods (optional)
}
