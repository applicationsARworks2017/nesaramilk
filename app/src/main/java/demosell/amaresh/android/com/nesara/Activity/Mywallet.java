package demosell.amaresh.android.com.nesara.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import demosell.amaresh.android.com.nesara.R;
import demosell.amaresh.android.com.nesara.Util.Constants;
import demosell.amaresh.android.com.nesara.Util.Util;

/**
 * Created by Rasmita on 7/18/2017.
 */

public class Mywallet extends BaseActivity {
    Button cashpickup,onlinepay,hundredpay,five_hundredpay,thousandpay;
    EditText amount;
    TextView tv_rs_in_wallet;
    Double change_amount=0.0;
    String product_name;
    int user_id;
    int server_status;
    String server_message,Payment_type;
    double Wallet_balance=0.0;
    double balance=0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mywallet);
        if(null!=toolbar) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitle(getResources().getString(R.string.my_wallet));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(Mywallet.this);
                }
            });

        }
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            product_name = extras.getString("PRODUCTNAME");
            Payment_type = extras.getString("PAYMENTTYPE");
            //id = extras.getInt("id");
        }
        tv_rs_in_wallet=(TextView)findViewById(R.id.rupees_in_wallet);
        amount=(EditText)findViewById(R.id.et_amount);
        hundredpay=(Button)findViewById(R.id.hundred);
        five_hundredpay=(Button)findViewById(R.id.five_hundred);
        thousandpay=(Button)findViewById(R.id.one_thousand);
        hundredpay=(Button)findViewById(R.id.hundred);
        onlinepay=(Button)findViewById(R.id.payonline);

        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);
        /*if (Payment_type.equals("Prepaid")){
            getBalance();

        }*/
        getWalletBalance();
        getBalance();


        onlinepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String samount=amount.getText().toString();
                if(samount.length()<=0){
                    Toast.makeText(Mywallet.this,"Enter Amount",Toast.LENGTH_LONG).show();
                }
                else {
                    Intent intent = new Intent(Mywallet.this, PaymentActivity.class);
                    intent.putExtra("samount",samount);
                    intent.putExtra("PRODUCTNAME",product_name);
                    intent.putExtra("WALLETBALANCE",balance);
                    intent.putExtra("PAYMENTTYPE",Payment_type);
                    startActivity(intent);
                }
            }
        });
        hundredpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String samount=amount.getText().toString();
                if (samount.length()<=0){
                    samount="0.0";
                }
                change_amount=Double.valueOf(samount);
                change_amount=change_amount+100;
                amount.setText(String.valueOf(change_amount));
            }
        });
        five_hundredpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String samount=amount.getText().toString();
                if (samount.length()<=0){
                    samount="0.0";
                }
                change_amount=Double.valueOf(samount);
                change_amount=change_amount+500;
                amount.setText(String.valueOf(change_amount));
            }
        });
        thousandpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String samount=amount.getText().toString();
                if (samount.length()<=0){
                    samount="0.0";
                }
                change_amount=Double.valueOf(samount);
                change_amount=change_amount+1000;
                amount.setText(String.valueOf(change_amount));
            }
        });
    }

    private void getWalletBalance() {
        if (Util.getNetworkConnectivityStatus(this)) {
            String sid=String.valueOf(user_id);
            getWdetails asyncTask = new getWdetails();
            asyncTask.execute(sid,Payment_type);
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }
    private void getBalance() {
        if (Util.getNetworkConnectivityStatus(this)) {
            String sid=String.valueOf(user_id);
            getbalance asyncTask = new getbalance();
            asyncTask.execute(sid,Payment_type);
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Async task to get sync camp table from server
     * */
    private class getWdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Mywallet.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userid = params[0];
                String _payment_type = params[1];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.WALLET_BALANCE;
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
                        .appendQueryParameter("user_id", _userid)
                        .appendQueryParameter("payment_type", _payment_type);

                //.appendQueryParameter("deviceid", deviceid);
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
                if(in == null){
                    return null;
                }
                BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "",data="";

                while ((data = reader.readLine()) != null){
                    response += data + "\n";
                }

                Log.i(TAG, "Response : "+response);

                /**
                 * {
                 "balance": "500.00",
                 "status": 1,
                 "message": "Records Found"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        Wallet_balance=res.optDouble("balance");
                    }
                    else{
                        Wallet_balance=00.00;
                    }
                }

                return null;


            } catch(SocketTimeoutException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            } catch(ConnectException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            } catch(MalformedURLException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            } catch (IOException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            } catch(Exception exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            tv_rs_in_wallet=(TextView)findViewById(R.id.rupees_in_wallet);
            tv_rs_in_wallet.setText("Rs."+Wallet_balance);
            progressDialog.cancel();
        }
    }
    /**
     * Async task to get sync camp table from server
     * */
    private class getbalance extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Mywallet.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userid = params[0];
                String _payment_type = params[1];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.BALANCE;
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
                        .appendQueryParameter("user_id", _userid)
                        .appendQueryParameter("payment_type", _payment_type);

                //.appendQueryParameter("deviceid", deviceid);
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
                if(in == null){
                    return null;
                }
                BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "",data="";

                while ((data = reader.readLine()) != null){
                    response += data + "\n";
                }

                Log.i(TAG, "Response : "+response);

                /**
                 * {
                 "balance": "500.00",
                 "status": 1,
                 "message": "Records Found"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        balance=res.optDouble("balance");
                    }
                    else{
                        balance=00.00;
                    }
                }

                return null;


            } catch(SocketTimeoutException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            } catch(ConnectException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            } catch(MalformedURLException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            } catch (IOException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            } catch(Exception exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.cancel();
        }
    }
}
