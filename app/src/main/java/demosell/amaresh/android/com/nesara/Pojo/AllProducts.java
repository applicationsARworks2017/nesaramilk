package demosell.amaresh.android.com.nesara.Pojo;

/**
 * Created by Amaresh on 3/28/18.
 */

public class AllProducts {
    String id,image,product_name,weight_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public AllProducts(String id, String image, String product_name, String weight_type) {
        this.id=id;
        this.product_name=product_name;
        this.weight_type=weight_type;
        this.image=image;


    }
}
