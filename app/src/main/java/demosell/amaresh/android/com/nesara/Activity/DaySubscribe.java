package demosell.amaresh.android.com.nesara.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nesara.amaresh.demosell.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import demosell.amaresh.android.com.nesara.Adapter.ProdudtAdapter;
import demosell.amaresh.android.com.nesara.Calander.CalendarCustomView;
import demosell.amaresh.android.com.nesara.Pojo.Products;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class DaySubscribe extends AppCompatActivity {
    TextView apartment_name,product_name,product_price,et_quantity;
    LinearLayout plus,minus;
    public static ArrayList<String> selected_dates;
    Button submit;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_subscribe);
        CalendarCustomView mView = (CalendarCustomView)findViewById(R.id.custom_calendar);
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);
        apartment_name=(TextView)findViewById(R.id.apartment);
        product_name=(TextView)findViewById(R.id.product);
        product_price=(TextView)findViewById(R.id.product_price);
        et_quantity=(TextView) findViewById(R.id.qnty);
        plus=(LinearLayout)findViewById(R.id.plus);
        minus=(LinearLayout)findViewById(R.id.min);
        submit=(Button)findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alldates= selected_dates.toString();
                String send_dates=alldates.substring(1,alldates.length()-1);
                submitDetails(send_dates);
                //Toast.makeText(DaySubscribe.this,send_dates,Toast.LENGTH_SHORT).show();

            }
        });

        apartment_name.setText(Home.appartment_name.toUpperCase());
        product_name.setText(Home.product_name +" (in "+Home.product_wei+")");
        product_price.setText("Rs. "+Home.product_price);
        et_quantity.setText(Home.product_minqnty);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double quantity=Double.valueOf(et_quantity.getText().toString().trim());
                Double newquantity;
                Double actualquantity=Double.valueOf(Home.product_minqnty);
                Double actualPrice=Double.valueOf(Home.product_price);
                if(Double.compare(quantity,actualquantity)==0){
                    Toast.makeText(DaySubscribe.this,"This is the minimum",Toast.LENGTH_SHORT).show();
                }
                else{
                    newquantity=quantity-0.5;
                    et_quantity.setText(newquantity.toString());
                    actualPrice=actualPrice*newquantity;
                    product_price.setText("Rs. "+actualPrice.toString());
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double quantity=Double.valueOf(et_quantity.getText().toString().trim());
                Double newquantity;
                Double actualPrice=Double.valueOf(Home.product_price);
                newquantity=quantity+0.5;
                et_quantity.setText(newquantity.toString());
                actualPrice=actualPrice*newquantity;
                product_price.setText("Rs. "+actualPrice.toString());

            }
        });


    }

    private void submitDetails(String dates) {
        if(CheckInternet.getNetworkConnectivityStatus(DaySubscribe.this)){
            Submitsubscription submitsubscription=new Submitsubscription();
            submitsubscription.execute(Home.price_id,"Daywise",et_quantity.getText().toString(),dates,String.valueOf(user_id));
        }
        else{
            Toast.makeText(DaySubscribe.this,"No Internet",Toast.LENGTH_SHORT).show();

        }
    }
     /*
    *
    * SUBMIT SUBSCRIPTION
    * */

    private class Submitsubscription extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Submitsubscription";
        ProgressDialog progress;
        int _status;
        String _message;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(DaySubscribe.this, "Please Wait",
                    "Subscripting...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _price_id = params[0];
                String _subscription_type = params[1];
                String _qnty = params[2];
                String _dates = params[3];
                String _user_id = params[4];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.SUBSCRIBE_DETAILS;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("price_id", _price_id)
                        .appendQueryParameter("subscription_type", _subscription_type)
                        .appendQueryParameter("quentity", _qnty)
                        .appendQueryParameter("dates", _dates)
                        .appendQueryParameter("user_id", _user_id);

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

/*
                *//**//*
                *
                * {
    "status": 1,
    "message": "Subscription inserted successfully"
}
            }                                    *
                * *//**//**/
                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    _status=res.getInt("status");
                    if(_status==1){
                       _message="Subscribed";
                    }
                    else{    // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));
                        _message="Subscription Failed";
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if(_status==1) {
                DaySubscribe.this.finish();
                Toast.makeText(DaySubscribe.this,_message,Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(DaySubscribe.this,_message,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
