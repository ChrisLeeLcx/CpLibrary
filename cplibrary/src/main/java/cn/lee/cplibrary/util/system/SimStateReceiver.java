package cn.lee.cplibrary.util.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cn.lee.cplibrary.util.LogUtil;

//import com.fxj.fxj_electrocar.base.BaseApplication;

/**
 * SIM卡状态监听广播
 * 拔卡检测：3~5s接收到广播
 * 插卡检测：10~15s接收到广播
 */
public class SimStateReceiver extends BroadcastReceiver {
    public static final String TAG = "SimStateReceiverDebug";
    public final static String ACTION_SIM_STATE_CHANGED = "android.intent.action.SIM_STATE_CHANGED";
    public final static String ACTION_CONNECTION = "cn.jpush.android.intent.CONNECTION";
    public final static String CATEGORY_fxj = "com.fxj.fxj_electrocar";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i(TAG, "SIM卡状态广播:onReceive" + intent.getAction());
        if (intent.getAction().equals(ACTION_SIM_STATE_CHANGED)) {//检测到改变
            String iccid = SIMPerUtil.getSimSerialNumber(context);
            LogUtil.i(TAG, "SIM卡状态：" +SIMUtil.getSimStateInfo(context));
            LogUtil.i(TAG, "iccid=" +iccid);
//            BaseApplication.setIccid(iccid);//保存SIM序列号
        }
    }


}
    //-------------------------SIM监测广播，以下需要写在Application中------------------------
//   private SimStateReceiver receiver;
//    /**
//     * 注冊SIM状态监测广播:需要在登录界面注册
//     * 注意：不能在Application中调用，因为监测SIM需要权限，权限在过度页面授权，而Application先于过度页面执行，
//     *      导致未授权时候打不开页面
//     */
//    public void registerSimStateReceiver() {
//        unregisterSimStateReceiver();//取消以前注册过的，下面进行重新注册
//        setIccid(SIMPerUtil.getSimSerialNumber1(getContext()));//保存此刻的ICCID
//        receiver = new SimStateReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(SimStateReceiver.ACTION_SIM_STATE_CHANGED);
//        filter.addAction(SimStateReceiver.ACTION_CONNECTION);
//        filter.addCategory(SimStateReceiver.CATEGORY_fxj);
//        registerReceiver(receiver, filter);
//    }
//
//    /**
//     * 取消SIM状态监测广播接受者的注册：（应该在整个APP退出的时候取消注册）
//     * MainActivity页面的OnDestroy方法中调用，或者MainActivity的onKeyDown中调用
//     */
//    public void unregisterSimStateReceiver() {
//        if (receiver != null) {
//            unregisterReceiver(receiver);
//            receiver = null;
//            LogUtil.i(SimStateReceiver.TAG, "BaseApplication unregisterSimStateReceiver");
//
//        }
//    }
//    //获取SIM卡序列表
//    public static String getIccid() {
//        String iccid = SharedPreferencesUtils
//                .getShareString(context, "iccid");
//        return ObjectUtils.isEmpty(iccid)?"":iccid;
//    }
//    //保存SIM卡序列表
//    public static void setIccid(String iccid) {
//        if(!getIccid().equals(iccid)){//新的iccid卡，则存起来
//            SharedPreferencesUtils.putShareValue(context, "iccid", iccid);
//            LogUtil.i(SimStateReceiver.TAG, "BaseApplication save:iccid=" + iccid);
//        }
//    }
