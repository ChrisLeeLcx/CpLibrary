package cn.lee.cplibrary.util.system;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.List;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.permissionutil.PerUtils;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;

/**
 * Created by ChrisLee on 2020/9/28.
 * SIM卡各种参数获取工具类（仅包含需要权限的功能）
 * telMgr.listen(PhoneStateListener listener, int events) ;
 * 解释：
 * -----------    SIM唯一    -----------
 * IMSI是国际移动用户识别码的简称(International Mobile Subscriber Identity)
 * IMSI共有15位，其结构如下：
 * MCC+MNC+MIN
 * MCC：Mobile Country Code，移动国家码，共3位，中国为460;
 * MNC:Mobile NetworkCode，移动网络码，共2位
 * 在中国，移动的代码为电00和02，联通的代码为01，电信的代码为03
 * 合起来就是（也是Android手机中APN配置文件中的代码）：
 * 中国移动：46000 46002
 * 中国联通：46001
 * 中国电信：46003
 * 举例，一个典型的IMSI号码为460030912121001
 * <p>
 * ----------------------  设备唯一   ----------------------
 * IMEI是International Mobile Equipment Identity （国际移动设备标识）的简称
 * IMEI由15位数字组成的”电子串号”，它与每台手机一一对应，而且该码是全世界唯一的
 * 其组成为：
 * 1. 前6位数(TAC)是”型号核准号码”，一般代表机型
 * 2. 接着的2位数(FAC)是”最后装配号”，一般代表产地
 * 3. 之后的6位数(SNR)是”串号”，一般代表生产顺序号
 * 4. 最后1位数(SP)通常是”0″，为检验码，目前暂备用
 * 8.0以上手机新增权限  READ_PHONE_NUMBERS 、ANSWER_PHONE_CALLS
 */

public class SIMPerUtil {
    //读取电话卡SIM权限
    //注意：https://www.zhihu.com/question/391240969
    // READ_PHONE_STATE 权限在Android 10 以上针对权限有空白通行证的手机 即使动态申请也不会弹出权限申请框，需要用户手动设置
    //READ_PHONE_NUMBERS 权限 android8.0以上的手机才有
    public static final String[] permissions0 = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS};
    public static final String[] permissions8 = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS,Manifest.permission.READ_PHONE_NUMBERS};
    public static final int REQUEST_READ_PHONE_STATE = 401;
    //SIM上的需要权限的参数
    private String line1Number = "", subscriberId = "", voiceMailAlphaTag = "", voiceMailNumber = "";
    private PermissionUtil permissionUtil;
    private boolean isHandleResult;//是否处理申请结果
    private boolean isActivity = true;//true：Activity中调用,false:fragment中调用
    private Activity activity;
    private Fragment fragment;
    private GetSIMInfoCallBack callBack;
    //android API中的TelephonyManager对象，可以取得SIM卡中的信息
    private static TelephonyManager telMgr;

    public interface GetSIMInfoCallBack {
        void onGet(SIMInfo info);//权限申请成功，获取到SIM信息

        void onFail();//权限申请失败
    }

    private SIMPerUtil() {
    }

    //activity中:在onCreate中先调用申请权限，但不处理申请结果
    public SIMPerUtil(Activity activity, GetSIMInfoCallBack callBack) {
        telMgr = (TelephonyManager) activity.getSystemService(activity.TELEPHONY_SERVICE);
        this.isActivity = true;
        this.activity = activity;
        this.callBack = callBack;
        permissionUtil = new PermissionUtil(proxy);
        //requestPer 每调取一次，就会立即执行onRequestPermissionsResult，注意此方法同时调取多次，会导致后面调用requestPer的对象在onRequestPermissionsResult空值
        requestPer(true);
    }

    //fragment中：在onCreate中先调用申请权限，但不处理申请结果
    public SIMPerUtil(Fragment fragment, GetSIMInfoCallBack callBack) {
        telMgr = (TelephonyManager) fragment.getActivity().getSystemService(activity.TELEPHONY_SERVICE);
        this.isActivity = false;
        this.fragment = fragment;
        this.callBack = callBack;
        permissionUtil = new PermissionUtil(proxy);
        requestPer(true);
    }

    /**
     * 返回获取sim卡各项信息的需要申请的权限
     * @return
     */
    private  String[] getPermissions() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0及以上
            return permissions8;
        }else{
            return permissions0;
        }
    }

    /**
     * 获取具体的SIM信息，
     *
     * @param context
     */
    public void getSIMInfo(Context context) {
        List<String> denies = PerUtils.findDeniedPermissions(activity, getPermissions());
        List<String> showDenies = PerUtils.findShowDeniedPermissions(activity, getPermissions());
        boolean showGuide = (denies.size() > 0 && showDenies.size() <= 0) ? true : false;//有未允许的权限，并且未允许的权限都被禁止
        if (showGuide) {
            ToastUtil.showToast(context, "请先允许访问手机状态权限");
        } else {
            requestPer(true);
        }
    }

    /**
     * 手机号：<br/>
     * 对于GSM网络来说即MSISDN
     *
     * @return null if it is unavailable.
     */
    private String getLine1Number() {
        return telMgr.getLine1Number();
    }

    /**
     * SIM卡的序列号即 ICCID：
     * 需要权限：READ_PHONE_STATE
     * Returns the serial number of the SIM, if applicable. Return null if it is
     * unavailable. 当用户没授权时候返回NULL
     * Android 10.0以上不适用
     */
