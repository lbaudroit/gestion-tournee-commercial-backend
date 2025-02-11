package friutrodez.backendtourneecommercial.service.itineraryGenerator.utils.objects;

import friutrodez.backendtourneecommercial.service.itineraryGenerator.objects.Point;

import java.util.ArrayList;
import java.util.HashMap;
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

    public List<Point> getPointsStatic1(Point startEnd) {
        List<Point> pointsStatic = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pointsStatic.add(pointsExemples.get(i));
        }
        generateDistancesWithHarvesineFormula(pointsStatic, startEnd);
        return pointsStatic;
    }

    public List<Point> getPointsStatic2(Point startEnd) {
        List<Point> pointsStatic = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pointsStatic.add(pointsExemples.get(i + 5));
        }
        generateDistancesWithHarvesineFormula(pointsStatic, startEnd);
        return pointsStatic;
    }

    public List<Point> getPointsStatic3(Point startEnd) {
        List<Point> pointsStatic = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            pointsStatic.add(pointsExemples.get(i + 10));
        }
        generateDistancesWithHarvesineFormula(pointsStatic, startEnd);
        return pointsStatic;
    }

    public List<Point> getPointsStatic4(Point startEnd) {
        List<Point> pointsStatic = new ArrayList<>();
        startEnd.getDistances().clear();
        pointsStatic.add(startEnd);
        pointsStatic.add(new Point("B", 0, 0));
        pointsStatic.add(new Point("C", 0, 0));
        pointsStatic.add(new Point("D", 0, 0));
        pointsStatic.add(new Point("E", 0, 0));
        pointsStatic.add(new Point("F", 0, 0));
        pointsStatic.add(new Point("G", 0, 0));
        HashMap<Point, Integer> pointValue = new HashMap<>();
        pointValue = pointsStatic.get(0).getDistances();
        pointValue.put(pointsStatic.get(1), 33);
        pointValue.put(pointsStatic.get(2), 45);
        pointValue.put(pointsStatic.get(3), 49);
        pointValue.put(pointsStatic.get(4), 32);
        pointValue.put(pointsStatic.get(5), 10);
        pointValue.put(pointsStatic.get(6), 31);
        pointValue = pointsStatic.get(1).getDistances();
        pointValue.put(pointsStatic.get(0), 28);
        pointValue.put(pointsStatic.get(2), 13);
        pointValue.put(pointsStatic.get(3), 46);
        pointValue.put(pointsStatic.get(4), 37);
        pointValue.put(pointsStatic.get(5), 30);
        pointValue.put(pointsStatic.get(6), 27);
        pointValue = pointsStatic.get(2).getDistances();
        pointValue.put(pointsStatic.get(0), 43);
        pointValue.put(pointsStatic.get(1), 14);
        pointValue.put(pointsStatic.get(3), 41);
        pointValue.put(pointsStatic.get(4), 32);
        pointValue.put(pointsStatic.get(5), 29);
        pointValue.put(pointsStatic.get(6), 21);
        pointValue = pointsStatic.get(3).getDistances();
        pointValue.put(pointsStatic.get(0), 49);
        pointValue.put(pointsStatic.get(1), 50);
        pointValue.put(pointsStatic.get(2), 38);
        pointValue.put(pointsStatic.get(4), 24);
        pointValue.put(pointsStatic.get(5), 44);
        pointValue.put(pointsStatic.get(6), 27);
        pointValue = pointsStatic.get(4).getDistances();
        pointValue.put(pointsStatic.get(0), 30);
        pointValue.put(pointsStatic.get(1), 34);
        pointValue.put(pointsStatic.get(2), 28);
        pointValue.put(pointsStatic.get(3), 27);
        pointValue.put(pointsStatic.get(5), 13);
        pointValue.put(pointsStatic.get(6), 27);
        pointValue = pointsStatic.get(5).getDistances();
        pointValue.put(pointsStatic.get(0), 6);
        pointValue.put(pointsStatic.get(1), 28);
        pointValue.put(pointsStatic.get(2), 32);
        pointValue.put(pointsStatic.get(3), 41);
        pointValue.put(pointsStatic.get(4), 17);
        pointValue.put(pointsStatic.get(6), 37);
        pointValue = pointsStatic.get(6).getDistances();
        pointValue.put(pointsStatic.get(0), 32);
        pointValue.put(pointsStatic.get(1), 31);
        pointValue.put(pointsStatic.get(2), 19);
        pointValue.put(pointsStatic.get(3), 26);
        pointValue.put(pointsStatic.get(4), 24);
        pointValue.put(pointsStatic.get(5), 41);
        pointsStatic.remove(startEnd);
        return pointsStatic;
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
