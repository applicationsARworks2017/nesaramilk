package demosell.amaresh.android.com.nesara.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.nesara.amaresh.demosell.R;

import java.util.ArrayList;

import demosell.amaresh.android.com.nesara.Activity.AdminUserList;
import demosell.amaresh.android.com.nesara.Pojo.Users;

/**
 * Created by User on 20-08-2017.
 */

public class UserListingAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Users> UserList;
    Holder holder;
    public UserListingAdapter(AdminUserList adminUserList, ArrayList<Users> userList) {
        _context=adminUserList;
        UserList=userList;

    }

    @Override
    public int getCount() {
        return UserList.size();
    }

    @Override
    public Object getItem(int position) {
        return UserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView user_name,ap_name;
        ImageView iv_letter;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Users user_pos = UserList.get(position);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.userlisting, parent, false);
            holder.user_name=(TextView)convertView.findViewById(R.id.Name);
            holder.ap_name=(TextView)convertView.findViewById(R.id.ap_name);
            holder.iv_letter=(ImageView)convertView.findViewById(R.id.iv_letterView);
            convertView.setTag(holder);

        }
        else{
            holder = (Holder) convertView.getTag();

        }
        holder.user_name.setTag(position);
        holder.iv_letter.setTag(position);
        holder.ap_name.setTag(position);
        if(!user_pos.getUser_name().isEmpty()) {
            //get first letter of each String item
            holder.user_name.setText(user_pos.getUser_name()+"("+user_pos.getUser_mobile_number()+")");
            holder.ap_name.setText(user_pos.getFlat_num()+", "+user_pos.getAppartment());
            String firstLetter = user_pos.getUser_name().substring(0, 1);
            ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
            int color = generator.getColor(user_pos.getUser_name());
            //int color = generator.getRandomColor();
            TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
            holder.iv_letter.setImageDrawable(drawable);
        }
        return convertView;
    }
}