//    public static String getSimSerialNumber1(Context context) {
//        TelephonyManager telMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
//        String simSerialNum = telMgr.getSimSerialNumber1();
//        return TextUtils.isEmpty(simSerialNum)?"":simSerialNum;
//    }

    /**
     * SIM卡的序列号即 ICCID：需要权限：READ_PHONE_STATE
     * 适配Android 10.0以上，5.1以上适配双卡,有双卡也只返回1个
     * Returns the serial number of the SIM, if applicable. Return null if it is
     * unavailable. 当用户没授权时候返回NULL
     */
    public static String getSimSerialNumber(Context context) {
        String simSerialNum = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {//5.1以上
            SubscriptionManager sm = SubscriptionManager.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();
            if (sis != null) {
                if (sis.size() >= 1) {
                    SubscriptionInfo si1 = sis.get(0);//卡槽1
                    simSerialNum = si1.getIccId();
                }
                if (TextUtils.isEmpty(simSerialNum) && sis.size() >= 2) {//卡槽1无卡 并且有2个卡槽 ，则获取第2个卡槽卡CCID
                    SubscriptionInfo si2 = sis.get(1);
                    simSerialNum = si2.getIccId();
                }
            }
        } else {  //5.1以下，
            TelephonyManager telMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            simSerialNum = telMgr.getSimSerialNumber();//Android 10.0以上不适用
        }
        return TextUtils.isEmpty(simSerialNum) ? "" : simSerialNum;
    }

    /**
     * SIM卡的序列号即 ICCID：需要权限：READ_PHONE_STATE
     * 适配Android 10.0以上，5.1以上适配双卡, 有多卡返回多个
     * Returns the serial number of the SIM, if applicable. Return null if it is
     * unavailable. 当用户没授权时候返回NULL
     */
    public static String[] getMoreSimSerialNumber(Context context) {
        String[] serialNum = new String[2];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {//5.1以上
            SubscriptionManager sm = SubscriptionManager.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return serialNum;
            }
            List<SubscriptionInfo> sis = sm.getActiveSubscriptionInfoList();
            if(sis!=null){
                if (sis.size() >= 1) {
                    SubscriptionInfo si1 = sis.get(0);//卡槽1
                    serialNum[0] = si1.getIccId();
                }
                if (sis.size() >= 2) {//卡槽2
                    SubscriptionInfo si2 = sis.get(1);
                    serialNum[1] = si2.getIccId();
                }
            }
        } else {  //5.1以下，
            TelephonyManager telMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            serialNum[0] = telMgr.getSimSerialNumber();//Android 10.0以上不适用
        }
        return serialNum;
    }

    /**
     * 唯一的用户ID：<br/>
     * 例如：IMSI(国际移动用户识别码) for a GSM phone.<br/>
     * 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    private String getSubscriberId() {
        return telMgr.getSubscriberId();
    }

    /**
     * 取得和语音邮件相关的标签，即为识别符<br/>
     * 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    private String getVoiceMailAlphaTag() {
        return telMgr.getVoiceMailAlphaTag();
    }

    /**
     * 获取语音邮件号码：<br/>
     * 需要权限：READ_PHONE_STATE
     *
     * @return
     */
    private String getVoiceMailNumber() {
        return telMgr.getVoiceMailNumber();
    }

    /**
     * 申请权限
     */
    public void requestPer(boolean isHandleResult) {
        this.isHandleResult = isHandleResult;
        if (isActivity) {
            permissionUtil.requestPermissions(activity, REQUEST_READ_PHONE_STATE, getPermissions());
        } else {
            permissionUtil.requestPermissions(fragment, REQUEST_READ_PHONE_STATE, getPermissions());
        }
    }

    /**
     * （必须调用本方法）
     * 权限申请结果处理,在Activity或者Fragment的onRequestPermissionsResult方法中的super方法上一行调用
     * eg：
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (isHandleResult) {
            if (isActivity) {
                permissionUtil.onRequestPermissionsResult(activity, requestCode, permissions, grantResults);
            } else {
                permissionUtil.onRequestPermissionsResult(fragment, requestCode, permissions, grantResults);
            }
        }

    }

    private PermissionProxy proxy = new PermissionProxy() {
        @Override
        public void granted(Object source, int requestCode) {
            String line1Number = getLine1Number();
            String subscriberId = getSubscriberId();
            String voiceMailAlphaTag = getVoiceMailAlphaTag();
            String voiceMailNumber = getVoiceMailNumber();
            if (callBack != null) {
                SIMInfo simInfo = new SIMInfo(line1Number, subscriberId, voiceMailAlphaTag, voiceMailNumber);
                simInfo.setSimSerialNumber(getSimSerialNumber(activity));
                simInfo.setSimSerialNumber1(getMoreSimSerialNumber(activity)[0]);
                simInfo.setSimSerialNumber2(getMoreSimSerialNumber(activity)[1]);
                callBack.onGet(simInfo);
            }
            String info =
                    "SIM卡的手机号:" + line1Number
                            + "\n" + "方法1获取-SIM卡的序列号1:" + getSimSerialNumber(activity)
                            + "\n" + "方法2获取-SIM卡的序列号1:" + getMoreSimSerialNumber(activity)[0]
                            + "\n" + "方法2获取-SIM卡的序列号2:" + getMoreSimSerialNumber(activity)[1]
                            + "\n" + "唯一的用户ID:" + subscriberId
                            + "\n" + "当前卡槽数量:" + subscriberId
                            + "\n" + "当前实际插卡数量:" + subscriberId
                            + "\n" + "取得和语音邮件相关的标签，即为识别符:" + voiceMailAlphaTag
                            + "\n" + "获取语音邮件号码:" + voiceMailNumber;
            LogUtil.i("", "******SIMPerUtil工具类******\n" + info);
        }

        @Override
        public void denied(Object source, int requestCode, List deniedPermissions) {
            LogUtil.i("", "denied=" + deniedPermissions);
            if (callBack != null) {
                callBack.onFail();
            }


        }

        @Override
        public void deniedNoShow(Object source, int requestCode, List noShowPermissions) {
            LogUtil.i("", "deniedNoShow=" + noShowPermissions);
            if (callBack != null) {
                callBack.onFail();
            }
        }

        @Override
        public void rationale(Object source, int requestCode) {

        }

        @Override
        public boolean needShowRationale(int requestCode) {
            return false;
        }
    };


    public static class SIMInfo {
        private String line1Number = "";
        private String simSerialNumber = "";
        private String simSerialNumber1 = "";
        private String simSerialNumber2 = "";
        private String subscriberId = "";
        private String voiceMailAlphaTag = "";
        private String voiceMailNumber = "";

        public SIMInfo() {
        }

        public SIMInfo(String line1Number, String subscriberId, String voiceMailAlphaTag, String voiceMailNumber) {
            this.line1Number = line1Number;
            this.subscriberId = subscriberId;
            this.voiceMailAlphaTag = voiceMailAlphaTag;
            this.voiceMailNumber = voiceMailNumber;
        }


        public String getLine1Number() {
            return line1Number;
        }

        public void setLine1Number(String line1Number) {
            this.line1Number = line1Number;
        }

        public String getSimSerialNumber1() {
            return simSerialNumber1;
        }

        public void setSimSerialNumber1(String simSerialNumber1) {
            this.simSerialNumber1 = simSerialNumber1;
        }

        public String getSubscriberId() {
            return subscriberId;
        }

        public void setSubscriberId(String subscriberId) {
            this.subscriberId = subscriberId;
        }

        public String getVoiceMailAlphaTag() {
            return voiceMailAlphaTag;
        }

        public void setVoiceMailAlphaTag(String voiceMailAlphaTag) {
            this.voiceMailAlphaTag = voiceMailAlphaTag;
        }

        public String getVoiceMailNumber() {
            return voiceMailNumber;
        }

        public void setVoiceMailNumber(String voiceMailNumber) {
            this.voiceMailNumber = voiceMailNumber;
        }

        public String getSimSerialNumber2() {
            return simSerialNumber2;
        }

        public void setSimSerialNumber2(String simSerialNumber2) {
            this.simSerialNumber2 = simSerialNumber2;
        }

        public String getSimSerialNumber() {
            return simSerialNumber;
        }

        public void setSimSerialNumber(String simSerialNumber) {
            this.simSerialNumber = simSerialNumber;
        }
    }
}
