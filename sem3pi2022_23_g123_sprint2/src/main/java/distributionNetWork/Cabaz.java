package distributionNetWork;

import java.util.List;

public class Cabaz  {
    private int dia;
    private List<Float> produtos;



    public Cabaz(int dia, List<Float> produtos) {
        this.dia = dia;
        this.produtos = produtos;

    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public List<Float> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Float> produtos) {
        this.produtos = produtos;
    }



    @Override
    public String toString() {
        return "Cabaz{ " +
                " Dia = " + dia +
                ", Produtos=" + produtos + '\'' +
                '}' + "\n";
    }
}

