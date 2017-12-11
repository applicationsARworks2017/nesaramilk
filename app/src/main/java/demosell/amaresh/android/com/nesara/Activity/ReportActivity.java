package demosell.amaresh.android.com.nesara.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import demosell.amaresh.android.com.nesara.Adapter.ReportDetailsAdapter;
import demosell.amaresh.android.com.nesara.Pojo.ReportDetails;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class ReportActivity extends AppCompatActivity {
    Button selcetDate;
    Calendar myCalendar=Calendar.getInstance();
    String curr_date;
    int server_status;
    TextView no_data;
    ArrayList<ReportDetails> deliveryDetailsArrayList;
    String server_response;
    ReportDetailsAdapter mAdapter;
    ListView deliverList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        selcetDate=(Button)findViewById(R.id.selcetDate);
        no_data=(TextView) findViewById(R.id.no_data);
        deliverList=(ListView)findViewById(R.id.deliverList);
        selcetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateStartDate();

                    }

                };

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                //  datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });
    }
    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        selcetDate.setText(sdf.format(myCalendar.getTime()));
        curr_date=sdf.format(myCalendar.getTime());
        if(curr_date!=null) {
            if (CheckInternet.getNetworkConnectivityStatus(ReportActivity.this)) {
                new getDelivery().execute(curr_date);

            }
            else{
                Toast.makeText(ReportActivity.this,"No Internet",Toast.LENGTH_LONG).show();
            }        }
        else{
            /*visitorsList.setVisibility(View.GONE);
            et_no_visitors.setVisibility(View.VISIBLE);*/

        }
    }
    /*
* GET STAFF LIST ASYNTASK*/
    private class getDelivery extends AsyncTask<String, Void, Void> {



        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ReportActivity.this, "Please Wait",
                    "Loading ...", true);

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                String _date = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.LIVE_URL+ Constants.FOLDER+ Constants.REPORT;
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
                        .appendQueryParameter("delivery_date", _date);

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
                *
                * {
    "subscription": [
        {
            "id": "22",
            "delivery_date": "26-09-2017",
            "subscription_id": "157",
            "amount": "30",
            "is_delivered": "0",
            "is_debited": "0",
            "is_paused": "0",
            "created": "15-01-2017",
            "modified": "2017-10-17 12:29:45",
            "user_id": "22",
            "mobile_no": "9090403050",
            "name": "amaresh",
            "emailid": "a@gmaibdhs.com",
            "address": "syshs",
            "city": "Bengaluru",
            "otp": "7673",
            "appartment_name": "Sumadhura Madhuram",
            "flat_no": "hsjs",
            "block_name": "gshs",
            "alternet_no": "9090403050",
            "user_type": "User"
        }
    ],
    "status": 1,
    "message": "Records Found"
}
                    },*/

                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        JSONArray staff_List = res.getJSONArray("subscription");
                        deliveryDetailsArrayList = new ArrayList<>();
                        for (int i = 0; i < staff_List.length(); i++) {
                            JSONObject o_list_obj = staff_List.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String subscription_id = o_list_obj.getString("subscription_id");
                            String amount = o_list_obj.getString("amount");
                            String mobile_no = o_list_obj.getString("mobile_no");
                            String name = o_list_obj.getString("name");
                            String flat_no = o_list_obj.getString("flat_no");
                            String address = o_list_obj.getString("address");
                            String block_name = o_list_obj.getString("block_name");
                            String appartment_name = o_list_obj.getString("appartment_name");
                            String product_name = o_list_obj.getString("product_name");
                            String s_quantity = o_list_obj.getString("quentity");
                            String s_liter = o_list_obj.getString("weight_type");
                            /*String appartment = o_list_obj.getString("plot_no");
                            String photo = o_list_obj.getString("photo");*/

                            ReportDetails list1 = new ReportDetails(id,subscription_id,amount,mobile_no,name,flat_no,address,block_name,
                                    appartment_name,product_name,s_quantity,s_liter);
                            deliveryDetailsArrayList.add(list1);
                        }
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
            if(server_status==1) {
                ReportDetailsAdapter mAdapter = new ReportDetailsAdapter(ReportActivity.this, deliveryDetailsArrayList);
                deliverList.setAdapter(mAdapter);
            }
            else{
                deliverList.setVisibility(View.GONE);
                no_data.setVisibility(View.VISIBLE);
            }

        }
    }


}
