package cn.lee.cplibrary.widget.rollviewpager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * function:
 * Created by ChrisLee on 2018/4/9.
 */

public class RollViewPagerUtil {
    /**
     * dpתpx
     *
     */
    public static int dip2px(Context ctx, float dpValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     *	pxתdp
     */
    public static int px2dip(Context ctx, float pxValue) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static String chengDate(String date){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date d = sdf1.parse(date);
            String s = sdf2.format(d);
            return s;
        } catch (ParseException e) {
            e.printStackTrace();
            return "时间转换错误";
        }
    }
    /**
     * 获取当前网络状态
     *
     * @param context 上下文
     * @return 网络连接返回true；未连接返回false
     */
    public static boolean getNetworkIsConnected(Context context) {
        // 获取网络连接管理器
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 如果管理器为null，返回false
        if (manager == null) {
            return false;
        }
        // 获取正在活动的网络信息
        NetworkInfo info = manager.getActiveNetworkInfo();
        // 如果网络信息为null，返回false
        if (info == null) {
            return false;
        }
        // 返回网络是否连接
        return info.isConnected();
    }
}
