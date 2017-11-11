package demosell.amaresh.android.com.nesara.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import demosell.amaresh.android.com.nesara.Pojo.Apartments;
import demosell.amaresh.android.com.nesara.R;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class DeliveryDetails extends AppCompatActivity {
    Button button;
    String ph_no;
    String usertype;
    int id;
    String name,apprtmnt,flat,phase,address,phnum,alt_ph,mailid;
    EditText Name,Flat,Phase,H_address,Phone_no,Alt_ph_no,Mail_id;
    int server_status;
    String server_message;
    public static EditText Appartment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            ph_no = extras.getString("ph_no");
         //   usertype = extras.getString("usertype");
            id = extras.getInt("id");
        }

        button=(Button)findViewById(R.id.submit);
        Name=(EditText)findViewById(R.id.name);
        Appartment=(EditText)findViewById(R.id.et_appartment);
        Flat=(EditText)findViewById(R.id.et_flatno);
        Phase=(EditText)findViewById(R.id.et_phase);
        H_address=(EditText)findViewById(R.id.address);
        Mail_id=(EditText)findViewById(R.id.email);
        Phone_no=(EditText)findViewById(R.id.et_phone);
        Phone_no.setText(ph_no);
        Alt_ph_no=(EditText)findViewById(R.id.et_a_ph);
        Appartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DeliveryDetails.this,AddApartments.class);
                intent.putExtra("PAGE","user");
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });
    }
    private void checkValidation() {
//name,apprtmnt,flat,phase,address,phnum,alt_ph,mailid;
        name=Name.getText().toString();
        mailid=Mail_id.getText().toString();
        apprtmnt=Appartment.getText().toString();
        flat=Flat.getText().toString();
        phase=Phase.getText().toString();
        address=H_address.getText().toString();
        phnum=Phone_no.getText().toString();
        alt_ph=Alt_ph_no.getText().toString();
        // String sname=name;
        if(name.length()<=0){
            Toast.makeText(DeliveryDetails.this,"Enter Your Name",Toast.LENGTH_LONG).show();
        }
        else if(mailid.length()<=0){
            Toast.makeText(DeliveryDetails.this,"Enter Your Mail",Toast.LENGTH_LONG).show();
        }
        else if(apprtmnt.length()<=0){
            Toast.makeText(DeliveryDetails.this,"Enter Your Appartment Name",Toast.LENGTH_LONG).show();
        }else if(flat.length()<=0){
            Toast.makeText(DeliveryDetails.this,"Enter Your Flat Number",Toast.LENGTH_LONG).show();
        }else if(phnum.length()<=0){
            Toast.makeText(DeliveryDetails.this,"Enter Your Phone Number",Toast.LENGTH_LONG).show();
        }else if(alt_ph.length()<=0){
            Toast.makeText(DeliveryDetails.this,"Enter Your Alername Phone Number",Toast.LENGTH_LONG).show();
        }

        else {

            sendDatatoserver();
        }

    }
    private void sendDatatoserver() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            sendDdetails asyncTask = new sendDdetails();
            String sid=String.valueOf(id);
            String city="Bengaluru";
            String usertype="User";

            asyncTask.execute(sid,name,mailid,city,apprtmnt,flat,ph_no,phase,alt_ph,address,usertype);
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }

    }
    /**
     * Async task to save deliver address details
     * */
    private class sendDdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Send Delivery details";
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(DeliveryDetails.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
// asyncTask.execute(sid,name,mailid,city,apprtmnt,flat,ph_no,phase,alt_ph,address);
                String _userid = params[0];
                String _username = params[1];
                String _usermail = params[2];
                String _usercity = params[3];
                String _userappartment = params[4];
                String _userflat = params[5];
                String _userphone = params[6];
                String _userphase = params[7];
                String _user_alt_ph = params[8];
                String _useraddress = params[9];
                String _usertype = params[10];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.DELIVER_DETAILS;
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

                /*
* id:1
name:avinash test
emailid:avinas@ffggh.com
city:Bhubaneswar
appartment_name :
flat_no:
block_name:
alternet_no:
*
* */

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", _userid)
                        .appendQueryParameter("name", _username)
                        .appendQueryParameter("emailid", _usermail)
                        .appendQueryParameter("appartment_name", _userappartment)
                        .appendQueryParameter("flat_no", _userflat)
                        .appendQueryParameter("empphone", _userphone)
                        .appendQueryParameter("alternet_no", _user_alt_ph)
                        .appendQueryParameter("block_name", _userphase)
                        .appendQueryParameter("address", _useraddress)
                        .appendQueryParameter("city", _usercity)
                        .appendQueryParameter("user_type", EnterPhone.usertype);

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
                 "message": "User Updated Successfully"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        /*JSONObject userObj = res.optJSONObject("user");
                        id=userObj.optInt("id");
                        otp = userObj.optInt("otp");*/
                        server_message="Successfully Upldated";
                    }
                    else{
                        server_message="Network Error";
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
            postExecute();
            progressDialog.cancel();
        }
    }

    private void postExecute() {
        if(server_status==1){
            Intent intent=new Intent(DeliveryDetails.this,Home.class);
            intent.putExtra("id",id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        else{
            Toast.makeText(DeliveryDetails.this,server_message,Toast.LENGTH_LONG).show();
        }
    }

}
