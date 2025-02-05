package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class BestRoute {
    private final List<Point> points;
    private int distance;

    public BestRoute(List<Point> points, int distance) {
        this.points = points;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "BestRoute{" +
                "points=" + points +
                ", distance=" + distance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BestRoute bestRoute = (BestRoute) o;

        if (distance != bestRoute.distance) return false;
        return Objects.equals(points, bestRoute.points) ||Objects.equals(points.reversed(),bestRoute.points);
    }
}
