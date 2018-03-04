package demosell.amaresh.android.com.nesara.Calander;

import java.util.Date;

/**
 * Created by Amaresh on 1/30/18.
 */

class EventObjects {
    private int id;
    private String message;
    private Date date;
    public EventObjects(String message, Date date) {
        this.message = message;
        this.date = date;
    }
    public EventObjects(int id, String message, Date date) {
        this.date = date;
        this.message = message;
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public String getMessage() {
        return message;
    }
    public Date getDate() {
        return date;
    }
}
