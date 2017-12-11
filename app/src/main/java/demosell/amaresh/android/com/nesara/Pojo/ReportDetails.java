package demosell.amaresh.android.com.nesara.Pojo;

/**
 * Created by Amaresh on 10/18/17.
 */

public class ReportDetails {
    String id,subscription_id,amount,mobile_no,name,flat_no,address,block_name,appartment_name,product_name,s_quantity,s_liter;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlat_no() {
        return flat_no;
    }

    public void setFlat_no(String flat_no) {
        this.flat_no = flat_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBlock_name() {
        return block_name;
    }

    public void setBlock_name(String block_name) {
        this.block_name = block_name;
    }

    public String getAppartment_name() {
        return appartment_name;
    }

    public void setAppartment_name(String appartment_name) {
        this.appartment_name = appartment_name;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public ReportDetails(String id, String subscription_id, String amount, String mobile_no,
                         String name, String flat_no, String address, String block_name, String appartment_name,
                         String product_name,String s_quantity,String s_liter) {

        this.id=id;
        this.subscription_id=subscription_id;
        this.amount=amount;
        this.mobile_no=mobile_no;
        this.name=name;
        this.flat_no=flat_no;
        this.address=address;
        this.block_name=block_name;
        this.appartment_name=appartment_name;
        this.product_name=product_name;
        this.s_quantity=s_quantity;
        this.s_liter=s_liter;


    }

    public String getS_liter() {
        return s_liter;
    }

    public void setS_liter(String s_liter) {
        this.s_liter = s_liter;
    }

    public String getS_quantity() {
        return s_quantity;
    }

    public void setS_quantity(String s_quantity) {
        this.s_quantity = s_quantity;
    }
}
