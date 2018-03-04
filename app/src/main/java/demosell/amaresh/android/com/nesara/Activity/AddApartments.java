package demosell.amaresh.android.com.nesara.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import demosell.amaresh.android.com.nesara.Adapter.ApartmentListAdapter;
import demosell.amaresh.android.com.nesara.Pojo.Apartments;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class AddApartments extends AppCompatActivity {
    ListView lv_apartmentList;
    TextView tv_no_aoartments;
    ArrayList<Apartments> apartmentlist;
    int server_status;
    String server_message;
    RelativeLayout aplist_rel;
    ApartmentListAdapter aAdapter;
    FloatingActionButton float_addPrtments;
    String Page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_apartments);
        lv_apartmentList=(ListView)findViewById(R.id.id_apartments);
        tv_no_aoartments=(TextView)findViewById(R.id.no_apartments);
        aplist_rel=(RelativeLayout) findViewById(R.id.aplist_rel);
        float_addPrtments=(FloatingActionButton) findViewById(R.id.float_addPrtments);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            Page = extras.getString("PAGE");
        }
        if(Page.contains("user")){
            float_addPrtments.setVisibility(View.GONE);
        }

        getAllFlats();
        float_addPrtments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AddApartments.this,Addapartmentdeatils.class);
                startActivity(intent);

            }
        });
        lv_apartmentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(Page.contains("user")){
                    Apartments ap_details= (Apartments) adapterView.getItemAtPosition(i);
                    DeliveryDetails.Appartment.setText(ap_details.getApartment_name());
                    DeliveryDetails.appartment_id=ap_details.getId();
                    DeliveryDetails.Appartment.setText(ap_details.getApartment_name());
                    AddApartments.this.finish();
                }

            }
        });
    }

    private void getAllFlats() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            new getApartmentList().execute();

        }else {

            showSnackbar("No Internet");
            //Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Async task to get user details
     * */
    private class getApartmentList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        // private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.GET_APARTMENTS;
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
                        .appendQueryParameter("id", "");

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
                 "appointmentlist": [
                 {
                 "id": "1",
                 "name": "jklhjhjkhjk hjkhjkh",
                 "address": "fgh jkhjkgj",
                 "created": "16-09-2017 06:08 PM",
                 "modified": "2017-09-16 18:08:22"
                 },
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        apartmentlist=new ArrayList<>();
                        JSONArray usrrray = res.getJSONArray("appointmentlist");
                        for(int i=0;i<usrrray.length();i++){
                            JSONObject userObj=usrrray.getJSONObject(i);
                            String id=userObj.optString("id");
                            String apartment_name=userObj.optString("name");
                            String apartments_address=userObj.optString("address");
                            String user_created=userObj.optString("created");
                            apartmentlist.add(new Apartments(id,apartment_name,apartments_address));

                        }

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
                aAdapter=new ApartmentListAdapter(AddApartments.this,apartmentlist);
                lv_apartmentList.setAdapter(aAdapter);

            }
            else{
                showSnackbar(server_message);
                lv_apartmentList.setVisibility(View.GONE);
                tv_no_aoartments.setVisibility(View.VISIBLE);
            }
        }
    }
    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar
                .make(aplist_rel, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

}
