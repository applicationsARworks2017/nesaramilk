package demosell.amaresh.android.com.nesara.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import demosell.amaresh.android.com.nesara.Activity.UserDetailsPage;
import demosell.amaresh.android.com.nesara.R;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class ThreeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int server_status;
    String server_response;
    TextView headername,headeraddress;
    EditText et_name,et_phone,et_altphone,et_email,et_appartment,et_flat,et_phase,et_city;
    String user_id,name,phone,altphone,email,appartment,flat,phase,city,server_message,sname,sappartment,sflat,sphase,scity;
    FrameLayout profilerel;

    private OnFragmentInteractionListener mListener;

    public ThreeFragment() {
        // Required empty public constructor
    }

    public static ThreeFragment newInstance(String param1, String param2) {
        ThreeFragment fragment = new ThreeFragment();
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
        View view=inflater.inflate(R.layout.fragment_three, container, false);
        headername=(TextView)view.findViewById(R.id.pro_name);
        profilerel=(FrameLayout)view.findViewById(R.id.profilerel);
        headeraddress=(TextView)view.findViewById(R.id.pro_add);
        et_name=(EditText)view.findViewById(R.id.edit_name);
        et_name.setEnabled(false);
        et_phone=(EditText)view.findViewById(R.id.edit_phone);
        et_phone.setEnabled(false);
        et_altphone=(EditText)view.findViewById(R.id.edit_alt_phone);
        et_altphone.setEnabled(false);
        et_email=(EditText)view.findViewById(R.id.edit_mail);
        et_email.setEnabled(false);
        et_appartment=(EditText)view.findViewById(R.id.edit_appartment);
        et_appartment.setEnabled(false);
        et_flat=(EditText)view.findViewById(R.id.edit_flat);
        et_flat.setEnabled(false);
        et_phase=(EditText)view.findViewById(R.id.edit_phase);
        et_phase.setEnabled(false);
        et_city=(EditText)view.findViewById(R.id.edit_city);
        et_city.setEnabled(false);
        getProfiledetails();


        return view;
    }
    private void getProfiledetails() {
        if (CheckInternet.getNetworkConnectivityStatus(getActivity())) {
            getDdetails asyncTask = new getDdetails();
            asyncTask.execute(UserDetailsPage.user_id);
        }else {
        }
    }
    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar
                .make(profilerel, s, Snackbar.LENGTH_LONG);
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
    private class getDdetails extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(getActivity(), "Loading", "Please wait...");
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

                        JSONArray user_list = res.getJSONArray("user");

                        //db=new DBHelper(QAAnsweredListActivity.this);

                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject userObj = user_list.getJSONObject(i);
                            // user_id = userObj.optString("id");
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
            progressDialog.cancel();
        }
    }

    private void postDetails() {
        headername.setText(name);
        headeraddress.setText(flat+","+appartment+","+city);
        et_name.setText(name);
        et_phone.setText(phone);
        et_altphone.setText(altphone);
        et_email.setText(email);
        et_flat.setText(flat);
        et_phase.setText(phase);
        et_appartment.setText(appartment);
        et_city.setText(city);
    }
}
