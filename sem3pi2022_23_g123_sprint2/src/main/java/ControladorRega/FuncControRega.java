package ControladorRega;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FuncControRega {
    ControladorRega controladorRega;
    DateTimeFormatter parser = DateTimeFormatter.ofPattern("[dd-MM-yyyy][d-MM-yyyy][d-M-yyyy]");
    DateTimeFormatter parser2 = DateTimeFormatter.ofPattern("[HH:mm][H:mm]");
    String nowS = LocalDate.now().getDayOfMonth()+"-"+LocalDate.now().getMonthValue()+"-"+LocalDate.now().getYear();
    LocalDate now = LocalDate.parse(nowS,parser);

    public FuncControRega(){}

    public void readFile(String filename) {
        ArrayList<LocalTime> horasRega = new ArrayList<>();
        ArrayList<RegaParcela> regaParcelas = new ArrayList<>();

        try (BufferedReader input = new BufferedReader(new FileReader(filename))){

            String line = input.readLine();
            String[] horas = line.split(",");//ler primeira linha e ignorar
            for (String hora : horas) {
                horasRega.add(LocalTime.parse(hora,parser2));
            }

            while((line = input.readLine()) != null){
                String separator = ",";
                String[] lineFields = line.split(separator);

                RegaParcela regaParcela = new RegaParcela(lineFields[0],Integer.parseInt(lineFields[1]),lineFields[2]);
                regaParcelas.add(regaParcela);
            }

            controladorRega=new ControladorRega(horasRega,regaParcelas);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isWatering(String date, String time) {
        try {
            LocalDate date1 = LocalDate.parse(date,parser);
            LocalTime time1 = LocalTime.parse(time,parser2);

            ArrayList<LocalTime> horasRega = controladorRega.getHorasRega();

            if(now.plusDays(30).compareTo(date1)<0){
                System.out.println("A data inserida não se encontra dentro do plano de rega.");
            }else{
                for(LocalTime hour : horasRega){
                    LocalTime after = hour.plusHours(controladorRega.getTempoRegaTotal()/60).plusMinutes(controladorRega.getTempoRegaTotal()%60);
                    if(hour.compareTo(time1)<=0 && after.compareTo(time1)>=0){
                        getPortion(date1, time1, hour);
                    }
                }
            }
        }catch (DateTimeException e){
            return false;
        }
        return true;
    }

    public void getPortion(LocalDate date, LocalTime time, LocalTime hour){
        for(RegaParcela regaParcela : controladorRega.getRegaParcelas()){
            hour = hour.plusHours(regaParcela.getDuracao()/60).plusMinutes(regaParcela.getDuracao()%60);
            if(hour.compareTo(time)>=0){
                if(isWateringDay(date, regaParcela)){
                    System.out.println("Está a ser regado o sector \"" + regaParcela.getParcela() + "\" e faltam " + getTimeLeft(time,hour) + " minutos para terminar.");
                }else System.out.println("Não está a ser regado nenhum sector de momento.");
                break;
            }
        }
    }

    public boolean isWateringDay(LocalDate date, RegaParcela regaParcela){
        if(regaParcela.getRegularidade().equalsIgnoreCase("t")) return true;
        else if(regaParcela.getRegularidade().equalsIgnoreCase("p") && (date.getDayOfMonth()%2)==0) return true;
        else return regaParcela.getRegularidade().equalsIgnoreCase("i") && (date.getDayOfMonth() % 2) != 0;
    }

    public int getTimeLeft(LocalTime time, LocalTime end){
        if(time.getHour()==end.getHour()) return end.getMinute()-time.getMinute();
        else return 60-time.getMinute()+end.getMinute();
    }
}
