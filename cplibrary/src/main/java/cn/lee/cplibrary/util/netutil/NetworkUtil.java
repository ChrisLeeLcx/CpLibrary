package cn.lee.cplibrary.util.netutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * function:
 * Created by ChrisLee on 2018/5/10.
 */

public class NetworkUtil {
    /**
     * 是否有网络
     * @param context
     * @return
     */
    public static boolean hasNetwork(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (null == activeNetInfo || !activeNetInfo.isConnected()) {
            return false;
        }
        return true;
    }

}
