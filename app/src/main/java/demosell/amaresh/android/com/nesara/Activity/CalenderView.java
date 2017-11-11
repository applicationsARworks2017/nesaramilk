package demosell.amaresh.android.com.nesara.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.timessquare.CalendarPickerView;

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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import demosell.amaresh.android.com.nesara.R;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class CalenderView extends AppCompatActivity {
    private String start_date,end_date,sub_id;
     CalendarPickerView calendar_view;
    ArrayList<String> pauseDate=new ArrayList();
    Button btn_show_dates;
    String  sDate,eDate,server_message;
    int server_status;
    RelativeLayout rel_calender;
     List<Date> dates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_view);
        btn_show_dates = (Button) findViewById(R.id.btn_show_dates);
        rel_calender=(RelativeLayout)findViewById(R.id.rel_calander);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            start_date = extras.getString("STARTDATE");
            end_date = extras.getString("ENDDATE");
            sub_id = extras.getString("SUB_ID");
        }
        calendar_view = (CalendarPickerView) findViewById(R.id.calendar_view);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        //Date startDate = null,enddtae=null;
        try {
            //Date startDate = df.parse(start_date);
            Date startDate = new Date();
            startDate=new Date(startDate.getTime()+(1000 * 60 * 60 * 24));

            Date enddtae=df.parse(end_date);
            enddtae=new Date(enddtae.getTime()+(1000 * 60 * 60 * 24));
            calendar_view.init(startDate, enddtae)
                    .inMode(CalendarPickerView.SelectionMode.RANGE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar_view.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {

            }

            @Override
            public void onDateUnselected(Date date) {
               /*  eDate=changeDateFormat(date.toString());
                Log.e("enddate",eDate);*/
            }
        });

        //individual dates
        dates = calendar_view.getSelectedDates();


        btn_show_dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pausePlay("Pause");
            }
        });
    }

    private void pausePlay(String type) {
        if(CheckInternet.getNetworkConnectivityStatus(CalenderView.this)){
            for (int i = 0; i< calendar_view.getSelectedDates().size();i++){
                sDate = changeDateFormat(calendar_view.getSelectedDates().get(0).toString());
                Log.e("startdate", sDate);
                eDate = changeDateFormat(calendar_view.getSelectedDates().get(i).toString());
                Log.e("enddate", eDate);
                //here you can fetch all dates
             //   Toast.makeText(getApplicationContext(),calendar_view.getSelectedDates().get(i).toString(),Toast.LENGTH_SHORT).show();

            }


            /*for(int i=0;i<dates.size();i++) {
                sDate = changeDateFormat(dates.get(0).toString());
                Log.e("startdate", sDate);
                eDate = changeDateFormat(dates.get(i).toString());
                Log.e("enddate", eDate);
            }*/

            if(sDate==null||sDate.trim().length()<=0){
                showSnackbar("No Start Date Selected");
            }
            else if(eDate==null||eDate.trim().length()<=0){
                showSnackbar("No End Date Selected");
            }
            else{
                startstop asyncTask = new startstop();
                asyncTask.execute(sub_id,sDate,eDate,type);
            }
        }
        else{
            showSnackbar("No Internet");
        }

    }

    public String changeDateFormat(String time) {
        String inputPattern = "EEE MMM dd HH:mm:ss zzz yyyy";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar
                .make(rel_calender, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private class startstop extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Submit User details";
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(CalenderView.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _sub_id = params[0];
                String _s_date = params[1];
                String _e_date = params[2];
                String _type = params[3];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL + Constants.FOLDER + Constants.PLAYPAUSE;
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
                        .appendQueryParameter("subscription_id", _sub_id)
                        .appendQueryParameter("start_date", _s_date)
                        .appendQueryParameter("end_date", _e_date)
                        .appendQueryParameter("type", _type);

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
                 "status": 1,
                 "message": "Subscription inserted successfully"
                 }

                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if (server_status == 1) {
                        server_message = "Sucessful";
                    } else if (server_status == 2) {
                        server_message = "Error";
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
            progressDialog.cancel();
            if (server_status == 1) {
                Snackbar snackbar = Snackbar.make(rel_calender, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
                CalenderView.this.finish();
            } else {
                Snackbar snackbar = Snackbar.make(rel_calender, server_message, Snackbar.LENGTH_LONG);
                snackbar.show();
            }


            // postDetails();

        }
    }

}
