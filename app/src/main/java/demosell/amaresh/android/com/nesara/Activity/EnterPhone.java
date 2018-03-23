package demosell.amaresh.android.com.nesara.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nesara.amaresh.demosell.R;

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
import demosell.amaresh.android.com.nesara.Util.OTPListener;
import demosell.amaresh.android.com.nesara.Util.OtpReader;

public class EnterPhone extends AppCompatActivity implements OTPListener {
    EditText phone;
    Button button;
    int id,otp;
    String v_otp;
    TextView tv_help;
    LinearLayout lin1,lin2;
    EditText et_otp;
    TextView tv_resend;
    String phn_num;
    String fcm_id;
    public static String usertype;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION =100;
    private static final int PERMISSION_ACCESS_MESSAGE =101;
    private static final int PERMISSION_ACCESS_CALL =102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                PERMISSION_ACCESS_MESSAGE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                PERMISSION_ACCESS_CALL);
        fcm_id =EnterPhone.this.getSharedPreferences(Constants.SHAREDPREFERENCEFCM_KEY, 0).getString(Constants.FCM_ID, null);

        phone=(EditText)findViewById(R.id.et_phone);
        button=(Button)findViewById(R.id.bt_sendotp);
        et_otp=(EditText)findViewById(R.id.et_otp);
        tv_resend=(TextView)findViewById(R.id.tv_resend);
        tv_help=(TextView)findViewById(R.id.tv_help);
        lin1=(LinearLayout)findViewById(R.id.lin1);
        lin2=(LinearLayout)findViewById(R.id.lin2);
        lin1.setVisibility(View.VISIBLE);
        lin2.setVisibility(View.GONE);
        tv_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTP();
            }
        });
        tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EnterPhone.this,FileView.class);
                startActivity(intent);
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>9){
                    button.performClick();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString().trim().length()<10){
                    Toast.makeText(getApplicationContext(),"Invalid Phone Number",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(button.getText().toString().contentEquals("SEND OTP")) {
                        sendOTP();
                    }
                    else{
                        if(et_otp.getText().toString().trim().length()<4){
                            Toast.makeText(getApplicationContext(),"Invalid OTP",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            verifyOTP();
                        }

                    }
                }
            }
        });
    }
    private void verifyOTP() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            veryfiy_otp();
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }
    private void veryfiy_otp() {
        verifyMobile asyncTask = new verifyMobile();
        v_otp=et_otp.getText().toString();
        asyncTask.execute(phn_num,v_otp);
    }

    private void sendOTP() {
        OtpReader.bind(this);
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            phn_num=phone.getText().toString();
            // Toast.makeText(Signup.this, R.string.otp_sent, Toast.LENGTH_LONG).show();
            send_mob_num(phn_num);
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }
    private void send_mob_num(String phn_num){
        SynchMobnum asyncTask = new SynchMobnum();
        asyncTask.execute(phn_num);
    }

    @Override
    public void otpReceived(String messageText) {
        v_otp=messageText.substring(messageText.length() - 4);
        et_otp.setText(v_otp);
            button.performClick();

    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        return 0;
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }

    /**
     * Async task to get OTP
     * */
    private class SynchMobnum extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(EnterPhone.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userphone = params[0];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.USER_OTP;
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
                        .appendQueryParameter("mobile_no",_userphone);

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
                 "user": {
                 "id": "24",
                 "otp": 4309
                 },
                 "status": 1,
                 "message": "User inserted successfully"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        JSONObject userObj = res.optJSONObject("user");
                        id=userObj.optInt("id");
                        otp = userObj.optInt("otp");
                        server_message="OTP Sent";
                    }
                    else{
                        server_message="Invalid Credentials";
                    }
                }

                return null;

            } catch(Exception exception){
                server_message="Network Error";
                Log.e(TAG, "otp : doInBackground"+exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(server_status==1) {
                calltopostExecuteveryfy();
            }
            else {
                Toast.makeText(EnterPhone.this, server_message, Toast.LENGTH_LONG).show();
            }
            progressDialog.cancel();
        }
    }
    private void calltopostExecuteveryfy(){
        lin1.setVisibility(View.GONE);
        lin2.setVisibility(View.VISIBLE);
        button.setText("SUBMIT");
           /* Intent i = new Intent(this, Verify.class);
            i.putExtra("id", id);
            i.putExtra("ph_no", phone.getText().toString());
            startActivity(i);*/
    }
    /**
     * Async task to veryfy mobile number and otp
     * */
    private class verifyMobile extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Verify mobile andd otp";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(EnterPhone.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userphone = params[0];
                String _userotp = params[1];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.VERIFY_OTP;
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
                        .appendQueryParameter("mobile_no", _userphone)
                        .appendQueryParameter("otp", _userotp);

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
                 "status": 1,
                 "message": "Successfully verified"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        /*JSONObject userObj = res.optJSONObject("user");
                        id=userObj.optInt("id");
                        otp = userObj.optInt("otp");*/
                        server_message="Verified";
                    }
                    else{
                        server_message="Invalid Credentials";
                    }
                }

                return null;


            } catch(SocketTimeoutException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(ConnectException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(MalformedURLException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (IOException exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(Exception exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.cancel();

            if(server_status==1) {
                calltopostExecute();
               // calltoAdminScreen();
            }
            else {
                Toast.makeText(getApplicationContext(),server_message,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void calltoAdminScreen() {
        String user_type="Admin";
        Intent in=new Intent(getApplicationContext(),AdminDetails.class);
        in.putExtra("id", id);
        in.putExtra("usertype", user_type);
        in.putExtra("ph_no", phn_num);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        in.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(in);
    }

    private void calltopostExecute(){
        if(phn_num.trim().contentEquals("1111111111") ||(phn_num.trim().contentEquals("8884144429")
                ||(phn_num.trim().contentEquals("8884144492")||(phn_num.trim().contentEquals("8249705943"))))) {
            usertype="Admin";
            calltoAdminScreen();
        }
        else {
            usertype="User";
            Intent intent = new Intent(getApplicationContext(), DeliveryDetails.class);
            intent.putExtra("id", id);
            intent.putExtra("ph_no", phn_num);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
    }
}
