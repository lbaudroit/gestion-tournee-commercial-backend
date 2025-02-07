package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Noeud {
    private int[][] matrixContent;
    private HashMap<Point, Integer> pointToIndexColumn;
    private HashMap<Point, Integer> pointToIndexRow;
    @Getter
    private int value;
    public Noeud left;
    public Noeud right;

    public Noeud(int[][] matrixContent, HashMap<Point, Integer> pointToIndexColumn, HashMap<Point, Integer> pointToIndexRow, int value) {
        this.matrixContent = matrixContent;
        this.pointToIndexColumn = pointToIndexColumn;
        this.pointToIndexRow = pointToIndexRow;
        this.value = value;
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
            return leftNode.getValue() < rightNode.getValue() ? leftNode : rightNode;
        } else {
            return this;
        }
    }

    public void expand() {
        // Right side
        HighestRegret highestRegret = regret();
        int[][] newMatrix = new int[matrixContent.length][matrixContent.length];
        for (int i = 0; i < matrixContent.length; i++) {
            System.arraycopy(matrixContent[i], 0, newMatrix[i], 0, matrixContent.length);
        }
        newMatrix[pointToIndexRow.get(highestRegret.ligne())][pointToIndexColumn.get(highestRegret.colonne())] = Integer.MAX_VALUE;
        newMatrix = reduceMatrix(newMatrix).matrix();
        right = new Noeud(newMatrix, pointToIndexColumn, pointToIndexRow, value + highestRegret.regret());
        // Left side
        HashMap<Point, Integer> newPointToIndexColumn = removeFromPointToIndex(highestRegret.colonne(), pointToIndexColumn);
        HashMap<Point, Integer> newPointToIndexRow = removeFromPointToIndex(highestRegret.ligne(), pointToIndexRow);
        int[][] newMatrixContent = removeLineAndColumn(matrixContent, highestRegret.ligne(), highestRegret.colonne());
        newMatrixContent[newPointToIndexRow.get(highestRegret.colonne())][newPointToIndexColumn.get(highestRegret.ligne())] = Integer.MAX_VALUE;
        ReduceReturn reduceReturn = reduceMatrix(newMatrixContent);
        newMatrixContent = reduceReturn.matrix();
        left = new Noeud(newMatrixContent, newPointToIndexColumn, newPointToIndexRow, value + reduceReturn.value());
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
            valeur += min;
            for (int column = 0; column < matrix[line].length; column++) {
                newMatrix[line][column] = matrix[line][column] - min;
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
            valeur += min;
            for (int line = 0; line < newMatrix.length; line++) {
                newMatrix[line][column] -= min;
            }
        }
        return valeur;
    }

    private HighestRegret regret() {
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
}
