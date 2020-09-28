package com.lee.demo.ui.activity;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.SwipeBackActivity;

import cn.lee.cplibrary.util.DeviceIdUtil;
import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.SIMPerUtil;
import cn.lee.cplibrary.util.SIMUtil;

public class OtherActivity1 extends SwipeBackActivity {

    private TextView  tvDeviceUniqueId, tvSim, tvSim1;
    private SIMPerUtil simPerUtil;
    private  SIMPerUtil.SIMInfo simInfo;
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_other1;
    }

    @Override
    public String getPagerTitle() {
        return "其他1";
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        findViews();
    }

    @Override
    protected void initData() {
        //获取设备唯一标识
        String deviceUniqueId = DeviceIdUtil.getDeviceInfoSimple(getSelfActivity());
        tvDeviceUniqueId.setText("6、" + deviceUniqueId);
        LogUtil.i("", deviceUniqueId);
        //获取SIM信息（不需要权限的）
        tvSim.setText("7、" + SIMUtil.getInstance(getSelfActivity()).getSIMInfoSimple());
        //获取SIM信息（需要权限的）
        simPerUtil = new SIMPerUtil(getSelfActivity(), new SIMPerUtil.GetSIMInfoCallBack() {
            @Override
            public void onGet(SIMPerUtil.SIMInfo info) {
                simInfo= info;
                String in =
                        "SIM卡的手机号:" + info.getLine1Number()
                                + "\n" + "SIM卡的序列号:" + info.getSimSerialNumber()
                                + "\n" + "唯一的用户ID:" + info.getSubscriberId()
                                + "\n" + "取得和语音邮件相关的标签，即为识别符:" + info.getVoiceMailAlphaTag()
                                + "\n" + "获取语音邮件号码:" + info.getVoiceMailNumber();
                tvSim1.setText("8、" + in);
            }

            @Override
            public void onFail() {

            }
        });
        tvSim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(simInfo==null){
                    toast("未获取到SIM信息");
                    simPerUtil.getSIMInfo(getSelfActivity());
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        simPerUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //查找控件
    private void findViews() {
        tvDeviceUniqueId = (TextView) findViewById(R.id.tv_device_unique_id);
        tvSim = (TextView) findViewById(R.id.tv_sim);
        tvSim1 = (TextView) findViewById(R.id.tv_sim1);
    }


}
