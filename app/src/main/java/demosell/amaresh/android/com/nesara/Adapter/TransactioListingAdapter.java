package demosell.amaresh.android.com.nesara.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import demosell.amaresh.android.com.nesara.Activity.Home;
import demosell.amaresh.android.com.nesara.Activity.TransactionHistory;
import demosell.amaresh.android.com.nesara.Pojo.TransactionHistoryList;
import demosell.amaresh.android.com.nesara.R;

/**
 * Created by Rasmita on 8/5/2017.
 */

public class TransactioListingAdapter extends BaseAdapter {
    Context context;
    List<TransactionHistoryList> transactionHistoryLists;
    Holder holder;


    public TransactioListingAdapter(TransactionHistory transactionHistory, List<TransactionHistoryList> trans_list) {
        this.context=transactionHistory;
        this.transactionHistoryLists=trans_list;

    }

    public TransactioListingAdapter(FragmentActivity activity, List<TransactionHistoryList> trans_list) {
        this.context=activity;
        this.transactionHistoryLists=trans_list;
    }
    public TransactioListingAdapter(Home activity, List<TransactionHistoryList> trans_list) {
        this.context=activity;
        this.transactionHistoryLists=trans_list;
    }

    @Override
    public int getCount() {
        //return 0;
        return transactionHistoryLists.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public class Holder {
        private TextView Transaction_date;
        private TextView Transaction_type;
        private TextView Transaction_amount;
        private RelativeLayout transaction_layout;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TransactionHistoryList user_pos=transactionHistoryLists.get(position);
        holder = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.trasactionlist, parent, false);

            holder.Transaction_date=(TextView) convertView.findViewById(R.id.trans_date);
            holder.Transaction_type=(TextView) convertView.findViewById(R.id.trans_type);
            holder.Transaction_amount=(TextView) convertView.findViewById(R.id.trans_amount);
            holder.transaction_layout=(RelativeLayout) convertView.findViewById(R.id.transaction_layout);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.Transaction_date.setTag(position);
        holder.Transaction_type.setTag(position);
        holder.Transaction_amount.setTag(position);
        holder.transaction_layout.setTag(position);

        holder.Transaction_date.setText(user_pos.getT_date());
        holder.Transaction_type.setText(user_pos.getT_type());
        holder.Transaction_amount.setText(user_pos.getT_amount());

        String tr_type=user_pos.getT_type();
        if (tr_type.contentEquals("credit")){

            convertView.setBackgroundColor(Color.parseColor("#bdfbaa"));
        }
        else {
            convertView.setBackgroundColor(Color.parseColor("#ffb9b9"));

        }

        return convertView;
    }
}
