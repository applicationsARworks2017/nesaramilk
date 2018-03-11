package demosell.amaresh.android.com.nesara.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nesara.amaresh.demosell.R;

import demosell.amaresh.android.com.nesara.Calander.CalendarCustomView;
import demosell.amaresh.android.com.nesara.Calander.NewCalendarCustomView;

public class PausePlayActivity extends AppCompatActivity {
    public static String subscription_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_play);
        NewCalendarCustomView mView = (NewCalendarCustomView)findViewById(R.id.new_custom_calendar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subscription_id = extras.getString("SUB_ID");
        }
        mView.setvalues(subscription_id);
    }
}
