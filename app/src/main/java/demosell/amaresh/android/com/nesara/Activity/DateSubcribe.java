package demosell.amaresh.android.com.nesara.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import demosell.amaresh.android.com.nesara.Util.Constants;

public class DateSubcribe extends AppCompatActivity {
    EditText s_date,e_date;
    Calendar myCalendar;
    TextView apartment_name,product_name,product_price,et_quantity;
    LinearLayout plus,minus;
    Button submit;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_subcribe);
        s_date=(EditText)findViewById(R.id.s_date);
        e_date=(EditText)findViewById(R.id.e_date);
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);
        apartment_name=(TextView)findViewById(R.id.apartment);
        product_name=(TextView)findViewById(R.id.product);
        product_price=(TextView)findViewById(R.id.product_price);
        et_quantity=(TextView) findViewById(R.id.qnty);
        plus=(LinearLayout)findViewById(R.id.plus);
        minus=(LinearLayout)findViewById(R.id.min);
        submit=(Button)findViewById(R.id.submit);

        myCalendar = Calendar.getInstance();


        s_date.setOnClickListener(new View.OnClickListener() {
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(DateSubcribe.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.show();

                //start.setText(sdf.format(myCalendar.getTime()));
            }
        });
        e_date.setOnClickListener(new View.OnClickListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(DateSubcribe.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });
        apartment_name.setText(Home.appartment_name.toUpperCase());
        product_name.setText(Home.product_name +" (in "+Home.product_wei+")");
        product_price.setText("Rs. "+Home.product_price);
        et_quantity.setText(Home.product_minqnty);

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double quantity=Double.valueOf(et_quantity.getText().toString().trim());
                Double newquantity;
                Double actualquantity=Double.valueOf(Home.product_minqnty);
                Double actualPrice=Double.valueOf(Home.product_price);
                if(Double.compare(quantity,actualquantity)==0){
                    Toast.makeText(DateSubcribe.this,"This is the minimum",Toast.LENGTH_SHORT).show();
                }
                else{
                    newquantity=quantity-0.5;
                    et_quantity.setText(newquantity.toString());
                    actualPrice=actualPrice*newquantity;
                    product_price.setText("Rs. "+actualPrice.toString());
                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double quantity=Double.valueOf(et_quantity.getText().toString().trim());
                Double newquantity;
                Double actualPrice=Double.valueOf(Home.product_price);
                newquantity=quantity+0.5;
                et_quantity.setText(newquantity.toString());
                actualPrice=actualPrice*newquantity;
                product_price.setText("Rs. "+actualPrice.toString());

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start_date,end_date;
                start_date=s_date.getText().toString().trim();
                end_date=e_date.getText().toString().trim();
                if(start_date.contentEquals("Start Date") || start_date.contentEquals("")){
                    Toast.makeText(DateSubcribe.this,"Please Select Start Date",Toast.LENGTH_SHORT).show();
                }
                else if(end_date.contentEquals("End Date") || end_date.contentEquals("")){
                    Toast.makeText(DateSubcribe.this,"Please Select End Date",Toast.LENGTH_SHORT).show();

                }
                else{
                    submitDetails(start_date,end_date);

                }
            }
        });
    }

    private void submitDetails(String start_date, String end_date) {
        Submitsubscription submitsubscription=new Submitsubscription();
        submitsubscription.execute(Home.price_id,"Datewise",et_quantity.getText().toString().trim(),start_date,end_date,String.valueOf(user_id));
    }

    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        s_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        e_date.setText(sdf.format(myCalendar.getTime()));

    }
      /*
    *
    * SUBMIT SUBSCRIPTION
    * */

    private class Submitsubscription extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Submitsubscription";
        ProgressDialog progress;
        int _status;
        String _message;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(DateSubcribe.this, "Please Wait",
                    "Subscripting...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _price_id = params[0];
                String _subscription_type = params[1];
                String _qnty = params[2];
                String _startDate = params[3];
                String _endDate = params[4];
                String _user_id = params[5];
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
                        .appendQueryParameter("subscription_type", _subscription_type)
                        .appendQueryParameter("quentity", _qnty)
                        .appendQueryParameter("start_date", _startDate)
                        .appendQueryParameter("end_date", _endDate)
                        .appendQueryParameter("user_id", _user_id);

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
                *//**//*
                *
                * {
    "status": 1,
    "message": "Subscription inserted successfully"
}
            }                                    *
                * *//**//**/
                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    _status=res.getInt("status");
                    if(_status==1){
                        _message="Subscribed";
                    }
                    else{    // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));
                        _message="Subscription Failed";
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
            if(_status==1) {
                DateSubcribe.this.finish();
                Toast.makeText(DateSubcribe.this,_message,Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(DateSubcribe.this,_message,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
