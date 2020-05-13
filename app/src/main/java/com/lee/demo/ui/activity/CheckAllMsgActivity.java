package com.lee.demo.ui.activity;

import android.Manifest;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lee.demo.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;

/**
 * 查看所有短信
 * 短信监听功能:测试过后 魅族7.0不行，华为9.0可以查看除验证码以外的短信、TCL4.4.4可以，MANN刷机定制的ROM的系统7.1可以查看所有短信包括验证码
 * Created by ChrisLee on 2019/12/19.
 */

public class CheckAllMsgActivity extends AppCompatActivity implements PermissionProxy {
//    private String phone = "10659865";
//    private String phone = "106593111";
//    private String phone = "10693407103008994652";
    private String phone = "1069127938905474652";

    private PermissionUtil permissionUtil;

    /*定位权限数组*/
    private String[] permissionArray = new String[]{
            Manifest.permission.READ_SMS,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_monitor_msg);
        permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(this, 2, permissionArray);

    }

    public String getSmsInPhone() {
        final String SMS_URI_ALL = "content://sms/";
        final String SMS_URI_INBOX = "content://sms/inbox";
        final String SMS_URI_SEND = "content://sms/sent";
        final String SMS_URI_DRAFT = "content://sms/draft";
        final String SMS_URI_OUTBOX = "content://sms/outbox";
        final String SMS_URI_FAILED = "content://sms/failed";
        final String SMS_URI_QUEUED = "content://sms/queued";

        StringBuilder smsBuilder = new StringBuilder();
        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
            Cursor cur = getContentResolver().query(uri, projection, null, null, "date desc");		// 获取手机内部短信
//            Cursor cur = getContentResolver().query(uri, projection, "address=?",  new String[]{phone}, "date desc");		// 获取手机内部短信
//            Cursor cur = getContentResolver().query(uri, projection, "address=?",  new String[]{phone}, "date desc");		// 获取手机内部短信

            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else {
                        strType = "null";
                    }

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");

//                Pattern p = Pattern.compile("(?<![0-9])([0-9]{4,6" + "})(?![0-9])");
////                Pattern p = Pattern.compile("(?<![0-9])([0-9]{" + 6+ "})(?![0-9])");
//                Matcher m = p.matcher(strbody);
//                if (m.find()) {
////                    System.out.println(m.group());
////                    return m.group(0);
//
//                    String code = m.group();
//                    // 显示到界面上
////                    tv.setText(code);
//                                    smsBuilder.append(code + ", ");
//
//                }

                smsBuilder.append(strbody + ", ");


                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");
                }while (cur.moveToNext());


                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

            smsBuilder.append("getSmsInPhone has executed!");

        } catch (SQLiteException ex) {

            Log.d("debuginfo", ex.getMessage());
//            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }

        return smsBuilder.toString();
    }

    @Override
    public void granted(Object source, int requestCode) {
        TextView tv = new TextView(this);
        tv.setText(getSmsInPhone());

        ScrollView sv = new ScrollView(this);
        sv.addView(tv);
        setContentView(sv);
    }

    @Override
    public void deniedNoShow(Object source, int requestCode, List noShowPermissions) {

    }

    @Override
    public void rationale(Object source, int requestCode) {

    }

    @Override
    public boolean needShowRationale(int requestCode) {
        return false;
    }

    @Override
    public void denied(Object source, int requestCode, List deniedPermissions) {

    }
}