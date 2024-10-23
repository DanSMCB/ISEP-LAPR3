package distributionNetWork;

import graph.Algorithms;
import graph.Edge;
import graph.Graph;
import graph.map.MapGraph;
import javafx.util.Pair;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DistributionNetwork {
    private MapGraph<User,Float> networkGraph;
    Map<Empresa,Float> hubs = new HashMap<>();

    public DistributionNetwork()  {}

    public void read(String filename) {
        String line;
        networkGraph = new MapGraph<>(false);

        try (BufferedReader input = new BufferedReader(new FileReader(filename))){
            input.readLine();

            while((line = input.readLine()) != null){
                String separator = ",";
                String[] lineFields = line.split(separator);

                Localizacao localizacao = new Localizacao(lineFields[0],Float.valueOf(lineFields[1]),Float.valueOf(lineFields[2]));
                if(lineFields[3].contains("E")){
                    Empresa empresa = new Empresa(localizacao,lineFields[3]);
                    if(!networkGraph.validVertex(empresa)){
                        networkGraph.addVertex(empresa);
                    }
                }else if (lineFields[3].contains("P")){
                    Produtor produtor = new Produtor(localizacao,lineFields[3]);
                    if(!networkGraph.validVertex(produtor)){
                        networkGraph.addVertex(produtor);
                    }
                }else if (lineFields[3].contains("C")) {
                    Cliente cliente = new Cliente(localizacao, lineFields[3]);
                    if (!networkGraph.validVertex(cliente)) {
                        networkGraph.addVertex(cliente);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readDistances(String filename){
        String line;
        try (BufferedReader input = new BufferedReader(new FileReader(filename))){
            input.readLine();

            while((line = input.readLine()) != null){
                String separator = ",";
                String[] lineFields = line.split(separator);
                for(User u : networkGraph.vertices()){
                    if(u.getLocalization().getCodLoc().equalsIgnoreCase(lineFields[0])){
                        for(User u1: networkGraph.vertices()){
                            if(u1.getLocalization().getCodLoc().equalsIgnoreCase(lineFields[1])){
                                networkGraph.addEdge(u,u1,Float.valueOf(lineFields[2]));
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Graph<User, Float> getNetworkGraph() {
        return networkGraph;
    }

    //US307
    public void readCabaz(String filename){
        String line;

        try (BufferedReader input = new BufferedReader(new FileReader(filename))){      //O(1)
            input.readLine();

            while((line = input.readLine()) != null) {       //O(n)
                List<Float> produtos = new ArrayList<>();

                String separator = ",";
                String[] lineFields = line.split(separator);

                for( int i = 2 ; i < lineFields.length;i++){
                    produtos.add(Float.parseFloat(lineFields[i]));
                }
                Cabaz cabaz = new Cabaz(Integer.parseInt(lineFields[1]),produtos);

                for(User u : networkGraph.vertices()){
                    if (lineFields[0].substring(1,3).equalsIgnoreCase(u.getCodUser())) {
                        u.addCabaz(cabaz);
                    }
                }
            }

        } catch (IOException e) {       //O(1)
            throw new RuntimeException(e);
        }

    }

    //US303
    public Map<Empresa, Float> networkHubs(int n){
        hubs.clear();
        float sum;
        ArrayList<Float> dists = new ArrayList<>();

        for(User u : networkGraph.vertices()){
            sum=0;
            if(u.getClass().equals(Empresa.class)){
                Algorithms.shortestPaths(networkGraph,u,Float::compare,Float::sum,0F,new ArrayList<>(),dists);
                for(Float f : dists){
                    sum+=f;
                }
                hubs.put((Empresa) u,sum/(networkGraph.numVertices()-1));
            }
        }
        hubs=hubs.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(n).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)->e1, LinkedHashMap::new));

        for(Empresa c : hubs.keySet()){
            c.setHubTrue();
        }

        return hubs;
    }


    /** US304
     * Determine the nearest hub for each client (particular or empresarial)
     * @return clientNearestHub a map where the keys are the clients and the values are the nearest hub
     */
    public Map<User, Empresa> getClientsNearestHub (){
        Map<User, Empresa> clientNearestHub = new HashMap<>();

        Map<Empresa, Float> lengthPaths = new HashMap<>();

        for (User client : networkGraph.vertices()){
            // percorrer clientes e empresas, condição para não percorrer para produtores
            if(!client.getClass().equals(Produtor.class)){
                for (Empresa hub : hubs.keySet()){
                    Float lenPath = Algorithms.shortestPath(networkGraph,client,hub,Float::compare,Float::sum,0F,new LinkedList<>());
                    lengthPaths.put(hub, lenPath);
                }
                Stream<Map.Entry<Empresa, Float>> sorted = lengthPaths.entrySet().stream().sorted(Map.Entry.comparingByValue());

                clientNearestHub.put(client, sorted.findFirst().get().getKey());
                lengthPaths.clear();
            }
        }
        clientNearestHub=clientNearestHub.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2)->e1,LinkedHashMap::new));
        return clientNearestHub;
    }

    //US305
    public float redeDistanciaMin (Graph<User, Float> g){
        Graph<User, Float> mst = Algorithms.kruskall(g);
        float distance = 0;
        if (mst == null){
            System.out.println("\no grafo não é conexo");
        } else {
            for (Edge<User, Float> edge : mst.edges()) {
                distance = distance + edge.getWeight();
            }
            System.out.println("\nRede que conecte todos os users com uma distância total mínima:\n" );
            printfGrafo(mst);
        }
        return distance/2;
    }

    //US302
    public int numeroLigacoesMin (Graph<User, Float> g){
        int diametro = 0;
        //LinkedList<User> pathRes = null;
        for (User v : g.vertices()){
            ArrayList<LinkedList<User>> paths = new ArrayList<>();
            ArrayList<Float> dists = new ArrayList<>();
            Algorithms.shortestPaths(g, v, Float::compare, Float::sum, 0f, paths, dists);

            for (LinkedList<User> path : paths) {
                if (path.size() > diametro) {
                    diametro = path.size()-1;
                    //pathRes=path;
                }
            }
        }
        //System.out.println(pathRes);
        return diametro;
    }

    public void printfGrafo(Graph<User, Float> g){
        for (User vOrig : g.vertices()){
            for (User vDest : g.adjVertices(vOrig)) {
                System.out.println("vértice Origem: " + vOrig + "\n");
                System.out.println("vértice Destino: " + vDest + "\n");
                System.out.println("Distância " + g.edge(vOrig, vDest).getWeight() + "\n");
                System.out.println("\n");
            }
        }
    }


    /**
     * US309
     * Percorrer todos os hubs existentes - Map<Company, Float> hubs
     * Invocar o método shortestPath() para ver as distâncias de cada produtor aos hub existentes
     * Guardar os n mais próximos map auxiliar e ordenar por distâncias, de forma ascendente
     * Guardar no map de retorno os n primeiros produtores
     * @param n número de produtores agrícolas mais próximos do hub do cliente
     * @return hubProds map em que a chave são os hubs e os valores correspondentes um arrayList com os produtores mais próximos
     */
    public Map<Empresa,ArrayList<Produtor>> getNearestProducers (int n, Map<Empresa, Float> hubs){
        ArrayList<Produtor> produtores;
        Map<User, Float> lengthPaths = new HashMap<>();
        Map<Empresa,ArrayList<Produtor>> hubProds = new HashMap<>();

        for (Empresa hub : hubs.keySet()){
            for (User produtor : networkGraph.vertices()) {
                if (produtor.getClass().equals(Produtor.class)) {
                    Float lenPath = Algorithms.shortestPath(networkGraph, hub, produtor, Float::compare, Float::sum, 0F, new LinkedList<>());
                    lengthPaths.put(produtor, lenPath);
                }
            }
            lengthPaths = lengthPaths.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(n).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                                                                                                                         (e1, e2)->e1, LinkedHashMap::new));

            produtores = new ArrayList<>();
            for(User prod : lengthPaths.keySet()){
                produtores.add((Produtor) prod);
            }
            hubProds.put(hub, produtores);
            lengthPaths.clear();
        }
        return hubProds;
    }

    /**
     * US309
     * Gerar uma lista de expedição para um determinado dia que fornecida com os N produtores mais próximos
     * Caso a quantidade encomendada seja igual a 0.0, então o produto não aparece na lista
     * Caso não exista quantidade para expedir então aparece a mensagem "Não há produto para expedir"
     * A lista é exportada para um ficheiro através do método ficheiroOutput()
     * @param n número de produtores agrícolas mais próximos do hub do cliente
     * @param dia determinado dia do cabaz
     * @param filename nome do ficheiro para qual a lista vai ser exportada
     */
    public Map<User, ArrayList<Produtor>> listaExpedicaoRestricao (int n, int dia, String filename){
        Map<User, ArrayList<Produtor>> lista = new HashMap<>();
        int nProd=0;
        float aux;
        String prod="";
        Produtor produtor;
        Map<Empresa,ArrayList<Produtor>> hubProds = getNearestProducers(n, hubs);
        StringBuilder output = new StringBuilder();
        StringBuilder output2;

        output.append(dia).append("\n");
        for (User client : getClientsNearestHub().keySet()){
            boolean next = false;
            int i = 0;
            output2 = new StringBuilder();
            for (Cabaz cabaz : client.getCabaz()){
                if (cabaz.getDia() == dia){
                    output.append(client.getCodUser()).append(", ").append(getClientsNearestHub().get(client).getCodUser()).append("\n");
                    ArrayList<Produtor> produtores = new ArrayList<>();
                    for (Float produto : cabaz.getProdutos()){
                        produtor=null;
                        aux=0;
                        if(produto!=0){
                            nProd++;
                            for(Produtor p : hubProds.get(getClientsNearestHub().get(client))){
                                for(Cabaz c : p.getCabaz()){
                                    if(c.getDia()==dia){
                                        if(c.getProdutos().get(i)>=produto){
                                            produtor=p;
                                            prod=produto.toString();
                                            next=true;
                                            break;
                                        }
                                        if(c.getProdutos().get(i)>aux && c.getProdutos().get(i)<produto){
                                            produtor=p;
                                            prod=c.getProdutos().get(i).toString();
                                            aux=c.getProdutos().get(i);
                                        }
                                    }
                                }
                                if(next) break;
                            }

                            //diminuir ao produtor a quantidade fornecida do produto requesitado pelo cliente
                            if(produtor!=null){
                                for(Cabaz c : produtor.getCabaz()){
                                    if(c.getDia()==dia) {
                                        c.getProdutos().set(i,c.getProdutos().get(i)-produto);
                                    }
                                }
                            }


                            output2.append("\n").append("Produto ").append(i+1).append(",").append(produto).append(", ");
                            if(produtor!=null){
                                output2.append(prod).append(", ").append(produtor.getCodUser());
                                produtores.add(produtor);
                            }else{
                                output2.append("Não há produto para expedir");
                                produtores.add(null);
                            }
                        }
                        i++;
                    }
                    lista.put(client, produtores);
                    output.append(nProd).append(output2).append("\n");

                    nProd=0;
                }
            }
        }

        ficheiroOutput(filename, output.toString());

        return lista;
    }


    //US311
   public void estatisticasCabaz(String inputFile, String outputFile){
        int nProdS=0, nProdPS=0, nProdNS=0;
        float qProdReq=0, qProdFor=0;
        ArrayList<String> produtores = new ArrayList<>();
        StringBuilder output= new StringBuilder();

        output.append("--------------Estatísticas por cabaz/cliente--------------").append("\n").append("\n");

        try (BufferedReader input = new BufferedReader(new FileReader(inputFile))){

            String line=input.readLine();
            String separator = ",";
            String[] lineFields = line.split(separator);
            output.append("Dia ").append(lineFields[0]).append("\n").append("\n");

            while((line = input.readLine()) != null){
                lineFields = line.split(separator);
                StringBuilder output2 = new StringBuilder();
                output2.append("Cabaz -> ").append("Cliente: ").append(lineFields[0]).append("; Hub: ").append(lineFields[1]);

                line=input.readLine();
                lineFields = line.split(separator);
                int lenght = Integer.parseInt(lineFields[0]);

                if(lenght!=0){
                    for(int i=0;i<lenght;i++){
                        line=input.readLine();
                        lineFields = line.split(separator);

                        if(lineFields.length==4){
                            if(Float.parseFloat(lineFields[1])>Float.parseFloat(lineFields[2]) && Float.parseFloat(lineFields[2])!=0){
                                nProdPS++;
                            }else if(Float.parseFloat(lineFields[1])==Float.parseFloat(lineFields[2])){
                                nProdS++;
                            }
                            if(!produtores.contains(lineFields[3])){
                                produtores.add(lineFields[3]);
                            }
                            qProdFor+=Float.parseFloat(lineFields[2]);
                        }else{nProdNS++;}
                        qProdReq+=Float.parseFloat(lineFields[1]);

                    }

                    output.append(output2).append("\n").append("nº de produtos totalmente satisfeitos: ").append(nProdS).append("\n")
                            .append("nº de produtos parcialmente satisfeitos: ").append(nProdPS).append("\n").append("nº de produtos não satisfeitos: ")
                            .append(nProdNS).append("\n").append("percentagem total do cabaz satisfeito: ").append((qProdFor/qProdReq)*100).append("%")
                            .append("\n").append("nº de produtores que forneceram o cabaz: ").append(produtores.size()).append("\n").append("\n");
                }

                nProdS=0;
                nProdPS=0;
                nProdNS=0;
                qProdFor=0;
                qProdReq=0;
                produtores.clear();
            }
            estatisticasProdutorHub(inputFile, outputFile, output);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void estatisticasProdutorHub(String inputFile, String outputFile, StringBuilder output) {
        String line, cliente, hub;

        ArrayList<Pair<String, ArrayList<String>>> prodCliente = new ArrayList<>(); //vai registar cada produtor juntamente com uma lista dos clientes a quem forneceram algum produto

        ArrayList<Pair<String, ArrayList<String>>> prodHub = new ArrayList<>();     //vai registar cada produtor juntamente com uma lista dos hubs onde forneceram algum produto

        ArrayList<Pair<String, Pair<Integer, Integer>>> prodNC = new ArrayList<>(); //vai registar cada produtor juntamente com um par de ints em que a chave vai guardar
                                                                                        //nº de cabazes fornecidos totalmente e o valor o nº de cabazes fornecidos parcialmente

        ArrayList<String> hubsLista = new ArrayList<>();                            //vai registar todos os hubs presentes na lista de expedição

        ArrayList<Pair<String, ArrayList<String>>> hubProd = new ArrayList<>();     //vai registar cada hub juntamente com uma lista dos produtores que forneceram algum produto neste

        ArrayList<Pair<String, ArrayList<String>>> hubCliente = new ArrayList<>();  //vai registar cada hub juntamente com uma lista dos clientes que foram buscar algum produto a este

        output.append("--------------Estatísticas por produtor--------------").append("\n").append("\n");

        try (BufferedReader input = new BufferedReader(new FileReader(inputFile))) {
            line = input.readLine();
            String separator = ",";
            String[] lineFields = line.split(separator);
            int dia = Integer.parseInt(lineFields[0]);

            while ((line = input.readLine()) != null) {
                lineFields = line.split(separator);
                ArrayList<String> prodCabaz = new ArrayList<>();

                cliente = lineFields[0];
                hub = lineFields[1];

                boolean contains = false;
                //adição do hub à lista de hubs
                if (!hubsLista.contains(hub)) {
                    hubsLista.add(hub);
                }


                line = input.readLine();
                lineFields = line.split(separator);
                int lenght = Integer.parseInt(lineFields[0]);

                boolean cabazFT = true;
                if (lenght != 0) {
                    for (int i = 0; i < lenght; i++) {
                        line = input.readLine();
                        lineFields = line.split(separator);

                        if (lineFields.length == 4) {

                            if (Float.parseFloat(lineFields[1]) != Float.parseFloat(lineFields[2])) {
                                cabazFT = false;
                            }

                            //adição do produtor à lista de produtores do cabaz
                            if (!prodCabaz.contains(lineFields[3])) {
                                prodCabaz.add(lineFields[3]);
                            }
                        }
                    }

                    //adição do cliente à lista de clientes do hub
                    for (Pair<String, ArrayList<String>> pair : hubCliente) {
                        if (pair.getKey().replaceAll("\\s+", "").equalsIgnoreCase(hub.replaceAll("\\s+", ""))) {
                            contains = true;
                            if (!pair.getValue().contains(cliente)) {
                                ArrayList<String> clientes = pair.getValue();
                                clientes.add(cliente);
                                hubCliente.set(hubCliente.indexOf(pair), new Pair<>(hub, clientes));
                            }
                        }
                    }
                    if (!contains) {
                        ArrayList<String> clientes = new ArrayList<>();
                        clientes.add(cliente);
                        hubCliente.add(new Pair<>(hub, clientes));
                    }

                    //adição dos produtores à lista de produtores que forneceram produto no hub
                    for (String produtor : prodCabaz) {
                        contains = false;
                        for (Pair<String, ArrayList<String>> pair : hubProd) {
                            if (pair.getKey().replaceAll("\\s+", "").equalsIgnoreCase(hub.replaceAll("\\s+", ""))) {
                                contains = true;
                                if (!pair.getValue().contains(produtor)) {
                                    ArrayList<String> produtores = pair.getValue();
                                    produtores.add(produtor);
                                    hubProd.set(hubProd.indexOf(pair), new Pair<>(hub, produtores));
                                }
                            }
                        }
                        if (!contains) {
                            ArrayList<String> produtores = new ArrayList<>();
                            produtores.add(produtor);
                            hubProd.add(new Pair<>(hub, produtores));
                        }

                    }

                    //adição do nº de cabazes fornecidos totalmente e nº de cabazes fornecidos parcialmente ao(s) produtor(es)
                    if (cabazFT && prodCabaz.size() == 1) {
                        for (Pair<String, Pair<Integer, Integer>> pair : prodNC) {
                            if (pair.getKey().replaceAll("\\s+", "").equalsIgnoreCase(prodCabaz.get(0).replaceAll("\\s+", ""))) {
                                if (pair.getKey().equalsIgnoreCase(prodCabaz.get(0))) {
                                    prodNC.set(prodNC.indexOf(pair), new Pair<>(prodCabaz.get(0), new Pair<>(pair.getValue().getKey() + 1, pair.getValue().getValue())));
                                    contains = true;
                                }
                                if (!contains) prodNC.add(new Pair<>(prodCabaz.get(0), new Pair<>(1, 0)));
                            }
                        }
                            } else {
                                for (String produtor : prodCabaz) {
                                    contains = false;
                                    for (Pair<String, Pair<Integer, Integer>> pair : prodNC) {
                                        if (pair.getKey().replaceAll("\\s+", "").equalsIgnoreCase(produtor.replaceAll("\\s+", ""))) {
                                            prodNC.set(prodNC.indexOf(pair), new Pair<>(produtor, new Pair<>(pair.getValue().getKey(), pair.getValue().getValue() + 1)));
                                            contains = true;
                                        }
                                    }
                                    if (!contains) prodNC.add(new Pair<>(produtor, new Pair<>(0, 1)));
                                }
                            }



                        //adição do cliente à lista de clientes que o produtor forneceu produto
                        for (String produtor : prodCabaz) {
                            contains = false;
                            for (Pair<String, ArrayList<String>> pair : prodCliente) {
                                if (pair.getKey().replaceAll("\\s+", "").equalsIgnoreCase(produtor.replaceAll("\\s+", ""))) {
                                    contains = true;
                                    if (!pair.getValue().contains(cliente)) {
                                        ArrayList<String> clientes = pair.getValue();
                                        clientes.add(cliente);
                                        prodCliente.set(prodCliente.indexOf(pair), new Pair<>(produtor, clientes));
                                    }
                                }
                            }
                            if (!contains) {
                                ArrayList<String> clientes = new ArrayList<>();
                                clientes.add(cliente);
                                prodCliente.add(new Pair<>(produtor, clientes));
                            }
                        }

                        //adição do hub à lista de hubs onde o produtor forneceu produto
                        for (String produtor : prodCabaz) {
                            contains = false;
                            for (Pair<String, ArrayList<String>> pair : prodHub) {
                                if (pair.getKey().replaceAll("\\s+", "").equalsIgnoreCase(produtor.replaceAll("\\s+", ""))) {
                                    contains = true;
                                    if (!pair.getValue().contains(hub)) {
                                        ArrayList<String> hubs = pair.getValue();
                                        hubs.add(hub);
                                        prodHub.set(prodHub.indexOf(pair), new Pair<>(produtor, hubs));
                                    }
                                }
                            }
                            if (!contains) {
                                ArrayList<String> hubs = new ArrayList<>();
                                hubs.add(hub);
                                prodHub.add(new Pair<>(produtor, hubs));
                            }
                        }
                        prodCabaz.clear();

                }
            }


            for (Pair<String, Pair<Integer, Integer>> pair : prodNC) {
                output.append("Produtor ").append(pair.getKey()).append(":\n").append("nº de cabazes fornecidos totalmente: ").append(pair.getValue().getKey()).append("\n")
                        .append("nº de cabazes fornecidos parcialmente: ").append(pair.getValue().getValue()).append("\n");

                for (Pair<String, ArrayList<String>> pair2 : prodCliente) {
                    if (pair2.getKey().equalsIgnoreCase(pair.getKey())) {
                        output.append("nº de clientes distintos fornecidos: ").append(pair2.getValue().size()).append("\n");
                    }
                }

                for (User u : networkGraph.vertices()) {
                    if (u.getCodUser().equalsIgnoreCase(pair.getKey().replaceAll("\\s+", ""))) {
                        int nprodE = 0;
                        for (Cabaz c : u.getCabaz()) {
                            if (c.getDia() == dia) {
                                for (Float f : c.getProdutos()) {
                                    if (f == 0) nprodE++;
                                }
                            }
                        }
                        output.append("nº de produtos totalmente esgotados: ").append(nprodE).append("\n");
                    }
                }


                for (Pair<String, ArrayList<String>> pair3 : prodHub) {
                    if (pair3.getKey().equalsIgnoreCase(pair.getKey())) {
                        output.append("nº de hubs fornecidos: ").append(pair3.getValue().size()).append("\n").append("\n");
                    }
                }
            }

            output.append("--------------Estatísticas por hub--------------").append("\n").append("\n");

            for (String hubLista : hubsLista) {
                boolean exists=false;
                output.append("\n").append("Hub ").append(hubLista).append("\n");

                for (Pair<String, ArrayList<String>> pair : hubCliente) {
                    if (pair.getKey().replaceAll("\\s+", "").equalsIgnoreCase(hubLista.replaceAll("\\s+", ""))) {
                        exists=true;
                        output.append("nº de clientes distintos que recolhem cabazes no hub: ").append(pair.getValue().size()).append("\n");
                    }
                }
                if(!exists) output.append("nº de clientes distintos que recolhem cabazes no hub: 0").append("\n");

                exists=false;
                for (Pair<String, ArrayList<String>> pair : hubProd) {
                    if (pair.getKey().replaceAll("\\s+", "").equalsIgnoreCase(hubLista.replaceAll("\\s+", ""))) {
                        exists=true;
                        output.append("nº de produtores distintos que fornecem cabazes para o hub: ").append(pair.getValue().size()).append("\n");
                    }
                }
                if(!exists) output.append("nº de produtores distintos que fornecem cabazes para o hub: 0").append("\n");
            }


            ficheiroOutput(outputFile, output.toString());
        } catch(IOException e){
            throw new RuntimeException(e);
        }

    }

    public void ficheiroOutput(String filename, String output){
        try {
            File myObj = new File(filename);
            if (myObj.createNewFile()) {
                System.out.println("\nFicheiro criado com sucesso: " + myObj.getName());
                try {
                    FileWriter myWriter = new FileWriter(filename);
                    myWriter.write(output);
                    myWriter.close();
                    System.out.println("\nFicheiro preenchido com sucesso.");
                } catch (IOException e) {
                    System.out.println("\nOcorreu um erro no preenchimento do ficheiro.");
                    e.printStackTrace();
                }
            } else {
                System.out.println("\nJá existe um ficheiro com o mesmo nome.");
            }

        } catch (IOException e) {
            System.out.println("\nOcorreu um erro na criação do ficheiro.");
            e.printStackTrace();
        }
    }

    //US308
    public Map<User, ArrayList<Produtor>> listaExpedicao(int dia, String filename){
        Map<User, ArrayList<Produtor>> lista = new HashMap<>();
        int nProd=0;
        float aux;
        String prod="";
        Produtor produtor;
        StringBuilder output = new StringBuilder();
        StringBuilder output2;

        output.append(dia).append("\n");
        for (User client : getClientsNearestHub().keySet()){
            boolean next = false;
            int i = 0;
            output2 = new StringBuilder();
            for (Cabaz cabaz : client.getCabaz()){
                if (cabaz.getDia() == dia){
                    output.append(client.getCodUser()).append(", ").append(getClientsNearestHub().get(client).getCodUser()).append("\n");
                    ArrayList<Produtor> produtores = new ArrayList<>();
                    for (Float produto : cabaz.getProdutos()){
                        produtor=null;
                        aux=0;
                        if(produto!=0){
                            nProd++;
                            for(User user : networkGraph.vertices()){
                                if (user.getClass().equals(Produtor.class)) {
                                    for(Cabaz c : user.getCabaz()){
                                        if(c.getDia()==dia){
                                            if(c.getProdutos().get(i)>=produto){
                                                produtor= (Produtor) user;
                                                prod=produto.toString();
                                                next=true;
                                                break;
                                            }
                                            if(c.getProdutos().get(i)>aux && c.getProdutos().get(i)<produto){
                                                produtor= (Produtor) user;
                                                prod=c.getProdutos().get(i).toString();
                                                aux=c.getProdutos().get(i);
                                            }
                                        }
                                    }
                                }
                                if(next) break;
                            }

                            //diminuir ao produtor a quantidade fornecida do produto requesitado pelo cliente
                            if(produtor!=null){
                                for(Cabaz c : produtor.getCabaz()){
                                    if(c.getDia()==dia) {
                                        c.getProdutos().set(i,c.getProdutos().get(i)-produto);
                                    }
                                }
                            }

                            output2.append("\n").append("Produto ").append(i+1).append(", ").append(produto).append(", ");
                            if(produtor!=null){
                                output2.append(prod).append(", ").append(produtor.getCodUser());
                                produtores.add(produtor);
                            }else{
                                output2.append("Não há produto para expedir");
                                produtores.add(null);
                            }
                        }
                        i++;
                    }
                    lista.put(client, produtores);
                    output.append(nProd).append(output2).append("\n");

                    nProd=0;
                }
            }
        }

        ficheiroOutput(filename, output.toString());

        return lista;
    }



    //US310 - Para uma lista de expedição diária gerar o percurso de entrega que minimiza a distância total percorrida.

    public <V,E> Graph<User, Float> percursoEntregaMin (Map<User, ArrayList<Produtor>> lista) {

        Graph<User, Float> grafoPercursoMin = new MapGraph<>(true);
        Graph<User, Float> grafoAuxiliar = networkGraph.clone();

        float distanciaTotal = 0;

        List<User> lp = new ArrayList<>();
        List<User> lh = new ArrayList<>();

        for (User u : lista.keySet()) {
            grafoAuxiliar.removeVertex(u);
            lh.add(u);
            ArrayList<Produtor> listaProdutores = lista.get(u);
            for (int i=0; i<listaProdutores.size(); i++) {
                if (networkGraph.validVertex(listaProdutores.get(i))){
                    lp.add(listaProdutores.get(i));
                }
            }
        }

        boolean[] visited = new boolean[networkGraph.numVertices()];

        int vKey;


        Float distMin = Float.MAX_VALUE;

        for (User user : networkGraph.vertices()) {
            vKey = networkGraph.key(user);
            visited[vKey] = false;
        }

        Edge<User, Float> ultAdicionado = null;

        for (int i = 0; i < lp.size(); i++) {

            if (!visited[networkGraph.key(lp.get(i))]) {

                Float dist;


                LinkedList<User> pathMin = new LinkedList<>();

                for (int j = i + 1; j < lp.size(); j++) {
                    LinkedList<User> path = new LinkedList<>();
                    if (!visited[networkGraph.key(lp.get(i))]) {
                        dist = Algorithms.shortestPath(grafoAuxiliar, lp.get(i), lp.get(j), Float::compare, Float::sum, 0F, path);

                        if (dist < distMin) {
                            pathMin = path;
                        }
                    }
                }

                List<User> pp = new ArrayList<>(pathMin);
                for (int k = 0; k < pp.size(); k++) {
                    grafoPercursoMin.addVertex(pp.get(k));
                    for (int j = k + 1; j < pp.size(); j++) {
                        ultAdicionado = grafoAuxiliar.edge(pp.get(k), pp.get(j));
                        grafoPercursoMin.addVertex(pp.get(j));
                        grafoPercursoMin.addEdge(pp.get(k), pp.get(j), networkGraph.edge(pp.get(k), pp.get(j)).getWeight());
                        visited[networkGraph.key(lp.get(i))] = true;
                        visited[networkGraph.key(lp.get(i))] = true;
                        distanciaTotal = distanciaTotal + (ultAdicionado.getWeight());
                    }
                }
            }

        }

        Map<Empresa, Float> lengthPaths = new HashMap<>();

        for (Empresa hub : hubs.keySet()){
            Float lenPath = Algorithms.shortestPath(networkGraph,ultAdicionado.getVDest(),hub,Float::compare,Float::sum,0F,new LinkedList<>());
            lengthPaths.put(hub, lenPath);
        }
        Stream<Map.Entry<Empresa, Float>> sorted = lengthPaths.entrySet().stream().sorted(Map.Entry.comparingByValue());

        User hub = sorted.findFirst().get().getKey();
        lengthPaths.clear();

        grafoPercursoMin.addVertex(hub);
        assert ultAdicionado != null;
        grafoPercursoMin.addEdge(ultAdicionado.getVDest(), hub, networkGraph.edge(ultAdicionado.getVDest(), hub).getWeight());
        distanciaTotal= distanciaTotal + (networkGraph.edge(ultAdicionado.getVDest(), hub).getWeight());

        int index = lh.indexOf(hub);
        lh.remove(index);
        lh.add(0, hub);

        distMin = Float.MAX_VALUE;

        for (int i = 0; i < lh.size(); i++) {
            if (!visited[networkGraph.key(lp.get(i))]) {
                Float dist;
                LinkedList<User> path = new LinkedList<>();
                LinkedList<User> pathMin = new LinkedList<>();

                for (int j = i + 1; j < lh.size(); j++) {
                    if (!visited[networkGraph.key(lp.get(i))]) {
                        dist = Algorithms.shortestPath(networkGraph, lh.get(i), lh.get(j), Float::compare, Float::sum, 0F, path);

                        if (dist < distMin) {
                            pathMin = path;
                        }
                    }
                }

                List<User> pp = new ArrayList<>(pathMin);
                for (int k = 0; k < pp.size(); k++) {
                    grafoPercursoMin.addVertex(pp.get(k));
                    for (int j = k + 1; j < pp.size(); j++) {
                        grafoPercursoMin.addVertex(pp.get(j));
                        grafoPercursoMin.addEdge(pp.get(k), pp.get(j), networkGraph.edge(pp.get(k), pp.get(j)).getWeight());
                        visited[networkGraph.key(lp.get(i))] = true;
                        visited[networkGraph.key(lp.get(i))] = true;
                        distanciaTotal= distanciaTotal + (networkGraph.edge(pp.get(k), pp.get(j)).getWeight());
                    }
                }

            }

        }

        System.out.println("Distância Total Percorrida = "+ distanciaTotal +" \n");
        return grafoPercursoMin;
    }

}