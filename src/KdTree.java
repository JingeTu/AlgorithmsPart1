import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by jg on 07/03/2017.
 */
public class KdTree {

    private static final Boolean X = true;
    private static final Boolean Y = false;

    private class Node {
        private Point2D point;
        private RectHV boundingBox;

        private boolean splitXY;

        private Node right;
        private Node left;

        Node(Point2D pt, RectHV boundingBox, Boolean splitXY) {
            this.point = pt;
            this.boundingBox = boundingBox;
            this.right = null;
            this.left = null;
            this.splitXY = splitXY;
        }
    }

    private Node root;
    private int pointsNum;

    public KdTree() {
        root = null;
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return root == null;
    }                     // is the set empty?

    public int size() {
        return pointsNum;
    }                         // number of points in the set

    public void insert(Point2D p) {

        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        if (contains(p))
            return;

        if (root == null) {
            root = new Node(p, new RectHV(0, 0, 1, 1), X);
            pointsNum++;
            return;
        }
        Node ptr = root;
        Node pre = root;
        boolean left = false;

        while (ptr != null) {
            if (ptr.splitXY == X) {
                if (p.x() < ptr.point.x()) {
                    pre = ptr;
                    ptr = ptr.left;
                    left = true;
                }
                else {
                    pre = ptr;
                    ptr = ptr.right;
                    left = false;
                }
            }
            else {
                if (p.y() < ptr.point.y()) {
                    pre = ptr;
                    ptr = ptr.left;
                    left = true;
                }
                else {
                    pre = ptr;
                    ptr = ptr.right;
                    left = false;
                }
            }
        }
        if (left == true) {
            Node n;
            if (pre.splitXY == X) {
                n = new Node(p, new RectHV(pre.boundingBox.xmin(), pre.boundingBox.ymin(),
                        pre.point.x(), pre.boundingBox.ymax()), !pre.splitXY);
            }
            else { // Y
                n = new Node(p, new RectHV(pre.boundingBox.xmin(), pre.boundingBox.ymin(),
                        pre.boundingBox.xmax(), pre.point.y()), !pre.splitXY);
            }
            pre.left = n;
            pointsNum++;
        }
        else {
            Node n;
            if (pre.splitXY == X) {
                n = new Node(p, new RectHV(pre.point.x(), pre.boundingBox.ymin(),
                        pre.boundingBox.xmax(), pre.boundingBox.ymax()), !pre.splitXY);
            }
            else { // Y
                n = new Node(p, new RectHV(pre.boundingBox.xmin(), pre.point.y(),
                        pre.boundingBox.xmax(), pre.boundingBox.ymax()), !pre.splitXY);
            }
            pre.right = n;
            pointsNum++;
        }

    }             // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {

        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        if (root == null) {
            return false;
        }

        Node ptr = root;
        while (ptr != null) {
            if (ptr.point.x() == p.x() && ptr.point.y() == p.y()) {
                return true;
            }
            if (ptr.splitXY == X) {
                if (p.x() < ptr.point.x()) {
                    ptr = ptr.left;
                }
                else {
                    ptr = ptr.right;
                }
            }
            else {
                if (p.y() < ptr.point.y()) {
                    ptr = ptr.left;
                }
                else {
                    ptr = ptr.right;
                }
            }
        }
        return false;
    }           // does the set contain point p?

    public void draw() {
        drawHelper(root);
    }                        // draw all points to standard draw

    private void drawHelper(Node p) {

        if (p == null)
            return;

        p.point.draw();
        if (p.splitXY == X) {
            Point2D p1 = new Point2D(p.point.x(), p.boundingBox.ymin());
            Point2D p2 = new Point2D(p.point.x(), p.boundingBox.ymax());
            StdDraw.setPenColor(StdDraw.RED);
            p1.drawTo(p2);
        }
        else {
            Point2D p1 = new Point2D(p.boundingBox.xmin(), p.point.y());
            Point2D p2 = new Point2D(p.boundingBox.xmax(), p.point.y());
            StdDraw.setPenColor(StdDraw.BLUE);
            p1.drawTo(p2);
        }

        drawHelper(p.right);
        drawHelper(p.left);
    }

    public Iterable<Point2D> range(RectHV rect) {

        if (rect == null) {
            throw new java.lang.NullPointerException();
        }

        if (rect == null) {
            throw new java.lang.NullPointerException();
        }

        ArrayList<Point2D> arrayList = new ArrayList<Point2D>();

        Iterable<Point2D> iterable = search(root, rect);
        Iterator<Point2D> iterator = iterable.iterator();
        while (iterator.hasNext()) {
            arrayList.add(iterator.next());
        }

        return arrayList;
    }            // all points that are inside the rectangle

    private Iterable<Point2D> search(Node node, RectHV rect) {

        ArrayList<Point2D> arrayList = new ArrayList<Point2D>();

        if (node == null) {
            return arrayList;
        }

        if (!node.boundingBox.intersects(rect)) {
            return arrayList;
        }

        if (rect.contains(node.point)) {
            arrayList.add(node.point);
        }

        Iterable<Point2D> leftPoints = search(node.left, rect);
        Iterator<Point2D> leftIter = leftPoints.iterator();
        while (leftIter.hasNext()) {
            arrayList.add(leftIter.next());
        }

        Iterable<Point2D> rightPoints = search(node.right, rect);
        Iterator<Point2D> rightIter = rightPoints.iterator();
        while (rightIter.hasNext()) {
            arrayList.add(rightIter.next());
        }

        return arrayList;
    }

    public Point2D nearest(Point2D p) {

        if (p == null) {
            throw new java.lang.NullPointerException();
        }

        return searchNearest(root, p, Double.MAX_VALUE);
    }            // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D searchNearest(Node node, Point2D p, double minDist2) {
        if (node.boundingBox.distanceSquaredTo(p) > minDist2) {
            return null;
        }

        double dist2 = p.distanceSquaredTo(node.point);

        if (dist2 < minDist2)
            minDist2 = dist2;

        Point2D pointLeft = searchNearest(node.left, p, minDist2);
        Point2D pointRight = searchNearest(node.right, p, minDist2);

        double leftDist2 = Double.MAX_VALUE;
        double rightDist2 = Double.MAX_VALUE;

        if (pointLeft != null)
            leftDist2 = p.distanceSquaredTo(pointLeft);
        if (pointRight != null)
            rightDist2 = p.distanceSquaredTo(pointRight);

        double min = Double.min(dist2, Double.min(leftDist2, rightDist2));

        if (min == dist2) {
            return node.point;
        }
        if (min == leftDist2) {
            return pointLeft;
        }
        if (min == rightDist2) {
            return pointRight;
        }
        return null;
    }

    public static void main(String[] args) {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        while (true) {
            if (StdDraw.mousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.show();
                }
            }
            StdDraw.pause(50);
        }

    }                 // unit testing of the methods (optional)
}
