package demosell.amaresh.android.com.nesara.Calander;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nesara.amaresh.demosell.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demosell.amaresh.android.com.nesara.Activity.DaySubscribe;

/**
 * Created by Amaresh on 1/30/18.
 */

public class GridAdapter extends ArrayAdapter {
    private static final String TAG = GridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<EventObjects> allEvents;
    Context _context;
    public GridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<EventObjects> allEvents) {
        super(context, R.layout.single_cell_layout);
        this.monthlyDates = monthlyDates;
        this.currentDate = currentDate;
        this.allEvents = allEvents;
        mInflater = LayoutInflater.from(context);
        this._context=context;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Date mDate = monthlyDates.get(position);
        Calendar dateCal = Calendar.getInstance();
        dateCal.setTime(mDate);
        final int dayValue = dateCal.get(Calendar.DAY_OF_MONTH);
        final int displayMonth = dateCal.get(Calendar.MONTH) + 1;
        final int displayYear = dateCal.get(Calendar.YEAR);
        final int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        final int currentYear = currentDate.get(Calendar.YEAR);
        View view = convertView;
        if(view == null){
            view = mInflater.inflate(R.layout.single_cell_layout, parent, false);
        }
        if(displayMonth == currentMonth && displayYear == currentYear){
            view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else{
            view.setBackgroundColor(Color.parseColor("#cccccc"));
        }
        DaySubscribe.selected_dates=new ArrayList<>();
        //Add day to calendar
        TextView cellNumber = (TextView)view.findViewById(R.id.calendar_date_id);
        cellNumber.setText(String.valueOf(dayValue));
        //Add events to the calendar
        TextView eventIndicator = (TextView)view.findViewById(R.id.event_id);
        final LinearLayout lingrid = (LinearLayout) view.findViewById(R.id.lingrid);
        Calendar eventCalendar = Calendar.getInstance();
       /* for(int i = 0; i < allEvents.size(); i++){
            eventCalendar.setTime(allEvents.get(i).getDate());
            if(dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && displayYear == eventCalendar.get(Calendar.YEAR)){
                eventIndicator.setText("0.5 L");
                lingrid.setBackgroundColor(Color.parseColor("#81C784"));
            }
        }*/
        lingrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ColorDrawable buttonColor = (ColorDrawable) lingrid.getBackground();
                if(buttonColor.getColor()==Color.parseColor("#81C784")){
                    if(displayMonth == currentMonth && displayYear == currentYear){
                        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    }else{
                        view.setBackgroundColor(Color.parseColor("#cccccc"));
                    }
                    DaySubscribe.selected_dates.remove(dayValue+"-"+displayMonth+"-"+displayYear);
                    //Toast.makeText(_context, "Clicked " +dayValue+"-"+displayMonth+"-"+displayYear, Toast.LENGTH_LONG).show();
                }
                else {
                    lingrid.setBackgroundColor(Color.parseColor("#81C784"));
                    DaySubscribe.selected_dates.add(dayValue+"-"+displayMonth+"-"+displayYear);

                   // Toast.makeText(_context, "Clicked " + dayValue + displayMonth + displayYear, Toast.LENGTH_LONG).show();
                }

            }
        });
        return view;
    }
    @Override
    public int getCount() {
        return monthlyDates.size();
    }
    @Nullable
    @Override
    public Object getItem(int position) {
        return monthlyDates.get(position);
    }
    @Override
    public int getPosition(Object item) {
        return monthlyDates.indexOf(item);
    }
}
