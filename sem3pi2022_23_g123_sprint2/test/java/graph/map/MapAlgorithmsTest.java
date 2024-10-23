package graph.map;

import graph.Algorithms;
import graph.Edge;
import graph.Graph;
import graph.matrix.MatrixGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MapAlgorithmsTest {

    final Graph<String, Integer> completeMap = new MapGraph<>(false);
    Graph<String, Integer> incompleteMap = new MapGraph<>(false);

    public MapAlgorithmsTest() {
    }

    @BeforeEach
    public void setUp() {

        completeMap.addVertex("Porto");
        completeMap.addVertex("Braga");
        completeMap.addVertex("Vila Real");
        completeMap.addVertex("Aveiro");
        completeMap.addVertex("Coimbra");
        completeMap.addVertex("Leiria");

        completeMap.addVertex("Viseu");
        completeMap.addVertex("Guarda");
        completeMap.addVertex("Castelo Branco");
        completeMap.addVertex("Lisboa");
        completeMap.addVertex("Faro");

        completeMap.addEdge("Porto", "Aveiro", 75);
        completeMap.addEdge("Porto", "Braga", 60);
        completeMap.addEdge("Porto", "Vila Real", 100);
        completeMap.addEdge("Viseu", "Guarda", 75);
        completeMap.addEdge("Guarda", "Castelo Branco", 100);
        completeMap.addEdge("Aveiro", "Coimbra", 60);
        completeMap.addEdge("Coimbra", "Lisboa", 200);
        completeMap.addEdge("Coimbra", "Leiria", 80);
        completeMap.addEdge("Aveiro", "Leiria", 120);
        completeMap.addEdge("Leiria", "Lisboa", 150);

        incompleteMap = completeMap.clone();

        completeMap.addEdge("Aveiro", "Viseu", 85);
        completeMap.addEdge("Leiria", "Castelo Branco", 170);
        completeMap.addEdge("Lisboa", "Faro", 280);
    }

    private void checkContentEquals(List<String> l1, List<String> l2, String msg) {
        Collections.sort(l1);
        Collections.sort(l2);
        assertEquals(l1, l2, msg);
    }

    /**
     * Test of BreadthFirstSearch method, of class Algorithms.
     */
    @Test
    public void testBreadthFirstSearch() {
        System.out.println("Test BreadthFirstSearch");

        Assertions.assertNull(Algorithms.BreadthFirstSearch(completeMap, "LX"), "Should be null if vertex does not exist");

        LinkedList<String> path = Algorithms.BreadthFirstSearch(incompleteMap, "Faro");

        assertEquals(1, path.size(), "Should be just one");

        assertEquals("Faro", path.peekFirst());

        path = Algorithms.BreadthFirstSearch(incompleteMap, "Porto");
        assertEquals(7, path.size(), "Should give seven vertices");

        assertEquals("Porto", path.removeFirst(), "BreathFirst Porto");

        LinkedList<String> expected = new LinkedList<>(Arrays.asList("Aveiro", "Braga", "Vila Real"));
        checkContentEquals(expected, path.subList(0, 3), "BreathFirst Porto");

        expected = new LinkedList<>(Arrays.asList("Coimbra", "Leiria"));
        checkContentEquals(expected, path.subList(3, 5), "BreathFirst Porto");

        expected = new LinkedList<>(Arrays.asList("Lisboa"));
        checkContentEquals(expected, path.subList(5, 6), "BreathFirst Porto");

        path = Algorithms.BreadthFirstSearch(incompleteMap, "Viseu");
        expected = new LinkedList<>(Arrays.asList("Viseu", "Guarda", "Castelo Branco"));
        assertEquals(expected, path, "BreathFirst Viseu");
    }

    /**
     * Test of DepthFirstSearch method, of class Algorithms.
     */
    @Test
    public void testDepthFirstSearch() {
        System.out.println("Test of DepthFirstSearch");

        assertNull(Algorithms.DepthFirstSearch(completeMap, "LX"), "Should be null if vertex does not exist");

        LinkedList<String> path = Algorithms.DepthFirstSearch(incompleteMap, "Faro");
        assertEquals(1, path.size(), "Should be just one");

        assertEquals("Faro", path.peekFirst());

        path = Algorithms.BreadthFirstSearch(incompleteMap, "Porto");
        assertEquals(7, path.size(), "Should give seven vertices");

        assertEquals("Porto", path.removeFirst(), "DepthFirst Porto");
        assertTrue(new LinkedList<>(Arrays.asList("Aveiro", "Braga", "Vila Real")).contains(path.removeFirst()), "DepthFirst Porto");

        path = Algorithms.BreadthFirstSearch(incompleteMap, "Viseu");
        List<String> expected = new LinkedList<>(Arrays.asList("Viseu", "Guarda", "Castelo Branco"));
        assertEquals(expected, path, "DepthFirst Viseu");
    }

    /**
     * Test of shortestPath method, of class Algorithms.
     */
    @Test
    public void testShortestPath() {
        System.out.println("Test of shortest path");

        LinkedList<String> shortPath = new LinkedList<>();

        Integer lenPath = Algorithms.shortestPath(completeMap, "Porto", "LX", Integer::compare, Integer::sum, 0, shortPath);
        assertNull(lenPath, "Length path should be null if vertex does not exist");
        assertEquals(0, shortPath.size(), "Shortest Path does not exist");

        lenPath = Algorithms.shortestPath(incompleteMap, "Porto", "Faro", Integer::compare, Integer::sum, 0, shortPath);
        assertNull(lenPath, "Length path should be null if vertex does not exist");
        assertEquals(0, shortPath.size(), "Shortest Path does not exist");

        lenPath = Algorithms.shortestPath(completeMap, "Porto", "Porto", Integer::compare, Integer::sum, 0, shortPath);
        assertEquals(0, lenPath, "Length path should be 0 if vertices are the same");
        assertEquals(Arrays.asList("Porto"), shortPath, "Shortest Path only contains Porto");

        lenPath = Algorithms.shortestPath(incompleteMap, "Porto", "Lisboa", Integer::compare, Integer::sum, 0, shortPath);
        assertEquals(335, lenPath, "Length path should be 0 if vertices are the same");
        assertEquals(Arrays.asList("Porto", "Aveiro", "Coimbra", "Lisboa"), shortPath, "Shortest Path Porto - Lisboa");

        lenPath = Algorithms.shortestPath(incompleteMap, "Braga", "Leiria", Integer::compare, Integer::sum, 0, shortPath);
        assertEquals(255, lenPath, "Length path should be 0 if vertices are the same");
        assertEquals(Arrays.asList("Braga", "Porto", "Aveiro", "Leiria"), shortPath, "Shortest Path Braga - Leiria");

        lenPath = Algorithms.shortestPath(completeMap, "Porto", "Castelo Branco", Integer::compare, Integer::sum, 0, shortPath);
        assertEquals(335, lenPath, "Length path should be 0 if vertices are the same");
        assertEquals(Arrays.asList("Porto", "Aveiro", "Viseu", "Guarda", "Castelo Branco"), shortPath, "Shortest Path Porto - Castelo Branco");

        //Changing Edge: Aveiro-Viseu with Edge: Leiria-C.Branco
        //should change shortest path between Porto and Castelo Branco

        completeMap.removeEdge("Aveiro", "Viseu");
        completeMap.addEdge("Leiria", "Castelo Branco", 170);

        lenPath = Algorithms.shortestPath(completeMap, "Porto", "Castelo Branco", Integer::compare, Integer::sum, 0, shortPath);
        assertEquals(365, lenPath, "Length path should be 0 if vertices are the same");
        assertEquals(Arrays.asList("Porto", "Aveiro", "Leiria", "Castelo Branco"), shortPath, "Shortest Path Porto - Castelo Branco");
    }

    /**
     * Test of shortestPaths method, of class Algorithms.
     */
    @Test
    public void testShortestPaths() {
        System.out.println("Test of shortest path");

        ArrayList<LinkedList<String>> paths = new ArrayList<>();
        ArrayList<Integer> dists = new ArrayList<>();

        Algorithms.shortestPaths(completeMap, "Porto", Integer::compare, Integer::sum, 0, paths, dists);

        assertEquals(paths.size(), dists.size(), "There should be as many paths as sizes");
        assertEquals(completeMap.numVertices(), paths.size(), "There should be a path to every vertex");
        assertEquals(Arrays.asList("Porto"), paths.get(completeMap.key("Porto")), "Number of nodes should be wwww if source and vertex are the same");
        assertEquals(Arrays.asList("Porto", "Aveiro", "Coimbra", "Lisboa"), paths.get(completeMap.key("Lisboa")), "Path to Lisbon");
        assertEquals(Arrays.asList("Porto", "Aveiro", "Viseu", "Guarda", "Castelo Branco"), paths.get(completeMap.key("Castelo Branco")), "Path to Castelo Branco");
        assertEquals(335, dists.get(completeMap.key("Castelo Branco")), "Path between Porto and Castelo Branco should be 335 Km");

        //Changing Edge: Aveiro-Viseu with Edge: Leiria-C.Branco
        //should change shortest path between Porto and Castelo Branco
        completeMap.removeEdge("Aveiro", "Viseu");
        completeMap.addEdge("Leiria", "Castelo Branco", 170);
        Algorithms.shortestPaths(completeMap, "Porto", Integer::compare, Integer::sum, 0, paths, dists);
        assertEquals(365, dists.get(completeMap.key("Castelo Branco")), "Path between Porto and Castelo Branco should now be 365 Km");
        assertEquals(Arrays.asList("Porto", "Aveiro", "Leiria", "Castelo Branco"), paths.get(completeMap.key("Castelo Branco")), "Path to Castelo Branco");

        Algorithms.shortestPaths(incompleteMap, "Porto", Integer::compare, Integer::sum, 0, paths, dists);
        assertNull(dists.get(completeMap.key("Faro")), "Length path should be null if there is no path");
        assertEquals(335, dists.get(completeMap.key("Lisboa")), "Path between Porto and Lisboa should be 335 Km");
        assertEquals(Arrays.asList("Porto", "Aveiro", "Coimbra", "Lisboa"), paths.get(completeMap.key("Lisboa")), "Path to Lisboa");

        Algorithms.shortestPaths(incompleteMap, "Braga", Integer::compare, Integer::sum, 0, paths, dists);
        assertEquals(255, dists.get(completeMap.key("Leiria")), "Path between Braga and Leiria should be 255 Km");
        assertEquals(Arrays.asList("Braga", "Porto", "Aveiro", "Leiria"), paths.get(completeMap.key("Leiria")), "Path to Leiria");
    }
    
    /**
     * Test minimum distance graph using Floyd-Warshall.
     */
    @Test
    public void testminDistGraph() {

        System.out.println("Test Floyd-Warshall Algorithm");

        MatrixGraph<String,Integer> newMap = Algorithms.minDistGraph(incompleteMap, Integer::compare, Integer::sum);

        //Faro is not connected to other cities
        assertEquals(0, newMap.outDegree("Faro"), "Faro should have no connections");

        //Viseu connects two cities
        assertEquals(2, newMap.outDegree("Viseu"), "Faro should have t connections");

        //Viseu should be directly connected to Guarda and Castelo Branco
        Collection<String> cs = newMap.adjVertices("Viseu");
        assertTrue(cs.contains("Guarda"), "Viseu should be connected to Guarda");
        assertTrue(cs.contains("Castelo Branco"), "Viseu should be connected to Castelo Branco");

        //Porto should be connected to 6 other cities
        assertEquals(6, newMap.outDegree("Porto"), "Porto should have 6 connections");

        cs = newMap.adjVertices("Porto");
        assertTrue(cs.contains("Braga"), "Porto should be connected to Braga");
        assertTrue(cs.contains("Vila Real"), "and Vila Real");
        assertTrue(cs.contains("Aveiro"), "and Aveiro");
        assertTrue(cs.contains("Coimbra"), "and Coimbra");
        assertTrue(cs.contains("Leiria"), "and Leiria");
        assertTrue(cs.contains("Lisboa"), "and Lisboa");

        LinkedList<String> shortPath = new LinkedList<>();

        Integer lenPath = Algorithms.shortestPath(incompleteMap, "Porto", "Leiria", Integer::compare, Integer::sum, 0, shortPath);
        assertEquals(lenPath, newMap.edge("Porto", "Leiria").getWeight(), "Path between Porto and Leiria should be 19.");
    }

    //assertEquals(true, cs.contains("Lisboa"), "and Lisboa"); == assertTrue(cs.contains("Lisboa"), "and Lisboa");

    /**
     * Test if graph is connect.
     */
    @Test
    public void testConnectComps() {
        System.out.println("Test Connect Comps");

        //o grafo incompleto não é conexo
        //[[Porto, Aveiro, Braga, Vila Real, Coimbra, Leiria, Lisboa], [Viseu, Guarda, Castelo Branco], [Faro]]
        boolean result = Algorithms.connectComps(incompleteMap);
        assertFalse(result, "Grafo não conexo");

        //o grafo completo é conexo
        //[[Porto, Aveiro, Braga, Vila Real, Coimbra, Leiria, Viseu, Lisboa, Castelo Branco, Guarda, Faro]]
        result = Algorithms.connectComps(completeMap);
        assertTrue(result, "Grafo conexo");

    }

    /**
     * Testa caminho de custo mínimo de um grafo.
     */
    @Test
    public void testKruskall() {

        System.out.println("Test Kruskall");

        Graph<String, Integer> kruskalComplete = Algorithms.kruskall(completeMap);
        Collection<Edge<String, Integer>> res;

        Edge<String, Integer> um = new Edge<>("Porto", "Aveiro", 75);
        Edge<String,Integer> dois = new Edge<>("Porto", "Braga", 60);
        Edge<String,Integer> tres = new Edge<>("Porto", "Vila Real", 100);
        Edge<String,Integer> quatro = new Edge<>("Viseu", "Guarda", 75);
        Edge<String,Integer> cinco = new Edge<>("Coimbra", "Leiria", 80);
        Edge<String,Integer> seis = new Edge<>("Aveiro", "Viseu", 85);
        Edge<String,Integer> sete = new Edge<>("Guarda", "Castelo Branco", 100);
        Edge<String,Integer> oito = new Edge<>("Aveiro", "Coimbra", 60);
        Edge<String,Integer> nove = new Edge<>("Lisboa", "Faro", 280);


        //o grafo tem 18 arestas (9*t)
        assertEquals(20, kruskalComplete.numEdges(), "O grafo tem 18 arestas, 9*t uma vez que é não direcionado");

        //o Grafo incompleto retorna null já que não é conexo.
        Graph<String, Integer> kruskalIncomplete = Algorithms.kruskall(incompleteMap);
        assertNull(kruskalIncomplete);

        //Porto connects Aveiro, Vila Real, Braga
        res = kruskalComplete.outgoingEdges("Porto");
        assertTrue(res.contains(um));
        assertTrue(res.contains(dois));
        assertTrue(res.contains(tres));

        //Aveiro connects Viseu, Coimbra
        res = kruskalComplete.outgoingEdges("Aveiro");
        assertTrue(res.contains(seis));
        assertTrue(res.contains(oito));

        //Lisboa connects Faro
        res = kruskalComplete.outgoingEdges("Lisboa");
        assertTrue(res.contains(nove));

        //Guarda connects Castelo Branco
        res = kruskalComplete.outgoingEdges("Guarda");
        assertTrue(res.contains(sete));

        //Leiria connects Coimbra
        res = kruskalComplete.outgoingEdges("Coimbra");
        assertTrue(res.contains(cinco));

        //Viseu connects Guarda
        res = kruskalComplete.outgoingEdges("Viseu");
        assertTrue(res.contains(quatro));

    }

}