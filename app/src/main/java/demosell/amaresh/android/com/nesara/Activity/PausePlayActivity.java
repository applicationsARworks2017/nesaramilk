package demosell.amaresh.android.com.nesara.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.nesara.amaresh.demosell.R;

import demosell.amaresh.android.com.nesara.Calander.CalendarCustomView;
import demosell.amaresh.android.com.nesara.Calander.NewCalendarCustomView;

public class PausePlayActivity extends AppCompatActivity {
    Toolbar tool_pp;
    LinearLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            NewCalendarCustomView.subscription_id = extras.getString("SUB_ID");
        }
        setContentView(R.layout.activity_pause_play);
        NewCalendarCustomView mView = (NewCalendarCustomView)findViewById(R.id.new_custom_calendar);
        tool_pp=(Toolbar)findViewById(R.id.tool_pp);
        back=(LinearLayout)tool_pp.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //mView.setvalues(subscription_id);
    }
}
