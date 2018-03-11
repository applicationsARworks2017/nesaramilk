package demosell.amaresh.android.com.nesara.Calander;

import java.util.Date;

/**
 * Created by Amaresh on 3/11/18.
 */

class DetailsPP {
    String id,subscription_id,quentity,is_delivered,is_paused;
    Date deliverydate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDeliverydate() {
        return deliverydate;
    }

    public void setDeliverydate(Date deliverydate) {
        this.deliverydate = deliverydate;
    }

    public String getSubscription_id() {
        return subscription_id;
    }

    public void setSubscription_id(String subscription_id) {
        this.subscription_id = subscription_id;
    }

    public String getQuentity() {
        return quentity;
    }

    public void setQuentity(String quentity) {
        this.quentity = quentity;
    }

    public String getIs_delivered() {
        return is_delivered;
    }

    public void setIs_delivered(String is_delivered) {
        this.is_delivered = is_delivered;
    }

    public String getIs_paused() {
        return is_paused;
    }

    public void setIs_paused(String is_paused) {
        this.is_paused = is_paused;
    }

    public DetailsPP(String id, Date deliverydate, String subscription_id, String quentity, String is_delivered, String is_paused) {
        this.id=id;
        this.deliverydate=deliverydate;
        this.subscription_id=subscription_id;
        this.quentity=quentity;
        this.is_delivered=is_delivered;
        this.is_paused=is_paused;

    }
}
