package demosell.amaresh.android.com.nesara.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import demosell.amaresh.android.com.nesara.R;
import demosell.amaresh.android.com.nesara.Util.Constants;

/**
 * Created by Rasmita on 7/27/2017.
 */

public class AdminActivity extends AppCompatActivity {
    LinearLayout cutomerList,product_price,subscriptionList,send_message,
                    generate_cupon,profile_page,generate_report,add_ppartments;
    Button admin_logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        cutomerList=(LinearLayout)findViewById(R.id.cutomers);
        product_price=(LinearLayout)findViewById(R.id.productprice);
        send_message=(LinearLayout)findViewById(R.id.sendMessage);
        generate_cupon=(LinearLayout)findViewById(R.id.cuponcode);
        add_ppartments=(LinearLayout)findViewById(R.id.add_ppartments);
        generate_report=(LinearLayout)findViewById(R.id.report);
        admin_logout=(Button)findViewById(R.id.admin_logout);
        admin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });



        cutomerList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,AdminUserList.class);
                startActivity(intent);
            }
        });
        product_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,SetProductPrice.class);
                startActivity(intent);
            }
        });
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,MessageActivity.class);
                startActivity(intent);
            }
        });
        generate_cupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,CuponCodeActivity.class);
                startActivity(intent);
            }
        });
        add_ppartments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,AddApartments.class);
                intent.putExtra("PAGE","admin");
                startActivity(intent);
            }
        });
        generate_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AdminActivity.this,ReportActivity.class);
                startActivity(intent);
            }
        });

    }

    private void logout() {
        SharedPreferences sharedPreferences = AdminActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        Intent intent=new Intent(AdminActivity.this,EnterPhone.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}