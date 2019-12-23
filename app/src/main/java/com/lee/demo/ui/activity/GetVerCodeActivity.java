package com.lee.demo.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.constant.Config;
import com.lee.demo.service.PhoneCodeObserver;

import java.util.List;

import cn.lee.cplibrary.util.EditTextUtil;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.util.permissionutil.PermissionProxy;
import cn.lee.cplibrary.util.permissionutil.PermissionUtil;
/**
 * 通过内容观察者 即使获取特定的短信的验证码
 * 测试过后 魅族7.0不行，华为9.0可以查看除验证码以外的短信、TCL4.4.4可以，MANN刷机定制的ROM的系统7.1可以查看所有短信包括验证码
 * 原因：各大厂商对于Android系统重新定制了
 * Created by ChrisLee on 2019/12/23.
 */

public class GetVerCodeActivity extends BaseActivity implements PermissionProxy, View.OnClickListener {
    private PermissionUtil permissionUtil;
    /*定位权限数组*/
    private String[] permissionArray = new String[]{
            Manifest.permission.READ_SMS,
    };
    EditText etPhone, etPrefix, etCode;
    Button btn;
    private PhoneCodeObserver mPhoneCode;

    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_monitor_msg;
    }

    @Override
    public String getPagerTitle() {
        return null;
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        etPhone = findViewById(R.id.et_phone);
        etPrefix = findViewById(R.id.et_prefix);
        etCode = findViewById(R.id.et_code);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_save_prefix).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn:
                startActivity(new Intent(getSelfActivity(), CheckAllMsgActivity.class));
                break;
            case R.id.btn_save:
                save();
                break;
            case R.id.btn_save_prefix:
                savePrefix();
                break;
        }
    }

    @Override
    protected void initData() {
        permissionUtil = new PermissionUtil(this);
        permissionUtil.requestPermissions(this, 2, permissionArray);
        etPhone.setText(baseApplication.getTel());
        EditTextUtil.clearEtFocus(etPhone);
        EditTextUtil.setEditTextCursorLast(etPhone);
        if (ObjectUtils.isEmpty(baseApplication.getPrefixMsg())) {
            baseApplication.setPrefixMsg("【腾讯科技】");
        }
        etPrefix.setText(baseApplication.getPrefixMsg());
    }


    private void save() {
        String num = etPhone.getText().toString().trim();
        if (ObjectUtils.isEmpty(num) || num.length() != 11) {
            toast("手机号不正确");
            return;
        }
        baseApplication.setTel(num);
        toast("保存成功");
    }

    private void savePrefix() {
        String prefix = etPrefix.getText().toString().trim();
        if (ObjectUtils.isEmpty(prefix)) {
            toast("请输入筛选短信前缀");
            return;
        }
        baseApplication.setPrefixMsg(prefix);
        toast("保存成功");
    }


    /**
     * 固定手机号码
     */
    private void fixedPhone() {
        //【腾讯科技】
        mPhoneCode = new PhoneCodeObserver(this, baseApplication.getPrefixMsg(), new Handler(),
                new PhoneCodeObserver.SmsListener() {
                    @Override
                    public void onResult(String result) {
                        toast(result);
                        etCode.setText(result);
//                        request(result);
                    }
                });
        // 注册短信变化监听  Uri.parse("content://sms/")
        this.getContentResolver().registerContentObserver(
                Uri.parse(Config.SMS_URI_ALL), true, mPhoneCode);

    }


    @Override
    public void granted(Object source, int requestCode) {
        fixedPhone();
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
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(mPhoneCode); // 解除注册
    }

//    private void request(String verCode) {
//        if (ObjectUtils.isEmpty(baseApplication.getTel())) {
//            toast("请先保存用户手机号");
//            return;
//        }
//        CpComDialog.showProgressDialog(getSelfActivity(), "");
//        UserApi.carVerCode(baseApplication.getTel(), verCode).subscribe(new BaseSubscriber<BaseResponse>(getSelfActivity()) {
//            @Override
//            protected void onSuccess(BaseResponse response) {
//                toast(response.getDescribe());
//            }
//        });
//    }
}
