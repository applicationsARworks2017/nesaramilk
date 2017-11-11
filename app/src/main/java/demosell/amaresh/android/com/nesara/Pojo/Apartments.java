package demosell.amaresh.android.com.nesara.Pojo;

/**
 * Created by Amaresh on 9/22/17.
 */

public class Apartments {
    String id,apartment_name,apartments_address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApartment_name() {
        return apartment_name;
    }

    public void setApartment_name(String apartment_name) {
        this.apartment_name = apartment_name;
    }

    public String getApartments_address() {
        return apartments_address;
    }

    public void setApartments_address(String apartments_address) {
        this.apartments_address = apartments_address;
    }

    public Apartments(String id, String apartment_name, String apartments_address) {
        this.id=id;
        this.apartment_name=apartment_name;
        this.apartments_address=apartments_address;


    }
}
