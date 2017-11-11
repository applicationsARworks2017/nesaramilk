    package demosell.amaresh.android.com.nesara.Activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

import demosell.amaresh.android.com.nesara.Adapter.ApartmentListAdapter;
import demosell.amaresh.android.com.nesara.Pojo.Apartments;
import demosell.amaresh.android.com.nesara.R;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

    public class Addapartmentdeatils extends AppCompatActivity {
    Button clear,submit;
    EditText ap_name,ap_address;
    RelativeLayout ap_details;
    int server_status;
        String server_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addapartmentdeatils);
        clear=(Button)findViewById(R.id.clear);
        submit=(Button)findViewById(R.id.submit_apartments);
        ap_name=(EditText)findViewById(R.id.ap_name);
        ap_address=(EditText)findViewById(R.id.ap_add);
        ap_details=(RelativeLayout)findViewById(R.id.apdetails_rel);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAll();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckInternet.getNetworkConnectivityStatus(Addapartmentdeatils.this)) {
                    validateFields();

                }else {

                    showSnackbar("No Internet");
                    //Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

        private void validateFields() {
            if (ap_name.getText().toString().trim().length()<=0){
                showSnackbar("Enter Apartment Name");
            }
            else if(ap_address.getText().toString().trim().length()<=0){
                showSnackbar("Enter Address");
            }
            else{
                 new setApartments().execute(ap_name.getText().toString().trim(),ap_address.getText().toString().trim());

            }
        }
    private void clearAll() {
        ap_name.setText("");
        ap_address.setText("");
    }
        /**
         * Async task to update apartments
         * */
        private class setApartments extends AsyncTask<String, Void, Void> {

            private static final String TAG = "Set Apartment details";
            // private ProgressDialog progressDialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(String... params) {

                String name=params[0];
                String address=params[1];


                try {

                    InputStream in = null;
                    int resCode = -1;

                    String link = Constants.LIVE_URL+Constants.FOLDER+Constants.SET_APARTMENTS;
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
                            .appendQueryParameter("name",name )
                            .appendQueryParameter("address",address );

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
                     "status": 1,
                     "message": "Appartments inserted successfully"
                     * */

                    if(response != null && response.length() > 0) {
                        JSONObject res = new JSONObject(response.trim());
                        server_status = res.optInt("status");
                        if(server_status==1) {

                        server_message="Aparment added successfully";

                        }
                        else{
                            server_message="Error in data load";
                        }
                    }

                    return null;


                } catch(Exception exception){
                    Log.e(TAG, "SynchMobnum : doInBackground", exception);
                    server_message="Network Error";
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void user) {
                super.onPostExecute(user);
                if(server_status==1){
                   showSnackbar(server_message);
                    clearAll();

                }
                else{
                    showSnackbar(server_message);

                }
            }
        }
        private void showSnackbar(String s) {
            Snackbar snackbar = Snackbar
                    .make(ap_details, s, Snackbar.LENGTH_LONG);
            snackbar.show();
        }

}
