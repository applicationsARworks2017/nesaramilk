package demosell.amaresh.android.com.nesara.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.nesara.amaresh.demosell.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import demosell.amaresh.android.com.nesara.Adapter.AllProdudtAdapter;
import demosell.amaresh.android.com.nesara.Adapter.ProdudtAdapter;
import demosell.amaresh.android.com.nesara.Pojo.AllProducts;
import demosell.amaresh.android.com.nesara.Pojo.Products;
import demosell.amaresh.android.com.nesara.Util.Constants;
import demosell.amaresh.android.com.nesara.Util.Util;

public class ManageProducts extends AppCompatActivity {
    ArrayList <AllProducts> allProducts_list;
    SwipeRefreshLayout swipe_products;
    GridView product_grid;
    AllProdudtAdapter pAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);
        swipe_products=(SwipeRefreshLayout)findViewById(R.id.swipe_products);
        product_grid=(GridView)findViewById(R.id.product_grid);
        getProducts();
        swipe_products.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_products.setRefreshing(false);
                allProducts_list=new ArrayList<>();
                getProducts();
            }
        });

    }
    private void getProducts() {
        if (Util.getNetworkConnectivityStatus(this)) {
            getProductsAsyntask asyncTask = new getProductsAsyntask();
            asyncTask.execute();
        }else {
            Toast.makeText(this, "You are in Offline Mode", Toast.LENGTH_LONG).show();
        }
    }

     /*
    *
    * GET PRODUCTS ASYNTASK
    * */

    private class getProductsAsyntask extends AsyncTask<String, Void, Void> {

        private static final String TAG = "Product List";
        ProgressDialog progress;
        int product_status;
        String product_message;

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ManageProducts.this, "Please Wait",
                    "Loading Product List...", true);

        }


        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.LIVE_URL+Constants.FOLDER+Constants.GET_PRODUCT_LIST;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("apartment_id", "");

                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

/*
                *//**//*
                *
                * "product": [
        {
            "id": "1",
            "name": "Regular Milk",
            "image": "http://nesaramilk.in/admin/files/photo/image1520182278.jpg",
            "information": "Regular Milk",
            "weight_type": "Ltr",
            "status": "Active",
            "created": "01-01-1970 12:00 AM",
            "modified": "2018-03-04 16:51:18"
        },            *
                * *//**//**/
                if (response != null && response.length() > 0) {

                    JSONObject res = new JSONObject(response);
                    product_status=res.getInt("status");
                    if(product_status==1){
                        JSONArray user_list = res.getJSONArray("product");
                        allProducts_list = new ArrayList();
                        for (int i = 0; i < user_list.length(); i++) {

                            JSONObject q_list_obj = user_list.getJSONObject(i);

                            String id = q_list_obj.getString("id");
                            String image = q_list_obj.getString("image");
                            String product_name = q_list_obj.getString("name");
                            String weight_type = q_list_obj.getString("weight_type");

                            AllProducts p_list = new AllProducts(id,image,product_name,weight_type);
                            allProducts_list.add(p_list);
                        }
                    }
                    else{    // db.addAnsweredQuestionList(new AnswerDetails(qid,uid,q_title,qdesc,q_admin_desc,q_isanswer,q_ispublish,q_fullname,q_postdate,q_created));
                        product_message="No Products Found";
                    }

                }

                return null;


            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progress.dismiss();
            if(product_status==1) {
                swipe_products.setVisibility(View.VISIBLE);
                pAdapter = new AllProdudtAdapter(ManageProducts.this, allProducts_list);
                product_grid.setAdapter(pAdapter);
            }
            else{
                swipe_products.setVisibility(View.GONE);
                Toast.makeText(ManageProducts.this,product_message,Toast.LENGTH_SHORT).show();
            }

        }
    }
}
