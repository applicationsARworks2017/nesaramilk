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

import demosell.amaresh.android.com.nesara.Activity.ReportActivity;
import demosell.amaresh.android.com.nesara.Pojo.ReportDetails;

/**
 * Created by Amaresh on 10/18/17.
 */

public class ReportDetailsAdapter extends BaseAdapter {
    Context _context;
    ArrayList<ReportDetails> mlist;
    Holder holder;
    public ReportDetailsAdapter(ReportActivity reportActivity, ArrayList<ReportDetails> deliveryDetailsArrayList) {
        this._context=reportActivity;
        this.mlist=deliveryDetailsArrayList;

    }

    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView tv_apartment,tv_flat,tv_product,tv_quantity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ReportDetails _pos = mlist.get(position);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.deliverlist, parent, false);
            holder.tv_apartment=(TextView)convertView.findViewById(R.id.apartmentname);
            holder.tv_flat=(TextView)convertView.findViewById(R.id.flatname);
            holder.tv_product=(TextView)convertView.findViewById(R.id.productname);
            holder.tv_quantity=(TextView)convertView.findViewById(R.id.quanity);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();

        }

        holder.tv_apartment.setTag(position);
        holder.tv_flat.setTag(position);
        holder.tv_product.setTag(position);
        holder.tv_quantity.setTag(position);

        String ltr_qunty=_pos.getS_quantity()+" "+_pos.getS_liter();
        holder.tv_quantity.setText(ltr_qunty);
        holder.tv_apartment.setText(_pos.getAppartment_name());
        holder.tv_flat.setText(_pos.getFlat_no());
        holder.tv_product.setText(_pos.getProduct_name());
        return convertView;
    }
}
