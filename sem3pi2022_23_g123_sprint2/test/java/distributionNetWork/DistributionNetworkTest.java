package java.distributionNetWork;

import distributionNetWork.DistributionNetwork;
import distributionNetWork.Empresa;
import distributionNetWork.Localizacao;
import distributionNetWork.User;
import graph.Algorithms;
import graph.Graph;
import graph.map.MapGraph;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DistributionNetworkTest {

    private final DistributionNetwork distributionNetwork = new DistributionNetwork();

    private List<User> testList = new ArrayList<>();

    private Graph<User,Float> netWorkGraph = new MapGraph<>(false);
    private List<User> realList = new ArrayList<>();

    @BeforeEach
    void setUp() {

        Localizacao localizacao = new Localizacao("CT1",40.6389F,-8.6553F);
        User user = new User(localizacao,"C1");
        Localizacao localizacao1 = new Localizacao("CT2",38.0333F,-7.8833F);
        User user1 = new User(localizacao1,"C2");
        Localizacao localizacao2 = new Localizacao("CT3",41.5333F,-8.4167F);
        User user2 = new User(localizacao2,"C3");
        Localizacao localizacao3 = new Localizacao("CT5",39.823F,-7.4931F);
        Empresa empresa = new Empresa(localizacao3,"E3");

        testList.add(user);
        testList.add(user1);
        testList.add(user2);
        testList.add(empresa);



    }
    @Test
    void testReadMethod(){
        distributionNetwork.read("files\\input\\small\\clientes-produtores_small_test.csv");
        assertEquals(4,distributionNetwork.getNetworkGraph().numVertices());
        //assertEquals(testList,list);
    }

    @Test
    void testReadDistances(){
        distributionNetwork.read("files\\input\\small\\clientes-produtores_small.csv");
        assertEquals(17,distributionNetwork.getNetworkGraph().numVertices());
        distributionNetwork.readDistances("files\\input\\small\\distancias_small.csv");
        assertEquals(66,distributionNetwork.getNetworkGraph().numEdges());
    }

    @Test
    void networkHubs() {
        Map<Empresa,Float> map = new HashMap<>();

        distributionNetwork.read("files\\input\\small\\clientes-produtores_small.csv");
        distributionNetwork.readDistances("files\\input\\small\\distancias_small.csv");
        Graph<User, Float> networkGraph = distributionNetwork.getNetworkGraph();
        int n=3;

        Map<Empresa, Float> hubCompanies = distributionNetwork.networkHubs(n);
        System.out.println("\nHubs: \n");
        for(Empresa c : hubCompanies.keySet()){
            System.out.println("Empresa: " + c.getCodUser() + "; Distância média: " + hubCompanies.get(c));
        }


        System.out.println("\nLista da distância média de todas as empresas:\n");
        float sum;
        ArrayList<LinkedList<User>> paths = new ArrayList<>();
        ArrayList<Float> dists = new ArrayList<>();
        for(User u : networkGraph.vertices()){
            sum=0;
            if(u.getClass().equals(Empresa.class)){
                Algorithms.shortestPaths(networkGraph,u,Float::compare,Float::sum,0F,paths,dists);
                for(Float f : dists){
                    sum+=f;
                }
                map.put((Empresa) u,sum/networkGraph.numVertices());
            }
        }
        map=map.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)->e1, LinkedHashMap::new));
        for(Empresa c : map.keySet()){
            System.out.println("Empresa: " + c.getCodUser() + "; Distância média: " + map.get(c));
        }
    }

   @Test //US304
    void getClientsNearestHubTestSmall() {
        distributionNetwork.read("files\\input\\small\\clientes-produtores_small.csv");
        distributionNetwork.readDistances("files\\input\\small\\distancias_small.csv");

        int n=3;
        distributionNetwork.networkHubs(n);

        Map<User, Empresa> clientsNearestHub = distributionNetwork.getClientsNearestHub();
        Map<String, String> nearestHub = new HashMap<>();
        for (User user : clientsNearestHub.keySet()){
            nearestHub.put(user.getCodUser(), clientsNearestHub.get(user).getCodUser());
        }

        Map<String, String> expected = new HashMap<>();
        expected.put("C1","E4");
        expected.put("C2","E2");
        expected.put("C3","E4");
        expected.put("C4","E4");
        expected.put("C5","E4");
        expected.put("C6","E4");
        expected.put("C7","E2");
        expected.put("C8","E2");
        expected.put("C9","E2");
        expected.put("E1","E2");
        expected.put("E2","E2");
        expected.put("E3","E3");
        expected.put("E4","E4");
        expected.put("E5","E4");

        assertEquals(expected, nearestHub, "All the nearest hubs are correct");
        assertEquals(expected.size(), clientsNearestHub.size(), "The sizes are equal");
        nearestHub.remove("C1");
        nearestHub.remove("C7");
        nearestHub.remove("E4");

        expected.remove("C1");
        expected.remove("C7");
        expected.remove("E4");

        assertEquals(expected, nearestHub, "All the nearest hubs are correct");
        assertEquals(expected.size(), nearestHub.size(), "The sizes are equal");

        System.out.println("\ngetClientsNearestHubTestSmall passed");
    }

    @Test
    public void readCabazTeste(){
        distributionNetwork.read("files\\input\\small\\clientes-produtores_small.csv");
        distributionNetwork.readCabaz("files\\input\\small\\cabazes_small.csv");

        System.out.println("Linhas do ficheiro referentes ao cliente C1:\n");
        try (BufferedReader input = new BufferedReader(new FileReader("files\\input\\small\\cabazes_small.csv"))){
            String line;
            input.readLine();
            while ((line = input.readLine()) != null){
                String separator = ",";
                String[] lineFields = line.split(separator);
                if(lineFields[0].substring(1,3).equalsIgnoreCase("C1")){
                    System.out.println(line);
                }
            }
        } catch (IOException e) {       //O(1)
            throw new RuntimeException(e);
        }

        System.out.println("\nCabazes do cliente C1 depois da importação do ficheiro:\n");
        for(User u : distributionNetwork.getNetworkGraph().vertices()){
            if(u.getCodUser().equalsIgnoreCase("C1")){
                System.out.println(u.getCabaz());
            }
        }
    }
}