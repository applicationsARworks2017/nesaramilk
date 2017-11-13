package demosell.amaresh.android.com.nesara.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nesara.amaresh.demosell.R;

import org.json.JSONArray;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import demosell.amaresh.android.com.nesara.Util.Constants;
import demosell.amaresh.android.com.nesara.Util.Util;

/**
 * Created by Rasmita on 7/18/2017.
 */

public class Subscription_edit extends BaseActivity {
    String id;
    String server_message,userid,productname,quantity,deliverytype,startdate,enddate;
    int server_status,user_id,price_id;
    TextView Prod_name,Prod_qnty,EditVprice;
    EditText Start,End;
    Button update;
    double min_price,min_quan;
    Double price=0.0;
    Double v_amnt=0.0;
    Double a_qnty=0.0;
    Double qnty=0.0;
    String changing_price="",changing_amount="";
    ImageView e_plus,e_minus;
    Calendar myCalendar;
    LinearLayout lay_everyday,lay_alernate,lay_monwedfri,lay_montofri;
    ImageView everyday,alernate,monwedfri,montofri;
    String delivery_type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_edit);

        user_id= getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);
        EditVprice=(TextView)findViewById(R.id.edit_vprice);
        e_minus=(ImageView)findViewById(R.id.edit_min);
        e_plus=(ImageView)findViewById(R.id.edit_plus);
        Start=(EditText)findViewById(R.id.s_edit_date);
        End=(EditText)findViewById(R.id.e_edit_date);
        myCalendar = Calendar.getInstance();
        everyday=(ImageView)findViewById(R.id.edit_everydayimg);
        alernate=(ImageView)findViewById(R.id.edit_alternateimg);
        monwedfri=(ImageView)findViewById(R.id.edit_mn_wedimg);
        montofri=(ImageView)findViewById(R.id.edit_mn_friimg);

        lay_everyday=(LinearLayout)findViewById(R.id.edit_everyday);
        lay_alernate=(LinearLayout)findViewById(R.id.edit_alternate);
        lay_monwedfri=(LinearLayout)findViewById(R.id.edit_mn_wed);
        lay_montofri=(LinearLayout)findViewById(R.id.edit_mn_fri);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
        }
        if (Util.getNetworkConnectivityStatus(Subscription_edit.this)) {
            subscriptionEdit();
        }
        else{
            Toast.makeText(this,"offline mode",Toast.LENGTH_LONG).show();

        }


        e_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(qnty==0.5){
                    Toast.makeText(Subscription_edit.this,"This is the Minimum Amount",Toast.LENGTH_LONG).show();
                }
                else{
                    qnty=qnty-min_quan;
                    v_amnt=v_amnt-min_price;
                    changing_amount= String.valueOf(qnty);
                    changing_price= String.valueOf(v_amnt);
                    String c_p_a="Rs "+changing_price+" / "+changing_amount+" lt";
                    EditVprice=(TextView)findViewById(R.id.edit_vprice);
                    EditVprice.setText(c_p_a);
                    Prod_qnty=(TextView)findViewById(R.id.edit_qnty);
                    String f_q= changing_amount+" lt";
                    Prod_qnty.setText(f_q);

                }


            }
        });

        e_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qnty=qnty+min_quan;
                v_amnt=v_amnt+min_price;
                changing_amount= String.valueOf(qnty);
                changing_price= String.valueOf(v_amnt);
                String c_p_a="Rs "+changing_price+" / "+changing_amount+" lt";
                EditVprice=(TextView)findViewById(R.id.edit_vprice);
                EditVprice.setText(c_p_a);
                Prod_qnty=(TextView)findViewById(R.id.edit_qnty);
                String f_q= changing_amount+" lt";
                Prod_qnty.setText(f_q);

            }
        });
        Start.setOnClickListener(new View.OnClickListener() {
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

                new DatePickerDialog(Subscription_edit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                //start.setText(sdf.format(myCalendar.getTime()));
            }
        });
        End.setOnClickListener(new View.OnClickListener() {
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

                        updateEndDate();
                    }

                };

                new DatePickerDialog(Subscription_edit.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                //   end.setText(sdf.format(myCalendar.getTime()));

            }
        });
        everyday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   lay_everyday.setBackgroundColor(Color.BLUE);
                lay_alernate.setBackgroundColor(Color.WHITE);
                lay_montofri.setBackgroundColor(Color.WHITE);
                lay_monwedfri.setBackgroundColor(Color.WHITE);
                lay_everyday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                delivery_type="Everyday";

            }
        });
        alernate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay_everyday.setBackgroundColor(Color.WHITE);
                //  lay_alernate.setBackgroundColor(Color.BLUE);
                lay_montofri.setBackgroundColor(Color.WHITE);
                lay_monwedfri.setBackgroundColor(Color.WHITE);
                lay_alernate.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                delivery_type="Alternet Day";




            }
        });
        monwedfri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay_everyday.setBackgroundColor(Color.WHITE);
                lay_alernate.setBackgroundColor(Color.WHITE);
                lay_montofri.setBackgroundColor(Color.WHITE);
                // lay_monwedfri.setBackgroundColor(Color.BLUE);
                lay_monwedfri.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                delivery_type="Mon - Wed - Fri";



            }
        });
        montofri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay_everyday.setBackgroundColor(Color.WHITE);
                lay_alernate.setBackgroundColor(Color.WHITE);
                // lay_montofri.setBackgroundColor(Color.BLUE);
                lay_monwedfri.setBackgroundColor(Color.WHITE);
                lay_montofri.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                delivery_type="Mon - Fri";




            }
        });


        update=(Button)findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.getNetworkConnectivityStatus(Subscription_edit.this)) {

                    String S_price_id=String.valueOf(price_id);
                    String S_delivery_type=delivery_type;
                    String S_quantity=changing_amount;
                    String S_date=Start.getText().toString();
                    String E_date=End.getText().toString();
                    String S_user_id=String.valueOf(user_id);
                    if(S_price_id=="0"){
                        Toast.makeText(Subscription_edit.this, "Invalid Price", Toast.LENGTH_LONG).show();

                    }else if(S_delivery_type==null){
                        Toast.makeText(Subscription_edit.this, "Invalid Delivery Type", Toast.LENGTH_LONG).show();

                    }else if(S_date.length()<=0){
                        Toast.makeText(Subscription_edit.this, "Invalid Start Date", Toast.LENGTH_LONG).show();

                    }else if(E_date.length()<=0){
                        Toast.makeText(Subscription_edit.this, "Invalid End Date", Toast.LENGTH_LONG).show();

                    }
                    else{
                        updatePdetails asyncTask = new updatePdetails();
                        asyncTask.execute(S_price_id,S_delivery_type,S_quantity,S_date,E_date,S_user_id,id);
                    }

                }else {
                    Toast.makeText(Subscription_edit.this, "Check Your Internet", Toast.LENGTH_LONG).show();
                }


                /*
                                    id:1
                    price_id:1
                    delivery_type:Everyday
                    quentity:Postpaid
                    start_date:10-01-2017
                    end_date:10-02-2017
                    user_id:1
Toast.makeText(Subscription_edit.this,"Updated",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Subscription_edit.this,MySubscription.class);
                startActivity(intent);*/
            }
        });
    }

    private void subscriptionEdit() {
        EditSub asyncTask = new EditSub();
        asyncTask.execute(id);
    }
    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Start.setText(sdf.format(myCalendar.getTime()));
    }

    private  void  updateEndDate(){
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        End.setText(sdf.format(myCalendar.getTime()));

    }


    private class EditSub extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Subscription_edit.this, "Please Wait",
                    "Loading Subscription...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _id = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.GET_SUBSCRIBE_LIST;
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
                        .appendQueryParameter("id", _id);

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
                *{
                     "subscription": [
                        {
                          "id": "6",
                          "user_id": "22",
                          "prosuct_name": "Organic Milk",
                          "quentity": "2.00",
                          "weight_type": "Ltr",
                          "min_quantity": "0.50",
                          "price": "31.00",
                          "delivery_type": "",
                          "start_date": "31-01-2017",
                          "end_date": "01-02-2017"
                          "price_id": "3",
                        }
                      ],
                      "status": 1,
                      "message": "Records Found"
                        }
                                        *
                * */



                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    JSONArray user_list = res.getJSONArray("subscription");

                    //db=new DBHelper(QAAnsweredListActivity.this);

                    for (int i = 0; i < user_list.length(); i++) {

                        JSONObject q_list_obj = user_list.getJSONObject(i);

                        userid=q_list_obj.optString("user_id");
                        productname=q_list_obj.getString("prosuct_name");
                        quantity=q_list_obj.getString("quentity");
                        deliverytype=q_list_obj.getString("delivery_type");
                        startdate=q_list_obj.getString("start_date");
                        enddate=q_list_obj.getString("end_date");
                        min_price=q_list_obj.getDouble("price");
                        min_quan=q_list_obj.getDouble("min_quantity");
                        price_id=q_list_obj.getInt("price_id");

                    }

                }
                else{
                    server_message="Network Error";
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
            qnty=min_quan*2;
            v_amnt=min_price*2;
            changing_amount= String.valueOf(qnty);
            changing_price= String.valueOf(v_amnt);
            String c_p_a="Rs "+changing_price+" / "+changing_amount+" lt";
            EditVprice=(TextView)findViewById(R.id.edit_vprice);
            EditVprice.setText(c_p_a);

            Prod_qnty=(TextView)findViewById(R.id.edit_qnty);
            String f_q= changing_amount+" lt";
            Prod_qnty.setText(f_q);

            Prod_name=(TextView)findViewById(R.id.Mhead);
            Prod_name.setText(productname);
            Start=(EditText) findViewById(R.id.s_edit_date);
            Start.setText(startdate);
            End=(EditText) findViewById(R.id.e_edit_date);
            End.setText(enddate);
            progress.dismiss();


        }
    }
    private class updatePdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Submit User details";
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Subscription_edit.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _price_id = params[0];
                String _delivery_type = params[1];
                String _quantity = params[2];
                String _s_date = params[3];
                String _e_date = params[4];
                String _user_id = params[5];
                String _id = params[6];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.SUBSCRIBE_DETAILS;
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
                        .appendQueryParameter("price_id", _price_id)
                        .appendQueryParameter("delivery_type", _delivery_type)
                        .appendQueryParameter("quentity", _quantity)
                        .appendQueryParameter("start_date", _s_date)
                        .appendQueryParameter("end_date", _e_date)
                        .appendQueryParameter("user_id", _user_id)
                        .appendQueryParameter("id", _id);

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
                 "message": "Subscription inserted successfully"
                 }

                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        server_message="Subscription Updated";
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
            progressDialog.cancel();
            if(server_status==1){
                Toast.makeText(Subscription_edit.this, server_message, Toast.LENGTH_LONG).show();

            }
            else {
                Toast.makeText(Subscription_edit.this, server_message, Toast.LENGTH_LONG).show();
            }

            // postDetails();

        }
    }
}

