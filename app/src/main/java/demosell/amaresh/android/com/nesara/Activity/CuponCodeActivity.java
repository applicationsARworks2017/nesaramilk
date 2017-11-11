package demosell.amaresh.android.com.nesara.Activity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Locale;

import demosell.amaresh.android.com.nesara.R;
import demosell.amaresh.android.com.nesara.Util.Constants;

public class CuponCodeActivity extends AppCompatActivity {
    public static String user_ids=null;
    ImageView selectUser;
    TextView headingUsers,tv_cuponcode;
    EditText et_discount,et_expiry;
    Button generatecode;
    RadioGroup product_radio;
    RadioButton oraganic,desi;
    ImageView sharecode,copycode,refresh;
    RelativeLayout relcupon;
    Calendar myCalendar= Calendar.getInstance();
    String ExpiryDate,promocode;
    int price_id=4;
    LinearLayout lin_cuponcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupon_code);
        headingUsers=(TextView)findViewById(R.id.headingUsers);
        tv_cuponcode=(TextView)findViewById(R.id.tv_cuponcode);
        selectUser=(ImageView)findViewById(R.id.selectUser);
        generatecode=(Button)findViewById(R.id.bt_generate);
        relcupon=(RelativeLayout) findViewById(R.id.relcupon);
        product_radio=(RadioGroup)findViewById(R.id.product_group);
        oraganic=(RadioButton)findViewById(R.id.orf_radio);
        desi=(RadioButton)findViewById(R.id.a2_radio);
        lin_cuponcode=(LinearLayout)findViewById(R.id.lin_cuponcode);
        et_discount=(EditText)findViewById(R.id.et_discountprice);
        et_expiry=(EditText)findViewById(R.id.et_expiry);
        sharecode=(ImageView)findViewById(R.id.share_code);
        copycode=(ImageView)findViewById(R.id.copy_code);
        refresh=(ImageView)findViewById(R.id.refresh);
        sharecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, promocode);
                startActivity(Intent.createChooser(share, "Title of the dialog the system will open"));
            }
        });
        copycode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied",promocode);
                clipboard.setPrimaryClip(clip);
                showSnackbar("Copied to Clipboard");
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatecode.setVisibility(View.VISIBLE);
                lin_cuponcode.setVisibility(View.GONE);
                et_expiry.setText("");
                tv_cuponcode.setText("");
                headingUsers.setText("");
                user_ids=null;
            }
        });
        product_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId== R.id.orf_radio){
                    price_id=4;
                }
                else{
                    price_id=5;
                }
            }
        });
        selectUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsActivity.tv_user_name=headingUsers;
                Intent intent=new Intent(CuponCodeActivity.this,ContactsActivity.class);
                startActivity(intent);
            }
        });
        generatecode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                generateCode();
            }
        });
        et_expiry.setOnClickListener(new View.OnClickListener() {
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(CuponCodeActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });
    }
    private void updateStartDate() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ExpiryDate = sdf.format(myCalendar.getTime());
        et_expiry.setText(sdf.format(myCalendar.getTime()));
    }
    private void showSnackbar(String s) {
        Snackbar snackbar = Snackbar
                .make(relcupon, s, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
    private void generateCode() {
        if(user_ids==null){
            showSnackbar("Select User");
        }
        else if(ExpiryDate.isEmpty()){
            showSnackbar("Select Expiry Date");
        }
        else if(et_discount.getText().toString().trim().length()<=0){
            showSnackbar("Enter Discount Price");
        }
        else {
            new setPromo().execute(user_ids,String.valueOf(price_id),et_discount.getText().toString().trim(),ExpiryDate);


        }
    }
    /**
     * Async task to set Promo
     * */
    private class setPromo extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Get User details";
        int server_status;
        String server_response;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(String... params) {

            try {

                String _userids = params[0];
                String _priceId = params[1];
                String _dis_price = params[2];
                String _expiryDate = params[3];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.LIVE_URL+ Constants.FOLDER+ Constants.CREATE_PROMO;
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
                        .appendQueryParameter("user_id", _userids)
                        .appendQueryParameter("price_id", _priceId)
                        .appendQueryParameter("value", _dis_price)
                        .appendQueryParameter("expired_on", _expiryDate);

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
/*
* {
    "promo_code": "C8HW54BR",
    "status": 1,
    "message": "Promo code inserted successfully"
}*/
                Log.i(TAG, "Response : "+response);

                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    server_status = res.optInt("status");
                    if(server_status==1) {
                        promocode=res.optString("promo_code");
                    }
                    else{
                        server_response="Promo creation Failed";
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
            if(server_status==1){
                generatecode.setVisibility(View.GONE);
                lin_cuponcode.setVisibility(View.VISIBLE);
                tv_cuponcode.setText(promocode);

            }
            else{
                showSnackbar(server_response);

            }
        }
    }

}
