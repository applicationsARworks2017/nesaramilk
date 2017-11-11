package demosell.amaresh.android.com.nesara.Pojo;

/**
 * Created by LIPL on 27/01/17.
 */

public class Users {
    String id,user_mobile_number,user_name,block_name,user_email,city,flat_num,
            appartment,alternate_num,user_type,user_address,user_created;
    private boolean selected;

    public Users() {

    }
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_mobile_number() {
        return user_mobile_number;
    }

    public void setUser_mobile_number(String user_mobile_number) {
        this.user_mobile_number = user_mobile_number;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFlat_num() {
        return flat_num;
    }

    public void setFlat_num(String flat_num) {
        this.flat_num = flat_num;
    }

    public String getAppartment() {
        return appartment;
    }

    public void setAppartment(String appartment) {
        this.appartment = appartment;
    }

    public String getAlternate_num() {
        return alternate_num;
    }

    public void setAlternate_num(String alternate_num) {
        this.alternate_num = alternate_num;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_created() {
        return user_created;
    }

    public void setUser_created(String user_created) {
        this.user_created = user_created;
    }

    public Users(String id, String user_mobile_number, String user_name, String block_name, String user_email,
                 String city, String flat_num, String appartment, String alternate_num, String user_type,
                 String user_address, String user_created) {

        this.id=id;
        this.user_mobile_number=user_mobile_number;
        this.user_name=user_name;
        this.block_name=block_name;
        this.user_email=user_email;
        this.city=city;
        this.flat_num=flat_num;
        this.appartment=appartment;
        this.alternate_num=alternate_num;
        this.user_type=user_type;
        this.user_address=user_address;
        this.user_created=user_created;


    }
}
