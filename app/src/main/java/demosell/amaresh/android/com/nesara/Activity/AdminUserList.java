package demosell.amaresh.android.com.nesara.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
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
import java.util.ArrayList;

import demosell.amaresh.android.com.nesara.Adapter.UserListingAdapter;
import demosell.amaresh.android.com.nesara.Pojo.Users;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class AdminUserList extends AppCompatActivity {
    ListView lv_users;
    SearchView sv_users;
    TextView emplty_usertext;
    RelativeLayout rel_userslist;
    ArrayList<Users> userList;
    String server_response;
    int server_status;
    UserListingAdapter uAdapter;
    ProgressBar userlistprogress;
    private int index = 0, top = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_list);
        lv_users=(ListView)findViewById(R.id.lv_users);
        userlistprogress=(ProgressBar)findViewById(R.id.userlistprogress);
        sv_users=(SearchView)findViewById(R.id.searchusers);
        rel_userslist=(RelativeLayout)findViewById(R.id.rel_userslist);
        emplty_usertext=(TextView)findViewById(R.id.emplty_usertext);
        if(savedInstanceState==null) {
            userList = new ArrayList<>();
            getAllUsers();
        }
        sv_users.setQueryHint("Search users Apartment wise");
        sv_users.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        //*** setOnQueryTextListener ***
        sv_users.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setQuestionList(newText);
                return false;
            }
        });
        lv_users.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               //  Users users=(Users)lv_users.getItemAtPosition(position);
                Users users= (Users) parent.getItemAtPosition(position);
              //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(AdminUserList.this,UserDetailsPage.class);
                intent.putExtra("ID",users.getId());
                startActivity(intent);
                index = lv_users.getFirstVisiblePosition();
                View v = lv_users.getChildAt(0);
                top = (v == null) ? 0 : (v.getTop() - lv_users.getPaddingTop());

            }
        });

    }
    private void setQuestionList(String filterText) {

        final ArrayList<Users> flatlist_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < userList .size(); i++) {
                String q_title = userList.get(i).getFlat_num();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    flatlist_search.add(userList.get(i));
                }
            }
        }else{
            flatlist_search.addAll(userList);
        }
        // create an Object for Adapter
         uAdapter = new UserListingAdapter(AdminUserList.this, flatlist_search);
        lv_users.setAdapter(uAdapter);
        //  mAdapter.notifyDataSetChanged();


        if (flatlist_search.isEmpty()) {
            lv_users.setVisibility(View.GONE);
            emplty_usertext.setVisibility(View.VISIBLE);
        } else {
            lv_users.setVisibility(View.VISIBLE);
            emplty_usertext.setVisibility(View.GONE);
        }

        uAdapter.notifyDataSetChanged();
    }

    private void getAllUsers() {
        if (CheckInternet.getNetworkConnectivityStatus(this)) {
            new getUserdetails().execute();

        }else {

           showSnackbar("No Internet");
            //Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar
                .make(rel_userslist, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    /**
     * Async task to get user details
     * */
    private class getUserdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        // private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            userlistprogress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {

            try {

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
                 * {
                 {
                 "id": "447",
                 "mobile_no": "9344043142",
                 "name": "Kar",
                 "emailid": "abc123@gmail.com",
                 "address": "Denkanikottai - Palakodu Road",
                 "city": "Bengaluru",
                 "otp": "3913",
                 "appartment_name": "ABC",
                 "flat_no": "2",
                 "block_name": "Il",
                 "alternet_no": "9344043142",
                 "user_type": "User",
                 "created": "2017-08-17 20:23:26",
                 "modified": "2017-08-17 20:25:16"
                 }
                 ],
                 "status": 1,
                 "message": "Records Found"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        JSONArray usrrray = res.getJSONArray("user");
                        for(int i=0;i<usrrray.length();i++){
                            JSONObject userObj=usrrray.getJSONObject(i);
                            String id=userObj.optString("id");
                            String user_mobile_number=userObj.optString("mobile_no");
                            String user_name=userObj.optString("name");
                            String block_name=userObj.optString("block_name");
                            String user_email=userObj.optString("emailid");
                            String city=userObj.optString("city");
                            String flat_num=userObj.optString("flat_no");
                            String appartment=userObj.optString("appartment_name");
                            String alternate_num=userObj.optString("alternet_no");
                            String user_type=userObj.optString("user_type");
                            String user_address=userObj.optString("address");
                            String user_created=userObj.optString("created");
                            if(!user_name.isEmpty()) {
                                userList.add(new Users(id, user_mobile_number, user_name, block_name, user_email, city, flat_num,
                                        appartment, alternate_num, user_type, user_address, user_created));
                            }
                        }

                    }
                    else{
                        server_response="Error in data load";
                    }
                }

                return null;


            } catch(Exception exception){
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
                server_response="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            userlistprogress.setVisibility(View.GONE);
            if(server_status==1){
                 uAdapter=new UserListingAdapter(AdminUserList.this,userList);
                lv_users.setAdapter(uAdapter);
                lv_users.setSelectionFromTop(index, top);

            }
            else{
                showSnackbar(server_response);
                lv_users.setVisibility(View.GONE);
                emplty_usertext.setVisibility(View.VISIBLE);
            }
        }
    }
}
