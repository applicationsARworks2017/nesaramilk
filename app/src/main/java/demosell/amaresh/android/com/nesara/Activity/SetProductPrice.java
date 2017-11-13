package demosell.amaresh.android.com.nesara.Activity;

import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nesara.amaresh.demosell.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class SetProductPrice extends AppCompatActivity {
    LinearLayout price_organicmilk,price_a2;
    int product_id=0;
    RelativeLayout rel_prod_price;
    String  server_message, product_name;
    int server_status, price_id;
    Double a_qnty,price;
    TextView tv_currentPrice;
    EditText update_amount,update_price;
    LinearLayout update_plus,update_minus;
    Button bt_update;
    Double var_qnty;
    Dialog dialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_product_price);
        dialog= new Dialog(SetProductPrice.this);
        price_organicmilk=(LinearLayout)findViewById(R.id.price_organicmilk);
        price_a2=(LinearLayout)findViewById(R.id.price_a2);
        rel_prod_price=(RelativeLayout)findViewById(R.id.rel_prod_price);
        price_organicmilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_id=1;
                getDialouge(product_id);
            }
        });
        price_a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product_id=2;
                getDialouge(product_id);
            }
        });


    }

    public void getDialouge(int product_id) {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            getPdetails asyncTask = new getPdetails();
            String S_product_id = String.valueOf(product_id);
            asyncTask.execute(S_product_id);
        } else {
            Showsnack("No Internet");
            // Toast.makeText(this, "You are not able to see the price due to offline mode", Toast.LENGTH_LONG).show();
        }
    }

    private void Showsnack(String s) {
        Snackbar snackbar = Snackbar
                .make(rel_prod_price,s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private class getPdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        // private ProgressDialog progressDialog = null;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _product_id = params[0];
                // String _city = params[1];
                //String _cash_type = params[1];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL + Constants.FOLDER + Constants.GET_PRODUCT_DETAILS;
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
                        .appendQueryParameter("product_id", _product_id);
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

                /**
                 * {
                 "price": {
                 "product_id": "1",
                 "product_name": "Organic Milk",
                 "product_weight": "ltr",
                 "quentity": "0.50",
                 "price": "60.00",
                 "city": "Bhubaneswar",
                 "payment_type": "Postpaid"
                 },
                 "status": 1,
                 "message": "Records Found"
                 }

                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        JSONArray userObjarr = res.getJSONArray("price");
                        for(int i=0;i<=userObjarr.length();i++) {
                            JSONObject userObj=userObjarr.getJSONObject(i);
                            product_name = userObj.optString("product_name");
                            a_qnty = userObj.getDouble("quentity");
                            price = userObj.getDouble("price");
                            price_id = userObj.getInt("price_id");
                        }


                    } else {
                        server_message = "Network Error";
                    }
                }

                return null;
            } catch (Exception exception) {
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message = "Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(server_status==1){
               postExecute();
            }
        }
    }

    private void postExecute() {
        dialog.setContentView(R.layout.dialog_changeprice);
        ImageView imageView=(ImageView)dialog.findViewById(R.id.close);
        tv_currentPrice=(TextView)dialog.findViewById(R.id.tv_curPrice);
        update_price=(EditText)dialog.findViewById(R.id.priceupdate);
        update_amount=(EditText)dialog.findViewById(R.id.priceqnty);
        update_plus=(LinearLayout)dialog.findViewById(R.id.plusprice);
        update_minus=(LinearLayout)dialog.findViewById(R.id.minusprice);
        bt_update=(Button)dialog.findViewById(R.id.updatePrice);

        tv_currentPrice.setText("Current Price:"+" \u20B9 "+price+" / "+a_qnty+"L");
        update_amount.setText(a_qnty.toString());
        var_qnty=a_qnty;
        update_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                var_qnty=var_qnty+0.5;
                update_amount.setText(String.valueOf(var_qnty));
            }
        });
        update_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(var_qnty<=0.5){
                    Showsnack("This is the Minimum");
                }
                else {
                    var_qnty = var_qnty - 0.5;
                    update_amount.setText(String.valueOf(var_qnty));
                }
            }
        });
        dialog.show();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdatePrice updatePrice=new UpdatePrice();
                updatePrice.execute(String.valueOf(price_id),update_price.getText().toString().trim(),String.valueOf(var_qnty));
            }
        });

    }
    private class UpdatePrice extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        // private ProgressDialog progressDialog = null;

        @Override

        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _price_id = params[0];
                String _price = params[1];
                String _quantity = params[2];
                // String _city = params[1];
                //String _cash_type = params[1];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL + Constants.FOLDER + Constants.SET_PRODUCT_DETAILS;
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
                        .appendQueryParameter("id", _price_id)
                        .appendQueryParameter("price", _price)
                        .appendQueryParameter("min_quantity", _quantity);
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

                /**
                 * {
                 "price": {
                 "product_id": "1",
                 "product_name": "Organic Milk",
                 "product_weight": "ltr",
                 "quentity": "0.50",
                 "price": "60.00",
                 "city": "Bhubaneswar",
                 "payment_type": "Postpaid"
                 },
                 "status": 1,
                 "message": "Records Found"
                 }

                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        server_message = "Price Updated";

                    } else {
                        server_message = "Network Error";
                    }
                }

                return null;
            } catch (Exception exception) {
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message = "Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if (server_status == 1) {
                dialog.dismiss();
            }
            Showsnack(server_message);
        }
    }
}
