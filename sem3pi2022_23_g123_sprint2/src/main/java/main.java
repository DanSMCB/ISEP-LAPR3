import ControladorRega.FuncControRega;
import distributionNetWork.Empresa;
import distributionNetWork.DistributionNetwork;
import distributionNetWork.Produtor;
import distributionNetWork.User;
import graph.Algorithms;
import graph.Graph;

import java.io.File;
import java.util.*;

public class main {
    static DistributionNetwork distributionNetwork = new DistributionNetwork();
    static Scanner sc = new Scanner(System.in);
    static FuncControRega funcControRega = new FuncControRega();
    static boolean existHubs=false;
    static Map<User, ArrayList<Produtor>> lista = new HashMap<>();

    private static final int MAX_DIA=5;

    public static void main(String[] args) {
        String fileClientesProdutores, fileDistancias, fileCabazes;

//        file -> (big ou small)\nomeFicheiro

//        ficheiros small:
//        small\clientes-produtores_small.csv
//        small\distancias_small.csv
//        small\cabazes_small.csv

//        ficheiros big:
//        big\clientes-produtores_big.csv
//        big\distancias_big.csv
//        big\cabazes_big.csv

        System.out.println("Introduza o nome do ficheiro com os clientes e produtores ((small ou big)\\nomeFicheiro).");
        boolean valid=false;
        do{
            fileClientesProdutores=sc.next();
            File file = new File("files\\input\\"+fileClientesProdutores);
            if(file.exists()) valid=true;
            if(!valid) System.out.println("Ficheiro não encontrado, digite novamente.");
        }while(!valid);
        distributionNetwork.read("files\\input\\"+fileClientesProdutores);

        System.out.println("Introduza o nome do ficheiro com as distâncias.");
        valid=false;
        do{
            fileDistancias=sc.next();
            File file = new File("files\\input\\"+fileDistancias);
            if(file.exists()) valid=true;
            if(!valid) System.out.println("Ficheiro não encontrado, digite novamente.");
        }while(!valid);
        distributionNetwork.readDistances("files\\input\\"+fileDistancias);

        System.out.println("Introduza o nome do ficheiro com os cabazes.");
        valid=false;
        do{
            fileCabazes=sc.next();
            File file = new File("files\\input\\"+fileCabazes);
            if(file.exists()) valid=true;
            if(!valid) System.out.println("Ficheiro não encontrado, digite novamente.");
        }while(!valid);
        distributionNetwork.readCabaz("files\\input\\"+fileCabazes);


        int option;
        do{
            System.out.println("\n1 - Mostrar rede de distribuição (US301)");
            System.out.println("2 - Verificar se o grafo é conexo (US302)");
            System.out.println("3 - Definir os hubs da rede de distribuição (US303).");
            System.out.println("4 - Determinar o hub mais próximo de cada cliente (US304)");
            System.out.println("5 - Determinar a rede que conecte todos os clientes e produtores agrícolas com uma distância total mínima (US305)");
            System.out.println("6 - Simulação do funcionamento de um controlador de rega (US306)");
            System.out.println("7 - Mostar lista de cabazes (US307)");
            System.out.println("8 - Gerar uma lista de expedição para um determinado dia sem qualquer restrição quanto aos produtores (US308)");
            System.out.println("9 - Gerar uma lista de expedição para um determinado dia que forneça apenas com os N produtores agrícolas mais próximos do hub de entrega do cliente (US309)");
            System.out.println("10 - Para uma lista de expedição diária gerar o percurso de entrega que minimiza a distância total percorrida (US310)");
            System.out.println("11 - Para uma lista de expedição calcular estatísticas (US311)");

            System.out.println("\n\nDigite a opção desejada ou digite \"0\" para terminar\n");
            option=sc.nextInt();

            switch (option){
                case 0:
                    break;
                case 1:
                    us301();
                    break;
                case 2:
                    us302();
                    break;
                case 3:
                    us303();
                    break;
                case 4:
                    us304();
                    break;
                case 5:
                    us305();
                    break;
                case 6:
                    us306();
                    break;
                case 7:
                    us307();
                    break;
                case 8:
                    us308();
                    break;
                case 9:
                    us309();
                    break;
                case 10:
                    us310();
                    break;
                case 11:
                    us311();
                    break;

                default:
                    System.out.println("Opção inválida.");
            }


        }while(option!=0);
    }

    public static void us301(){
        System.out.println(distributionNetwork.getNetworkGraph());
    }

    public static void us302(){
        System.out.println("\nUS302 - Verificar se o grafo é conexo\n");
        boolean res302_1 = Algorithms.connectComps(distributionNetwork.getNetworkGraph());
        if (res302_1) {
            System.out.println("O grafo é conexo.\n");
        }else {
            System.out.println("O grafo não é conexo.\n");
        }

        System.out.println("\nUS302 - Número mínimo de ligações necessárias\n");
        int res302_2 = distributionNetwork.numeroLigacoesMin(distributionNetwork.getNetworkGraph());
        System.out.println(res302_2);
    }

