/**
 * A faster, sorting-based solution fpr the "collinear points" problem
 */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;


public class FastCollinearPoints {

    // the line segments
    private ArrayList<LineSegmentInfo> lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
        lineSegments = new ArrayList<>();

        for (Point p: points) {

            Point[] sortedPoints = points.clone();
            Arrays.sort(sortedPoints, p.slopeOrder());
            Point q = sortedPoints[1];
            ArrayList<Point> segment = new ArrayList<>(4);
            segment.add(p);
            segment.add(q);

            double slopeQ = p.slopeTo(q);
            for (int i = 2; i < sortedPoints.length; i++) {

                double slopeCurrent = p.slopeTo(sortedPoints[i]);

                if (Double.compare(slopeQ, slopeCurrent) == 0) {
                    segment.add(sortedPoints[i]);
                }
                else {
                    checkAddNewSegment(segment);

                    q = sortedPoints[i];
                    segment = initNewSegment(p, q);
                    slopeQ = slopeCurrent;
                }
            }

            checkAddNewSegment(segment);
        }

    }

    private ArrayList<Point> initNewSegment(Point p, Point q) {
        ArrayList<Point> result = new ArrayList<>(4);
        result.add(p);
        result.add(q);

        return result;
    }

    private void checkAddNewSegment(ArrayList<Point> segment) {

        if (segment.size() < 4) {
            return;
        }

        Point[] points = new Point[segment.size()];
        segment.toArray(points);
        Arrays.sort(points);

        Point p = points[0];
        Point q = points[segment.size() - 1];

        double slopeToQ = p.slopeTo(q);
        boolean isDuplicate = false;
        for (LineSegmentInfo ls : lineSegments) {

            if (Double.compare(ls.getSlope(), slopeToQ) == 0
                    && (ls.getA().compareTo(p) == 0
                    || ls.getB().compareTo(q) == 0)) {
                isDuplicate = true;
                break;
            }
        }

        if (!isDuplicate) {
            lineSegments.add(new LineSegmentInfo(p, q));
        }
    }

    /**
     * LineSegment with information about its start, end and slope
     */
    private class LineSegmentInfo {
        private Point a, b;
        private double slope;

        public LineSegmentInfo(Point a, Point b) {
            this.a = a;
            this.b = b;
            this.slope = a.slopeTo(b);
        }

        public Point getA() {
            return this.a;
        }

        public Point getB() {
            return this.b;
        }

        public double getSlope() {
            return this.slope;
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {

        LineSegment[] segmentsArray = new LineSegment[lineSegments.size()];
        int i = 0;
        for (LineSegmentInfo ls : lineSegments) {
            segmentsArray[i++] = new LineSegment(ls.getA(), ls.getB());
        }

        return segmentsArray;
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
