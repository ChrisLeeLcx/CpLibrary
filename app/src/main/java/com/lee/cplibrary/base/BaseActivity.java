package com.lee.cplibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import cn.lee.cplibrary.util.ToastUtil;

/**
 * @author: ChrisLee
 * @time: 2018/11/23
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract BaseActivity getSelfActivity();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getSelfActivity());
    }

    public void toast(String msg) {
        ToastUtil.showToast(getSelfActivity(), msg);
    }
}
