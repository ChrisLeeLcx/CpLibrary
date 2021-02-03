package com.lee.demo.ui.activity.webview;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.SwipeBackActivity;

import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.widget.webview.BaseWebView;

/**
 * WebView 展示
 */
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
        url = ObjectUtils.isEmpty(intent.getStringExtra(KEY_URL)) ? url : intent.getStringExtra(KEY_URL);
    }

    @Override
    protected void initData() {
        webview.loadUrl(url);//方式1：加载url

        /**
         //方式2:加载一段html代码
         String detailHtml = "<!DOCTYPE html>\n" +
         "<html lang=\"zh-cn\">\n" +
         "\t<head>\n" +
         "\t\t<meta charset=\"UTF-8\">\n" +
         "\t\t<title>标题</title>\n" +
         "\t</head>\n" +
         "\t<body>\n" +
         "\t<p style=\"font-size: 60px;margin: 0 auto;color: #FF5722;\">我是一段html代码</p>\n" +
         "\t</body>\n" +
         "</html>";
         webview.loadData(detailHtml, "text/html", "UTF-8");
         */
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
