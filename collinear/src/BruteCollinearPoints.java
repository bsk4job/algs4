import java.util.ArrayList;
import java.util.Arrays;
// algs4 
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment,
 * returning all such line segments. To check whether the 4 points p, q, r, and s are collinear,
 * check whether the three slopes between p and q, between p and r, and between p and s are all equal.
 */
public class BruteCollinearPoints {

    // the line segments
    private ArrayList<LineSegmentInfo> lineSegments;
    private LineSegment[] segmentsArray;
    private Point[] pointsArray;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

        lineSegments = new ArrayList<>();
        if (points.length < 4) return;

        pointsArray = points.clone();
        Arrays.sort(pointsArray);
        Point p, q, r, s;

        // looking for the collinear segments
        for (int i1 = 0; i1 < pointsArray.length - 3; i1++) {
            p = pointsArray[i1];

            for (int i2 = i1 + 1; i2 < pointsArray.length - 2; i2++) {
                q = pointsArray[i2];
                double slopeToQ = p.slopeTo(q);

                for (int i3 = i2 + 1; i3 < pointsArray.length - 1; i3++) {
                    r = pointsArray[i3];
                    double slopeToR = p.slopeTo(r);

                    if (Double.compare(slopeToQ, slopeToR) != 0) continue;

                    int maxPointIndex = 0;
                    for (int i4 = i3 + 1; i4 < pointsArray.length; i4++) {

                        s = pointsArray[i4];
                        double slopeToS = p.slopeTo(s);

                        // saving the last collinear point
                        if (Double.compare(slopeToQ, slopeToS) == 0) {
                            maxPointIndex = i4;
                        }

                        if (i4 == pointsArray.length - 1 && maxPointIndex > 0) {

                            boolean isDuplicate = false;
                            for (LineSegmentInfo ls : lineSegments) {

                                if (Double.compare(ls.getSlope(), slopeToQ) == 0
                                        && (ls.getA().compareTo(p) == 0
                                            || ls.getB().compareTo(pointsArray[maxPointIndex]) == 0)) {
                                    isDuplicate = true;
                                    break;
                                }
                            }

                            if (!isDuplicate) {
                                lineSegments.add(new LineSegmentInfo(p, pointsArray[maxPointIndex]));
                            }
                        }
                    }
                }
            }
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

        if (segmentsArray == null) {
            segmentsArray = new LineSegment[lineSegments.size()];
            int i = 0;
            for (LineSegmentInfo ls : lineSegments) {
                segmentsArray[i++] = new LineSegment(ls.getA(), ls.getB());
            }
        }

        return segmentsArray.clone();
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
