package demosell.amaresh.android.com.nesara.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nesara.amaresh.demosell.R;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import demosell.amaresh.android.com.nesara.Adapter.MessagelistAdapter;
import demosell.amaresh.android.com.nesara.Adapter.ProdudtAdapter;
import demosell.amaresh.android.com.nesara.Adapter.SubscriptionListingAdapter;
import demosell.amaresh.android.com.nesara.Adapter.TransactioListingAdapter;
import demosell.amaresh.android.com.nesara.Pojo.Messagelist;
import demosell.amaresh.android.com.nesara.Pojo.Products;
import demosell.amaresh.android.com.nesara.Pojo.SubscriptionListing;
import demosell.amaresh.android.com.nesara.Pojo.TransactionHistoryList;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;
import demosell.amaresh.android.com.nesara.Util.Util;


public class Home extends AppCompatActivity {
    private BottomBar bottomBar;
    RelativeLayout homelayout,subscriptionLayout,settingslayout,contactlayout,activity_wallet,activity_message;
    int id,server_status;
    int user_id;
    int product_id;
    GridView product_grid;
    SwipeRefreshLayout swipe_home;
    TextView tvNoRecordFoundText;
    RelativeLayout main_relative;
    TextView wallet_point,pagehead,c_email,c_phone, alt_phone,tvEdit;
    TextView headername,headeraddress;
    String user_mobile_number,user_name,user_email,user_location,
            user_locality,user_house,user_address,user_city,user_otp,user_created,user_type;
    SubscriptionListingAdapter qadapter;
    String s_id,s_user_id,s_product_name,s_quantity,s_delivery_type,s_start_date,s_end_date,s_price_id,s_liter;
    List<SubscriptionListing> subList;
    ListView lvSubscriptions;
    String userid,name,phone,altphone,email,appartment,flat,phase,city,server_message,sname,sappartment,sflat,sphase,scity,apartment_id;
    EditText et_name,et_phone,et_altphone,et_email,et_appartment,et_flat,et_phase,et_city,help;
    Button edit,done,logout,okay;
    FloatingActionButton fab;
    ImageView bagmoney;
    RelativeLayout rel_notification;
    TextView tv_balance,tv_refresh,tv_addMoney;
    String server_response;
    SwipeRefreshLayout thistory_rel;
    double Wallet_balance;
    int history_status;
    ListView lv,lvmessage;
    List<TransactionHistoryList> trans_list;
    List<Messagelist> message_list;
    ArrayList<Products> prouct_list;
    TransactioListingAdapter transadapter;
    ProdudtAdapter pAdapter;
    MessagelistAdapter adapter;
    String Message_id,location_id;
    public static String price_id,product_name,product_price,product_minqnty,product_wei,appartment_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        main_relative = (RelativeLayout) findViewById(R.id.main_relative);
        rel_notification = (RelativeLayout) findViewById(R.id.rel_notification);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        homelayout = (RelativeLayout) findViewById(R.id.home_layout);
        subscriptionLayout=(RelativeLayout)findViewById(R.id.activity_my_subscription);
        contactlayout=(RelativeLayout)findViewById(R.id.activity_contact);
        tvNoRecordFoundText=(TextView)findViewById(R.id.tvNoRecordFoundText);
        // for home tab

        swipe_home=(SwipeRefreshLayout)findViewById(R.id.swipe_home);
        product_grid=(GridView)findViewById(R.id.product_grid);


