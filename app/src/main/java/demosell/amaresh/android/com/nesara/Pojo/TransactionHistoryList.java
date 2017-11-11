package demosell.amaresh.android.com.nesara.Pojo;

/**
 * Created by Rasmita on 8/5/2017.
 */

public class TransactionHistoryList{


    private String t_amount,t_type,t_date;
        public TransactionHistoryList(String t_amount, String t_type, String t_date) {
            this.t_amount=t_amount;
            this.t_date=t_date;
            this.t_type=t_type;

        }


        public String getT_amount(){
            return t_amount;
        }
        public void setT_amount(String t_amount){
            this.t_amount=t_amount;
        }

        public String getT_type(){
            return t_type;
        }
        public void setT_type(String t_type){
            this.t_type=t_type;
        }

        public String getT_date(){
            return t_date;
        }
        public void setT_date(String t_date){
            this.t_date=t_date;
        }
}
