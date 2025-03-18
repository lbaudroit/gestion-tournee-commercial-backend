package friutrodez.backendtourneecommercial.service.itineraryGenerator.objects;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.*;

/**
 * Classe représentant un nœud dans l'arbre de recherche de l'algorithme Little.
 * Chaque nœud contient une matrice de distances, une valeur associée à cette matrice,
 * un point de départ et un point d'arrivée, ainsi que des références vers ses enfants et son parent.
 *
 * @author Benjamin NICOL, Enzo CLUZEL, Ahmed BRIBACH, Leïla BAUDROIT
 */
public class Node {
    @Getter
    private final int value;
    @Getter(AccessLevel.MODULE)
    private int[][] matrixContent;
    @Getter(AccessLevel.MODULE)
    private HashMap<Point, Integer> pointToIndexColumn;
    @Getter(AccessLevel.MODULE)
    private HashMap<Point, Integer> pointToIndexRow;
    @Getter
    private Point start;
    @Getter
    private Point end;
    @Getter(AccessLevel.MODULE)
    private Node left;
    @Getter(AccessLevel.MODULE)
    private Node right;
    @Getter(AccessLevel.MODULE)
    private Node parent;

    /**
     * Constructeur pour initialiser un nœud avec une matrice et des points de départ et d'arrivée.
     * Cette méthode est utilisée pour créer les nœuds enfants.
     *
     * @param matrixContent      Contenu de la matrice.
     * @param pointToIndexColumn Mappage des points aux indices de colonne.
     * @param pointToIndexRow    Mappage des points aux indices de ligne.
     * @param value              Valeur du nœud.
     * @param start              Point de départ.
     * @param end                Point d'arrivée.
     * @param parent             Noeud parent.
     */
    public Node(int[][] matrixContent, HashMap<Point, Integer> pointToIndexColumn, HashMap<Point, Integer> pointToIndexRow, int value, Point start, Point end, Node parent) {
        this.matrixContent = matrixContent;
        this.pointToIndexColumn = pointToIndexColumn;
        this.pointToIndexRow = pointToIndexRow;
        this.value = value;
        this.start = start;
        this.end = end;
        this.parent = parent;
    }

    /**
     * Constructeur pour initialiser un nœud avec une liste de points.
     * Cette méthode n'est utilisée que pour créer le nœud racine.
     *
     * @param pointList Liste des points.
     */
    public Node(List<Point> pointList) {
        createMatrixWithPointList(pointList);
        ReduceReturn reduceReturn = reduceMatrix(matrixContent);
        this.matrixContent = reduceReturn.matrix();
        this.value = reduceReturn.value();
    }

    /**
     * Réduit les lignes de la matrice.
     * Pour chaque ligne, trouve la valeur minimale et la soustrait de chaque élément de la ligne.
     * Ajoute la valeur minimale de chaque ligne à la valeur totale de réduction.
     *
     * @param matrix    Matrice d'origine.
     * @param newMatrix Nouvelle matrice.
     * @return Valeur de réduction.
     */
    private static int reduceLines(int[][] matrix, int[][] newMatrix) {
        int valeur = 0;
        for (int line = 0; line < matrix.length; line++) {
            int min = Arrays.stream(matrix[line]).min().orElse(Integer.MAX_VALUE);
            if (min != Integer.MAX_VALUE) {
                valeur += min;
                for (int column = 0; column < matrix[line].length; column++) {
                    newMatrix[line][column] = matrix[line][column] - min;
                }
            }
        }
        return valeur;
    }

