package demosell.amaresh.android.com.nesara.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import demosell.amaresh.android.com.nesara.Activity.MessageDetilActivity;
import demosell.amaresh.android.com.nesara.Activity.TransactionHistory;
import demosell.amaresh.android.com.nesara.Pojo.Messagelist;
import demosell.amaresh.android.com.nesara.Pojo.TransactionHistoryList;
import demosell.amaresh.android.com.nesara.R;

/**
 * Created by RN on 11/11/2017.
 */

public class MessagelistAdapter extends BaseAdapter {
    Context context;
    List<Messagelist> mesglist;
    Holder holder;


    public MessagelistAdapter(Context contex, List<Messagelist> message_list) {
        this.context=contex;
        this.mesglist=message_list;

    }

    @Override
    public int getCount() {
        //return 0;
        return mesglist.size();
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
        private TextView name;
        private TextView title;
        private TextView msgbody;
        private RelativeLayout rel_rmsg;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Messagelist user_pos=mesglist.get(position);
        holder = new Holder();

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.messagelist, parent, false);

            holder.name=(TextView) convertView.findViewById(R.id.name);
            holder.title=(TextView) convertView.findViewById(R.id.title);
            holder.msgbody=(TextView) convertView.findViewById(R.id.msgbdy_r);
            holder.rel_rmsg=(RelativeLayout) convertView.findViewById(R.id.rel_rmsg);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.name.setTag(position);
        holder.title.setTag(position);
        holder.msgbody.setTag(position);
        holder.rel_rmsg.setTag(position);

        final String namee=user_pos.getName();
        final String messagee=user_pos.getMesage();
        holder.name.setText(user_pos.getName());
        holder.title.setText(user_pos.getTitle());
        holder.msgbody.setText(user_pos.getCreated());
        holder.rel_rmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,MessageDetilActivity.class);
                i.putExtra("NAME",namee);
                i.putExtra("Message",messagee);
                context.startActivity(i);

            }
        });
        return convertView;
    }
}
