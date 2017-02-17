import java.util.*;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment,
 * returning all such line segments. To check whether the 4 points p, q, r, and s are collinear,
 * check whether the three slopes between p and q, between p and r, and between p and s are all equal.
 */
public class BruteCollinearPoints {

    // finds all line segments containing 4 points
    public BruteCollinearPoints (Point[] points) {
        // corner cases : checking for nulls and duplicates
        if (points == null) {
            throw new NullPointerException();
        }

        for (int i = 0; i < points.length; i++) {

            if (points[i] == null) {
                throw new NullPointerException();
            }

            for (int j = i + 1; j < points.length; j++) {

                if (points[j] == null) {
                    throw new NullPointerException();
                }

                if (points[i].compareTo(points[j]) == 0) {
                    throw new java.lang.IllegalArgumentException();
                }
            }
        }

        Arrays.sort(points);
        Point p, q, r, s;

        lineSegments = new ArrayList<>();
        int[] links = new int[points.length];
        for (int i = 0; i < links.length; i++) {
            links[i] = i;
        }


        // looking for the collinear segments
        for (int i1 = 0; i1 < points.length - 3; i1++) {
            p = points[i1];

            for (int i2 = i1 + 1; i2 < points.length - 2; i2++) {
                q = points[i2];
                double slopeToQ = p.slopeTo(q);

                for (int i3 = i2 + 1; i3 < points.length - 1; i3++) {
                    r = points[i3];
                    double slopeToR = p.slopeTo(r);

                    if (slopeToQ != slopeToR) continue;

                    int maxPointIndex = 0;
                    for (int i4 = i3 + 1; i4 < points.length; i4++) {

                        s = points[i4];
                        double slopeToS = p.slopeTo(s);

                        // saving the last collinear point
                        if (slopeToQ == slopeToS
                                && links[i1] != links[i2] && links[i2] != links[i3]
                                ) {
                            links[i4] = i1;
                            maxPointIndex = i4;
                        }

                        if (i4 == points.length - 1 && maxPointIndex > 0) {

                            links[i1] = i1;
                            links[i2] = i1;
                            links[i3] = i1;
                            links[i4] = i1;
                            LineSegment seg = new LineSegment(p, points[maxPointIndex]);
                            lineSegments.add(seg);
                        }
                    }
                }
            }
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return segments().length;
    }

    // the line segments
    private List<LineSegment> lineSegments;
    private LineSegment[] _segments;
    public LineSegment[] segments() {
        if (_segments == null) {
            _segments = new LineSegment[lineSegments.size()];
            lineSegments.toArray(_segments);
        }
        return _segments;
    }

    public static void main (String[] args) {

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
