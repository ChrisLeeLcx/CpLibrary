package com.lee.demo.service;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;


import com.lee.demo.constant.Config;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lee.cplibrary.util.LogUtil;

/**
 * Created by ChrisLee on 2019/12/23.
 */

public class PhoneCodeObserver extends ContentObserver {
    private Context mContext; // 上下文
    private String code; // 验证码
    SmsListener mListener;
    private String mPhone;//固定的电话
    private String prefix;//短信固定前缀
    Cursor mCursor;
    boolean hasPhoneSender;

    /**
     * 获取固定电话的电话二维码观察者
     */
    public PhoneCodeObserver(Context context, Handler handler, String phone, SmsListener listener) {
        super(handler);
        this.mContext = context;
        this.mPhone = phone;
        this.mListener = listener;
        hasPhoneSender = true;
    }

    public PhoneCodeObserver(Context context, String prefix, Handler handler, SmsListener listener) {
        super(handler);
        this.mContext = context;
        this.mListener = listener;
        this.prefix = prefix;
        hasPhoneSender = false;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange);
        LogUtil.i("", "onChange=" + uri.toString());
        // 第一次回调 不是我们想要的 直接返回
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        // 第二次回调 查询收件箱里的内容
        Uri inboxUri = Uri.parse(Config.SMS_URI_INBOX);
        // 按时间顺序排序短信数据库
//        String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };//取这些列
        String[] projection = new String[]{"address", "body"};
        if (hasPhoneSender) {//没有发送者电话，目标手机号 则不限制
            mCursor = mContext.getContentResolver().query(inboxUri, projection, "address=?", new String[]{mPhone},
                    "date desc");
        } else { //取发送者为mPhone的短息
            mCursor = mContext.getContentResolver().query(inboxUri, projection, null,
                    null, "date desc");
        }
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                String address = mCursor.getString(mCursor.getColumnIndex("address"));  // 获取手机号
                LogUtil.i("", "address=" + address);
                String body = mCursor.getString(mCursor.getColumnIndex("body"));  // 获取短信内容
                LogUtil.i("", "body=" + body);
                // 判断手机号是否为目标号码，服务号号码不固定请用正则表达式判断前几位。
                //加上这个判断必须知道发送方的电话号码，局限性比较高
                if (hasPhoneSender) {
                    if (!address.equals(mPhone)) {
                        return;
                    }
                } else {
                    if (!body.contains(prefix)) {
//                    if (!body.startsWith(prefix)) {
                        return;
                    }
                }

                // 正则表达式截取短信中的6位验证码
                String regEx =  "(?<![0-9])([0-9]{4,6})(?![0-9])" ;
//                String regEx = "(?<![0-9])([0-9]{" + 6 + "})(?![0-9])";
                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(body);
                // 如果找到通过Handler发送给主线程
                while (matcher.find()) {
                    code = matcher.group();
                    if (mListener != null) {
                        mListener.onResult(code);
                    }
                }

            }
        }
        mCursor.close();
    }

    /**
     * 短信回调接口
     */
    public interface SmsListener {
        /**
         * @param result 回调内容（验证码）
         */
        void onResult(String result);
    }
}
