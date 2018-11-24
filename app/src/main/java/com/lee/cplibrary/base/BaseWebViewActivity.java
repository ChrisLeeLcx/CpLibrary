package com.lee.cplibrary.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.lee.cplibrary.R;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_web_view);
        initView();
        webview.loadUrl(url);
    }
    public void initView() {
        webview = getSelfActivity().findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webview.setProgressBar(progressBar);
        Intent intent = getIntent();
        title = intent.getStringExtra(KEY_TITLE);
        url = ObjectUtils.isEmpty(intent.getStringExtra(KEY_URL))?url:intent.getStringExtra(KEY_URL);
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
