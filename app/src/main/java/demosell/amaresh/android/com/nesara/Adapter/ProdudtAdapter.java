package demosell.amaresh.android.com.nesara.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nesara.amaresh.demosell.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import demosell.amaresh.android.com.nesara.Activity.Home;
import demosell.amaresh.android.com.nesara.Pojo.Products;
import demosell.amaresh.android.com.nesara.Util.Constants;

/**
 * Created by Amaresh on 3/4/18.
 */

public class ProdudtAdapter extends BaseAdapter{
    Context _context;
    ArrayList<Products> myList;
    Holder holder;

    public ProdudtAdapter(Home home, ArrayList<Products> prouct_list) {
        this._context=home;
        this.myList=prouct_list;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView p_name,tv_price;
        ImageView p_image;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Products unity_pos=myList.get(position);

        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)_context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.allproduct,parent, false);
            holder.p_name=(TextView)convertView.findViewById(R.id.tv_produt);
            holder.tv_price=(TextView)convertView.findViewById(R.id.tv_price);
            holder.p_image=(ImageView)convertView.findViewById(R.id.iv_product);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder) convertView.getTag();
        }
        holder.p_name.setTag(position);
        holder.p_image.setTag(position);
        holder.tv_price.setTag(position);

        holder.p_name.setText(unity_pos.getProduct_name().toUpperCase());
        holder.tv_price.setText("Rs. "+unity_pos.getPrice()+"  per "+unity_pos.getMin_quantity()+" "+unity_pos.getWeight_type());

        if(unity_pos.getImage()=="" || unity_pos.getImage()==null || unity_pos.getImage().isEmpty()){

        }
        else {
            Picasso.with(_context).load(Constants.PHOTO_URL+unity_pos.getImage())
                    .placeholder(R.drawable.error)
                    .error(R.drawable.error)
                    .into(holder.p_image);
        }
        return convertView;
    }
}
