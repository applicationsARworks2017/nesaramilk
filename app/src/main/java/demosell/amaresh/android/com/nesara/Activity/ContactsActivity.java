package demosell.amaresh.android.com.nesara.Activity;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

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

import demosell.amaresh.android.com.nesara.Adapter.ContactsAdapter;
import demosell.amaresh.android.com.nesara.Adapter.UserListingAdapter;
import demosell.amaresh.android.com.nesara.Pojo.Users;
import demosell.amaresh.android.com.nesara.R;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class ContactsActivity extends AppCompatActivity {
    ListView lv_users;
    SearchView sv_users;
    TextView emplty_usertext;
    RelativeLayout rel_userslist;
    ArrayList<Users> userList=new ArrayList<>();
    String server_response;
    int server_status;
    ContactsAdapter uAdapter;
    ProgressBar userlistprogress;
    Button bt_ok,bt_cancel;
    public static TextView tv_user_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        lv_users=(ListView)findViewById(R.id.lv_users);
        userlistprogress=(ProgressBar)findViewById(R.id.userlistprogress);
        sv_users=(SearchView)findViewById(R.id.searchusers);
        rel_userslist=(RelativeLayout)findViewById(R.id.rel_userslist);
        emplty_usertext=(TextView)findViewById(R.id.emplty_usertext);
        sv_users.setQueryHint("Search Users");
        bt_ok=(Button)findViewById(R.id.bt_ok);
        bt_cancel=(Button)findViewById(R.id.bt_cancel);
        getAllUsers();
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
                CheckBox chk = (CheckBox) view.findViewById(R.id.checkbox);
                Users bean = userList.get(position);
                if (bean.isSelected()) {
                    bean.setSelected(false);
                    chk.setChecked(false);
                } else {
                    bean.setSelected(true);
                    chk.setChecked(true);
                }


            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer sb = new StringBuffer();
                StringBuffer id_sb=new StringBuffer();

                for (Users bean : userList) {
                    if (bean.isSelected()) {
                        if (sb.toString().trim().contains(bean.getUser_name())) {
                        } else {
                            sb.append(bean.getUser_name());
                            sb.append(",");
                            id_sb.append(bean.getId());
                            id_sb.append(",");
                        }



                    }
                    if (sb.length() <= 0) {
                        tv_user_name.setText("To :");
                        ContactsActivity.this.finish();
                    } else {
                        tv_user_name.setText("To :"+sb.toString().trim().substring(0, sb.length() - 1));
                        MessageActivity.rcpt_id=id_sb.toString().trim().substring(0,id_sb.length()-1);
                        CuponCodeActivity.user_ids=id_sb.toString().trim().substring(0,id_sb.length()-1);
                        ContactsActivity.this.finish();
                    }

                }
            }
        });
    }
    private void setQuestionList(String filterText) {

        final ArrayList<Users> flatlist_search = new ArrayList<>();
        if (filterText != null && filterText.trim().length() > 0) {
            for (int i = 0; i < userList .size(); i++) {
                String q_title = userList.get(i).getAppartment();
                if (q_title != null && filterText != null &&
                        q_title.toLowerCase().contains(filterText.toLowerCase())) {
                    flatlist_search.add(userList.get(i));
                }
            }
        }else{
            flatlist_search.addAll(userList);
        }
        // create an Object for Adapter
        uAdapter = new ContactsAdapter(ContactsActivity.this, flatlist_search);
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
                uAdapter=new ContactsAdapter(ContactsActivity.this,userList);
                lv_users.setAdapter(uAdapter);

            }
            else{
                showSnackbar(server_response);
                lv_users.setVisibility(View.GONE);
                emplty_usertext.setVisibility(View.VISIBLE);
            }
        }
    }
}
