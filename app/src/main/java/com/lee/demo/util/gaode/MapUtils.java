package com.lee.demo.util.gaode;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.dialog.BaseDialogBean;
import cn.lee.cplibrary.util.dialog.CpBaseDialog;
import cn.lee.cplibrary.util.dialog.CpBaseDialogAdapter;
import cn.lee.cplibrary.util.dialog.CpComDialog;
import cn.lee.cplibrary.util.dialog.bottomround.CpBottomRoundDialog;


public class MapUtils {
    public static final String PN_GAODE_MAP = "com.autonavi.minimap";// 高德地图包名
    public static final String PN_BAIDU_MAP = "com.baidu.BaiduMap"; // 百度地图包名
    public static final String PN_TENCENT_MAP = "com.tencent.map"; // 腾讯地图包名
    //导航
    /**
     * 清单文件有的权限
     **/
    public final static String authBaseArr[] = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION};
    public final static String authComArr[] = {Manifest.permission.READ_PHONE_STATE};
    public final static int authBaseRequestCode = 1;
    public final static int authComRequestCode = 2;

    private static final double PI = 3.14159265358979324 * 3000.0 / 180.0;

    private static MapUtils mapUtils = new MapUtils();

    private MapUtils() {
    }

    public static MapUtils getInstance() {
        if (mapUtils == null) {
            mapUtils = new MapUtils();
        }
        return mapUtils;
    }

    public static Map<Integer, String> getMaps(Context context) {
        Map<Integer, String> maps = new HashMap<Integer, String>();
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;

                LogUtil.d("", "pn:" + pn);
                if (pn.equals(PN_BAIDU_MAP)) {
                    maps.put(0, "百度地图");
                }
                if (pn.equals(PN_GAODE_MAP)) {
                    maps.put(1, "高德地图");
                }
                if (pn.equals(PN_TENCENT_MAP)) {
                    maps.put(2, "腾讯地图");
                }
            }
        }

        return maps;
    }

    public static Map<String, Double> bdEncrypt(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;

        Map<String, Double> map = new HashMap<String, Double>();
        map.put("lon", bd_lon);
        map.put("lat", bd_lat);
        return map;
    }

    public static double bdEncryptLat(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        double bd_lat = z * Math.sin(theta) + 0.006;
        return bd_lat;
    }

    public static double bdEncryptLon(double gg_lat, double gg_lon) {
        double x = gg_lon, y = gg_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        double bd_lon = z * Math.cos(theta) + 0.0065;
        return bd_lon;
    }

    public static Map<String, Double> bdDecrypt(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        double gg_lon = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("lon", gg_lon);
        map.put("lat", gg_lat);
        return map;
    }

    public static double bdDecryptLat(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        double gg_lat = z * Math.sin(theta);
        return gg_lat;
    }

    public static double bdDecryptLon(double bd_lat, double bd_lon) {
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        double gg_lon = z * Math.cos(theta);
        return gg_lon;
    }

    /**
     * 百度导航需要的SdcardDir
     **/
    public static String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    public static boolean hasBasePhoneAuth(Context context) {
        PackageManager pm = context.getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean hasCompletePhoneAuth(Context context) {
        PackageManager pm = context.getPackageManager();
        for (String auth : authComArr) {
            if (pm.checkPermission(auth, context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }

    /**
     * bd09ll  表示百度经纬度坐标，
     * gcj02   表示经过国测局加密的坐标，
     * 百度转高德
     *
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    /**
     * 高德、腾讯转百度
     *
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }


//    /**
//     * 显示地图选择Dialog
//     */
//    public static void showMapSelectDialog(final Activity activity, final Map<String, Object> params) {
//        Map<Integer, String> maps = MapUtils.getMaps(activity);
//        final List<BaseDialogBean> list = new ArrayList<>();
//        if (maps.size() == 0) {
//            DialogUtils.show1BtnDialog(activity, "提示", "您的手机没有安装地图应用,请安装后使用导航功能", "知道了", false, new DialogUtils.DialogSureCallBack() {
//                @Override
//                public void sure() {
//
//                }
//            });
//            return;
//        }
//        for (Map.Entry<Integer, String> entry : maps.entrySet()) {
//            String bean = entry.getValue();
//            list.add(new BaseDialogBean(bean));
//        }
//        CpBottomRoundDialog.Builder.builder(activity, list)
//                .setItemHeight(54)
//                .setTxtSize(16).setTxtColor(activity.getResources().getColor(R.color.font_2d))
//                .setShowCancel(true)
//                .setCancelSize(17)
//                .setCancelTxtColor(Color.parseColor("#ff949494"))
//                .setShowTitle(false)
//                .build().showDialog(new CpBaseDialog.CpDialogBottomListCallBack() {
//            @Override
//            public void sure(CpBaseDialogAdapter adapter, View rootView, int position) {
//                String name = list.get(position).getName();
//                if (name.equals("百度地图")) {
//                    baiduNavi(activity, params);
//                } else if (name.equals("高德地图")) {
//                    amapNavi(activity, params);
//                } else if (name.equals("腾讯地图")) {
////                    tencentNavi(activity,params);
//                    tencentNavi(activity, params);
//                }
//
//            }
//
//            @Override
//            public void cancel() {
//
//            }
//        });
//    }

    /**
     * 调起百度导航:: http://lbsyun.baidu.com/index.php?title=uri/api/android
     */
    public static void baiduNavi(Activity activity, Map<String, Object> params) {
        String slat = params.get("originlat") + "";//起点
        String slon = params.get("originlon") + "";//起点
        String dlat = params.get("destinationlat") + "";//终点
        String dlon = params.get("destinationlon") + ""; //终点
        String sname = params.get("originaddress") + "";//起点
        String dname = params.get("destinationAddress") + "";//终点
        String uriString = null;
        double destination[] = {Double.valueOf(dlat), Double.valueOf(dlon)};
//        double destination[] = MapUtils.gaoDeToBaidu(Double.valueOf(dlat), Double.valueOf(dlon));
        StringBuilder builder = new StringBuilder("baidumap://map/direction?mode=driving&");
        if (!TextUtils.isEmpty(slat) && Double.valueOf(slat) != 0) {//加入起点数据
            double[] origin = {Double.valueOf(slat), Double.valueOf(slon)};
            builder.append("origin=latlng:")
                    .append(origin[0])
                    .append(",")
                    .append(origin[1])
                    .append("|name:")
                    .append(sname);
        }
        builder.append("&destination=latlng:")//终点
                .append(destination[0])
                .append(",")
                .append(destination[1])
                .append("|name:")
                .append(dname);
        builder.append("&src=andr.baidu.openAPIdemo").append("&coord_type=bd09ll");//src和coord_type必选，百度用的bd09ll坐标系,如果不设置bd09ll 会有偏差
        uriString = builder.toString();
        LogUtil.i("", MapUtils.class, "百度导航uriString=" + uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_BAIDU_MAP);
        intent.setData(Uri.parse(uriString));
        activity.startActivity(intent);
    }

    /**
     * 调起高德导航：：http://lbs.amap.com/api/amap-mobile/guide/android/route
     */
    public static void amapNavi(Activity activity, Map<String, Object> params) {
        String slat = params.get("originlat") + "";//起点
        String slon = params.get("originlon") + "";//起点
        String dlat = params.get("destinationlat") + "";//起点
        String dlon = params.get("destinationlon") + ""; //终点
        String sname = params.get("originaddress") + "";//终点
        String dname = params.get("destinationAddress") + "";//终点
        double[] origin = MapUtils.bdToGaoDe(Double.valueOf(slat), Double.valueOf(slon));
        double[] destination = MapUtils.bdToGaoDe(Double.valueOf(dlat), Double.valueOf(dlon));
        String uriString = null;
        StringBuilder builder = new StringBuilder("amapuri://route/plan?sourceApplication=maxuslife");
        if (!TextUtils.isEmpty(slat) && Double.valueOf(slat) != 0) {
            builder.append("&sname=").append(sname)
                    .append("&slat=").append(origin[1])
                    .append("&slon=").append(origin[0]);
        }
        builder.append("&dlat=").append(destination[1])
                .append("&dlon=").append(destination[0])
                .append("&dname=").append(dname)
                .append("&dev=0")// 起终点是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
                .append("&t=0");//t = 0（驾车）= 1（公交）= 2（步行）= 3（骑行）= 4（火车）= 5（长途客车） （骑行仅在V7.8.8以上版本支持）
        uriString = builder.toString();
        LogUtil.i("", MapUtils.class, "高德导航uriString=" + uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setPackage(PN_GAODE_MAP);
        intent.setData(Uri.parse(uriString));
        activity.startActivity(intent);
    }


    /**
     * 腾讯的uri在官网文档上的我是找不到的，后来被我在这里发现了：https://github.com/xujinyang/IntentMapGuide
     * 官网也只有http的文档，所以就参考：http://lbs.qq.com/uri_v1/guide-route.html
     * <p>
     * https://github.com/xujinyang/IntentMapGuide
     * 打开腾讯地图
     * params 参考http://lbs.qq.com/uri_v1/guide-route.html
     * 驾车：type=drive，policy有以下取值 0：较快捷 1：无高速 2：距离  policy的取值缺省为0
     * &from=" + dqAddress + "&fromcoord=" + dqLatitude + "," + dqLongitude + "
     * <p>
     * <p>
     * <p>
     * policy取值：本参数取决于type参数的取值
     * 公交：type=bus，policy有以下取值
     * 0：较快捷
     * 1：少换乘
     * 2：少步行
     * 3：不坐地铁
     * 驾车：type=drive，policy有以下取值
     * 0：较快捷
     * 1：无高速
     * 2：距离
     * policy的取值缺省为0
     * <p>
     * referer=您的应用名
     */
//
    public static void tencentNavi(Context context, Map<String, Object> params) {
        String slat = params.get("originlat") + "";//起点
        String slon = params.get("originlon") + "";//起点
        String dlat = params.get("destinationlat") + "";//起点
        String dlon = params.get("destinationlon") + ""; //终点
        String sname = params.get("originaddress") + "";//终点
        String dname = params.get("destinationAddress") + "";//终点
        double[] origin = MapUtils.bdToGaoDe(Double.valueOf(slat), Double.valueOf(slon));
        double[] destination = MapUtils.bdToGaoDe(Double.valueOf(dlat), Double.valueOf(dlon));
        String uriString = null;
        StringBuilder builder = new StringBuilder("qqmap://map/routeplan?type=drive&policy=0&referer=zhongshuo");// * referer=您的应用名
        if (!TextUtils.isEmpty(slat) && Double.valueOf(slat) != 0) {
            builder.append("&from=").append(sname)
                    .append("&fromcoord=").append(origin[1])
                    .append(",")
                    .append(origin[0]);
        }
        builder.append("&to=").append(dname)
                .append("&tocoord=").append(destination[1])
                .append(",")
                .append(destination[0]);
        uriString = builder.toString();
        LogUtil.i("", MapUtils.class, "腾讯导航uriString=" + uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setPackage(PN_TENCENT_MAP);
        intent.setData(Uri.parse(uriString));
        context.startActivity(intent);
    }

//    public static Map<String, Object> getMapParams(BaseApplication baseApplication, NearbyBean.DataBean bean) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("originlat", baseApplication.getLocationBean().getLatitude());
//        params.put("originlon", baseApplication.getLocationBean().getLongitude());
//        params.put("originaddress", baseApplication.getLocationBean().getAddress());
//        params.put("destinationlat", bean.getStoreLat());
//        params.put("destinationlon", bean.getStoreLon());
//        params.put("destinationAddress", bean.getStoreAddress());
//        params.put("city", baseApplication.getLocationBean().getCity());
//        return params;
//    }

    /**
     * 显示地图选择Dialog
     */
    public static void showMapSelectDialog(final Activity activity, final Map<String, Object> params) {
        Map<Integer, String> maps = MapUtils.getMaps(activity);
        final List<BaseDialogBean> list = new ArrayList<>();
        if (maps.size() == 0) {
            CpComDialog.Builder.builder(activity).
                    setTitle("提示").setContent("您的手机没有安装地图应用,请安装后使用导航功能") .setSure("知道了")
                    .setCancel(false)
                    .build().show1BtnDialog(new CpComDialog.Dialog1BtnCallBack() {
                @Override
                public void sure() {

                }
            });
            return;
        }
        for (Map.Entry<Integer, String> entry : maps.entrySet()) {
            String bean = entry.getValue();
            list.add(new BaseDialogBean(bean));
        }
        CpBottomRoundDialog.Builder.builder(activity, list)
                .setItemHeight(54)
                .setTxtSize(16).setTxtColor(Color.parseColor("#0091FF"))
                .setShowCancel(true)
                .setCancelSize(17)
                .setCancelTxtColor(Color.parseColor("#ff949494"))
                .setShowTitle(false)
                .build().showDialog(new CpBaseDialog.CpDialogBottomListCallBack() {
            @Override
            public void sure(CpBaseDialogAdapter adapter, View rootView, int position) {
                String name = list.get(position).getName();
                if (name.equals("百度地图")) {
                    baiduNavi(activity, params);
                } else if (name.equals("高德地图")) {
                    amapNavi(activity, params);
                } else if (name.equals("腾讯地图")) {
//                    tencentNavi(activity,params);
                    tencentNavi(activity, params);
                }

            }

            @Override
            public void cancel() {

            }
        });
    }

    /**
     * 判断定位服务是否开启
     *
     * @param
     * @return true 表示开启
     */
    public static boolean isLocationEnabled(Activity activity) {
        int locationMode = 0;
        String locationProviders;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(activity.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }
}
