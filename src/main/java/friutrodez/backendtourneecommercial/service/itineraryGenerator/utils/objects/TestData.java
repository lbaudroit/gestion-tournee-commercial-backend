package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.List;

public class TestData {

    private static List<Point> pointsExemples;

    public TestData() {
        setUpTestData();
    }

    public List<Point> getPointsExemples() {
        return pointsExemples;
    }

    public Point getStartEnd() {
        return new Point("startEnd", 2.8847171, 43.9596889);
    }

    public List<Point> getXRandPoints(int x, Point startEnd) {
        List<Point> newList = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            int randomIndex = (int) (Math.random() * pointsExemples.size());
            Point originalPoint = pointsExemples.get(randomIndex);
            Point newPoint = new Point(originalPoint);
            if (!newList.contains(newPoint)) {
                newList.add(newPoint);
            } else {
                i--;
            }
        }
        generateDistancesWithHarvesineFormula(newList, startEnd);
        return newList;
    }

    public List<Point> getXFirstPoints(int x) {
        List<Point> newList = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            Point originalPoint = pointsExemples.get(i);
            Point newPoint = new Point(originalPoint);
        }
        Point startEnd = new Point("startEnd", 2.8847171, 43.9596889);
        generateDistancesWithHarvesineFormula(newList, startEnd);
        return newList;
    }

    private void setUpTestData() {
        pointsExemples = new ArrayList<>(List.of(
                new Point("1", 2.5731058, 44.3489985),
                new Point("2", 2.576037, 44.3350156),
                new Point("3", 2.7635276, 44.5259912),
                new Point("4", 2.5593095, 44.3300483),
                new Point("5", 3.0621842, 44.1089357),
                new Point("6", 2.575482, 44.3607091),
                new Point("7", 2.8847171, 43.9596889),
                new Point("8", 2.5618809, 44.5392277),
                new Point("9", 2.4414059, 44.4761361),
                new Point("10", 2.8513199, 44.6840223),
                new Point("11", 2.7271058, 44.2792943),
                new Point("12", 2.0415595, 44.2308501),
                new Point("13", 2.0415595, 44.2308501),
                new Point("14", 3.1608165, 44.0076749),
                new Point("15", 3.0721795, 44.3215041),
                new Point("16", 2.240297, 44.3082695),
                new Point("17", 2.2354147, 44.3086837),
                new Point("18", 2.2497527, 44.5640878),
                new Point("19", 3.0777094, 44.1019),
                new Point("20", 2.2882397, 44.4084791)
        ));
    }

    private void generateDistancesWithHarvesineFormula(List<Point> points, Point startEnd) {
        points.add(startEnd);
        for (Point point : points) {
            for (Point otherPoint : points) {
                if (!point.equals(otherPoint)) {
                    point.addPoint(otherPoint);
                }
            }
        }
        points.remove(startEnd);
    }

    public static void main(String[] args) {
        TestData testData = new TestData();
        Point startEnd = new Point("startEnd", 2.8847171, 43.9596889);
        List<Point> pointsExemples = testData.getPointsExemples();
        List<Point> points = testData.getXRandPoints(5, startEnd);
    }
}