    /**
     * Réduit les colonnes de la matrice.
     * Pour chaque colonne, trouve la valeur minimale et la soustrait de chaque élément de la colonne.
     * Ajoute la valeur minimale de chaque colonne à la valeur totale de réduction.
     *
     * @param newMatrix Nouvelle matrice.
     * @return Valeur de réduction.
     */
    private static int reduceColumns(int[][] newMatrix) {
        int valeur = 0;
        for (int column = 0; column < newMatrix.length; column++) {
            int min = Integer.MAX_VALUE;
            for (int[] line : newMatrix) {
                if (line[column] < min) {
                    min = line[column];
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

    /**
     * Supprime un point du mappage des indices.
     *
     * @param toRemove             Point à supprimer.
     * @param originalPointToIndex Mappage d'origine des points aux indices.
     * @return Nouveau mappage des points aux indices.
     */
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

    /**
     * Retourne la taille de la matrice.
     *
     * @return Taille de la matrice.
     */
    public int getSizeMatrix() {
        return matrixContent.length;
    }

    /**
     * Retourne le nœud avec la plus petite valeur.
     * Si le nœud actuel n'a pas d'enfants, il est retourné.
     * Sinon, les nœuds enfants sont explorés récursivement pour trouver le nœud avec la plus petite valeur.
     * Si les deux nœuds enfants ont la même valeur, le nœud de gauche est retourné.
     * Sinon le nœud avec la plus petite valeur est retourné.
     *
     * @return Nœud avec la plus petite valeur.
     */
    public Node getLowestValueNode() {
        if (left == null || right == null) {
            return this;
        }
        Node leftNode = left.getLowestValueNode();
        Node rightNode = right.getLowestValueNode();
        return leftNode.getValue() <= rightNode.getValue() ? leftNode : rightNode;
    }

    /**
     * Développe le nœud en créant des nœuds enfants gauche et droit.
     * Le nœud de droite est créé en mettant à jour la direction inverse que celle avec le plus grand regret à l'infini.
     * Le nœud de gauche est créé en mettant à jour la matrice en supprimant la ligne et la colonne avec le plus grand regret.
     * Dans les deux cas, la matrice est réduite
     * Pour le cas du nœud de gauche, une règle est appliquée pour éviter la possibilité de circuits.
     */
    public void expand() {
        HighestRegret highestRegret = regret();
        generateRight(highestRegret);
        generateLeft(highestRegret);
    }

    /**
     * Génère le nœud enfant gauche en mettant à jour la matrice.
     * La matrice est mise à jour en supprimant la ligne et la colonne avec le plus grand regret.
     * La matrice est ensuite réduite et la nouvelle valeur est calculée.
     * Si la nouvelle valeur est infinie, elle est définie à Integer.MAX_VALUE.
     *
     * @param highestRegret Objet HighestRegret contenant les informations sur le plus grand regret.
     */
    private void generateLeft(HighestRegret highestRegret) {
        HashMap<Point, Integer> newPointToIndexColumn = removeFromPointToIndex(highestRegret.colonne(), pointToIndexColumn);
        HashMap<Point, Integer> newPointToIndexRow = removeFromPointToIndex(highestRegret.ligne(), pointToIndexRow);
        int[][] newMatrixContent = removeLineAndColumn(matrixContent, highestRegret.ligne(), highestRegret.colonne());
        if (newMatrixContent.length != 1) {
            avoidCircuits(newMatrixContent, newPointToIndexRow, newPointToIndexColumn, highestRegret.ligne(), highestRegret.colonne());
        }
        ReduceReturn reduceReturn = reduceMatrix(newMatrixContent);
        newMatrixContent = reduceReturn.matrix();
        int newValue = value + reduceReturn.value();
        if (isInfinity(newValue)) {
            newValue = Integer.MAX_VALUE;
        }
        left = new Node(newMatrixContent, newPointToIndexColumn, newPointToIndexRow, newValue, highestRegret.ligne(), highestRegret.colonne(), this);
    }

    /**
     * Génère le nœud enfant droit en mettant à jour la matrice.
     * La matrice est mise à jour en définissant la direction inverse avec le plus grand regret à l'infini.
     * La matrice est ensuite réduite et la nouvelle valeur est calculée.
     * Si la nouvelle valeur est infinie, elle est définie à Integer.MAX_VALUE.
     *
     * @param highestRegret Objet HighestRegret contenant les informations sur le plus grand regret.
     */
    private void generateRight(HighestRegret highestRegret) {
        int[][] newMatrix = copyMatrix(matrixContent);
        newMatrix[pointToIndexRow.get(highestRegret.ligne())][pointToIndexColumn.get(highestRegret.colonne())] = Integer.MAX_VALUE;
        newMatrix = reduceMatrix(newMatrix).matrix();
        int newValue = value + highestRegret.regret();
        if (isInfinity(newValue)) {
            newValue = Integer.MAX_VALUE;
        }
        right = new Node(newMatrix, pointToIndexColumn, pointToIndexRow, newValue, null, null, this);
    }

    /**
     * Vérifie si une valeur est infinie.
     * Une valeur est considérée comme infinie si elle est supérieure ou égale à 1 000 000 000 ou si elle est négative.
     * La valeur de 1 000 000 000 est utilisée comme seuil, car les valeurs Integer.MAX_VALUE sont quand même soustraites.
     * En revanche, toutes les valeurs non infinies ne peuvent pas être accidentellement infinies, parce que la circonférence
     * de la Terre est de 40 000 km. Donc, il est impossible d'avoir une distance de 1 000 000 000 km.
     *
     * @param value Valeur à vérifier.
     * @return True si la valeur est infinie, sinon False.
     */
    private boolean isInfinity(int value) {
        return value >= 1_000_000_000 || value < 0;
    }

    /**
     * Évite les circuits dans la matrice en mettant à jour les valeurs.
     * Mise en place de la règle suivante pour éviter tout circuit :
     * Pour chaque end qui ne sont pas dans la liste des starts dans le même circuit,
     * Alors ce end ne peut pas être relié à un start dans le même circuit.
     * Sinon, il y aurait un circuit.
     *
     * @param matrix             Matrice à mettre à jour.
     * @param pointToIndexRow    Mappage des points aux indices de ligne.
     * @param pointToIndexColumn Mappage des points aux indices de colonne.
     * @param start              Point de départ.
     * @param end                Point d'arrivée.
     */
    private void avoidCircuits(int[][] matrix, HashMap<Point, Integer> pointToIndexRow, HashMap<Point, Integer> pointToIndexColumn, Point start, Point end) {
        List<Point> starts = new ArrayList<>(List.of(start));
        List<Point> ends = new ArrayList<>(List.of(end));
        completeStartsEndsWithCircuit(starts, ends);
        for (Point endGiven : ends) {
            if (!starts.contains(endGiven)) {
                for (Point startGiven : starts) {
                    if (pointToIndexRow.containsKey(endGiven) && pointToIndexColumn.containsKey(startGiven)) {
                        matrix[pointToIndexRow.get(endGiven)][pointToIndexColumn.get(startGiven)] = Integer.MAX_VALUE;
                    }
                }
            }
        }
    }

    /**
     * Complète les listes de points de départ et d'arrivée avec les points du circuit.
     * Cette méthode parcourt tous les nœuds sur la route et ajoute les points de départ et d'arrivée
     * aux listes correspondantes si un point de départ est déjà dans la liste des points d'arrivée ou vice versa.
     *
     * @param starts Liste des points de départ.
     * @param ends   Liste des points d'arrivée.
     */
    private void completeStartsEndsWithCircuit(List<Point> starts, List<Point> ends) {
        List<Node> allNodesOnRoute = getAllNodesOnRoute();
        boolean added;
        do {
            added = false;
            Iterator<Node> iterator = allNodesOnRoute.iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                if (starts.contains(node.getEnd()) || ends.contains(node.getStart())) {
                    starts.add(node.getStart());
                    ends.add(node.getEnd());
                    iterator.remove();
                    added = true;
                }
            }
        } while (added);
    }

    /**
     * Supprime une ligne et une colonne de la matrice.
     * Attention, cette méthode ne modifie pas les PointToIndex. Unique la matrice des valeurs est modifiée.
     *
     * @param matrix  Matrice d'origine.
     * @param ligne   Ligne à supprimer.
     * @param colonne Colonne à supprimer.
     * @return Nouvelle matrice avec la ligne et la colonne supprimées.
     */
    private int[][] removeLineAndColumn(int[][] matrix, Point ligne, Point colonne) {
        int size = matrix.length - 1;
        int[][] newMatrix = new int[size][size];
        int newLine = 0;
        for (int line = 0; line < matrix.length; line++) {
            if (pointToIndexRow.get(ligne) != line) {
                copyRow(matrix, newMatrix, line, newLine++, colonne);
            }
        }
        return newMatrix;
    }

    /**
     * Est utilisé pour copier une ligne de la matrice d'origine dans une nouvelle matrice.
     * La colonne donnée est exclue de la copie.
     * (utilisé dans la méthode removeLineAndColumn)
     *
     * @param matrix    Matrice d'origine.
     * @param newMatrix Nouvelle matrice.
     * @param line      Ligne à copier.
     * @param newLine   Nouvelle ligne dans la nouvelle matrice.
     * @param colonne   Colonne à exclure.
     */
    private void copyRow(int[][] matrix, int[][] newMatrix, int line, int newLine, Point colonne) {
        int newColumn = 0;
        for (int column = 0; column < matrix.length; column++) {
            if (pointToIndexColumn.get(colonne) != column) {
                newMatrix[newLine][newColumn++] = matrix[line][column];
            }
        }
    }

    /**
     * Crée une matrice à partir d'une liste de points.
     * Initialise les mappages des points aux indices de colonne et de ligne.
     * Remplit la matrice avec les distances entre les points.
     *
     * @param pointList Liste des points.
     * @throws IllegalArgumentException Si la liste de points est invalide.
     */
    private void createMatrixWithPointList(List<Point> pointList) {
        pointToIndexColumn = new HashMap<>();
        pointToIndexRow = new HashMap<>();
        for (int index = 0; index < pointList.size(); index++) {
            pointToIndexColumn.put(pointList.get(index), index);
            pointToIndexRow.put(pointList.get(index), index);
        }
        matrixContent = new int[pointList.size()][pointList.size()];
        try {
            copyListValuesIntoMatrix(pointList);
        } catch (Exception e) {
            throw new IllegalArgumentException("The list of points is not valid, data is missing");
        }
    }

    /**
     * Copie les valeurs de la liste de points dans la matrice.
     * Pour chaque point de la liste, calcule la distance vers chaque autre point
     * et remplit la matrice avec ces distances.
     * Si un point est comparé à lui-même, la valeur est définie à Integer.MAX_VALUE.
     *
     * @param pointList Liste des points.
     */
    private void copyListValuesIntoMatrix(List<Point> pointList) {
        for (int i = 0; i < pointList.size(); i++) {
            for (int j = 0; j < pointList.size(); j++) {
                matrixContent[i][j] = (i == j) ? Integer.MAX_VALUE : pointList.get(i).getDistance(pointList.get(j));
            }
        }
    }

    /**
     * Réduit la matrice en soustrayant les valeurs minimales des lignes PUIS des colonnes.
     *
     * @param matrix Matrice à réduire.
     * @return Objet ReduceReturn contenant la nouvelle matrice et la valeur de réduction.
     */
    private ReduceReturn reduceMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];
        int valeur = reduceLines(matrix, newMatrix);
        valeur += reduceColumns(newMatrix);
        return new ReduceReturn(newMatrix, valeur);
    }

    /**
     * Calcule le plus grand regret pour un nœud.
     * Le regret est calculé en trouvant les zéros dans la matrice et en déterminant
     * la somme des plus petits éléments de la ligne et de la colonne pour chaque zéro.
     * Le zéro avec la plus grande somme est considéré comme ayant le plus grand regret.
     *
     * @return Objet HighestRegret contenant la ligne, la colonne et la valeur du plus grand regret.
     */
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

    /**
     * Calcule le regret le plus élevé pour une ligne et une colonne données.
     * Pour cela pour chaque ZÉRO dans la matrice,
     * on calcule la somme des plus petits éléments de la ligne et de la colonne.
     * (hors le 0 en question)
     * Ensuite, on prend le maximum de ces sommes.
     * Si une des sommes est infinie, alors le regret est infini.
     * Ainsi, on retourne un objet HighestRegret contenant la ligne, la colonne et la valeur du regret.
     *
     * @return Objet HighestRegret contenant la ligne, la colonne et la valeur du regret.
     */
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
        if (isInfinity(minColumn) || isInfinity(minLine)) {
            return Integer.MAX_VALUE;
        }
        return minLine + minColumn;
    }

    /**
     * Retourne tous les nœuds sur la route.
     * Cette méthode est utilisée pour obtenir la liste de tous les nœuds
     * depuis le nœud courant jusqu'à la racine de l'arbre.
     *
     * @return Liste des nœuds sur la route.
     */
    public List<Node> getAllNodesOnRoute() {
        if (parent == null) {
            return new ArrayList<>();
        } else {
            List<Node> allNodes = parent.getAllNodesOnRoute();
            if (start != null && end != null) {
                allNodes.add(this);
            }
            return allNodes;
        }
    }

    /**
     * Copie une matrice.
     *
     * @param matrix Matrice à copier.
     * @return Nouvelle matrice copiée.
     */
    private int[][] copyMatrix(int[][] matrix) {
        int[][] newMatrix = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix.length);
        }
        return newMatrix;
    }
}
