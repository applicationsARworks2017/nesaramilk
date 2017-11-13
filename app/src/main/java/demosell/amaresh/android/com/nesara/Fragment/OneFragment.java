package demosell.amaresh.android.com.nesara.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Collections;
import java.util.List;

import demosell.amaresh.android.com.nesara.Activity.AdminUserList;
import demosell.amaresh.android.com.nesara.Activity.Home;
import demosell.amaresh.android.com.nesara.Activity.UserDetailsPage;
import demosell.amaresh.android.com.nesara.Adapter.SubscriptionListingAdapter;
import demosell.amaresh.android.com.nesara.Pojo.SubscriptionListing;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class OneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FrameLayout subscribeframe;
    ListView lv_subscriptionList;
    TextView tv_emptyText;
    SwipeRefreshLayout subscription_rel;
    List<SubscriptionListing> subList;
    SubscriptionListingAdapter qadapter;
    String server_response;
    int server_status;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OneFragment() {
        // Required empty public constructor
    }

    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_one, container, false);
        subscribeframe=(FrameLayout)view.findViewById(R.id.subscribeframe);
        subscription_rel=(SwipeRefreshLayout)view.findViewById(R.id.subscriptions_rel);
        lv_subscriptionList=(ListView)view.findViewById(R.id.subscriptionList);
        tv_emptyText=(TextView)view.findViewById(R.id.no_subscribe);
        subscription_rel.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSubscriptions();

            }
        });
        getSubscriptions();



        return view;
    }

    private void getSubscriptions() {
        subscription_rel.setRefreshing(false);
        if (CheckInternet.getNetworkConnectivityStatus(getActivity())) {
            SynchSdetails asyncTask = new SynchSdetails();
            asyncTask.execute(UserDetailsPage.user_id);
        }else {

            showSnackbar("No Internet");
            //Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar
                .make(subscribeframe, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class SynchSdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(getActivity(), "Please Wait",
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
                             "status": 1,
    "message": "Records Found"
                                        *
                * */


                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    server_status=res.getInt("status");
                    if(server_status==1) {
                        JSONArray user_list = res.getJSONArray("subscription");

                        subList = new ArrayList<SubscriptionListing>();

                        //db=new DBHelper(QAAnsweredListActivity.this);

                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject q_list_obj = user_list.getJSONObject(i);

                            String s_id = q_list_obj.getString("id");
                            String s_user_id = q_list_obj.getString("user_id");
                            String s_product_name = q_list_obj.getString("prosuct_name");
                            String s_quantity = q_list_obj.getString("quentity");
                            String s_delivery_type = q_list_obj.getString("delivery_type");
                            String s_start_date = q_list_obj.getString("start_date");
                            String s_end_date = q_list_obj.getString("end_date");
                            String s_price_id = q_list_obj.getString("price_id");
                            String is_stop = q_list_obj.getString("is_stop");

                            SubscriptionListing s_list = new SubscriptionListing(s_id, s_user_id, s_product_name, s_quantity,
                                    s_delivery_type, s_start_date, s_end_date, s_price_id,is_stop);
                            subList.add(s_list);

                        }
                    }
                    else{
                        server_response ="No result Found";}

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_response=exception.toString();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if(server_status==1) {
                Collections.reverse(subList);
                qadapter = new SubscriptionListingAdapter(getActivity(), subList);
                lv_subscriptionList.setAdapter(qadapter);
            }
            else{
                subscription_rel.setVisibility(View.GONE);
                tv_emptyText.setVisibility(View.VISIBLE);
                showSnackbar(server_response);

            }

        }
    }
}
