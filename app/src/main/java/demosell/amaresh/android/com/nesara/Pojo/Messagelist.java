package demosell.amaresh.android.com.nesara.Pojo;

/**
 * Created by RN on 11/11/2017.
 */

public class Messagelist {
    String name,mobile_no,title,mesage,is_read,created;
    public Messagelist(String name, String mobile_no, String title, String message, String is_read, String created) {
        this.name=name;
        this.mobile_no=mobile_no;
        this.title=title;
        this.mesage=message;
        this.is_read=is_read;
        this.created=created;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getIs_read() {
        return is_read;
    }

    public void setIs_read(String is_read) {
        this.is_read = is_read;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
