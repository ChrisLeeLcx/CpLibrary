package cn.lee.cplibrary.util.system;


import android.app.Service;
import android.content.Context;
import android.telephony.TelephonyManager;

import cn.lee.cplibrary.util.LogUtil;

/**
 * Created by ChrisLee on 2020/9/28.
 * SIM卡各种参数获取工具类（仅包含不需要权限的工具类）
 */

public class SIMUtil {
    private static SIMUtil simUtil = null;
    //android API中的TelephonyManager对象，可以取得SIM卡中的信息
    private static TelephonyManager telMgr;

    private SIMUtil() {
    }
    public static SIMUtil getInstance(Context context) {
        if (simUtil == null) {
            simUtil = new SIMUtil();
        }
        if (telMgr == null) {
            telMgr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        }
        return simUtil;
    }

    /**
     * 获取信息demo
     * @return
     */
    public String getSIMInfoSimple() {
        String info =
                "SIM的状态信息:" + getSimStateInfo()
                        + "\n" + "电话状态[0 无活动/1 响铃/2 摘机]:" + getCallState()
                        + "\n" + "获取ISO标准的国家码，即国际长途区号:" + getNetworkCountryIso()
                        + "\n" + "MCC+MNC:" + getNetworkOperator()
                        + "\n" + "(当前已注册的用户)的名字:" + getNetworkOperatorName()
                        + "\n" + "当前使用的网络类型:" + getNetworkType()
                        + "\n" + "手机类型:" + getPhoneType()
                        + "\n" + "SIM卡的国家码:" + getSimCountryIso()
                        + "\n" + "获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字:" + getSimOperator()
                        + "\n" + "服务商名称:" + getSimOperatorName()
                        + "\n" + "ICC卡是否存在:" + hasIccCard()
                        + "\n" + "是否漫游:" + isNetworkRoaming()
                        + "\n" + "获取数据活动状态:" + getDataActivity()
                        + "\n" + "获取数据连接状态:" + getDataState();
        LogUtil.i("", "******SIMUtil工具类******\n" + info);
        return info;
    }

    /**
     * 电话状态：<br/>
     * CALL_STATE_IDLE 无任何状态时<br/>
     * CALL_STATE_OFFHOOK 接起电话时<br/>
     * CALL_STATE_RINGING 电话进来时
     *
     * @return
     */
    public int getCallState() {
        return telMgr.getCallState();
    }


    /**
     * 获取ISO标准的国家码，即国际长途区号。<br/>
     * 注意：仅当用户已在网络注册后有效。<br/>
     * 在CDMA网络中结果也许不可靠。<br/>
     *
     * @return
     */
    public String getNetworkCountryIso() {
        return telMgr.getNetworkCountryIso();
    }

    /**
     * MCC+MNC(mobile country code + mobile network code)<br/>
     * 注意：仅当用户已在网络注册时有效。<br/>
     * 在CDMA网络中结果也许不可靠。<br/>
     *
     * @return
     */
    public String getNetworkOperator() {
        return telMgr.getNetworkOperator();
    }

    /**
     * 按照字母次序的current registered operator(当前已注册的用户)的名字<br/>
     * 注意：仅当用户已在网络注册时有效。<br/>
     * 在CDMA网络中结果也许不可靠。
     *
     * @return
     */
    public String getNetworkOperatorName() {
        return telMgr.getNetworkOperatorName();
    }

    /**
     * 当前使用的网络类型：<br/>
     * NETWORK_TYPE_UNKNOWN 网络类型未知 0<br/>
     * NETWORK_TYPE_GPRS GPRS网络 1<br/>
     * NETWORK_TYPE_EDGE EDGE网络 2<br/>
     * NETWORK_TYPE_UMTS UMTS网络 3<br/>
     * NETWORK_TYPE_HSDPA HSDPA网络 8<br/>
     * NETWORK_TYPE_HSUPA HSUPA网络 9<br/>
     * NETWORK_TYPE_HSPA HSPA网络 10<br/>
     * NETWORK_TYPE_CDMA CDMA网络,IS95A 或 IS95B. 4<br/>
     * NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5<br/>
     * NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6<br/>
     * NETWORK_TYPE_1xRTT 1xRTT网络 7<br/>
     * 在中国，联通的3G为UMTS或HSDPA，移动和联通的2G为GPRS或EGDE，电信的2G为CDMA，电信的3G为EVDO<br/>
     *
     * @return
     */
    public int getNetworkType() {
        return telMgr.getNetworkType();
    }


