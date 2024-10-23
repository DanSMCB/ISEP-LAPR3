package ControladorRega;

import java.time.LocalTime;
import java.util.ArrayList;

public class ControladorRega {
    private ArrayList<LocalTime> horasRega;
    private ArrayList<RegaParcela> regaParcelas;

    public ControladorRega(ArrayList<LocalTime> horasRega, ArrayList<RegaParcela> regaParcelas){
        this.horasRega=horasRega;
        this.regaParcelas=regaParcelas;
    }

    public ArrayList<LocalTime> getHorasRega() {
        return horasRega;
    }

    public void setHorasRega(ArrayList<LocalTime> horasRega) {
        this.horasRega = horasRega;
    }

    public ArrayList<RegaParcela> getRegaParcelas() {
        return regaParcelas;
    }

    public void setRegaParcelas(ArrayList<RegaParcela> regaParcelas) {
        this.regaParcelas = regaParcelas;
    }

    public int getTempoRegaTotal(){
        int total=0;
        for(int i=0;i<regaParcelas.size();i++){
            total+= regaParcelas.get(i).getDuracao();
        }
        return total;
    }


}
