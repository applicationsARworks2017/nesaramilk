package demosell.amaresh.android.com.nesara.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

import demosell.amaresh.android.com.nesara.Adapter.TransactioListingAdapter;
import demosell.amaresh.android.com.nesara.Pojo.TransactionHistoryList;
import demosell.amaresh.android.com.nesara.Util.Constants;
import demosell.amaresh.android.com.nesara.Util.Util;

public class TransactionHistory extends AppCompatActivity {
    ListView lv;
    int user_id;
    String S_user_id,T_amount,T_type,T_date,Payment_type;
    List<TransactionHistoryList> trans_list;
    TransactioListingAdapter qadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        if (null != toolbar) {
            toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
            toolbar.setTitle(getResources().getString(R.string.History));
            toolbar.setTitleTextColor(Color.GRAY);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(TransactionHistory.this);
                }
            });
        }

        Bundle extras = getIntent().getExtras();

        lv=(ListView)findViewById(R.id.trans_listview);
        user_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getInt(Constants.N_USER_ID, 0);
        S_user_id= String.valueOf(user_id);

        if (Util.getNetworkConnectivityStatus(TransactionHistory.this)) {
          transactionList();
        }
        else{
            Toast.makeText(TransactionHistory.this,"no internet", Toast.LENGTH_LONG).show();
        }
    }

    private void transactionList() {
        SynchSdetails asyncTask = new SynchSdetails();
        asyncTask.execute(S_user_id);
    }
   private class SynchSdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(TransactionHistory.this, "Please Wait",
                    "Loading SubscriptionList...", true);

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
                    JSONArray user_list = res.getJSONArray("transaction_history");

                    trans_list = new ArrayList<TransactionHistoryList>();

                    //db=new DBHelper(QAAnsweredListActivity.this);

                    for (int i = 0; i < user_list.length(); i++) {

                        JSONObject q_list_obj = user_list.getJSONObject(i);

                        T_amount = q_list_obj.getString("amount");
                        T_type = q_list_obj.getString("account_type");
                        T_date = q_list_obj.getString("created");
                        TransactionHistoryList s_list=new TransactionHistoryList(T_amount,T_type,T_date);
                        trans_list.add(s_list);

                        // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));

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
            qadapter = new TransactioListingAdapter(TransactionHistory.this,trans_list);
            lv.setAdapter(qadapter);
            progress.dismiss();

        }
    }
}
