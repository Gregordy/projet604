package projet;

import com.google.android.gms.maps.model.LatLng;

/**
 * CLASSE REPRÉSENTANT UN ÉVÉNEMENT
 */
public class SportField {
    private int id;
    private String name;
    private String adress;
    private String city;
    private LatLng coord;

    public SportField(){
        id = 0;
        name ="Undefined name";
        adress = "Undefined adress";
        city = "Undefined city";
        coord = null;
    }

    public SportField(int _id, String _name, String _adress, String _city, LatLng _coord){
        id = _id;
        name = _name;
        adress = _adress;
        city = _city;
        coord = _coord;
    }




    public int getId() {
        return id;
    }

    public String getAdress() {
        return adress;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public LatLng getCoord() {
        return coord;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCoord(LatLng coord) {
        this.coord = coord;
    }

    @Override
    public String toString() {
        return "SportField{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", adress='" + adress + '\'' +
                ", city='" + city + '\'' +
                ", coord=" + coord +
                '}';
    }
}