    public static void us303(){
        int n=0, hubs_max=0;
        System.out.println("\nUS303 - Definir os hubs da rede de distribuição.\n");

        for (User user : distributionNetwork.getNetworkGraph().vertices()){
            if (user.getClass().equals(Empresa.class)){
                hubs_max++;
            }
        }

        System.out.println("Digite o nº de hubs desejado sabendo que existem "+hubs_max+" disponíveis.");
        boolean valid=false;
        while(!valid){
            try{
                n=Integer.parseInt(sc.next());
                if(n<=0 || n>hubs_max){
                    System.out.println("Número invalido para a quantidade de empresas existentes ("+hubs_max+")");
                }else valid=true;
            }catch(NumberFormatException e){
                System.out.println("Número invalido, digite um número inteiro.");
            }
        }

        Map<Empresa, Float> hubCompanies = distributionNetwork.networkHubs(n);
        System.out.println("Hubs: \n");
        for(Empresa c : hubCompanies.keySet()){
            System.out.println("Empresa: " + c.getCodUser() + "; Distância média: " + hubCompanies.get(c) + "m");
        }

        existHubs=true;
    }

    public static void us304(){
        System.out.println("\nUS304 - Hub mais próximo de cada cliente: \n");
        Map<User, Empresa> clientsNearestHub = distributionNetwork.getClientsNearestHub();
        clientsNearestHub.forEach((k,v)->System.out.println(k.getCodUser()+" -> "+v.getCodUser()));
    }

    public static void us305(){
        System.out.println("\nUS305 - Determinar a rede que conecte todos os clientes e produtores agrícolas com uma distância total mínima\n");
        float res305 = distributionNetwork.redeDistanciaMin(distributionNetwork.getNetworkGraph());
        System.out.println(res305);
    }

    public static void us306(){
        String fileRega;
        System.out.println("\nUS306 - Simulação do funcionamento de um controlador de rega.\n");
        System.out.println("Introduza o nome do ficheiro com a simulação do funcionamento de um controlador de rega.");
        boolean valid=false;
        do{
            fileRega=sc.next();
            File file = new File("files\\input\\"+fileRega);
            if(file.exists()) valid=true;
            if(!valid) System.out.println("Ficheiro não encontrado, digite novamente.");
        }while(!valid);
        funcControRega.readFile("files\\input\\"+fileRega);

        String date="",time;
        while(!date.equalsIgnoreCase("0")){
            System.out.println("Digite uma data(dd-mm-aaaa) ou digite \"0\" para terminar.");
            date=sc.next();
            if(!date.equalsIgnoreCase("0")){
                System.out.println("Digite o tempo(hh:mm)");
                time=sc.next();
                if(!funcControRega.isWatering(date,time)){
                    System.out.println("Formato digitado errado. Data: dd-mm-aaaa; Tempo: hh:mm");
                }
            }
        }
    }

    public static void us307(){
        for(User u : distributionNetwork.getNetworkGraph().vertices()){
            if(u.getCabaz()!=null){
                System.out.println(u.getCodUser());
                System.out.println(u.getCabaz());
            }

        }
    }

    public static void us308(){
        if(!existHubs){
            System.out.println("Ainda não foram definidos hubs.");
            us303();
        }
        String filename;
        int dia;
        System.out.println("\nUS308 - Gerar uma lista de expedição para um determinado dia");
        System.out.println("Introduza o nome do ficheiro para o qual quer exportar a lista de expedição: ");
        filename = sc.next();

        System.out.println("\nIntroduza o dia, no formato dd, para qual quer a lista de expedição: ");
        do {
            dia = sc.nextInt();
        }while(dia<=0 || dia>MAX_DIA);

        lista = distributionNetwork.listaExpedicao(dia,"files\\output\\"+filename);
    }

    public static void us309(){
        if(!existHubs){
            System.out.println("Ainda não foram definidos hubs.");
            us303();
        }
        System.out.println("\nUS309 - Gerar uma lista de expedição para um determinado dia que forneça apenas com os N produtores agrícolas mais próximos do hub de entrega do cliente");
        int n_produtores, dia, produtores=0;
        String filename;

        System.out.println("\nIntroduza o nome do ficheiro para o qual quer exportar a lista de expedição: ");
        filename = sc.next();

        for (User user : distributionNetwork.getNetworkGraph().vertices()){
            if (user.getClass().equals(Produtor.class)){
                produtores++;
            }
        }

        System.out.println("\nIntroduza o número de produtores agrícolas que pretende que forneçam os clientes, sabendo que existem "+produtores+" disponíveis: ");
        n_produtores = sc.nextInt();
        while (n_produtores<=0 || n_produtores>produtores){
            System.out.println("\nNúmero de produtores inválidos. Introduza o número de produtores agrícolas mais próximos do hub do cliente, sabendo que existem "+produtores+" disponíveis: ");
            n_produtores = sc.nextInt();
        }

        System.out.println("\nIntroduza o dia, no formato dd, para qual quer a lista de expedição: ");
        do {
            dia = sc.nextInt();
        }while(dia<=0 || dia>MAX_DIA);

        lista = distributionNetwork.listaExpedicaoRestricao(n_produtores, dia, "files\\output\\"+filename);
    }

    public static void us310(){

        Graph<User, Float> g = distributionNetwork.percursoEntregaMin(lista);
        distributionNetwork.printfGrafo(g);


    }

    public static void us311(){
        String inputFile, outputFile;
        System.out.println("\nUS311\n");
        System.out.println("Introduza o nome do ficheiro da lista de expedição para calcular as estatísticas.");
        boolean valid=false;
        do{
            inputFile=sc.next();
            File file = new File("files\\output\\"+inputFile);
            if(file.exists()) valid=true;
            if(!valid) System.out.println("Ficheiro não encontrado, digite novamente.");
        }while(!valid);

        System.out.println("Introduza o nome do ficheiro para o qual quer exportar as estatísticas: ");
        outputFile=sc.next();
        distributionNetwork.estatisticasCabaz("files\\output\\"+inputFile,"files\\output\\"+outputFile);
    }
}
