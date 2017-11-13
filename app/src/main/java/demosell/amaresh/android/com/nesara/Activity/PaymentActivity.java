package demosell.amaresh.android.com.nesara.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nesara.amaresh.demosell.R;

import demosell.amaresh.android.com.nesara.Util.Constants;


public class PaymentActivity extends BaseActivity {

    Button Paynow;
    String samount;
    String name,pnumber,email,product_name,payment_type;
    double Wallet_balance=0.0;
    TextView tvname,tvproduct,tvamount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_activity_main);
       /* if(null!=toolbar) {
            toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
            toolbar.setTitle(getResources().getString(R.string.pay_confirm));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(PaymentActivity.this);
                }
            });

        }*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            samount = extras.getString("samount");
            product_name = extras.getString("PRODUCTNAME");
            Wallet_balance = extras.getDouble("WALLETBALANCE");
            payment_type = extras.getString("PAYMENTTYPE");
            //id = extras.getInt("id");
        }
        name = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_NAME, null);
        pnumber = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_MOBILE, null);
        email = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.N_USER_EMAIL, null);
       //email="nesaramilk@gmail.com";

        tvname=(TextView)findViewById(R.id.uname);
        tvamount=(TextView)findViewById(R.id.amount);
        tvproduct=(TextView)findViewById(R.id.p_name);

        tvname.setText(name);
        tvamount.setText("Rs."+ samount);
        tvproduct.setText(product_name);
        Paynow       = (Button)findViewById(R.id.Paynow);

        Paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getApplicationContext(), PayMentGateWayPayUbiz.class);
                intent.putExtra("FIRST_NAME",name);
                intent.putExtra("PHONE_NUMBER",pnumber);
                intent.putExtra("EMAIL_ADDRESS",email);
                intent.putExtra("RECHARGE_AMT",samount);
                intent.putExtra("WALLETBALANCE",Wallet_balance);
                intent.putExtra("PAYMENTTYPE",payment_type);
                startActivity(intent);

            }
        });

    }
}
