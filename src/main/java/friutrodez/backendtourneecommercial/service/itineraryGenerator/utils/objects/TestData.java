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
                new Point("20", 2.2882397, 44.4084791),
                new Point("21", 2.5781575, 44.3450212),
                new Point("22", 2.5719853, 44.3389929),
                new Point("23", 2.7665794, 44.5220135),
                new Point("24", 2.5623613, 44.3260710),
                new Point("25", 3.0591324, 44.1129134),
                new Point("26", 2.5724302, 44.3646868),
                new Point("27", 2.8877689, 43.9557112),
                new Point("28", 2.5649327, 44.5432054),
                new Point("29", 2.4444577, 44.4721584),
                new Point("30", 2.8482681, 44.6879996),
                new Point("31", 2.7301576, 44.2753170),
                new Point("32", 2.0446113, 44.2268724),
                new Point("33", 2.0385077, 44.2348278),
                new Point("34", 3.1638683, 44.0116526),
                new Point("35", 3.0691277, 44.3254818),
                new Point("36", 2.2433488, 44.3042918),
                new Point("37", 2.2384665, 44.3126614),
                new Point("38", 2.2528045, 44.5601101),
                new Point("39", 3.0807612, 44.1058777),
                new Point("40", 2.2912915, 44.4045014)
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
