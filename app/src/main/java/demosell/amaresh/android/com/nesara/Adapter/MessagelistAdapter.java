package demosell.amaresh.android.com.nesara.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
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
import java.util.List;

import demosell.amaresh.android.com.nesara.Activity.MessageDetilActivity;
import demosell.amaresh.android.com.nesara.Pojo.Messagelist;
import demosell.amaresh.android.com.nesara.Util.Constants;
import demosell.amaresh.android.com.nesara.Util.Util;

/**
 * Created by RN on 11/11/2017.
 */

public class MessagelistAdapter extends BaseAdapter {
    Context context;
    List<Messagelist> mesglist;
    Holder holder;
    Holder holder1;
    String namee;
    String messagee;

    public MessagelistAdapter(Context contex, List<Messagelist> message_list) {
        this.context=contex;
        this.mesglist=message_list;

    }

    @Override
    public int getCount() {
        //return 0;
        return mesglist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder {
        private TextView name;
        private TextView title;
        private TextView msgbody;
        private RelativeLayout rel_rmsg;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Messagelist user_pos=mesglist.get(position);
        holder = new Holder();
        holder1 = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.messagelist, parent, false);

            holder.name=(TextView) convertView.findViewById(R.id.name);
            holder.title=(TextView) convertView.findViewById(R.id.title);
            holder.msgbody=(TextView) convertView.findViewById(R.id.msgbdy_r);
            holder.rel_rmsg=(RelativeLayout) convertView.findViewById(R.id.rel_rmsg);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setTag(position);
        holder.title.setTag(position);
        holder.msgbody.setTag(position);
        holder.rel_rmsg.setTag(holder);

         String status=user_pos.getIs_read();
         namee=user_pos.getName();
         messagee=user_pos.getMesage();
        holder.name.setText(user_pos.getName());
        holder.title.setText(user_pos.getTitle());
        holder.msgbody.setText(user_pos.getCreated());
        if(status.contains("0")){
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            holder.title.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            holder.msgbody.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            holder.name.setTextColor(Color.parseColor("#350F0F"));
            holder.title.setTextColor(Color.parseColor("#350F0F"));
            holder.msgbody.setTextColor(Color.parseColor("#350F0F"));
            holder.name.setTypeface(Typeface.DEFAULT_BOLD);
            holder.title.setTypeface(Typeface.DEFAULT_BOLD);
            holder.msgbody.setTypeface(Typeface.DEFAULT_BOLD);
            holder.name.setText(user_pos.getName());
            holder.title.setText(user_pos.getTitle());
            holder.msgbody.setText(user_pos.getCreated());
        }

        holder.rel_rmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.getNetworkConnectivityStatus(context)) {
                   String is_read="1";
                    sendMessageStatus asyncTask = new sendMessageStatus();
                    asyncTask.execute(user_pos.getMessage_id(),is_read);
                }else {
                    Toast.makeText(context, "You are in Offline Mode", Toast.LENGTH_LONG).show();
                }

            }
        });
        return convertView;
    }

    private class sendMessageStatus extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SyncDetails";
        ProgressDialog progress;
        int history_status;
        String server_response;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(context, "Please Wait",
                    "Loading Message List...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                String _msgid= params[0];
                String _isread = params[1];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.SEND_MESSAGE;
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
                        .appendQueryParameter("id", _msgid)
                        .appendQueryParameter("is_read", _isread);

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
                        server_response=res.getString("message");
                    }
                    else{
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
                Intent i=new Intent(context,MessageDetilActivity.class);
                i.putExtra("NAME",namee);
                i.putExtra("Message",messagee);
                context.startActivity(i);

            }
            else{

            }
            progress.dismiss();

        }
    }
}
