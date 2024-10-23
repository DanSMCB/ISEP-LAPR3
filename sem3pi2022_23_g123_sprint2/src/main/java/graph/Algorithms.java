package graph;

import graph.map.MapGraph;
import graph.matrix.MatrixGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.BinaryOperator;

/**
 *
 * @author DEI-ISEP
 *
 */
public class Algorithms {

    /** Performs breadth-first search of a Graph starting in a vertex
     *
     * @param g Graph instance
     * @param vOrigin vertex that will be the source of the search
     * @return a LinkedList with the vertices of breadth-first search
     */
    public static <V, E> LinkedList<V> BreadthFirstSearch(Graph<V, E> g, V vOrigin) {
        if (!g.validVertex(vOrigin))
            return null;

        LinkedList<V> qbfs = new LinkedList<>();
        qbfs.add(vOrigin);

        LinkedList<V> qaux = new LinkedList<>();
        qaux.add(vOrigin);

        int numVertices = g.numVertices();
        boolean[] visited = new boolean[numVertices]; //default initialize.:false
        int vOrigKey = g.key(vOrigin);
        visited[vOrigKey] = true;

        while (!qaux.isEmpty()){
            vOrigin = qaux.remove();
            for (V vAdj : g.adjVertices(vOrigin)) {
                vOrigKey = g.key(vAdj);
                if (!visited[vOrigKey]) {
                    qbfs.add(vAdj);
                    qaux.add(vAdj);
                    visited[vOrigKey] = true;
                }
            }
        }
        return qbfs;
    }



    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vOrig vertex of graph g that will be the source of the search
     * @param visited set of previously visited vertices
     * @param qdfs return LinkedList with vertices of depth-first search
     */
    private static <V, E> void DepthFirstSearch(Graph<V, E> g, V vOrig, boolean[] visited, LinkedList<V> qdfs) {
        int vOrigKey = g.key(vOrig);          //ir buscar a chave do vértice que entra
        if (visited[vOrigKey]) return;        //se for um vértice que já visitamos saímos logo

        qdfs.add(vOrig);
        visited[vOrigKey]=true;

        for (V vAdj : g.adjVertices(vOrig)) {
            DepthFirstSearch(g, vAdj, visited, qdfs);
        }
    }

    /** Performs depth-first search starting in a vertex
     *
     * @param g Graph instance
     * @param vert vertex of graph g that will be the source of the search

     * @return a LinkedList with the vertices of depth-first search
     */
    public static <V, E> LinkedList<V> DepthFirstSearch(Graph<V, E> g, V vert) {
        if (!g.validVertex(vert)) return null;
        LinkedList<V> qdfs = new LinkedList<>();
        boolean[] visited = new boolean[g.numVertices()];

        DepthFirstSearch(g, vert, visited, qdfs);
        return qdfs;
    }

    /**
     * Computes shortest-path distance from a source vertex to all reachable
     * vertices of a graph g with non-negative edge weights
     * This implementation uses Dijkstra's algorithm
     *
     * @param g        Graph instance
     * @param vOrig    Vertex that will be the source of the path
     * @param visited  set of previously visited vertices
     * @param pathKeys minimum path vertices keys
     * @param dist     minimum distances
     */
    private static <V, E> void shortestPathDijkstra(Graph<V, E> g, V vOrig,
                                                    Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                                    boolean[] visited, V [] pathKeys, E [] dist) {
        int vKey = g.key(vOrig);
        dist[vKey]=zero;
        pathKeys[vKey]=vOrig;

        while (vOrig != null){
            vKey = g.key(vOrig);
            visited[vKey] = true;
            for(Edge<V, E> edge : g.outgoingEdges(vOrig)){
                int vKeyAdj = g.key(edge.getVDest());
                if (!visited[vKeyAdj]){
                    E s = sum.apply(dist[vKey], edge.getWeight());
                    if (dist[vKeyAdj]==null || ce.compare(dist[vKeyAdj], s)>0){
                        dist[vKeyAdj] = s;
                        pathKeys[vKeyAdj] = vOrig;
                    }
                }
            }
            E minDist = null;
            vOrig = null;
            for (V vert : g.vertices()){
                int i = g.key(vert);
                if (!visited[i] && dist[i] != null && (minDist == null || ce.compare(dist[i], minDist) < 0)){
                    minDist = dist[i];
                    vOrig = vert;
                }
            }
        }
    }

