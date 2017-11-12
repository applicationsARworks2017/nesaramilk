package demosell.amaresh.android.com.nesara.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import demosell.amaresh.android.com.nesara.R;

import static demosell.amaresh.android.com.nesara.R.id.msgbody;
import static demosell.amaresh.android.com.nesara.R.id.toolbar;

/**
 * Created by RN on 11/12/2017.
 */

public class MessageDetilActivity extends BaseActivity{

    TextView tv_sendername,tv_msgbody;
    Button okay;
    String sendby,msgbody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        if (null != toolbar) {
           // toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

            toolbar.setTitle(getResources().getString(R.string.message));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavUtils.navigateUpFromSameTask(MessageDetilActivity.this);
                }
            });

        }
        tv_sendername=(TextView)findViewById(R.id.sender);
        tv_msgbody=(TextView)findViewById(R.id.send_msgbody);
        okay=(Button)findViewById(R.id.ok);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sendby = extras.getString("NAME");
            msgbody = extras.getString("Message");
        }

        tv_sendername.setText("From : "+sendby);
        tv_msgbody.setText(msgbody);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MessageDetilActivity.this,MessageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }
}
