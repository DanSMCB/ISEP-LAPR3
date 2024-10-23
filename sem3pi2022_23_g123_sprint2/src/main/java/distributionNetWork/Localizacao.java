package distributionNetWork;

public class Localizacao {

    private String codLoc;
    private float latitude;
    private float longitude;

    public Localizacao(String codLoc, Float latitude, Float longitude) {
        this.codLoc=codLoc;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCodLoc() {return codLoc;}

    public void setCodLoc(String codLoc) {this.codLoc = codLoc;}

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Localization{" +
                "code=" + codLoc +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}