    /** Shortest-path between two vertices
     *
     * @param g graph
     * @param vOrig origin vertex
     * @param vDest destination vertex
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @param zero neutral element of the sum in elements of type E
     * @param shortPath returns the vertices which make the shortest path
     * @return if vertices exist in the graph and are connected, true, false otherwise
     */
    public static <V, E> E shortestPath(Graph<V, E> g, V vOrig, V vDest,
                                        Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                        LinkedList<V> shortPath) {

        if (!g.validVertex(vOrig) || !g.validVertex(vDest)) return null;

        shortPath.clear();
        int numVerts = g.numVertices();
        boolean[]visited = new boolean[numVerts];
        @SuppressWarnings("unchecked")
        V [] pathKeys = (V[]) new Object[numVerts];
        @SuppressWarnings("unchecked")
        E [] dist = (E[]) new Object[numVerts];
        for (int i =0; i< numVerts; i++){
            dist[i]=null;
            pathKeys[i]=null;
        }
        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathKeys, dist);
        E lengthPath = dist[g.key(vDest)];
        if (lengthPath==null){
            return null;
        }
        getPath(g, vOrig, vDest, pathKeys, shortPath);
        return lengthPath;
    }

    /**
     * Shortest-path between a vertex and all other vertices
     *
     * @param g     graph
     * @param vOrig start vertex
     * @param ce    comparator between elements of type E
     * @param sum   sum two elements of type E
     * @param zero  neutral element of the sum in elements of type E
     * @param paths returns all the minimum paths
     * @param dists returns the corresponding minimum distances
     * @return if vOrig exists in the graph true, false otherwise
     */
    public static <V, E> boolean shortestPaths(Graph<V, E> g, V vOrig, Comparator<E> ce, BinaryOperator<E> sum, E zero,
                                               ArrayList<LinkedList<V>> paths, ArrayList<E> dists) {

        if (!g.validVertex(vOrig)) {
            return false;
        }

        dists.clear();
        paths.clear();

        int numVerts = g.numVertices();

        for (int i = 0; i < numVerts; i++) {
            dists.add(null);
            paths.add(null);
        }

        boolean[] visited = new boolean[numVerts];
        @SuppressWarnings("unchecked")
        V[] pathkeys = (V[]) new Object[numVerts];
        @SuppressWarnings("unchecked")
        E[] dist = (E[]) new Object[numVerts];

        for (int i = 0; i < numVerts; i++) {
            dist[i] = null;
            pathkeys[i] = null;
        }
        //dist -> cada índice é o vetor destino e conteúdo é o custo até esse vértice

        shortestPathDijkstra(g, vOrig, ce, sum, zero, visited, pathkeys, dist);

        for (V vDest : g.vertices()) {
            int v = g.key(vDest);
            if (dist[v] != null) {
                LinkedList<V> shortPath = new LinkedList<>();
                getPath(g, vOrig, vDest, pathkeys, shortPath);
                paths.set(v, shortPath);
                dists.set(v, dist[v]);
            }
        }
        return true;
    }

    /**
     * Extracts from pathKeys the minimum path between voInf and vdInf
     * The path is constructed from the end to the beginning
     *
     * @param g        Graph instance
     * @param vOrig    information of the Vertex origin
     * @param vDest    information of the Vertex destination
     * @param pathKeys minimum path vertices keys
     * @param path     stack with the minimum path (correct order)
     */
    private static <V, E> void getPath(Graph<V, E> g, V vOrig, V vDest,
                                       V[] pathKeys, LinkedList<V> path) {
        if (vDest.equals(vOrig)) {
            path.push(vDest);
        } else {
            path.push(vDest);
            int vKey = g.key(vDest);
            vDest = pathKeys[vKey];
            getPath(g, vOrig, vDest, pathKeys, path);
        }
    }

    /** Calculates the minimum distance graph using Floyd-Warshall
     * 
     * @param g initial graph
     * @param ce comparator between elements of type E
     * @param sum sum two elements of type E
     * @return the minimum distance graph
     */
    public static <V,E> MatrixGraph <V,E> minDistGraph(Graph <V,E> g, Comparator<E> ce, BinaryOperator<E> sum) {
        
        int numVerts = g.numVertices();
        if (numVerts == 0){
            return null;
        }

        @SuppressWarnings("unchecked")
        E[][] m = (E[][]) new Object [numVerts] [numVerts];

        for (int i=0; i<numVerts; i++){
            for (int j=0; j<numVerts; j++){
                Edge<V,E> e = g.edge(i,j);
                if (e != null)
                    m[i][j] = e.getWeight();
            }
        }

        for (int k=0; k<numVerts; k++){
            for (int i=0; i<numVerts; i++){
                if (i != k && m[i][k] != null)
                    for (int j=0; j<numVerts; j++)
                        if (j != i && j != k && m[k][j] != null) {
                            E s = sum.apply(m[i][k], m[k][j]);
                            if ((m[i][j] == null) || ce.compare(m[i][j], s) > 0)
                                m[i][j] = s;
                        }
            }
        }

        return new MatrixGraph<>(!g.isDirected(), g.vertices(), m);
    }

    /**
     * número mínimo de ligações necessário para nesta rede qualquer cliente/produtor
     * conseguir contactar um qualquer outro usando Floyd-Warshall
     *
     * @param g initial graph
     * @return the minimum distance graph
     */
    public static <V,E> int[][] ligacoesMinGrafo(Graph <V,E> g) {

        int numVerts = g.numVertices();
        if (numVerts == 0){
            return null;
        }

        int[][] m = new int [numVerts] [numVerts];

        for (int i=0; i<numVerts; i++){
            for (int j=0; j<numVerts; j++){
                Edge<V,E> e = g.edge(i,j);
                if (e != null)
                    m[i][j] = 1;
            }
        }

        for (int k=0; k<numVerts; k++){
            for (int i=0; i<numVerts; i++){
                if (i != k && m[i][k] != 0)
                    for (int j=0; j<numVerts; j++)
                        if (j != i && j != k && m[k][j] != 0) {
                            int s = m[i][k] + m[k][j];
                            if ((m[i][j] == 0) || m[i][j] > s)
                                m[i][j] = s;
                        }
            }
        }

        return m;
    }

    private static <V,E> void connectComps (Graph<V,E> g, ArrayList<LinkedList<V>> ccs, boolean[] visited){

        int vKey;
        LinkedList<V> conComp = new LinkedList<>();

        for (V vert : g.vertices()){
            vKey = g.key(vert);
            visited[vKey] = false;
        }

        for (V vert : g.vertices()){
            vKey = g.key(vert);
            if (!visited[vKey]) {                           //in some previous BFS
                conComp = BreadthFirstSearch(g, vert);      //discovers precisely V’s
                ccs.add(conComp);                           //connected component
            }
            assert conComp != null;
            for (V v : conComp) {
                vKey = g.key(v);
                visited[vKey] = true;
            }
        }
    }

    //se existir algum vértice que não faz parte do arrayList conComp, então o grafo não é conexo

    public static <V, E> boolean connectComps(Graph<V, E> g) {

        ArrayList<LinkedList<V>> ccs = new ArrayList<>();

        boolean[] visited = new boolean[g.numVertices()];

        connectComps(g, ccs, visited);
        /*se o primeiro elemento (LinkedList) de css tiver o número de elementos igual
        ao número de vértices de g então o grafo é conexo*/
        return ccs.get(0).size() == g.numVertices();

    }

    public static <V,E> Graph<V,E> kruskall (Graph<V,E> g) {
        if (!connectComps(g)) {
            return null;
        }

        Graph<V, E> mst = new MapGraph<>(false);

        ArrayList<Edge<V, E>> lstEdges = new ArrayList<>();
        LinkedList<V> connectedVerts;

        V vOrig;
        V vDst;

        for (V vert : g.vertices()) {
            mst.addVertex(vert);
        }

        for (Edge<V, E> edge : g.edges()) {
            lstEdges.add(edge);
        }

        Comparator<Edge<V, E>> c = new Comparator<Edge<V, E>>() {
            @Override
            public int compare(Edge<V, E> o1, Edge<V, E> o2) {
                if (o1.getWeight().hashCode() < o2.getWeight().hashCode()) return -1;
                else if (o1.getWeight().hashCode() > o2.getWeight().hashCode()) return +1;
                else return 0;
            }
        };

        lstEdges.sort(c);        // in ascending order of weight

        for (Edge<V, E> e : lstEdges) {
            vOrig = e.getVOrig();
            vDst = e.getVDest();
            connectedVerts = DepthFirstSearch(mst, vOrig);
            assert connectedVerts != null;
            if (!connectedVerts.contains(vDst))
                mst.addEdge(vOrig, vDst, e.getWeight());
        }

        return mst;

    }




}