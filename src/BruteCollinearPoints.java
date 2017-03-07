import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * Created by jg on 11/02/2017.
 */
public class BruteCollinearPoints {

    private List<LineSegment> lineSegments;

    public BruteCollinearPoints(Point[] points) {
        Point[] copy = points.clone();
        Arrays.sort(copy, Point::compareTo);
        for (int i = 0; i < copy.length - 1; ++i) {
            if (copy[i].slopeTo(copy[i+1]) == Double.NEGATIVE_INFINITY) {
                throw new java.lang.IllegalArgumentException();
            }
        }
        lineSegments = new ArrayList<LineSegment>();
        for (int i1 = 0; i1 < copy.length; ++i1) {
            for (int i2 = i1 + 1; i2 < copy.length; ++i2) {
                double slope12 = copy[i1].slopeTo(copy[i2]);
                for (int i3 = i2 + 1; i3 < copy.length; ++i3) {
                    double slope13 = copy[i1].slopeTo(copy[i3]);
                    if (slope13 == slope12) {
                        int endPointIndex = -1;
                        for (int i4 = i3 + 1; i4 < copy.length; ++i4) {
                            double slope14 = copy[i1].slopeTo(copy[i4]);
                            if (slope14 == slope12) {
                                endPointIndex = i4;
                            }
                        }
                        if (endPointIndex != -1) {
                            lineSegments.add(new LineSegment(copy[i1], copy[endPointIndex]));
                            break;
                        }
                    }
                }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
