package distributionNetWork;

public class Empresa extends User {

    private boolean hub;

    public Empresa(Localizacao localizacao, String codUser) {
        super(localizacao, codUser);
        hub=false;
    }

    public boolean isHub() {
        return hub;
    }

    public void setHubTrue() {
        this.hub = true;
    }

}
