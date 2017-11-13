package demosell.amaresh.android.com.nesara.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nesara.amaresh.demosell.R;

import java.util.ArrayList;

import demosell.amaresh.android.com.nesara.Activity.AddApartments;
import demosell.amaresh.android.com.nesara.Pojo.Apartments;

/**
 * Created by Amaresh on 9/22/17.
 */

public class ApartmentListAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Apartments> AptmentList;
    Holder holder;
    public ApartmentListAdapter(AddApartments addApartments, ArrayList<Apartments> apartmentlist) {
        this._context=addApartments;
        AptmentList=apartmentlist;

    }

    @Override
    public int getCount() {
        return AptmentList.size();
    }

    @Override
    public Object getItem(int i) {
        return AptmentList.get(i);
    }
    public class Holder{
        TextView ap_name;

    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        final Apartments _pos = AptmentList.get(i);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.apartmentlisting, parent, false);
            holder.ap_name=(TextView)convertView.findViewById(R.id.apt_name);
            convertView.setTag(holder);

        }
        else{
            holder = (Holder) convertView.getTag();

        }

        holder.ap_name.setTag(i);
        holder.ap_name.setText(_pos.getApartment_name());
        return convertView;
    }
}
