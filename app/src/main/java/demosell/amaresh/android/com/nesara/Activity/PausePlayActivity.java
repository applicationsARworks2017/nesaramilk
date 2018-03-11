package demosell.amaresh.android.com.nesara.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nesara.amaresh.demosell.R;

import demosell.amaresh.android.com.nesara.Calander.CalendarCustomView;
import demosell.amaresh.android.com.nesara.Calander.NewCalendarCustomView;

public class PausePlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            NewCalendarCustomView.subscription_id = extras.getString("SUB_ID");
        }
        setContentView(R.layout.activity_pause_play);
        NewCalendarCustomView mView = (NewCalendarCustomView)findViewById(R.id.new_custom_calendar);


        //mView.setvalues(subscription_id);
    }
}
