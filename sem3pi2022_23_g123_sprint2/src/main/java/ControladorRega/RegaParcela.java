package ControladorRega;

public class RegaParcela {
    private String parcela;
    private int duracao;
    private String regularidade;

    public RegaParcela(String parcela, int duracao, String regularidade){
        this.parcela=parcela;
        this.duracao=duracao;
        this.regularidade=regularidade;
    }

    public String getParcela() {
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
    }

    public int getDuracao() {
        return duracao;
    }

    public void setDuracao(int duracao) {
        this.duracao = duracao;
    }

    public String getRegularidade() {
        return regularidade;
    }

    public void setRegularidade(String regularidade) {
        this.regularidade = regularidade;
    }
}
