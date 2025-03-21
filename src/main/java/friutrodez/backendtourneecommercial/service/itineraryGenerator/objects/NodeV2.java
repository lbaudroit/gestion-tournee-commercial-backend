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
@SuppressWarnings("DuplicatedCode")
public class NodeV2 {
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
    @Getter
    private NodeV2 left;
    @Getter
    private NodeV2 right;
    @Getter(AccessLevel.MODULE)
    private NodeV2 parent;

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
    public NodeV2(int[][] matrixContent, HashMap<Point, Integer> pointToIndexColumn, HashMap<Point, Integer> pointToIndexRow, int value, Point start, Point end, NodeV2 parent) {
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
    public NodeV2(List<Point> pointList) {
        createMatrixWithPointList(pointList);
        ReduceReturn reduceReturn = reduceMatrix(matrixContent);
        this.matrixContent = reduceReturn.matrix();
        this.value = reduceReturn.value();
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

        // Libère de la mémoire
        matrixContent = null;
        pointToIndexColumn = null;
        pointToIndexRow = null;
    }

    /**
     * Génère le nœud enfant gauche en mettant à jour la matrice.
     * La matrice est mise à jour en supprimant la ligne et la colonne avec le plus grand regret.
     * La matrice est ensuite réduite et la nouvelle valeur est calculée.
     * Si la nouvelle valeur est infinie, elle est définie à Integer.MAX_VALUE.
     *
     * @param highestRegret Objet HighestRegret contenant les information sur le plus grand regret.
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
        left = new NodeV2(newMatrixContent, newPointToIndexColumn, newPointToIndexRow, newValue, highestRegret.ligne(), highestRegret.colonne(), this);
    }

    /**
     * Génère le nœud enfant droit en mettant à jour la matrice.
     * La matrice est mise à jour en définissant la direction inverse avec le plus grand regret à l'infini.
     * La matrice est ensuite réduite et la nouvelle valeur est calculée.
     * Si la nouvelle valeur est infinie, elle est définie à Integer.MAX_VALUE.
     *
     * @param highestRegret Objet HighestRegret contenant les information sur le plus grand regret.
     */
    private void generateRight(HighestRegret highestRegret) {
        int[][] newMatrix = copyMatrix(matrixContent);
        newMatrix[pointToIndexRow.get(highestRegret.ligne())][pointToIndexColumn.get(highestRegret.colonne())] = Integer.MAX_VALUE;
        newMatrix = reduceMatrix(newMatrix).matrix();
        int newValue = value + highestRegret.regret();
        if (isInfinity(newValue)) {
            newValue = Integer.MAX_VALUE;
        }
        right = new NodeV2(newMatrix, pointToIndexColumn, pointToIndexRow, newValue, null, null, this);
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
    private void avoidCircuits(int[][] matrix, HashMap<Point, Integer> pointToIndexRow,
                               HashMap<Point, Integer> pointToIndexColumn, Point start, Point end) {
        List<Point> starts = new ArrayList<>();
        List<Point> ends = new ArrayList<>();
        starts.add(start);
        ends.add(end);

        completeStartsEndsWithCircuit(starts, ends);
        updateMatrixToAvoidCircuits(matrix, pointToIndexRow, pointToIndexColumn, starts, ends);
    }

    /**
     * Met à jour la matrice pour éviter les circuits en définissant les valeurs appropriées à Integer.MAX_VALUE.
     *
     * @param matrix             Matrice à mettre à jour.
     * @param pointToIndexRow    Mappage des points aux indices de ligne.
     * @param pointToIndexColumn Mappage des points aux indices de colonne.
     * @param starts             Liste des points de départ.
     * @param ends               Liste des points d'arrivée.
     */
    private void updateMatrixToAvoidCircuits(int[][] matrix, HashMap<Point, Integer> pointToIndexRow,
                                             HashMap<Point, Integer> pointToIndexColumn, List<Point> starts, List<Point> ends) {
        Set<Point> startsSet = new HashSet<>(starts);

        for (Point endPoint : ends) {
            if (!startsSet.contains(endPoint)) {
                Integer endRowIndex = pointToIndexRow.get(endPoint);
                if (endRowIndex != null) {
                    for (Point startPoint : starts) {
                        Integer startColIndex = pointToIndexColumn.get(startPoint);
                        if (startColIndex != null) {
                            matrix[endRowIndex][startColIndex] = Integer.MAX_VALUE;
                        }
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
        Set<Point> startSet = new HashSet<>(starts);
        Set<Point> endSet = new HashSet<>(ends);

        List<NodeV2> allNodesOnRoute = getAllNodesOnRoute();
        boolean hasNewPoints;
        do {
            hasNewPoints = false;
            hasNewPoints = processNodesForCircuits(allNodesOnRoute, startSet, endSet, hasNewPoints);
        } while (hasNewPoints);

        updateOriginalLists(starts, ends, startSet, endSet);
    }

    /**
     * Traite les nœuds pour trouver les circuits et met à jour les ensembles de départ et d'arrivée.
     *
     * @param allNodesOnRoute Liste de tous les nœuds sur la route.
     * @param startSet        Ensemble des points de départ.
     * @param endSet          Ensemble des points d'arrivée.
     * @param hasNewPoints    Indicateur de nouveaux points ajoutés.
     * @return True si de nouveaux points ont été ajoutés, sinon False.
     */
    private boolean processNodesForCircuits(List<NodeV2> allNodesOnRoute, Set<Point> startSet, Set<Point> endSet, boolean hasNewPoints) {
        for (NodeV2 node : allNodesOnRoute) {
            Point nodeStart = node.getStart();
            Point nodeEnd = node.getEnd();

            if (nodeStart != null && nodeEnd != null) {
                if (startSet.contains(nodeEnd) || endSet.contains(nodeStart)) {
                    if (startSet.add(nodeStart)) hasNewPoints = true;
                    if (endSet.add(nodeEnd)) hasNewPoints = true;
                }
            }
        }
        return hasNewPoints;
    }

    /**
     * Met à jour les listes originales de points de départ et d'arrivée avec les ensembles mis à jour.
     *
     * @param starts   Liste des points de départ.
     * @param ends     Liste des points d'arrivée.
     * @param startSet Ensemble des points de départ mis à jour.
     * @param endSet   Ensemble des points d'arrivée mis à jour.
     */
    private void updateOriginalLists(List<Point> starts, List<Point> ends, Set<Point> startSet, Set<Point> endSet) {
        starts.clear();
        ends.clear();
        starts.addAll(startSet);
        ends.addAll(endSet);
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
        int size = matrix.length;
        int[][] newMatrix = new int[size][size];
        int totalReduction = 0;

        totalReduction += reduceRows(matrix, newMatrix);

        totalReduction += reduceColumns(newMatrix);

        return new ReduceReturn(newMatrix, totalReduction);
    }

    /**
     * Réduit les lignes de la matrice en soustrayant les valeurs minimales de chaque ligne.
     *
     * @param matrix Matrice d'origine.
     * @param newMatrix Nouvelle matrice avec les lignes réduites.
     * @return Valeur totale de la réduction des lignes.
     */
    private int reduceRows(int[][] matrix, int[][] newMatrix) {
        int size = matrix.length;
        int totalReduction = 0;

        for (int i = 0; i < size; i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] < min) {
                    min = matrix[i][j];
                }
            }

            if (min != Integer.MAX_VALUE) {
                totalReduction += min;
                for (int j = 0; j < size; j++) {
                    newMatrix[i][j] = matrix[i][j] - min;
                }
            } else {
                System.arraycopy(matrix[i], 0, newMatrix[i], 0, size);
            }
        }

        return totalReduction;
    }

    /**
     * Réduit les colonnes de la matrice en soustrayant les valeurs minimales de chaque colonne.
     *
     * @param matrix Matrice avec les lignes réduites.
     * @return Valeur totale de la réduction des colonnes.
     */
    private int reduceColumns(int[][] matrix) {
        int size = matrix.length;
        int totalReduction = 0;
        int[] colMins = new int[size];
        Arrays.fill(colMins, Integer.MAX_VALUE);

        for (int[] ints : matrix) {
            for (int j = 0; j < size; j++) {
                if (ints[j] < colMins[j]) {
                    colMins[j] = ints[j];
                }
            }
        }

        for (int j = 0; j < size; j++) {
            if (colMins[j] != Integer.MAX_VALUE) {
                totalReduction += colMins[j];
                for (int i = 0; i < size; i++) {
                    matrix[i][j] -= colMins[j];
                }
            }
        }

        return totalReduction;
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
        int indexLigne = pointToIndexRow.get(ligne);
        int indexColonne = pointToIndexColumn.get(colonne);

        int minColonne = findMinInRow(indexLigne, indexColonne);
        int minLigne = findMinInColumn(indexColonne, indexLigne);

        if (isInfinity(minColonne) || isInfinity(minLigne)) {
            return Integer.MAX_VALUE;
        }

        return minLigne + minColonne;
    }

    /**
     * Trouve le minimum dans la ligne donnée en excluant la colonne actuelle.
     *
     * @param indexLigne Index de la ligne.
     * @param indexColonne Index de la colonne à exclure.
     * @return Valeur minimale dans la ligne.
     */
    private int findMinInRow(int indexLigne, int indexColonne) {
        int minColonne = Integer.MAX_VALUE;
        for (int i = 0; i < matrixContent.length; i++) {
            if (i != indexColonne && matrixContent[indexLigne][i] < minColonne) {
                minColonne = matrixContent[indexLigne][i];
            }
        }
        return minColonne;
    }

    /**
     * Trouve le minimum dans la colonne donnée en excluant la ligne actuelle.
     *
     * @param indexColonne Index de la colonne.
     * @param indexLigne Index de la ligne à exclure.
     * @return Valeur minimale dans la colonne.
     */
    private int findMinInColumn(int indexColonne, int indexLigne) {
        int minLigne = Integer.MAX_VALUE;
        for (int i = 0; i < matrixContent.length; i++) {
            if (i != indexLigne && matrixContent[i][indexColonne] < minLigne) {
                minLigne = matrixContent[i][indexColonne];
            }
        }
        return minLigne;
    }

    /**
     * Retourne tous les nœuds sur la route.
     * Cette méthode est utilisée pour obtenir la liste de tous les nœuds
     * depuis le nœud courant jusqu'à la racine de l'arbre.
     *
     * @return Liste des nœuds sur la route.
     */
    public List<NodeV2> getAllNodesOnRoute() {
        // Utilisation d'une LinkedList pour une opération addFirst efficace (O(1))
        LinkedList<NodeV2> allNodes = new LinkedList<>();

        // Commence à partir du nœud actuel et remonte jusqu'à la racine
        NodeV2 current = this;
        while (current != null) {
            if (current.getStart() != null && current.getEnd() != null) {
                // Ajoute au début pour maintenir l'ordre original (le plus ancien en premier)
                allNodes.addFirst(current);
            }
            current = current.getParent();
        }

        return allNodes;
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
