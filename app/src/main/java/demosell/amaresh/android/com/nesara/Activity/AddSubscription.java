package demosell.amaresh.android.com.nesara.Activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;
import demosell.amaresh.android.com.nesara.Util.Util;

/**
 * Created by Rasmita on 7/16/2017.
 */

public class AddSubscription extends AppCompatActivity {


    int product_id, user_id;
    String city, delivery_type, server_message, product_name;
    int server_status, price_id;
    ImageView everydayimg, everyday, alernate, monwedfri, montofri;
    Calendar myCalendar;
    EditText start, end;
    LinearLayout plus,minus;
    Button submit;
    TextView vprice, quantity, ProductName,walletPrice;
    Double a_qnty = 0.0;
    Double qnty = 0.0;
    Double price = 0.0;
    Double v_amnt = 0.0;
    String product_price_id;
    Double Wallet_balance;
    String changing_price = "", changing_amount = "";
    LinearLayout lay_everyday, lay_alernate, lay_montofri, lay_monwedfri;
    RelativeLayout prepaidwallet,postpaidwallet,main_relative;
    TextView tv_promo;
    Toolbar toolbar;
    Double discount_price;
    String ExpiryDate;
    TextView discountprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subscription);

        main_relative=(RelativeLayout)findViewById(R.id.activity_add_subscription);

        /*toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitle(getResources().getString(R.string.add_sub));
        setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Overriden
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(AddSubscription.this);
                }
            });*/
         discountprice=(TextView)findViewById(R.id.discountprice);
        vprice = (TextView) findViewById(R.id.vprice);
        tv_promo = (TextView) findViewById(R.id.tv_promo);
        quantity = (TextView) findViewById(R.id.qnty);
        ProductName = (TextView) findViewById(R.id.Mhead);
        everydayimg = (ImageView) findViewById(R.id.imgmilk);
        plus = (LinearLayout) findViewById(R.id.plus);
        minus = (LinearLayout) findViewById(R.id.min);
        start = (EditText) findViewById(R.id.s_date);
        end = (EditText) findViewById(R.id.e_date);
        submit = (Button) findViewById(R.id.submit);
        alernate = (ImageView) findViewById(R.id.alternateimg);
        monwedfri = (ImageView) findViewById(R.id.mn_wedimg);
        montofri = (ImageView) findViewById(R.id.mn_friimg);
        everyday = (ImageView) findViewById(R.id.everydayimg);
        lay_everyday = (LinearLayout) findViewById(R.id.everyday);
        lay_alernate = (LinearLayout) findViewById(R.id.alternate);
        lay_monwedfri = (LinearLayout) findViewById(R.id.mn_wed);
        lay_montofri = (LinearLayout) findViewById(R.id.mn_fri);
        prepaidwallet=(RelativeLayout)findViewById(R.id.prepaidwaillet);
        postpaidwallet=(RelativeLayout)findViewById(R.id.postpaidwaillet);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            product_id = extras.getInt("p_id");
            product_price_id=extras.getString("price_id");

        }
        if(user_id==0){
            user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);

        }
       /* FrameLayout mainFrame = ((FrameLayout) findViewById(R.id.frame));
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(this,
                R.anim.hyperspace_jump);
        mainFrame.startAnimation(hyperspaceJumpAnimation);
*/
        getPruductDetails();
      //  getWalletBalance();
        city = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_CITY, null);
      //  user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);


        if (product_id == 1) {
            everydayimg.setBackgroundResource(R.drawable.regular_new);
        }
        if (product_id == 2) {
            everydayimg.setBackgroundResource(R.drawable.a2desinew);
        }

        //for wallet


        prepaidwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AddSubscription.this,Mywallet.class);
                i.putExtra("PRODUCTNAME",product_name);
                startActivity(i);
            }
        });
        postpaidwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(AddSubscription.this,Mywallet.class);
                i.putExtra("PRODUCTNAME",product_name);
                startActivity(i);
            }
        });






        // For plus and minus
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qnty == 5.0) {
                    Snackbar snackbar = Snackbar
                            .make(main_relative, "This is the Maximum Quantity", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //Toast.makeText(AddSubscription.this, "This is the Maximum Quantity", Toast.LENGTH_LONG).show();
                } else {
                    qnty = qnty + a_qnty;
                    v_amnt = v_amnt + price;
                    changing_amount = String.valueOf(qnty);
                    changing_price = String.valueOf(v_amnt);
                    String c_p_a = "Rs " + changing_price + " / " + changing_amount + " lt";
                    vprice.setText(c_p_a);
                    String f_q = changing_amount + " lt";
                    quantity.setText(f_q);
                    if(discount_price!=null) {
                        Double setDiscount = v_amnt - (discount_price*qnty);
                        discountprice.setText("Dis Price :" + setDiscount);
                    }
                }

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qnty == 0.5) {
                    Snackbar snackbar = Snackbar
                            .make(main_relative, "This is the Minimum Quantity", Snackbar.LENGTH_LONG);
                    snackbar.show();
                   // Toast.makeText(AddSubscription.this, "This is the Minimum Quantity", Toast.LENGTH_LONG).show();
                } else {


                    qnty = qnty - a_qnty;
                    v_amnt = v_amnt - price;
                    changing_amount = String.valueOf(qnty);
                    changing_price = String.valueOf(v_amnt);
                    String c_p_a = "Rs " + changing_price + " / " + changing_amount + " lt";
                    vprice.setText(c_p_a);
                    String f_q = changing_amount + " lt";
                    quantity.setText(f_q);
                    if(discount_price!=null) {
                        Double setDiscount = v_amnt - (discount_price*qnty);
                        discountprice.setText("Dis Price :" + setDiscount);
                    }
                }

            }
        });



        /* start the calander  ............*/



        myCalendar = Calendar.getInstance();


        //// for start and end calender

        start.setOnClickListener(new View.OnClickListener() {
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddSubscription.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.show();

                //start.setText(sdf.format(myCalendar.getTime()));
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddSubscription.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        /*......end  the calender */

        everyday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   lay_everyday.setBackgroundColor(Color.BLUE);
                lay_alernate.setBackgroundColor(Color.WHITE);
                lay_montofri.setBackgroundColor(Color.WHITE);
                lay_monwedfri.setBackgroundColor(Color.WHITE);
                lay_everyday.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                delivery_type = "Everyday";

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
                delivery_type = "Alternet Day";


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
                delivery_type = "Mon - Wed - Fri";


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
                delivery_type = "Mon - Fri";

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.getNetworkConnectivityStatus(AddSubscription.this)) {

                    String S_price_id = String.valueOf(price_id);
                    String S_delivery_type = delivery_type;
                    String S_quantity = changing_amount;
                    String S_date = start.getText().toString();
                    String E_date = end.getText().toString();
                    String S_user_id = String.valueOf(user_id);
                    if (S_price_id == "0") {
                        Toast.makeText(AddSubscription.this, "Invalid Price", Toast.LENGTH_LONG).show();

                    } else if (S_delivery_type == null) {
                        Toast.makeText(AddSubscription.this, "Invalid Delivery Type", Toast.LENGTH_LONG).show();

                    } else if (S_date.length() <= 0) {
                        Toast.makeText(AddSubscription.this, "Invalid Start Date", Toast.LENGTH_LONG).show();

                    } else if (E_date.length() <= 0) {
                        Toast.makeText(AddSubscription.this, "Invalid End Date", Toast.LENGTH_LONG).show();

                    } else {
                        submitPdetails asyncTask = new submitPdetails();
                        asyncTask.execute(S_price_id, S_delivery_type, S_quantity, S_date, E_date, S_user_id);
                    }

                } else {
                    Snackbar snackbar = Snackbar
                            .make(main_relative, "Check Your Internet", Snackbar.LENGTH_LONG);
                    snackbar.show();
                  //  Toast.makeText(AddSubscription.this, "Check Your Internet", Toast.LENGTH_LONG).show();
                }
                  /* Intent i=new Intent(AddSubscription.this,MySubscription.class);
                    startActivity(i);*/

            }
        });

        //promocode
        tv_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(AddSubscription.this);
                dialog.setContentView(R.layout.dialog_promo);
                ImageView imageView=(ImageView)dialog.findViewById(R.id.close);
                Button bt_apply=(Button) dialog.findViewById(R.id.bt_apply);
                final EditText et_promo=(EditText)dialog.findViewById(R.id.et_promo);
                dialog.show();
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                bt_apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if(CheckInternet.getNetworkConnectivityStatus(AddSubscription.this)){
                            new getPromooffer().execute(String.valueOf(user_id),et_promo.getText().toString().trim(),product_price_id);
                        }
                        else {
                            Snackbar snackbar = Snackbar
                                    .make(main_relative, "Check Your Internet", Snackbar.LENGTH_LONG);
                            snackbar.show();
                            //  Toast.makeText(AddSubscription.this, "Check Your Internet", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }

    private void getWalletBalance() {
        if (Util.getNetworkConnectivityStatus(this)) {
            String sid=String.valueOf(user_id);
            getWdetails asyncTask = new getWdetails();
            asyncTask.execute(sid);
        }else {
            Snackbar snackbar = Snackbar
                    .make(main_relative, "You are in Offline Mode", Snackbar.LENGTH_LONG);
            snackbar.show();
          //  Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }


    private void getPruductDetails() {
        if (Util.getNetworkConnectivityStatus(this)) {
            getPdetails asyncTask = new getPdetails();
            String S_product_id = String.valueOf(product_id);
            asyncTask.execute(S_product_id);
        } else {

            Snackbar snackbar = Snackbar
                    .make(main_relative, "You are not able to see the price due to offline mode", Snackbar.LENGTH_LONG);
            snackbar.show();
           // Toast.makeText(this, "You are not able to see the price due to offline mode", Toast.LENGTH_LONG).show();
        }
    }

    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        start.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        end.setText(sdf.format(myCalendar.getTime()));

    }


    private void walletlay() {
        if(prepaidwallet.getVisibility()==View.VISIBLE) {
            prepaidwallet.setVisibility(View.GONE);
        }
        else{
            prepaidwallet.setVisibility(View.VISIBLE);

        }
    }




    /**
     * Async task to get sync camp table from server
     * */
    private class getWdetails extends AsyncTask<String, Void, Void> {

            private static final String TAG = "Get User details";
            //    private ProgressDialog progressDialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
              /*  if(progressDialog == null) {
                    progressDialog = ProgressDialog.show(AddSubscription.this, "Loading", "Please wait...");
                }*/
                // onPreExecuteTask();
            }

            @Override
            protected Void doInBackground(String... params) {

                try {

                    String _userid = params[0];
                   // String _payment_type = params[1];
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
                            .appendQueryParameter("user_id", _userid);
                           // .appendQueryParameter("payment_type", _payment_type);

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
                     "balance": "500.00",
                     "status": 1,
                     "message": "Records Found"
                     }
                     * */

                    if(response != null && response.length() > 0) {
                        JSONObject res = new JSONObject(response.trim());
                        server_status = res.optInt("status");
                        if(server_status==1) {
                            Wallet_balance=res.optDouble("balance");
                        }
                        else{
                            Wallet_balance=00.00;
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

                    walletPrice = (TextView) findViewById(R.id.price);
                    String swprice = String.valueOf(Wallet_balance);
                    walletPrice.setText("Rs." + swprice);
                    //  progressDialog.cancel();
                }
            }





            /**
             * Async task to get sync camp table from server
             */
            private class getPdetails extends AsyncTask<String, Void, Void> {

                private static final String TAG = "Get User details";
                // private ProgressDialog progressDialog = null;

                @Override

                protected void onPreExecute() {
                    super.onPreExecute();
                  /*  if(progressDialog == null) {
                        progressDialog = ProgressDialog.show(AddSubscription.this, "Loading", "Please wait...");
                    }*/
                    // onPreExecuteTask();
                }

                @Override
                protected Void doInBackground(String... params) {

                    try {

                        String _product_id = params[0];
                        // String _city = params[1];
                        //String _cash_type = params[1];
                        InputStream in = null;
                        int resCode = -1;

                        String link = Constants.LIVE_URL + Constants.FOLDER + Constants.GET_PRODUCT_DETAILS;
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
                                .appendQueryParameter("product_id", _product_id);
                        //  .appendQueryParameter("city", _city)
                        // .appendQueryParameter("payment_type", _cash_type);

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
                         "price": {
                         "product_id": "1",
                         "product_name": "Organic Milk",
                         "product_weight": "ltr",
                         "quentity": "0.50",
                         "price": "60.00",
                         "city": "Bhubaneswar",
                         "payment_type": "Postpaid"
                         },
                         "status": 1,
                         "message": "Records Found"
                         }

                         * */

                        if (response != null && response.length() > 0) {
                            JSONObject res = new JSONObject(response.trim());
                            server_status = res.optInt("status");
                            if (server_status == 1) {
                                JSONArray userObjarr = res.optJSONArray("price");
                                for(int i=0;i<userObjarr.length();i++) {
                                    JSONObject userObj= userObjarr.getJSONObject(i);
                                    product_name = userObj.optString("product_name");
                                    a_qnty = userObj.getDouble("quentity");
                                    price = userObj.getDouble("price");
                                    price_id = userObj.getInt("price_id");
                                }


                            } else {
                                server_message = "Network Error";
                            }
                        }

                        return null;


                    } catch (SocketTimeoutException exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
                    } catch (ConnectException exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
                    } catch (MalformedURLException exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
                    } catch (IOException exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
                    } catch (Exception exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
                    }

                    return null;
                }

                @Override
                protected void onPostExecute(Void user) {
                    super.onPostExecute(user);
                    // postDetails();
                    ProductName.setText(product_name);
                    qnty = a_qnty * 2;
                    v_amnt = price * 2;
                    changing_amount = String.valueOf(qnty);
                    changing_price = String.valueOf(v_amnt);
                    String c_p_a = "Rs " + changing_price + " / " + changing_amount + " lt";
                    vprice = (TextView) findViewById(R.id.vprice);
                    vprice.setText(c_p_a);
                    String f_q = changing_amount + " lt";
                    quantity = (TextView) findViewById(R.id.qnty);
                    quantity.setText(f_q);
                    //    progressDialog.cancel();
                }
            }


            private class submitPdetails extends AsyncTask<String, Void, Void> {

                private static final String TAG = "Submit User details";
                private ProgressDialog progressDialog = null;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    if (progressDialog == null) {
                        progressDialog = ProgressDialog.show(AddSubscription.this, "Loading", "Please wait...");
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

                        InputStream in = null;
                        int resCode = -1;

                        String link = Constants.LIVE_URL + Constants.FOLDER + Constants.SUBSCRIBE_DETAILS;
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
                                .appendQueryParameter("user_id", _user_id);

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
                                server_message = "Subscription Completed";
                            } else if (server_status == 2) {
                                server_message = "Kindly Credit your Wallet balance";
                            } else {
                                server_message = "Network Error";
                            }
                        }

                        return null;


                    } catch (SocketTimeoutException exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
                    } catch (ConnectException exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
                    } catch (MalformedURLException exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
                    } catch (IOException exception) {
                        Log.e(TAG, "SynchMobnum : doInBackground", exception);
                        server_message = "Network Error";
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
                        Snackbar snackbar = Snackbar.make(main_relative, server_message, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        // Changing message text color
                        snackbar.setActionTextColor(Color.GREEN);
                      //  Toast.makeText(AddSubscription.this, server_message, Toast.LENGTH_LONG).show();
                        // intent = new Intent(AddSubscription.this, MySubscription.class);
                       // startActivity(intent);
                    } else {
                        Snackbar snackbar = Snackbar.make(main_relative, server_message, Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.RED);
                        View sbView = snackbar.getView();
                        sbView.setBackgroundColor(Color.BLACK);
                        snackbar.show();
                        // Changing message text color
                       // Toast.makeText(AddSubscription.this, server_message, Toast.LENGTH_LONG).show();
                    }

                    // postDetails();

                }
            }
    /**
     * Async task to get sync camp table from server
     * */
    private class getPromooffer extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get Promo details";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userid = params[0];
                String _promocode = params[1];
                String _price_id = params[2];
                // String _payment_type = params[1];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.APPLY_PROMO;
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
                        .appendQueryParameter("user_id", _userid)
                        .appendQueryParameter("promo_code", _promocode)
                        .appendQueryParameter("price_id", _price_id);
                // .appendQueryParameter("payment_type", _payment_type);

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

                /*{
    "promo": {
        "id": "20",
        "promo_code": "2CO2RQ8P",
        "user_id": "22",
        "price_id": "5",
        "value": "10.00",
        "expired_on": "2017-08-30 00:00:00",
        "created": "2017-08-28 12:43:18",
        "modified": "2017-08-28 12:43:18"
    },
    "status": 1,
    "message": "Records Found"
}
                * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        JSONObject obj=res.getJSONObject("promo");
                        discount_price=obj.getDouble("value");
                        ExpiryDate=obj.getString("expired_on");
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
                discountprice.setVisibility(View.VISIBLE);
                Double setDiscount=v_amnt-discount_price;
                discountprice.setText("Dis Price :"+setDiscount);
            }
            else{
                Snackbar snackbar = Snackbar
                        .make(main_relative, "Ivalid Promo", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            //  progressDialog.cancel();
        }
    }

}