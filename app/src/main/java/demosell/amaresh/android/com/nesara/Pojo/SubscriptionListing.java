package demosell.amaresh.android.com.nesara.Pojo;

/**
 * Created by LIPL on 28/01/17.
 */

public class SubscriptionListing {
    String id,s_user_id,product_name,quantity,delivery_type,start_date,end_date,price_id,is_stop,s_liter;


    public SubscriptionListing(String id, String s_user_id, String product_name, String quantity, String delivery_type,
                               String start_date, String end_date, String price_id, String is_stop,String s_liter) {

        this.id=id;
        this.s_user_id=s_user_id;
        this.product_name=product_name;
        this.quantity=quantity;
        this.delivery_type=delivery_type;
        this.start_date=start_date;
        this.end_date=end_date;
        this.price_id=price_id;
        this.is_stop=is_stop;
        this.s_liter=s_liter;

    }

    public String getS_liter() {
        return s_liter;
    }

    public void setS_liter(String s_liter) {
        this.s_liter = s_liter;
    }

    public String getIs_stop() {
        return is_stop;
    }

    public void setIs_stop(String is_stop) {
        this.is_stop = is_stop;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id=id;
    }

    public String getS_user_id(){
        return s_user_id;
    }
    public void setS_user_id(String s_user_id){
        this.s_user_id=s_user_id;
    }

    public String getProduct_name(){
        return product_name;
    }
    public void setProduct_name(String product_name){
        this.product_name=product_name;
    }

    public String getQuantity(){
        return quantity;
    }
    public void setQuantity(String quantity){
        this.quantity=quantity;
    }

    public String getDelivery_type(){
        return delivery_type;
    }
    public void setDelivery_type(String delivery_type){
        this.delivery_type=delivery_type;
    }

    public String getStart_date(){
        return start_date;
    }
    public void setStart_date(String start_date){
        this.start_date=start_date;
    }

    public String getEnd_date(){
        return end_date;
    }
    public void setEnd_date(String end_date){
        this.end_date=end_date;
    }

    public String getPrice_id(){
        return price_id;
    }
    public void setPrice_id(String price_id){
        this.price_id=price_id;
    }



}