        // for wallet tab
        activity_wallet=(RelativeLayout)findViewById(R.id.activity_wallet);
        activity_message=(RelativeLayout)findViewById(R.id.activity_my_message);
        tv_balance=(TextView)findViewById(R.id.tv_balance);
        lv=(ListView)findViewById(R.id.trans_listview);
        thistory_rel=(SwipeRefreshLayout)findViewById(R.id.thistory_rel);
        tv_refresh=(TextView)findViewById(R.id.tv_refresh);
        tv_addMoney=(TextView)findViewById(R.id.tv_addMoney);
        tv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWalletBal();
            }
        });
        thistory_rel.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                transactionList();
            }
        });
        tv_addMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(Home.this);
                dialog.setContentView(R.layout.dialog_add_money);
                ImageView imageView=(ImageView)dialog.findViewById(R.id.close);
                Button add_money=(Button)dialog.findViewById(R.id.add_money);
                final EditText et_add_money=(EditText) dialog.findViewById(R.id.et_add_money);
                add_money.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(),PayMentGateWayPayUbiz.class);
                        intent.putExtra("FIRST_NAME",user_name);
                        intent.putExtra("PHONE_NUMBER",user_mobile_number);
                        intent.putExtra("EMAIL_ADDRESS",email);
                        intent.putExtra("RECHARGE_AMT",et_add_money.getText().toString().trim());
                        intent.putExtra("WALLETBALANCE",Wallet_balance);
                        intent.putExtra("PAYMENTTYPE","Prepaid");
                        startActivity(intent);
                    }
                });
                dialog.show();
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });

        //for profile tab

        settingslayout=(RelativeLayout)findViewById(R.id.activity_settings);
        pagehead = (TextView) findViewById(R.id.tv_pageheading);
        et_name=(EditText)findViewById(R.id.edit_name);
        et_name.setEnabled(false);
        et_phone=(EditText)findViewById(R.id.edit_phone);
        et_phone.setEnabled(false);
        et_phone.setEnabled(false);
        et_altphone=(EditText)findViewById(R.id.edit_alt_phone);
        et_altphone.setEnabled(false);
        et_email=(EditText)findViewById(R.id.edit_mail);
        et_email.setEnabled(false);
        et_appartment=(EditText)findViewById(R.id.edit_appartment);
        et_appartment.setEnabled(false);
        et_flat=(EditText)findViewById(R.id.edit_flat);
        et_flat.setEnabled(false);
        et_phase=(EditText)findViewById(R.id.edit_phase);
        et_phase.setEnabled(false);
        et_city=(EditText)findViewById(R.id.edit_city);
        et_city.setEnabled(false);
        edit=(Button)findViewById(R.id.edit);
        done=(Button)findViewById(R.id.done);
        logout=(Button)findViewById(R.id.logout);
        bagmoney=(ImageView) findViewById(R.id.bagmoney);

        //for Contact tab
        c_email = (EditText) findViewById(R.id.con_email);
        c_email.setText("info@nesaramilk.com");
        c_phone = (EditText) findViewById(R.id.con_phone);
        c_phone.setText("+91 8884144429");
        alt_phone = (EditText) findViewById(R.id.con_alt_phone);
        help = (EditText) findViewById(R.id.help);
        alt_phone.setText("+91 8884144492");
        okay = (Button) findViewById(R.id.btok);
        okay.setText("OK");
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this, FileView.class);
                startActivity(intent);
            }
        });

        // subscribtionpage initializations
        lvSubscriptions=(ListView)findViewById(R.id.subscribtion_listView);
        lvmessage=(ListView)findViewById(R.id.message_listView);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }
        if (id == 0) {
            id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);

        }
        location_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_APARTMENTID, null);


        getUserDetails();
        getWDetails();
