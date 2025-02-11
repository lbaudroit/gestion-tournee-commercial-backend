package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import lombok.Getter;

import java.util.*;

public class Noeud {
    private int[][] matrixContent;
    private HashMap<Point, Integer> pointToIndexColumn;
    private HashMap<Point, Integer> pointToIndexRow;
    @Getter
    private int value;
    private Point start;
    private Point end;
    private Noeud left;
    private Noeud right;
    private Noeud parent;

    public Noeud(int[][] matrixContent, HashMap<Point, Integer> pointToIndexColumn, HashMap<Point, Integer> pointToIndexRow, int value, Point start, Point end, Noeud parent) {
        this.matrixContent = matrixContent;
        this.pointToIndexColumn = pointToIndexColumn;
        this.pointToIndexRow = pointToIndexRow;
        this.value = value;
        this.start = start;
        this.end = end;
        this.parent = parent;
    }

    public Noeud(List<Point> pointList) {
        createMatrixWithPointList(pointList);
        ReduceReturn reduceReturn = reduceMatrix(matrixContent);
        assert reduceReturn != null;
        this.matrixContent = reduceReturn.matrix();
        this.value = reduceReturn.value();
    }

    public int getSizeMatrix() {
        return matrixContent.length;
    }

    public Noeud getLowestValueNode() {
        if (left != null && right != null) {
            Noeud leftNode = left.getLowestValueNode();
            Noeud rightNode = right.getLowestValueNode();
            return leftNode.getValue() <= rightNode.getValue() ? leftNode : rightNode;
        } else {
            return this;
        }
    }

    public void expand() {
        // Right side
        int[][] newMatrix = new int[matrixContent.length][matrixContent.length];
        for (int i = 0; i < matrixContent.length; i++) {
            System.arraycopy(matrixContent[i], 0, newMatrix[i], 0, matrixContent.length);
        }
        HighestRegret highestRegret = regret();
        newMatrix[pointToIndexRow.get(highestRegret.ligne())][pointToIndexColumn.get(highestRegret.colonne())] = Integer.MAX_VALUE;
        newMatrix = reduceMatrix(newMatrix).matrix();
        int valeur = value + highestRegret.regret();
        if (valeur > 2000000 || valeur < 0) {
            valeur = Integer.MAX_VALUE;
        }
        right = new Noeud(newMatrix, pointToIndexColumn, pointToIndexRow, valeur, null, null, this);
        // Left side
        HashMap<Point, Integer> newPointToIndexColumn = removeFromPointToIndex(highestRegret.colonne(), pointToIndexColumn);
        HashMap<Point, Integer> newPointToIndexRow = removeFromPointToIndex(highestRegret.ligne(), pointToIndexRow);
        int[][] newMatrixContent = removeLineAndColumn(matrixContent, highestRegret.ligne(), highestRegret.colonne());
        if (newPointToIndexRow.containsKey(highestRegret.colonne()) && newPointToIndexColumn.containsKey(highestRegret.ligne())) {
            newMatrixContent[newPointToIndexRow.get(highestRegret.colonne())][newPointToIndexColumn.get(highestRegret.ligne())] = Integer.MAX_VALUE;
        }
        ReduceReturn reduceReturn = reduceMatrix(newMatrixContent);
        newMatrixContent = reduceReturn.matrix();
        valeur = value + reduceReturn.value();
        if (valeur > 2000000 || valeur < 0) {
            valeur = Integer.MAX_VALUE;
        }
        left = new Noeud(newMatrixContent, newPointToIndexColumn, newPointToIndexRow, valeur, highestRegret.ligne(), highestRegret.colonne(), this);
    }

    private int[][] removeLineAndColumn(int[][] matrix, Point ligne, Point colonne) {
        int[][] newMatrix = new int[matrix.length - 1][matrix.length - 1];
        int newLine = 0;
        int newCollumn = 0;
        for (int line = 0; line < matrix.length; line++) {
            if (pointToIndexRow.get(ligne) != line) {
                for (int column = 0; column < matrix.length; column++) {
                    if (pointToIndexColumn.get(colonne) != column) {
                        newMatrix[newLine][newCollumn] = matrix[line][column];
                        newCollumn++;
                    }
                }
                newLine++;
                newCollumn = 0;
            }
        }
        return newMatrix;
    }
    private void createMatrixWithPointList(List<Point> pointList) {
        HashMap<Point, Integer> pointToIndexColumn = new HashMap<>();
        HashMap<Point, Integer> pointToIndexRow = new HashMap<>();
        for (int i = 0; i < pointList.size(); i++) {
            pointToIndexColumn.put(pointList.get(i), i);
            pointToIndexRow.put(pointList.get(i), i);
        }
        matrixContent = new int[pointList.size()][pointList.size()];
        this.pointToIndexColumn = pointToIndexColumn;
        this.pointToIndexRow = pointToIndexRow;
        try {
            copyListValuesIntoMatrix(pointList);
        } catch (Exception e) {
            throw new IllegalArgumentException("The list of points is not valid, data is missing");
        }
    }

