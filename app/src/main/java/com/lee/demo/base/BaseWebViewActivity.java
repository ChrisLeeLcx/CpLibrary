package com.lee.demo.base;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.lee.demo.R;

import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.widget.webview.BaseWebView;

public class BaseWebViewActivity extends SwipeBackActivity {
    private String title;
    private String url = "https://www.baidu.com/?tn=93380420_hao_pg";
    private BaseWebView webview;
    private ProgressBar progressBar;
    public static final String KEY_TITLE = "title";
    public static final String KEY_URL = "url";
    @Override
    protected BaseActivity getSelfActivity() {
        return this;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_web_view;
    }

    @Override
    public String getPagerTitle() {
        return title;
    }

    @Override
    public String getPagerRight() {
        return null;
    }

    @Override
    public void initView() {
        webview = getSelfActivity().findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webview.setProgressBar(progressBar);
        Intent intent = getIntent();
        title = intent.getStringExtra(KEY_TITLE);
        url = ObjectUtils.isEmpty(intent.getStringExtra(KEY_URL))?url:intent.getStringExtra(KEY_URL);
    }

    @Override
    protected void initData() {
        webview.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        } else {
           finish();
            return false;
        }
    }
}