// here we can't call because we are getting location id after userdetails
//        getProducts();



        bagmoney.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(Home.this,TransactionHistory.class);
               startActivity(i);
           }
       });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //done.setVisibility(View.VISIBLE);

                edit();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);

                updateinfo();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Dologout();
            }
        });



        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Home.class);
                startActivity(intent);
            }
        });
        c_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String semail=c_email.getText().toString().trim();
                Intent mailintent=new Intent(Intent.ACTION_SEND);
                mailintent.putExtra(Intent.EXTRA_EMAIL, new String[]{ semail});
                //mailintent.setData(Uri.parse("emai:"+semail));
                mailintent.setType("message/rfc822");

                startActivity(Intent.createChooser(mailintent, "Choose an Email client :"));

            }
        });
        c_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {String p_no = c_phone.getText().toString().replaceAll("-", "");;
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + p_no));
                if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);



            }
        });
        alt_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ap_no=alt_phone.getText().toString();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + ap_no));
                if (ActivityCompat.checkSelfPermission(Home.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);

            }
        });

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_home:
                        activity_wallet.setVisibility(View.GONE);
                        activity_message.setVisibility(View.GONE);
                        rel_notification.setVisibility(View.VISIBLE);
                        homelayout.setVisibility(View.VISIBLE);
                        contactlayout.setVisibility(View.GONE);
                        settingslayout.setVisibility(View.GONE);
                        subscriptionLayout.setVisibility(View.GONE);
                        pagehead.setText("Home");
                        pagehead.setTextColor(Color.GRAY);
                        break;
                    case R.id.tab_wallet:
                        rel_notification.setVisibility(View.GONE);
                        activity_message.setVisibility(View.GONE);
                        homelayout.setVisibility(View.GONE);
                        contactlayout.setVisibility(View.GONE);
                        settingslayout.setVisibility(View.GONE);
                        subscriptionLayout.setVisibility(View.GONE);
                        activity_wallet.setVisibility(View.VISIBLE);
                        getWalletBal();
                        transactionList();
                        break;
                    case R.id.tab_subscribe:
                        activity_wallet.setVisibility(View.GONE);
                        activity_message.setVisibility(View.GONE);
                        rel_notification.setVisibility(View.GONE);
                        homelayout.setVisibility(View.GONE);
                        settingslayout.setVisibility(View.GONE);
                        contactlayout.setVisibility(View.GONE);
                        pagehead.setText("Subscriptions");
                        pagehead.setTypeface(Typeface.DEFAULT_BOLD);
                        pagehead.setTextColor(Color.GRAY);
                        subscriptionLayout.setVisibility(View.VISIBLE);
                        subscriptionList();
                        break;
                    case R.id.tab_message:
                        activity_message.setVisibility(View.VISIBLE);
                        activity_wallet.setVisibility(View.GONE);
                        rel_notification.setVisibility(View.GONE);
                        homelayout.setVisibility(View.GONE);
                        settingslayout.setVisibility(View.GONE);
                        contactlayout.setVisibility(View.GONE);
                        pagehead.setText("Message");
                        pagehead.setTypeface(Typeface.DEFAULT_BOLD);
                        pagehead.setTextColor(Color.GRAY);
                        subscriptionLayout.setVisibility(View.GONE);
                        messageList();
                        break;
                    case R.id.tab_setting:
                        activity_wallet.setVisibility(View.GONE);
                        activity_message.setVisibility(View.GONE);
                        rel_notification.setVisibility(View.GONE);
                        homelayout.setVisibility(View.GONE);
                        subscriptionLayout.setVisibility(View.GONE);
                        contactlayout.setVisibility(View.GONE);
                        pagehead.setText("Profile");
                        pagehead.setTextColor(Color.GRAY);
                        settingslayout.setVisibility(View.VISIBLE);

                        settinglist();
                        break;

                    case R.id.tab_about:
                        activity_wallet.setVisibility(View.GONE);
                        activity_message.setVisibility(View.GONE);
                        rel_notification.setVisibility(View.GONE);
                        homelayout.setVisibility(View.GONE);
                        subscriptionLayout.setVisibility(View.GONE);
                        contactlayout.setVisibility(View.VISIBLE);
                        pagehead.setText("Contact");
                        pagehead.setTextColor(Color.GRAY);
                        settingslayout.setVisibility(View.GONE);

                        break;
                }



            }
        });


        /*
        * NOTIFICATION WORK START
        * */

        rel_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,NotificationActivity.class);
                startActivity(intent);
            }
        });

        /*
        * NOTIFICATION WORK END
        * */

               /*
        * HOMEPAGE WORK START
        * */

           product_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Products products=(Products) product_grid.getItemAtPosition(position);
                   price_id=products.getId();
                   product_name=products.getProduct_name();
                   product_price=products.getPrice();
                   product_minqnty=products.getMin_quantity();
                   product_wei=products.getWeight_type();
                   appartment_name=products.getAppartment_name();

                   Intent intent=new Intent(Home.this,NewSubscription.class);
                   //intent.putExtra("UNIT_ID",unityDetailsLocation.getLocation_id());
                   //intent.putExtra("UNIT_NAME",unityDetailsLocation.getLocation_name());
                   startActivity(intent);
               }
           });
           swipe_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
               @Override
               public void onRefresh() {
                   getProducts();
               }
           });

    }

    private void getProducts() {
        if (Util.getNetworkConnectivityStatus(this)) {
            getProductsAsyntask asyncTask = new getProductsAsyntask();
            asyncTask.execute(location_id);
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    private void messageList() {
        if (Util.getNetworkConnectivityStatus(this)) {
            String userid= String.valueOf(id);
            getMessagedetails asyncTask = new getMessagedetails();
            asyncTask.execute(userid);
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    private void Dologout() {
        SharedPreferences sharedPreferences = Home.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent=new Intent(Home.this,EnterPhone.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);

    }


    private void updateinfo() {

        String sid= String.valueOf(id);
        sname=et_name.getText().toString();
        String sphone=et_phone.getText().toString();
        String salt_phone=et_altphone.getText().toString();
        String semail=et_email.getText().toString();
        sappartment=et_appartment.getText().toString();
        sflat=et_flat.getText().toString();
        sphase=et_phase.getText().toString();
        scity=et_city.getText().toString();
        updatedetails asyncTask = new updatedetails();
        // asyncTask.execute(sid);
        asyncTask.execute(sid,sname,sphone,salt_phone,semail,sappartment,sflat,sphase,scity);

    }

    private void edit() {
        done.setVisibility(View.VISIBLE);
        edit.setVisibility(View.GONE);

        et_name.setEnabled(true);
        et_phone.setEnabled(true);
        et_altphone.setEnabled(true);
        et_email.setEnabled(true);
        et_appartment.setEnabled(true);
        et_flat.setEnabled(true);
        et_phase.setEnabled(true);
    }

    private void settinglist() {
        if (Util.getNetworkConnectivityStatus(this)) {
            String sid= String.valueOf(id);
            getSdetails asyncTask = new getSdetails();
            asyncTask.execute(sid);
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    /*
    * CHECK WALLET DETAILS AND UPDATE ON THE TOP BAR
    * */
    private void getWDetails() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            String uid=String.valueOf(id);
            getWdetails w_asyncTask = new getWdetails();
            w_asyncTask.execute(uid,"Prepaid");
        }else {

            Snackbar snackbar = Snackbar
                    .make(main_relative, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();
            //Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    /*
    *
    * GETTING USER DETAILS FOR ORDER
    * */
    private void getUserDetails() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            String sid=String.valueOf(id);
           // getDdetails asyncTask = new getDdetails();
            new getDdetails().execute(sid);
        }else {
            Snackbar snackbar = Snackbar
                    .make(main_relative, "No Internet", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    /**
     * Async task to get user details
     * */
    private class getDdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
       // private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Home.this, "Loading", "Please wait...");
            }*/
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userid = params[0];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.GET_USER_DETAILS;
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
                        .appendQueryParameter("id", _userid);

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
                 "message": "User Updated Successfully"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        JSONArray usrrray = res.getJSONArray("user");
                        for(int i=0;i<usrrray.length();i++){
                            JSONObject userObj=usrrray.getJSONObject(i);
                            user_id=userObj.optInt("id");
                            user_mobile_number=userObj.optString("mobile_no");
                            user_name=userObj.optString("name");
                            user_email=userObj.optString("emailid");
                            user_location=userObj.optString("loaction");
                            user_locality=userObj.optString("locality");
                            user_house=userObj.optString("house");
                            user_address=userObj.optString("address");
                            user_city=userObj.optString("city");
                            user_otp=userObj.optString("otp");
                            user_created=userObj.optString("created");
                            user_type=userObj.optString("user_type");
                            apartment_id=userObj.optString("apartment_id");

                        }

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
            postDetails();
           // progressDialog.cancel();
        }
    }

    private void postDetails() {
        if(server_status==1) {
            SharedPreferences sharedPreferences = Home.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
            //SharedPreferences.Editor editor = pref.edit();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Constants.N_USER_ID,user_id);
            editor.putString(Constants.N_USER_MOBILE,user_mobile_number);
            editor.putString(Constants.N_USER_EMAIL,user_email);
            editor.putString(Constants.N_USER_NAME,user_name);
            editor.putString(Constants.N_USER_LOCATION,user_location);
            editor.putString(Constants.N_USER_APARTMENTID,apartment_id);
            editor.putString(Constants.N_USER_LOCALITY,user_locality);
            editor.putString(Constants.N_USER_HOUSE,user_house);
            editor.putString(Constants.N_USER_ADDRESS,user_address);
            editor.putString(Constants.N_USER_CITY,user_city);
            editor.putString(Constants.N_USER_CREATED,user_created);
            editor.putString(Constants.N_USER_TYPE,user_type);
            editor.commit();

            getProducts();


            //   Toast.makeText(Home.this, "Logged In", Toast.LENGTH_LONG).show();
          /*   Intent i = new Intent(getContext(), Home.class);
            startActivity(i);
*/        }
        else {
            Toast.makeText(Home.this, server_message, Toast.LENGTH_LONG).show();
           /* Intent i = new Intent(Home.this, Home.class);
            startActivity(i);*/
        }
    }


    @Override
    public void onBackPressed() {
//            super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

    }


    /**
     * Async task to get wallet balance from  camp table from server
     * */
    private class getWdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get Wallet Balance";
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Home.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userid = params[0];
                String Prepaid = params[1];
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
                        .appendQueryParameter("user_id", _userid)
                        .appendQueryParameter("payment_type", Prepaid);

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
            wallet_point=(TextView)findViewById(R.id.tv_points);
            wallet_point.setText("Rs."+Wallet_balance);
            progressDialog.cancel();
            if(activity_wallet.getVisibility()==View.VISIBLE){
                tv_balance.setText("\u20B9 "+Wallet_balance);
            }

        }
    }

    /*
    * SUBSCRIPTION PAGE DETAILS
    *
    * */

    private void subscriptionList() {
        String s_user_id=String.valueOf(id);
        SynchSdetails asyncTask = new SynchSdetails();
        asyncTask.execute(s_user_id);
    }


    private class SynchSdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Home.this, "Please Wait",
                    "Loading SubscriptionList...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _userid = params[0];
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
                        .appendQueryParameter("user_id", _userid);

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
                * "subscription": [
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
                            },
                                        *
                * */


                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        JSONArray user_list = res.getJSONArray("subscription");

                        subList = new ArrayList<SubscriptionListing>();

                        //db=new DBHelper(QAAnsweredListActivity.this);

                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject q_list_obj = user_list.getJSONObject(i);

                            s_id = q_list_obj.getString("id");
                            s_user_id = q_list_obj.getString("user_id");
                            s_product_name = q_list_obj.getString("prosuct_name");
                            s_quantity = q_list_obj.getString("quentity");
                            s_liter = q_list_obj.getString("weight_type");
                            s_delivery_type = q_list_obj.getString("subscription_type");
                            s_start_date = q_list_obj.getString("start_date");
                            s_end_date = q_list_obj.getString("end_date");
                            s_price_id = q_list_obj.getString("price_id");
                            String total_day = q_list_obj.getString("total_day");
                            String total_price = q_list_obj.getString("total_price");
                            String is_stop = q_list_obj.getString("is_stop");


                            SubscriptionListing s_list = new SubscriptionListing(s_id, s_user_id, s_product_name, s_quantity,
                                    s_delivery_type, s_start_date, s_end_date, s_price_id,is_stop,s_liter,total_day,total_price);
                            subList.add(s_list);

                        }
                    }// db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));

                        else{
                            server_message="No Data found";
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
            if(server_status==1) {
               // Collections.reverse(subList);
                qadapter = new SubscriptionListingAdapter(Home.this, subList);
                lvSubscriptions.setAdapter(qadapter);
            }
            else {
                lvSubscriptions.setVisibility(View.GONE);
                tvNoRecordFoundText.setVisibility(View.VISIBLE);
            }
            progress.dismiss();

        }
    }


    private class getSdetails extends AsyncTask<String, Void, Void> {
        /**
         * Async task to get sync camp table from server
         * */

            private static final String TAG = "Get User details";
            private ProgressDialog progressDialog = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(progressDialog == null) {
                    progressDialog = ProgressDialog.show(Home.this, "Loading", "Please wait...");
                }
                // onPreExecuteTask();
            }

            @Override
            protected Void doInBackground(String... params) {

                try {

                    String _userid = params[0];
                    InputStream in = null;
                    int resCode = -1;

                    String link = Constants.LIVE_URL+Constants.FOLDER+Constants.GET_USER_DETAILS;
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
                            .appendQueryParameter("id", _userid);

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


                /*
                *
                *
                * {
                      "user": {
                        "id": "33",
                        "mobile_no": "7008623490",
                        "name": "",
                        "emailid": "",
                        "address": "",
                        "city": "",
                        "otp": "9260",
                        "appartment_name": null,
                        "flat_no": null,
                        "block_name": null,
                        "alternet_no": null,
                        "created": "2017-01-16 01:08:16",
                        "modified": "2017-01-16 01:08:16"
                      },
                      "status": 1,
                      "message": "Records Found"
                    }
                *
                *
                * */



                    if(response != null && response.length() > 0) {
                        JSONObject res = new JSONObject(response.trim());
                        server_status = res.optInt("status");
                        if(server_status==1) {

                            JSONArray jarray=res.getJSONArray("user");

                            for(int i=0;i<jarray.length();i++){

                                JSONObject userObj=jarray.getJSONObject(i);
                                userid = userObj.optString("id");
                                phone = userObj.optString("mobile_no");
                                name = userObj.optString("name");
                                email = userObj.optString("emailid");
                                appartment = userObj.optString("appartment_name");
                                flat = userObj.optString("flat_no");
                                phase = userObj.optString("block_name");
                                city = userObj.optString("city");
                                altphone = userObj.optString("alternet_no");

                            }

                        }
                        else{
                            server_message="No records found";
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
                postDetails1();
                progressDialog.cancel();
            }
        }

        private void postDetails1() {
            et_name.setText(name);
            et_phone.setText(phone);
            et_altphone.setText(altphone);
            et_email.setText(email);
            et_flat.setText(flat);
            et_phase.setText(phase);
            et_appartment.setText(appartment);
            et_city.setText(city);
        }

    /**
     * Async task to get sync camp table from server
     * */
    private class updatedetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Send Delivery details";
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Home.this, "Loading", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                //  asyncTask.execute(sid,sname,sphone,salt_phone,semail,sappartment,s_flat,sphase,scity);
                String _userid = params[0];
                String _username = params[1];
                String _userphone = params[2];
                String _user_alt_ph = params[3];
                String _usermail = params[4];
                String _userappartment = params[5];
                String _userflat = params[6];
                String _userphase = params[7];
                String _usercity = params[8];

                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.DELIVER_DETAILS;
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

                /*
* id:1
name:avinash test
emailid:avinas@ffggh.com
city:Bhubaneswar
appartment_name :
flat_no:
block_name:
alternet_no:
*
* */

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", _userid)
                        .appendQueryParameter("name", _username)
                        .appendQueryParameter("emailid", _usermail)
                        .appendQueryParameter("appartment_name", _userappartment)
                        .appendQueryParameter("flat_no", _userflat)
                        .appendQueryParameter("empphone", _userphone)
                        .appendQueryParameter("alternet_no", _user_alt_ph)
                        .appendQueryParameter("block_name", _userphase)
                        .appendQueryParameter("city", _usercity);

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
                 "message": "User Updated Successfully"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        /*JSONObject userObj = res.optJSONObject("user");
                        id=userObj.optInt("id");
                        otp = userObj.optInt("otp");*/
                        server_message="Successfully Upldated";
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
            postExecute();
            progressDialog.cancel();
        }
    }

    private void postExecute() {
        et_name.setEnabled(false);
        et_phone.setEnabled(false);
        et_altphone.setEnabled(false);
        et_email.setEnabled(false);
        et_appartment.setEnabled(false);
        et_flat.setEnabled(false);
        et_phase.setEnabled(false);

    }


    /*
    * FOR THE WALLET PAGE
    * */
    private void getWalletBal() {
        if (CheckInternet.getNetworkConnectivityStatus(Home.this)) {
            getWdetails asyncTask = new getWdetails();
            asyncTask.execute(String.valueOf(id),"Prepaid");
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }
    private void transactionList() {
        thistory_rel.setRefreshing(false);
        if (CheckInternet.getNetworkConnectivityStatus(Home.this)) {
            Transactions asyncTask = new Transactions();
            asyncTask.execute(String.valueOf(id));
        }
        else{
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }
    private class Transactions extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Home.this, "Please Wait",
                    "Loading Payment History...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _userid = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.TRANSACTION_HISTORY;
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
                  "transaction_history": [
                    {
                      "amount": "2170.00",
                      "account_type": "debit",
                      "created": "28-01-2017 02:00 AM"
                    },
                    {
                      "amount": "0.00",
                      "account_type": "debit",
                      "created": "03-02-2017 12:34 AM"
                    },
                            },
                                        *
                * *//**//**/


                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    history_status=res.getInt("status");
                    if(history_status==1){
                        JSONArray user_list = res.getJSONArray("transaction_history");

                        trans_list = new ArrayList<TransactionHistoryList>();

                        //db=new DBHelper(QAAnsweredListActivity.this);

                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject q_list_obj = user_list.getJSONObject(i);

                            String T_amount = q_list_obj.getString("amount");
                            String T_type = q_list_obj.getString("account_type");
                            String T_date = q_list_obj.getString("created");
                            TransactionHistoryList s_list = new TransactionHistoryList(T_amount, T_type, T_date);
                            trans_list.add(s_list);
                        }
                    }
                    else{    // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));
                        server_response="No History";
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
            if(history_status==1) {
                Collections.reverse(trans_list); // this line is used to reverse the list
                transadapter = new TransactioListingAdapter(Home.this, trans_list);
                lv.setAdapter(transadapter);
            }
            progress.dismiss();

        }
    }


    private class getMessagedetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Home.this, "Please Wait",
                    "Loading Message List...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _userid = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.GET_MESSAGE;
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
                "notifications": [
                    {
                      "id": "95",
                        "name": "hssjj",
                        "mobile_no": "8594938936",
                        "title": null,
                        "message": null,
                        "is_read": "1",
                        "created": "12-11-2017 08:00 AM"
                    }
                ],
                "status": 1,
                "message": "Records Found"
            }                                    *
                * *//**//**/
                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    history_status=res.getInt("status");
                    if(history_status==1){
                        JSONArray user_list = res.getJSONArray("notifications");
                        message_list = new ArrayList<Messagelist>();
                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject q_list_obj = user_list.getJSONObject(i);

                             Message_id = q_list_obj.getString("id");
                            String name = q_list_obj.getString("name");
                            String mobile_no = q_list_obj.getString("mobile_no");
                            String title = q_list_obj.getString("title");
                            String message = q_list_obj.getString("message");
                            String is_read = q_list_obj.getString("is_read");
                            String created = q_list_obj.getString("created");
                            String isread = q_list_obj.getString("is_read");
                            Messagelist s_list = new Messagelist(Message_id,name, mobile_no, title,message,is_read,created,isread);
                            message_list.add(s_list);
                        }
                    }
                    else{    // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));
                        server_response="No History";
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
            if(history_status==1) {
                adapter = new MessagelistAdapter(Home.this, message_list);
                lvmessage.setAdapter(adapter);
            }
            else{
                TextView tvmsg=(TextView)findViewById(R.id.tvNoRecordFoundmessage);

                lvmessage.setVisibility(View.GONE);
                tvmsg.setVisibility(View.VISIBLE);
            }
            progress.dismiss();

        }
    }

    /*
    *
    * GET PRODUCTS ASYNTASK
    * */

    private class getProductsAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Product Details";
        ProgressDialog progress;
        int product_status;
        String product_message;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(Home.this, "Please Wait",
                    "Loading Product List...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _loc_id = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.GET_PRODUCTS;
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
                        .appendQueryParameter("apartment_id", _loc_id);

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
                * "price": [
        {
            "id": "1",
            "product_id": "1",
            "apartment_id": "1",
            "min_quantity": "1.00",
            "payment_type": "Prepaid",
            "price": "10.00",
            "appartment_name": "Prestige Shantiniketan",
            "image": null,
            "information": null,
            "product_name": "Regular Milk",
            "status": "Active",
            "weight_type": "Ltr",
            "created": "01-01-1970 12:00 AM"
        },
                "status": 1,
                "message": "Records Found"
            }                                    *
                * *//**//**/
                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    product_status=res.getInt("status");
                    if(product_status==1){
                        JSONArray user_list = res.getJSONArray("price");
                        prouct_list = new ArrayList();
                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject q_list_obj = user_list.getJSONObject(i);

                            String id = q_list_obj.getString("id");
                            String product_id = q_list_obj.getString("product_id");
                            String min_quantity = q_list_obj.getString("min_quantity");
                            String price = q_list_obj.getString("price");
                            String appartment_name = q_list_obj.getString("appartment_name");
                            String image = q_list_obj.getString("image");
                            String product_name = q_list_obj.getString("product_name");
                            String weight_type = q_list_obj.getString("weight_type");

                            Products p_list = new Products(id,product_id,min_quantity,price,appartment_name,image,product_name,weight_type);
                            prouct_list.add(p_list);
                        }
                    }
                    else{    // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));
                        product_message="No Products Found";
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
            if(product_status==1) {
                swipe_home.setVisibility(View.VISIBLE);
                pAdapter = new ProdudtAdapter(Home.this, prouct_list);
                product_grid.setAdapter(pAdapter);
            }
            else{
                swipe_home.setVisibility(View.GONE);
                Toast.makeText(Home.this,product_message,Toast.LENGTH_SHORT).show();
            }

        }
    }

}
