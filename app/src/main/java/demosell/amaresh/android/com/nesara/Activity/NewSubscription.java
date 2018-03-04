package demosell.amaresh.android.com.nesara.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.nesara.amaresh.demosell.R;

public class NewSubscription extends AppCompatActivity {
    RelativeLayout subscribe_datewise,subcribe_dayewise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subscription);
        subcribe_dayewise=(RelativeLayout)findViewById(R.id.subscribe_daywise);
        subscribe_datewise=(RelativeLayout)findViewById(R.id.subscribe_datewise);

        subcribe_dayewise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewSubscription.this,DaySubscribe.class);
                startActivity(intent);
            }
        });
        subscribe_datewise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(NewSubscription.this,DateSubcribe.class);
                startActivity(intent);
            }
        });
    }
}
