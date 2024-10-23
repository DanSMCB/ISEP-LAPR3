package distributionNetWork;

import java.util.ArrayList;

public class User implements Comparable<User>{
    private Localizacao localizacao;
    private String codUser;

    private ArrayList<Cabaz> cabazes;

    public User(Localizacao localizacao, String codUser) {
        this.localizacao = localizacao;
        this.codUser = codUser;
        this.cabazes=new ArrayList<>();
    }

    public ArrayList<Cabaz> getCabaz() {
        return cabazes;
    }

    public void setCabaz(ArrayList<Cabaz> cabaz) {
        this.cabazes = cabaz;
    }
    public void addCabaz(Cabaz cabaz){
        cabazes.add(cabaz);
    }

    public Localizacao getLocalization() {
        return localizacao;
    }

    public void setLocalization(Localizacao localizacao) {
        this.localizacao = localizacao;
    }

    public String getCodUser() {
        return codUser;
    }

    public void setCodUser(String codCliente) {
        this.codUser = codCliente;
    }

    @Override
    public String toString() {
        return "\nUser{" +
                "localization=" + localizacao.getCodLoc() +
                ", user code='" + codUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        User otherUser = (User) obj;
        return otherUser.getCodUser().equals(this.getCodUser());

    }
    @Override
    public int compareTo(User o) {
        return this.getCodUser().compareTo(o.getCodUser());
    }
}