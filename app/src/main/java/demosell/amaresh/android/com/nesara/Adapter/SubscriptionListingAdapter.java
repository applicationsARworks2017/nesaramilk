package demosell.amaresh.android.com.nesara.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demosell.amaresh.android.com.nesara.Activity.CalenderView;
import demosell.amaresh.android.com.nesara.Activity.Home;
import demosell.amaresh.android.com.nesara.Activity.Resumecalender;
import demosell.amaresh.android.com.nesara.Activity.Subscription_edit;
import demosell.amaresh.android.com.nesara.Pojo.SubscriptionListing;
import demosell.amaresh.android.com.nesara.Util.CheckInternet;
import demosell.amaresh.android.com.nesara.Util.Constants;

/**
 * Created by LIPL on 28/01/17.
 */
public class SubscriptionListingAdapter extends BaseAdapter {
    Context context;
    ProgressDialog progress;
    List<SubscriptionListing> subscription_lis;
    Holder holder;
    String id;
    String server_message;
    int server_status;
    Date st_date,end_Date;
    private RelativeLayout activity_my_subscription;


    public SubscriptionListingAdapter(Home mySubscription, List<SubscriptionListing> subList) {
        this.context=mySubscription;
        this.subscription_lis=subList;
    }

    public SubscriptionListingAdapter(FragmentActivity activity, List<SubscriptionListing> subList) {
        this.context=activity;
        this.subscription_lis=subList;
    }


    @Override
    public int getCount() {
        return subscription_lis.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    public static class Holder {
        private TextView Product_type;
        private TextView Delivery_type;
        private TextView Start_date;
        private TextView End_date,e_total_value;
        private LinearLayout layoutoption;
        private ImageView Im_edit,iv_resume,iv_pause,iv_stop;

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SubscriptionListing user_pos = subscription_lis.get(position);
        holder = new Holder();
        Calendar myCalendar = Calendar.getInstance();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.subscriptionlist, parent, false);

            activity_my_subscription = (RelativeLayout) convertView.findViewById(R.id.activity_my_subscription);
            holder.Product_type = (TextView) convertView.findViewById(R.id.p_type);
            holder.Delivery_type = (TextView) convertView.findViewById(R.id.d_type);
            holder.Start_date = (TextView) convertView.findViewById(R.id.s_date_value);
            holder.e_total_value = (TextView) convertView.findViewById(R.id.e_total_value);
            holder.End_date = (TextView) convertView.findViewById(R.id.e_date_value);
            holder.iv_pause = (ImageView) convertView.findViewById(R.id.iv_pause);
            holder.iv_resume = (ImageView) convertView.findViewById(R.id.iv_resume);
            holder.Im_edit = (ImageView) convertView.findViewById(R.id.iv_edit);
            holder.iv_stop = (ImageView) convertView.findViewById(R.id.iv_stop);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.Product_type.setTag(position);
        holder.Delivery_type.setTag(position);
        holder.Start_date.setTag(position);
        holder.End_date.setTag(position);
        holder.iv_pause.setTag(position);
        holder.iv_resume.setTag(position);
        holder.iv_stop.setTag(position);
        holder.Im_edit.setTag(position);
        holder.e_total_value.setTag(position);


        String ltr_qunty=user_pos.getQuantity()+" "+user_pos.getS_liter();
        holder.Product_type.setText(user_pos.getProduct_name()+" "+","+" "+ltr_qunty);
        holder.Delivery_type.setText(user_pos.getDelivery_type());
        holder.Start_date.setText(user_pos.getStart_date());
        holder.End_date.setText(user_pos.getEnd_date());
        holder.e_total_value.setText(user_pos.getTotal_day()+" Days, Rs. "+user_pos.getTotal_price());
        // check whether edit is possible or not
        String s_date = user_pos.getStart_date();
        String e_date = user_pos.getEnd_date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Date strDate = null;
        Date endDate = null;
        // long date =System.currentTimeMillis();
        try {
            strDate = sdf.parse(s_date);
            endDate = sdf.parse(e_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (System.currentTimeMillis() >= strDate.getTime()) {
            holder.Im_edit.setEnabled(false);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_mode_edit_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.light_gray));
            holder.Im_edit.setImageDrawable(drawable);
        }
        else{
            holder.Im_edit.setEnabled(true);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_mode_edit_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.black));
            holder.Im_edit.setImageDrawable(drawable);
        }

        if (System.currentTimeMillis() >= endDate.getTime()) {
            holder.iv_pause.setEnabled(false);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_pause_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.light_gray));
            holder.iv_pause.setImageDrawable(drawable);
        }
        else{
            holder.iv_pause.setEnabled(true);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_pause_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.black));
            holder.iv_pause.setImageDrawable(drawable);

        }
        if (System.currentTimeMillis() >= endDate.getTime()) {
            holder.iv_resume.setEnabled(false);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_play_arrow_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.light_gray));
            holder.iv_resume.setImageDrawable(drawable);
        }
        else{
            holder.iv_resume.setEnabled(true);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_play_arrow_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.black));
            holder.iv_resume.setImageDrawable(drawable);

        }
        if (System.currentTimeMillis() >= endDate.getTime()) {
            holder.iv_stop.setEnabled(false);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_stop_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.light_gray));
            holder.iv_stop.setImageDrawable(drawable);
        }
        else if (user_pos.getIs_stop().contains("1")){
            holder.iv_stop.setEnabled(false);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_stop_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.light_gray));
            holder.iv_stop.setImageDrawable(drawable);
        }
        else{
            holder.iv_stop.setEnabled(true);
            Resources res = context.getResources();
            Drawable drawable = res.getDrawable(R.mipmap.ic_stop_black_24dp);
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.black));
            holder.iv_stop.setImageDrawable(drawable);

        }
        holder.iv_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent=new Intent(context,CalenderView.class);
                intent.putExtra("STARTDATE",user_pos.getStart_date());
                intent.putExtra("ENDDATE",user_pos.getEnd_date());
                intent.putExtra("SUB_ID",user_pos.getId());
                context.startActivity(intent);*/
            }
        });

        holder.iv_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(context,Resumecalender.class);
                intent.putExtra("SUB_ID",user_pos.getId());
                context.startActivity(intent);*/
            }
        });

        holder.Im_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    int pos = (Integer) v.getTag();
                    String id = user_pos.getId();
                    String product_name = user_pos.getProduct_name();

                   /* Intent intent = new Intent(context, Subscription_edit.class);
                    intent.putExtra("id", id);
                    intent.putExtra("PRODUCTNAME", product_name);
                    context.startActivity(intent);*/
            }
        });

        holder.iv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure to stop?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String subscription_id=user_pos.getId();
                                StopSubscribe(subscription_id);                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        return convertView;
    }

    private void StopSubscribe(String subscriid) {
        if (CheckInternet.getNetworkConnectivityStatus(context)) {
            new getstopsubscribe().execute(subscriid);

        }else {

            showSnackbar("No Internet");
            //Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar
                .make(activity_my_subscription, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private class getstopsubscribe extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        // private ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String subscription_id=params[0];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+Constants.FOLDER +Constants.STOP_SUBSCRIBE;
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
                        .appendQueryParameter("id", subscription_id);

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
                 "message": "Records Found"
                 }
                 * */

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        server_message="Subscription Stopped";
                    }
                    else{
                        server_message="Error in data load";
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
                showSnackbar(server_message);
            }
            else{
                showSnackbar(server_message);

            }
        }
    }
}


