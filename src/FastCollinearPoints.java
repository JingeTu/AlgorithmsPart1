import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jg on 12/02/2017.
 */
public class FastCollinearPoints {

    private List<LineSegment> lineSegments;

    public FastCollinearPoints(Point[] points) {
        Point[] copy = points.clone();
        Arrays.sort(copy, Point::compareTo);
        for (int i = 0; i < copy.length - 1; ++i) {
            if (copy[i].slopeTo(copy[i+1]) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException();
            }
        }
        lineSegments = new ArrayList<LineSegment>();
        for (int i = 0; i < copy.length - 3; ++i) { // save out the last three points
            Point p = copy[i];
            Point[] copySort = new Point[copy.length - i - 1];
            for (int j = i + 1; j < copy.length; ++j) {
                copySort[j - i - 1] = copy[j];
            }
            Arrays.sort(copySort, p.slopeOrder());

            Point[] linePoints = new Point[copySort.length];
            int count = 0;
            double slope = p.slopeTo(copySort[0]);
//            System.out.println(copySort.length);
            linePoints[count++] = copySort[0];
            for (int j = 1; j < copySort.length; ++j) {
                if (p.slopeTo(copySort[j]) == slope) {
                    linePoints[count++] = copySort[j];
                }
                else {
                    if (count >= 3) {
                        Arrays.sort(linePoints, 0, count, Point::compareTo);
                        lineSegments.add(new LineSegment(p, linePoints[count - 1]));
                    }

                    count = 0;
                    slope = p.slopeTo(copy[j]);
                    linePoints[count++] = copy[j];
                }
            }
            if (count >= 3) {
                Arrays.sort(linePoints, 0, count, Point::compareTo);
                lineSegments.add(new LineSegment(p, linePoints[count - 1]));
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] ret = new LineSegment[lineSegments.size()];
        lineSegments.toArray(ret);
        return ret;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
