package demosell.amaresh.android.com.nesara.Calander;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nesara.amaresh.demosell.R;

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
import java.util.Date;
import java.util.List;

import demosell.amaresh.android.com.nesara.Activity.DateSubcribe;
import demosell.amaresh.android.com.nesara.Activity.DaySubscribe;
import demosell.amaresh.android.com.nesara.Activity.Home;
import demosell.amaresh.android.com.nesara.Activity.Resumecalender;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

/**
 * Created by Amaresh on 1/30/18.
 */

public class PPGridAdapter extends ArrayAdapter {
    private static final String TAG = PPGridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<DetailsPP> allEvents;
    String action="hi";
    Context _context;
    public PPGridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<DetailsPP> allEvents) {
        super(context, R.layout.ppsingle_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        mInflater = LayoutInflater.from(context);
        this._context=context;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        final int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        final int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        final int displayYear = dateCal.get(Calendar.YEAR);
        final int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        final int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;
        if(view == null){
            view = mInflater.inflate(R.layout.ppsingle_cell_layout, parent, false);
        }
        if(displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        DaySubscribe.selected_dates=new ArrayList<>();
        //Add day to calendar
        TextView cellNumber = (TextView)view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));
        //Add events to the calendar
        final TextView eventIndicator = (TextView)view.findViewById(R.id.event_id);
        final TextView _id = (TextView)view.findViewById(R.id._id);
        final LinearLayout lingrid = (LinearLayout) view.findViewById(R.id.lingrid);
        lingrid.setTag(position);

        Calendar eventCalendar = Calendar.getInstance();
        for(int i = 0; i < allEvents.size(); i++){
            eventCalendar.setTime(allEvents.get(i).getDeliverydate());
            if(dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && displayYear == eventCalendar.get(Calendar.YEAR)){
                eventIndicator.setText(allEvents.get(i).getQuentity());
                // for getting the arraylist id
                _id.setText(allEvents.get(i).getId());
                if(allEvents.get(i).getIs_paused().contentEquals("1")) {
                    lingrid.setBackgroundColor(Color.parseColor("#FFA7A7"));
                }
                else{
                    lingrid.setBackgroundColor(Color.parseColor("#81C784"));

                }
            }
        }

        lingrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date clicked = null;
                String clicked_date=dayValue+"-" +displayMonth+ "-" + displayYear;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                     clicked=sdf.parse(clicked_date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(System.currentTimeMillis() >= clicked.getTime()){
                    Toast.makeText(_context, "This Date can't be modified", Toast.LENGTH_SHORT).show();

                }
                else {
                    // Access the row position here to get the correct data item
                    final Dialog dialog = new Dialog(_context);
                    dialog.setContentView(R.layout.chooseselection);
                    TextView date_details = (TextView) dialog.findViewById(R.id.date_details);
                    //date_details.setText(dayValue+"-"+displayMonth+"-"+displayYear+monthlyDates.get(position));
                    String price = NewCalendarCustomView.price;
                    String id = _id.getText().toString().trim();
                    //date_details.setText(_id.getText().toString());
                    // date_details.setText(price);

                    TextView lin_ok = (TextView) dialog.findViewById(R.id.lin_ok);
                    TextView lin_cancel = (TextView) dialog.findViewById(R.id.lin_cancel);
                    lin_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    final LinearLayout lin_pause = (LinearLayout) dialog.findViewById(R.id.lin_na);
                    final LinearLayout lin_play = (LinearLayout) dialog.findViewById(R.id.lin_p);
                    final LinearLayout lin_edit = (LinearLayout) dialog.findViewById(R.id.lin_a);
                    lin_pause.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Drawable buttonBackground = lingrid.getBackground();
                            action = "Pause";
                            lin_pause.setBackgroundResource(R.drawable.custombuttonselected);
                            lin_play.setBackgroundResource(R.drawable.customedittext);
                            lin_edit.setBackgroundResource(R.drawable.customedittext);


                        }
                    });
                    lin_play.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            action = "Play";
                            lin_pause.setBackgroundResource(R.drawable.customedittext);
                            lin_play.setBackgroundResource(R.drawable.custombuttonselected);
                            lin_edit.setBackgroundResource(R.drawable.customedittext);

                        }
                    });
                    lin_edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            action = "Edit";
                            lin_pause.setBackgroundResource(R.drawable.customedittext);
                            lin_play.setBackgroundResource(R.drawable.customedittext);
                            lin_edit.setBackgroundResource(R.drawable.custombuttonselected);

                        }
                    });
                    lin_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (action.contentEquals("Pause")) {
                                dialog.dismiss();
                                ColorDrawable buttonColor = (ColorDrawable) lingrid.getBackground();
                                int colorId = buttonColor.getColor();
                                if (colorId == Color.parseColor("#FFA7A7")) {
                                    Toast.makeText(_context, "You have already paused for this day", Toast.LENGTH_SHORT).show();
                                }
                                else if(colorId == Color.parseColor("#FFFFFF") || colorId == Color.parseColor("#cccccc")){
                                    Toast.makeText(_context, "Pause can be possible on subscribed Date", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    if (CheckInternet.getNetworkConnectivityStatus(_context)) {
                                        startstop startstop = new startstop();
                                        startstop.execute(_id.getText().toString().trim(), "1");
                                        lingrid.setBackgroundColor(Color.parseColor("#FFA7A7"));
                                    } else {
                                        Toast.makeText(_context, "No Internet", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } else if (action.contentEquals("Play")) {
                                dialog.dismiss();
                                ColorDrawable buttonColor = (ColorDrawable) lingrid.getBackground();
                                int colorId = buttonColor.getColor();
                                if (colorId == Color.parseColor("#81C784")) {
                                    Toast.makeText(_context, "It's on Play mode already", Toast.LENGTH_SHORT).show();
                                }
                                else if(colorId == Color.parseColor("#FFFFFF") || colorId == Color.parseColor("#cccccc")){
                                    Toast.makeText(_context, "Play can be possible on subscribed Date", Toast.LENGTH_SHORT).show();

                                }                                else {
                                    if (CheckInternet.getNetworkConnectivityStatus(_context)) {
                                        startstop startstop = new startstop();
                                        startstop.execute(_id.getText().toString().trim(), "0");
                                        lingrid.setBackgroundColor(Color.parseColor("#81C784"));
                                    } else {
                                        Toast.makeText(_context, "No Internet", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            } else if (action.contentEquals("Edit")) {
                                dialog.dismiss();
                                ColorDrawable buttonColor = (ColorDrawable) lingrid.getBackground();
                                int colorId = buttonColor.getColor();
                                if (colorId == Color.parseColor("#FFA7A7")) {
                                    Toast.makeText(_context, "You can't edit the Paused date", Toast.LENGTH_SHORT).show();
                                }
                                else if(colorId == Color.parseColor("#FFFFFF") || colorId == Color.parseColor("#cccccc")){
                                    Toast.makeText(_context, "Edit can be possible on subscribed Date", Toast.LENGTH_SHORT).show();

                                }
                                else {
                                    final Dialog dialog = new Dialog(_context);
                                    dialog.setContentView(R.layout.edit_selection);
                                    LinearLayout min = (LinearLayout) dialog.findViewById(R.id.min);
                                    LinearLayout plus = (LinearLayout) dialog.findViewById(R.id.plus);
                                    final TextView price = (TextView) dialog.findViewById(R.id.cost);
                                    final TextView qnty = (TextView) dialog.findViewById(R.id.qnty);
                                    TextView ok = (TextView) dialog.findViewById(R.id.ok);
                                    TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
                                    Double d_qnty = Double.valueOf(NewCalendarCustomView.min_qnty);
                                    Double d_price = Double.valueOf(NewCalendarCustomView.price);
                                    qnty.setText(d_qnty.toString());
                                    price.setText("Price: " + d_price.toString());
                                    min.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Double quantity = Double.valueOf(qnty.getText().toString().trim());
                                            Double newquantity;
                                            Double actualquantity = Double.valueOf(NewCalendarCustomView.min_qnty);
                                            Double actualPrice = Double.valueOf(NewCalendarCustomView.price);
                                            if (Double.compare(quantity, actualquantity) == 0) {
                                                Toast.makeText(_context, "This is the minimum", Toast.LENGTH_SHORT).show();
                                            } else {
                                                newquantity = quantity - 0.5;
                                                qnty.setText(newquantity.toString());
                                                actualPrice = actualPrice * newquantity;
                                                price.setText("Rs. " + actualPrice.toString());
                                            }
                                        }
                                    });
                                    plus.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            Double quantity = Double.valueOf(qnty.getText().toString().trim());
                                            Double newquantity;
                                            Double actualPrice = Double.valueOf(NewCalendarCustomView.price);
                                            newquantity = quantity + 0.5;
                                            qnty.setText(newquantity.toString());
                                            actualPrice = actualPrice * newquantity;
                                            price.setText("Rs. " + actualPrice.toString());
                                        }
                                    });
                                    cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                        }
                                    });
                                    ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (CheckInternet.getNetworkConnectivityStatus(_context)) {
                                                Edit edit = new Edit();
                                                edit.execute(_id.getText().toString().trim(), "0", qnty.getText().toString().trim(), price.getText().toString().trim());
                                                eventIndicator.setText(qnty.getText().toString().trim());
                                            } else {
                                                Toast.makeText(_context, "No Internet", Toast.LENGTH_SHORT).show();

                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                    dialog.show();
                                }
                            }


                        }
                    });

                    dialog.show();
                }

                /*ColorDrawable buttonColor = (ColorDrawable) lingrid.getBackground();
                if(buttonColor.getColor()==Color.parseColor("#81C784")){
                    if(displayMonth == currentMonth && displayYear == currentYear){
                        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }else{
                        view.setBackgroundColor(Color.parseColor("#cccccc"));
                    }
                    DaySubscribe.selected_dates.remove(dayValue+"-"+displayMonth+"-"+displayYear);
                    //Toast.makeText(_context, "Clicked " +dayValue+"-"+displayMonth+"-"+displayYear, Toast.LENGTH_LONG).show();
                }
                else {
                    lingrid.setBackgroundColor(Color.parseColor("#81C784"));
                    DaySubscribe.selected_dates.add(dayValue+"-"+displayMonth+"-"+displayYear);

                   // Toast.makeText(_context, "Clicked " + dayValue + displayMonth + displayYear, Toast.LENGTH_LONG).show();
                }*/

            }
        });

        return view;
    }
    @Override
    public int getCount() {
        return monthlyDates.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }

    private class startstop extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Submit User details";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(_context, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _id = params[0];
                String _is_paused = params[1];

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

                        .appendQueryParameter("id", _id)
                        .appendQueryParameter("subscription_id", "00")
                        .appendQueryParameter("is_paused", _is_paused);

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

            } else {
                Toast.makeText(_context,server_message,Toast.LENGTH_SHORT).show();
            }


            // postDetails();

        }
    }
    private class Edit extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Submit User details";
        private ProgressDialog progressDialog = null;
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(_context, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _id = params[0];
                String _is_paused = params[1];
                String _quentity = params[2];
                String _amount = params[3];

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

                        .appendQueryParameter("id", _id)
                        .appendQueryParameter("subscription_id", "00")
                        .appendQueryParameter("is_paused", _is_paused)
                        .appendQueryParameter("quentity", _quentity)
                        .appendQueryParameter("amount", _amount);

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
                Toast.makeText(_context,"Updated",Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(_context,server_message,Toast.LENGTH_SHORT).show();
            }


            // postDetails();

        }
    }
}