    private void copyListValuesIntoMatrix(List<Point> pointList) {
        for (int i = 0; i < pointList.size(); i++) {
            for (int j = 0; j < pointList.size(); j++) {
                if (i == j) {
                    matrixContent[i][j] = Integer.MAX_VALUE;
                } else {
                    matrixContent[i][j] = pointList.get(i).getDistance(pointList.get(j));
                }
            }
        }
    }


    private ReduceReturn reduceMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];
        int valeur = reduceLines(matrix, newMatrix);
        valeur += reduceColumns(newMatrix);
        return new ReduceReturn(newMatrix, valeur);
    }

    private static int reduceLines(int[][] matrix, int[][] newMatrix) {
        int valeur = 0;
        for (int line = 0; line < matrix.length; line++) {
            int min = Integer.MAX_VALUE;
            for (int value : matrix[line]) {
                if (value < min) {
                    min = value;
                }
            }
            if (min != Integer.MAX_VALUE) {
                valeur += min;
                for (int column = 0; column < matrix[line].length; column++) {
                    newMatrix[line][column] = matrix[line][column] - min;
                }
            }
        }
        return valeur;
    }

    private static int reduceColumns(int[][] newMatrix) {
        int valeur = 0;
        for (int column = 0; column < newMatrix.length; column++) {
            int min = Integer.MAX_VALUE;
            for (int line = 0; line < newMatrix.length; line++) {
                if (newMatrix[line][column] < min) {
                    min = newMatrix[line][column];
                }
            }
            if (min != Integer.MAX_VALUE) {
                valeur += min;
                for (int line = 0; line < newMatrix.length; line++) {
                    newMatrix[line][column] -= min;
                }
            }
        }
        return valeur;
    }

    private HighestRegret regret() {
        // If the matrix is of size 2 with [big value, 0] [0, big value] print hi
        int maxRegret = -1;
        Point maxRegretLine = null;
        Point maxRegretColumn = null;
        for (Point ligne : pointToIndexRow.keySet()) {
            for (Point colonne : pointToIndexColumn.keySet()) {
                if (matrixContent[pointToIndexRow.get(ligne)][pointToIndexColumn.get(colonne)] == 0) {
                    int valeur = calculateRegret(ligne, colonne);
                    if (valeur > maxRegret) {
                        maxRegret = valeur;
                        maxRegretLine = ligne;
                        maxRegretColumn = colonne;
                    }
                }
            }
        }
        return new HighestRegret(maxRegretLine, maxRegretColumn, maxRegret);
    }

    private int calculateRegret(Point ligne, Point colonne) {
        int minColumn = Integer.MAX_VALUE;
        int minLine = Integer.MAX_VALUE;
        for (int i = 0; i < matrixContent.length; i++) {
            if (pointToIndexColumn.get(colonne) != i && matrixContent[pointToIndexRow.get(ligne)][i] < minColumn) {
                minColumn = matrixContent[pointToIndexRow.get(ligne)][i];
            }
            if (pointToIndexRow.get(ligne) != i && matrixContent[i][pointToIndexColumn.get(colonne)] < minLine) {
                minLine = matrixContent[i][pointToIndexColumn.get(colonne)];
            }
        }
        if (minColumn > 2000000 || minLine > 2000000) {
            return Integer.MAX_VALUE;
        }
        return minLine + minColumn;
    }

    private static HashMap<Point, Integer> removeFromPointToIndex(Point toRemove, HashMap<Point, Integer> originalPointToIndex) {
        HashMap<Point, Integer> newPointToIndex = new HashMap<>();
        int indexToRemove = originalPointToIndex.get(toRemove);
        for (Map.Entry<Point, Integer> entry : originalPointToIndex.entrySet()) {
            int index = entry.getValue();
            if (index < indexToRemove) {
                newPointToIndex.put(entry.getKey(), index);
            } else if (index > indexToRemove) {
                newPointToIndex.put(entry.getKey(), index - 1);
            }
        }
        return newPointToIndex;
    }

    public List<Noeud> getAllNodesOnRoute() {
        if (parent == null) {
            return new ArrayList<>();
        } else {
            List<Noeud> allNodes = parent.getAllNodesOnRoute();
            if (start != null && end != null) {
                allNodes.add(this);
            }
            return allNodes;
        }
    }
}
