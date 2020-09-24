package cn.lee.cplibrary.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 获取设备唯一标识工具类
 *
 * 一、注意事项：getDeviceUniqueID获取的设备唯一标识有以下问题
 * （1）唯一标识DeviceUniqueID由：ANDROID_ID + Serial + 硬件uuid（即UniquePsuedoID）组合后ND5编码得到
 * （2）若3个硬件标识数据均未能获得，用 返回手机型号+"-111" 提示用户，获取不了情况是极少数极少数
 * （3）若要保证DeviceUniqueID唯一则需要：手机不被root、不恢复出厂设置
 * <p>
 * 二、以下参数不适用组合唯一标识的原因
 * 1、Mac地址：需权限，会重复，会改变，手机必须有上网功能
 * 2、Installtion ID：不同APP、同一个程序重新安装均会产生不同的ID
 * 3、IMEI:一个手机卡对应一个，需要权限READ_PHONE_STATE，因为双卡和全网通问题会改变
 * 4、UUID：重新安装APP会改变
 * <p>
 * 三、用于组合唯一标识的参数
 * 5、UniquePsuedoID(可用于组合)：硬件信息拼装 如果是同一批次出厂的的设备有可能出现生成的内容可能是一样的
 * 6、Android ID(可用于组合)：有时为null。出厂设置，手机被Root会被改变
 * 7、serial(可用于组合) 如：LKX7N18328000931,无需权限,极个别设备获取不到数据；在有些手机上会出现垃圾数据，比如红米手机返回的就是连续的非随机数
 * Created by ChrisLee on 2020/9/24.
 */

public class DeviceIdUtil {

    public static String getDeviceInfoSimple(Context context) {
        return "手机品牌：" + getDeviceBrand()
                + "\n" + "手机型号：" + getSystemModel().replace(" ", "")
                + "\n" + "唯一标识UniqueID：" + getDeviceUniqueID(context)
                + "\n" + "ANDROID_ID：" + getANDROID_ID(context)
                + "\n" + "SerialNumber：" + getSerialNumber()
                + "\n" + "UniquePsuedoID：" + getUniquePsuedoID()
                + "\n" + "randomUUID：" + UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取设备唯一标识（组合标识）
     *
     * @param context
     * @return
     */
    public static String getDeviceUniqueID(Context context) {
        //获得硬件uuid（根据硬件相关属性，生成uuid）（无需权限）
        String uuid = getUniquePsuedoID().replace("-", "");
        String onlyId = getANDROID_ID(context) + getSerialNumber() + uuid;
        //不空
        if (!TextUtils.isEmpty(onlyId) && onlyId.length() > 0) {
            return getMD5Str(onlyId);
        }
        //如果以上硬件标识数据均无法获得，返回字符串：手机型号+"-1"，来提示后台
        return getSystemModel().replace(" ", "")+"-111";
    }

    public static String getANDROID_ID(Context context) {
        String ANDROID_ID = "";
        try {
            ANDROID_ID = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
//            ANDROID_ID= Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);//方式2
        } catch (Exception e) {
            return "";
        }

        return TextUtils.isEmpty(ANDROID_ID) ? "" : ANDROID_ID;
    }


    public static String getSerialNumber() {
        String SerialNumber = "";
        try {
            SerialNumber = Build.SERIAL;
        } catch (Exception e) {
            return "";
        }
        return TextUtils.isEmpty(SerialNumber) ? "" : SerialNumber;
    }

    /**
     * 获取硬件UUID
     * 最终得到的串：类似00000000-2e7a-cdf8-ffff-ffffa7520891
     *
     * @return
     */
    public static String getUniquePsuedoID() {
        try {
            //获取方式1:7个硬件参数组合
            String m_szDevIDShort = "35"

                    + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10)

                    + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10)

                    + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10)

                    + (Build.PRODUCT.length() % 10);

            //另一种获取方式：13个硬件参数组合
//            String m_szDevIDShort = "35" +
//
//                    Build.BOARD.length()%10 + Build.BRAND.length()%10 +
//
//                    Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 +
//
//                    Build.DISPLAY.length()%10 + Build.HOST.length()%10 +
//
//                    Build.ID.length()%10 + Build.MANUFACTURER.length()%10 +
//
//                    Build.MODEL.length()%10 + Build.PRODUCT.length()%10 +
//
//                    Build.TAGS.length()%10 + Build.TYPE.length()%10 +
//
//                    Build.USER.length()%10 ; //13 位


            String serial = null;
            try {
                serial = Build.class.getField("SERIAL").get(null).toString();
                //API>=9 使用serial号
                return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            } catch (Exception e) {
                //serial需要一个初始化
                serial = "serial";//serial需要一个初始化
            }

            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception e) {
            return "";
        }
    }


    private static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
//            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }


    public static String getDeviceBrand() {
        String brand = Build.BRAND;
        return brand;
    }

    public static String getSystemModel() {
        String model = Build.MODEL;
        return model;
    }


}
