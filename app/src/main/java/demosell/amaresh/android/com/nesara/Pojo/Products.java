package demosell.amaresh.android.com.nesara.Pojo;

/**
 * Created by Amaresh on 3/4/18.
 */

public class Products {
    String id,product_id,min_quantity,price,appartment_name,image,product_name,weight_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(String min_quantity) {
        this.min_quantity = min_quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAppartment_name() {
        return appartment_name;
    }

    public void setAppartment_name(String appartment_name) {
        this.appartment_name = appartment_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getWeight_type() {
        return weight_type;
    }

    public void setWeight_type(String weight_type) {
        this.weight_type = weight_type;
    }

    public Products(String id, String product_id, String min_quantity, String price, String appartment_name,
                    String image, String product_name, String weight_type) {

        this.id=id;
        this.product_id=product_id;
        this.min_quantity=min_quantity;
        this.price=price;
        this.appartment_name=appartment_name;
        this.image=image;
        this.product_name=product_name;
        this.weight_type=weight_type;

    }
}
