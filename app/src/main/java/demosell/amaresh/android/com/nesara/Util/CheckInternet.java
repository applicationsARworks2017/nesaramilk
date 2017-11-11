package demosell.amaresh.android.com.nesara.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by subharam on 3/20/2017.
 */

public class CheckInternet {
    public static boolean getNetworkConnectivityStatus(Context context){

        if(context == null){
            return false;
        }

        boolean isConnected = false;;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if((info != null) && (info.isConnected())){
            isConnected = true;
        }

        return isConnected;
    }

}