    /**
     * 返回移动终端的类型：<br/>
     * PHONE_TYPE_CDMA 手机制式为CDMA，电信<br/>
     * PHONE_TYPE_GSM 手机制式为GSM，移动和联通<br/>
     * PHONE_TYPE_NONE 手机制式未知<br/>
     *
     * @return
     */
    public int getPhoneType() {
        return telMgr.getPhoneType();
    }

    /**
     * 获取ISO国家码，相当于提供SIM卡的国家码。
     *
     * @return Returns the ISO country code equivalent for the SIM provider's
     * country code.
     */
    public String getSimCountryIso() {
        return telMgr.getSimCountryIso();
    }

    /**
     * 获取SIM卡提供的移动国家码和移动网络码.5或6位的十进制数字.<br/>
     * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
     *
     * @return Returns the MCC+MNC (mobile country code + mobile network code)
     * of the provider of the SIM. 5 or 6 decimal digits.
     */
    public String getSimOperator() {
        return telMgr.getSimOperator();
    }

    /**
     * 服务商名称：<br/>
     * 例如：中国移动、联通<br/>
     * SIM卡的状态必须是 SIM_STATE_READY(使用getSimState()判断).
     *
     * @return
     */
    public String getSimOperatorName() {
        return telMgr.getSimOperatorName();
    }


    /**
     * SIM的状态信息：<br/>
     * SIM_STATE_UNKNOWN 未知状态 0<br/>
     * SIM_STATE_ABSENT 没插卡 1<br/>
     * SIM_STATE_PIN_REQUIRED 锁定状态，需要用户的PIN码解锁 2<br/>
     * SIM_STATE_PUK_REQUIRED 锁定状态，需要用户的PUK码解锁 3<br/>
     * SIM_STATE_NETWORK_LOCKED 锁定状态，需要网络的PIN码解锁 4<br/>
     * SIM_STATE_READY 就绪状态 5
     *
     * @return
     */
    public int getSimState() {
        return telMgr.getSimState();
    }

    public String getSimStateInfo() {
        String info = "";
        if (telMgr.getSimState() == telMgr.SIM_STATE_READY) {
            info = "SIM良好";
        } else if (telMgr.getSimState() == telMgr.SIM_STATE_ABSENT) {
            info = "无SIM卡";
        } else {
            info = "SIM卡被锁定或未知的状态";
        }
        return info;
    }

    public static String getSimStateInfo(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        int state = tm.getSimState();
        String info;
        switch (state) {
            case TelephonyManager.SIM_STATE_UNKNOWN ://0
                info = "未知状态";
                break;
            case TelephonyManager.SIM_STATE_ABSENT://1
                info = "无SIM卡";
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED://2
                info = "锁定状态，需要用户的PIN码解锁";
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED :// 3
                info = "锁定状态，需要用户的PUK码解锁";
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED ://4
                info = "锁定状态，需要网络的PIN码解锁";
                break;
            case TelephonyManager.SIM_STATE_READY://5
                info = "SIM良好";
                break;
            default:
                info = "SIM卡被锁定或未知的状态";
                break;
        }
        return info;
    }

    /**
     * ICC卡是否存在
     *
     * @return
     */
    public boolean hasIccCard() {
        return telMgr.hasIccCard();
    }

    /**
     * 是否漫游:(在GSM用途下)
     *
     * @return
     */
    public boolean isNetworkRoaming() {
        return telMgr.isNetworkRoaming();
    }

    /**
     * 获取数据活动状态<br/>
     * DATA_ACTIVITY_IN 数据连接状态：活动，正在接受数据<br/>
     * DATA_ACTIVITY_OUT 数据连接状态：活动，正在发送数据<br/>
     * DATA_ACTIVITY_INOUT 数据连接状态：活动，正在接受和发送数据<br/>
     * DATA_ACTIVITY_NONE 数据连接状态：活动，但无数据发送和接受<br/>
     *
     * @return
     */
    public int getDataActivity() {
        return telMgr.getDataActivity();
    }

    /**
     * 获取数据连接状态<br/>
     * DATA_CONNECTED 数据连接状态：已连接<br/>
     * DATA_CONNECTING 数据连接状态：正在连接<br/>
     * DATA_DISCONNECTED 数据连接状态：断开<br/>
     * DATA_SUSPENDED 数据连接状态：暂停<br/>
     *
     * @return
     */
    public int getDataState() {
        return telMgr.getDataState();
    }


}
