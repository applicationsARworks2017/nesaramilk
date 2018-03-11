package demosell.amaresh.android.com.nesara.Calander;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nesara.amaresh.demosell.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import demosell.amaresh.android.com.nesara.Activity.DaySubscribe;

/**
 * Created by Amaresh on 1/30/18.
 */

public class PPGridAdapter extends ArrayAdapter {
    private static final String TAG = PPGridAdapter.class.getSimpleName();
    private LayoutInflater mInflater;
    private List<Date> monthlyDates;
    private Calendar currentDate;
    private List<DetailsPP> allEvents;
    Context _context;
    public PPGridAdapter(Context context, List<Date> monthlyDates, Calendar currentDate, List<DetailsPP> allEvents) {
        super(context, R.layout.ppsingle_cell_layout);
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
            view = mInflater.inflate(R.layout.ppsingle_cell_layout, parent, false);
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
        final TextView eventIndicator = (TextView)view.findViewById(R.id.event_id);
        final TextView _id = (TextView)view.findViewById(R.id._id);
        final LinearLayout lingrid = (LinearLayout) view.findViewById(R.id.lingrid);
        lingrid.setTag(position);

        Calendar eventCalendar = Calendar.getInstance();
        for(int i = 0; i < allEvents.size(); i++){
            eventCalendar.setTime(allEvents.get(i).getDeliverydate());
            if(dayValue == eventCalendar.get(Calendar.DAY_OF_MONTH) && displayMonth == eventCalendar.get(Calendar.MONTH) + 1
                    && displayYear == eventCalendar.get(Calendar.YEAR)){
                eventIndicator.setText(allEvents.get(i).getQuentity());
                // for getting the arraylist id
                _id.setText(allEvents.get(i).getId());
                lingrid.setBackgroundColor(Color.parseColor("#81C784"));
            }
        }

        lingrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Access the row position here to get the correct data item
                final Dialog dialog = new Dialog(_context);
                dialog.setContentView(R.layout.chooseselection);
                TextView date_details=(TextView)dialog.findViewById(R.id.date_details);
                //date_details.setText(dayValue+"-"+displayMonth+"-"+displayYear+monthlyDates.get(position));
                DetailsPP detailsPP=new DetailsPP();
                String price=detailsPP.getPrice();
                //date_details.setText(_id.getText().toString());
                date_details.setText(price);

                TextView lin_ok=(TextView)dialog.findViewById(R.id.lin_ok);
                TextView lin_cancel=(TextView)dialog.findViewById(R.id.lin_cancel);
                lin_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });
                dialog.show();

                /*ColorDrawable buttonColor = (ColorDrawable) lingrid.getBackground();
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
                }*/

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