package demosell.amaresh.android.com.nesara.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class MessageActivity extends AppCompatActivity {
    ImageView iv_getContact;
    TextView rcpt_name;
    public static String rcpt_id;
    Button send;
    RelativeLayout msgrel;
    EditText et_title,msgbody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        iv_getContact=(ImageView) findViewById(R.id.iv_getContact);
        rcpt_name=(TextView)findViewById(R.id.rcpt_name);
        et_title=(EditText)findViewById(R.id.et_title);
        msgbody=(EditText)findViewById(R.id.msgbody);
        msgrel=(RelativeLayout)findViewById(R.id.msgrel);
        send=(Button)findViewById(R.id.send);
        iv_getContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsActivity.tv_user_name=rcpt_name;
                Intent intent=new Intent(MessageActivity.this,ContactsActivity.class);
                startActivity(intent);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(CheckInternet.getNetworkConnectivityStatus(MessageActivity.this)){
                sendMessage();
            }
            else{
                showSnackbar("No Internet");
            }
            }
        });



    }

    private void sendMessage() {
        if(et_title.getText().toString().length()<=0){
            showSnackbar("Please give the title");
        }
        else if(msgbody.getText().toString().length()<=0){
            showSnackbar("Enter message please");
        }
        else if(rcpt_id==null){
            showSnackbar("Select Receipent");
        }
        else {
            new sendMessage().execute(rcpt_id, et_title.getText().toString().trim(),msgbody.getText().toString().trim());
        }
    }

    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar
                .make(msgrel, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    /**
     * Async task to send message
     * */
    private class sendMessage extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        int server_status;
        String server_response;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userids = params[0];
                String _title = params[1];
                String _body = params[2];
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
                        .appendQueryParameter("user_id", _userids)
                        .appendQueryParameter("message", _body)
                        .appendQueryParameter("title", _title);

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

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        server_response="Message Sent";
                    }
                    else{
                        server_response="Message Failed";
                    }
                }

                return null;

            } catch(Exception exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_response="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            if(server_status==1){
                et_title.setText("");
                msgbody.setText("");
                rcpt_name.setText("To :");
                rcpt_id=null;
            }
           showSnackbar(server_response);
        }
    }
}
