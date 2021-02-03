package com.lee.demo.ui.activity.webview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.lee.demo.R;

import java.util.ArrayList;
import java.util.List;

import cn.lee.cplibrary.util.dialog.BaseDialogBean;
import cn.lee.cplibrary.util.dialog.CpBaseDialog;
import cn.lee.cplibrary.util.dialog.CpBaseDialogAdapter;
import cn.lee.cplibrary.util.dialog.bottom.CpBottomDialog;
import me.jingbin.web.ByWebTools;

public class WebViewDMActivity extends AppCompatActivity implements View.OnClickListener {
    private AutoCompleteTextView etSearch;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_dm);
        etSearch = findViewById(R.id.et_search);
        findViewById(R.id.bt_openUrl).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        jum2Web();
    }


    private int state = 0;

    /**
     * WebView的使用
     */
    private void jum2Web() {
        final List<BaseDialogBean> list1 = new ArrayList<>();
        list1.add(new BaseDialogBean("基本使用：加载页面"));
        list1.add(new BaseDialogBean("基本使用：与js交互"));
        list1.add(new BaseDialogBean("高级使用：打开页面输入框内页面"));
        list1.add(new BaseDialogBean("高级使用：百度一下"));
        list1.add(new BaseDialogBean("高级使用：网络视频"));
        list1.add(new BaseDialogBean("高级使用：上传图片"));//5
        list1.add(new BaseDialogBean("高级使用：打电话、发短信、发邮件、JS"));
        list1.add(new BaseDialogBean("高级使用：js与android原生代码互调"));
        list1.add(new BaseDialogBean("高级使用： DeepLink通过网页跳入App"));
        CpBottomDialog.Builder.builder(activity, list1)
                .setTopRound(true)
                .setTitle("网页类型")
                .setCancelTxt("取消")
                .build().showDialog(new CpBaseDialog.CpDialogBottomListCallBack() {
            @Override
            public void sure(CpBaseDialogAdapter adapter, View rootView, int position) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(activity, BaseWebViewActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(activity, WebViewJSDemo1Activity.class));
                        break;
                    case 2:
                        openUrl();
                        break;
                    case 3:// 百度一下
                        state = 0;
                        String baiDuUrl = "http://www.baidu.com";
                        loadUrl(baiDuUrl, "百度一下");
                        break;
                    case 4:// 网络视频
                        state = 0;
                        String movieUrl = "https://sv.baidu.com/videoui/page/videoland?context=%7B%22nid%22%3A%22sv_5861863042579737844%22%7D&pd=feedtab_h5";
                        loadUrl(movieUrl, "视频播放");
                        break;
                    case 5:// 上传图片
                        state = 0;
                        String uploadUrl = "file:///android_asset/upload_photo.html";
                        loadUrl(uploadUrl, "上传图片测试");
                        break;
                    case 6:// 打电话、发短信、发邮件、JS
                        state = 1;
                        String callUrl = "file:///android_asset/callsms.html";
                        loadUrl(callUrl, "电话、短信、邮件、注入js");
                        break;
                    case 7://  js与android原生代码互调
                        state = 2;
                        String javaJs = "file:///android_asset/java_js.html";
                        loadUrl(javaJs, "js与android原生代码互调");
                        break;
                    case 8:// DeepLink通过网页跳入App
                        state = 0;
                        String deepLinkUrl = "file:///android_asset/deeplink.html";
                        loadUrl(deepLinkUrl, "DeepLink 测试");
                    default:
                        break;
                }
            }

            @Override
            public void cancel() {

            }
        });
    }

    private void loadUrl(String mUrl, String mTitle) {
        ByWebViewActivity.loadUrl(this, mUrl, mTitle, state);
    }

    /**
     * deeplink.html
     * 打开网页
     */
    private void openUrl() {
        state = 0;
//        String url = ByWebTools.getUrl(etSearch.getText().toString().trim());
        String url = etSearch.getText().toString().trim();
        loadUrl(!TextUtils.isEmpty(url) ? url : "https://github.com/youlookwhat/WebViewStudy", "详情");
    }
}
