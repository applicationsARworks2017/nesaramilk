package demosell.amaresh.android.com.nesara.Calander;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import demosell.amaresh.android.com.nesara.Activity.Home;
import demosell.amaresh.android.com.nesara.Activity.PausePlayActivity;
import demosell.amaresh.android.com.nesara.Adapter.SubscriptionListingAdapter;
import demosell.amaresh.android.com.nesara.Pojo.SubscriptionListing;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class NewCalendarCustomView extends LinearLayout {
    private static final String TAG = NewCalendarCustomView.class.getSimpleName();
    private ImageView previousButton, nextButton;
    private TextView currentDate;
    private GridView calendarGridView;
    private Button addEventButton;
    private static final int MAX_CALENDAR_COLUMN = 42;
    private int month, year;
    private SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance(Locale.ENGLISH);
    private Context context;
    private PPGridAdapter mAdapter;
    List<DetailsPP> mEvents;
    public static String subscription_id;
    /*public void setvalues(String values) {
        subscription_id = values;
    }*/

    public NewCalendarCustomView(Context context) {
        super(context);
    }
    public NewCalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeUILayout();
        setUpCalendarAdapter();
        setPreviousButtonClickEvent();
        setNextButtonClickEvent();
        setGridCellClickEvents();
        Log.d(TAG, "I need to call this method");
    }
    public NewCalendarCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void initializeUILayout(){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_layout, this);
        previousButton = (ImageView)view.findViewById(R.id.previous_month);
        nextButton = (ImageView)view.findViewById(R.id.next_month);
        currentDate = (TextView)view.findViewById(R.id.display_current_date);
        addEventButton = (Button)view.findViewById(R.id.add_calendar_event);
        calendarGridView = (GridView)view.findViewById(R.id.calendar_grid);
    }
    private void setPreviousButtonClickEvent(){
        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, -1);
                setUpCalendarAdapter();
            }
        });
    }
    private void setNextButtonClickEvent(){
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cal.add(Calendar.MONTH, 1);
                setUpCalendarAdapter();
            }
        });
    }
    private void setGridCellClickEvents(){
        /*calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(context, "Clicked " + position, Toast.LENGTH_LONG).show();
            }
        });*/
    }
    private void setUpCalendarAdapter(){


        if(CheckInternet.getNetworkConnectivityStatus(getContext())){
            getDates getDates=new getDates();
            getDates.execute(subscription_id);
        }
        else{
            Toast.makeText(getContext(),"No Internet",Toast.LENGTH_SHORT).show();
        }

       /* Date date1=null,date2=null,date3=null;
        String sDate1="31/03/2018";
        String sDate2="20/03/2018";
        String sDate3="12/03/2018";
        try {
             date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
             date2=new SimpleDateFormat("dd/MM/yyyy").parse(sDate2);
             date3=new SimpleDateFormat("dd/MM/yyyy").parse(sDate3);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<Date> dayValueInCells = new ArrayList<Date>();
        EventObjects list1 = new EventObjects(1,"My birthday",date1);
        EventObjects list2 = new EventObjects(1,"My birthday",date2);
        EventObjects list3 = new EventObjects(1,"My birthday",date3);

        List<EventObjects> mEvents = new ArrayList<>();
        mEvents.add(list1);
        mEvents.add(list2);
        mEvents.add(list3);
        Calendar mCal = (Calendar)cal.clone();
        mCal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
        mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
        while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
            dayValueInCells.add(mCal.getTime());
            mCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        Log.d(TAG, "Number of date " + dayValueInCells.size());
        String sDate = formatter.format(cal.getTime());
        currentDate.setText(sDate);
        mAdapter = new PPGridAdapter(context, dayValueInCells, cal, mEvents);
        calendarGridView.setAdapter(mAdapter);*/
    }



    private class getDates extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Submit User details";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(getContext(), "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _sub_id = subscription_id;
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL + Constants.FOLDER + Constants.SUBSCRIPTION_DETAILS;
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
                        .appendQueryParameter("subscription_id", _sub_id);

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
                 *  "subscription_details": [
                 {
                 "id": "39",
                 "delivery_date": "04-03-2018",
                 "subscription_id": "8",
                 "quentity": null,
                 "amount": "30.00",
                 "is_delivered": "0",
                 "is_debited": "0",
                 "is_paused": "0",
                 "created": "04-03-2018 06:29 AM",
                 "modified": "2018-03-04 06:29:22",
                 "user_id": "22"
                 },

                 "price": {
                 "min_quantity": "1.00",
                 "price": "20.00"
                 },
                 "status": 1,
                 "message": "Records Found"
                 * */

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    mEvents=new ArrayList<>();
                    Date date1=null;
                    if (server_status == 1) {
                        JSONArray user_list = res.getJSONArray("subscription_details");
                        JSONObject p_obj=res.getJSONObject("price");
                        String min_qnty=p_obj.getString("min_quantity");
                        String price=p_obj.getString("price");
                        DetailsPP detailsP=new DetailsPP(min_qnty,price);

                        for (int i = 0; i < user_list.length(); i++) {
                            JSONObject q_list_obj = user_list.getJSONObject(i);
                            String id = q_list_obj.getString("id");
                            String deliverydate = q_list_obj.getString("delivery_date");
                            String subscription_id = q_list_obj.getString("subscription_id");
                            String quentity = q_list_obj.getString("quentity");
                            String is_delivered = q_list_obj.getString("is_delivered");
                            String is_paused = q_list_obj.getString("is_paused");

                            date1=new SimpleDateFormat("dd-MM-yyyy").parse(deliverydate);

                            DetailsPP detailsPP=new DetailsPP(id,date1,subscription_id,quentity,is_delivered,is_paused);
                            mEvents.add(detailsPP);
                        }

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
            progressDialog.dismiss();
            if(server_status==1) {
                List<Date> dayValueInCells = new ArrayList<Date>();
                Calendar mCal = (Calendar)cal.clone();
                mCal.set(Calendar.DAY_OF_MONTH, 1);
                int firstDayOfTheMonth = mCal.get(Calendar.DAY_OF_WEEK) - 1;
                mCal.add(Calendar.DAY_OF_MONTH, -firstDayOfTheMonth);
                while(dayValueInCells.size() < MAX_CALENDAR_COLUMN){
                    dayValueInCells.add(mCal.getTime());
                    mCal.add(Calendar.DAY_OF_MONTH, 1);
                }
                Log.d(TAG, "Number of date " + dayValueInCells.size());
                String sDate = formatter.format(cal.getTime());
                currentDate.setText(sDate);
                mAdapter = new PPGridAdapter(context, dayValueInCells, cal, mEvents);
                calendarGridView.setAdapter(mAdapter);
            }
            else {
                Toast.makeText(getContext(),server_message,Toast.LENGTH_LONG).show();
            }

        }
    }
}
