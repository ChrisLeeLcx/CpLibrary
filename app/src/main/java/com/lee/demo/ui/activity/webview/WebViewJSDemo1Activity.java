package com.lee.demo.ui.activity.webview;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lee.demo.R;
import com.lee.demo.base.BaseActivity;
import com.lee.demo.base.SwipeBackActivity;

import java.util.HashMap;
import java.util.Set;

import cn.lee.cplibrary.util.LogUtil;
import cn.lee.cplibrary.util.ObjectUtils;
import cn.lee.cplibrary.widget.webview.BaseWebView;

/**
 * WebView与JS通信Demo
 * <p>
 * 一、对于Android调用JS代码的方法有2种：
 * 1、 webview.loadUrl("javascript:callJS()")
 * 2、通过WebView的evaluateJavascript（）
 * <p>
 * 二、对于JS调用Android代码的方法有3种：
 * 1、通过WebView的addJavascriptInterface（）进行对象映射
 * 2、通过 WebViewClient 的shouldOverrideUrlLoading ()方法回调拦截 url
 * 3、通过 WebChromeClient 的onJsAlert()、onJsConfirm()、onJsPrompt（）方法回调拦截JS对话框alert()、confirm()、prompt（） 消息
 */
public class WebViewJSDemo1Activity extends SwipeBackActivity {
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
        return R.layout.activity_web_view_js1;
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
//        webview.loadUrl(url);
        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        webview.loadUrl("file:///android_asset/javascript1.html");
        callJS();
        callAndroid1();
        callAndroid2();
//        callAndroid3();

    }

    private void callAndroid3() {
        webview.setWebChromeClient(new WebChromeClient() {
                                       // 拦截输入框(原理同方式2)
                                       // 参数message:代表promt（）的内容（不是url）
                                       // 参数result:代表输入框的返回值
                                       @Override
                                       public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                                           // 根据协议的参数，判断是否是所需要的url(原理同方式2)
                                           // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                                           //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                                           Uri uri = Uri.parse(message);
                                           // 如果url的协议 = 预先约定的 js 协议
                                           // 就解析往下解析参数
                                           if (uri.getScheme().equals("js")) {

                                               // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                                               // 所以拦截url,下面JS开始调用Android需要的方法
                                               if (uri.getAuthority().equals("webview")) {

                                                   //
                                                   // 执行JS所需要调用的逻辑
                                                   toast("js调用了Android的方法");
                                                   // 可以在协议上带有参数并传递到Android上
                                                   HashMap<String, String> params = new HashMap<>();
                                                   Set<String> collection = uri.getQueryParameterNames();

                                                   //参数result:代表消息框的返回值(输入值)
                                                   result.confirm("js调用了Android的方法成功啦");
                                               }
                                               return true;
                                           }
                                           return super.onJsPrompt(view, url, message, defaultValue, result);
                                       }

                                                // 通过alert()和confirm()拦截的原理相同，此处不作过多讲述

                                       // 拦截JS的警告框
                                       @Override
                                       public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                                           return super.onJsAlert(view, url, message, result);
                                       }

                                       // 拦截JS的确认框
                                       @Override
                                       public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                                           return super.onJsConfirm(view, url, message, result);
                                       }
                                   }
        );


    }

    private void callAndroid2() {

// 复写WebViewClient类的shouldOverrideUrlLoading方法
        webview.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {

                                         // 步骤2：根据协议的参数，判断是否是所需要的url
                                         // 一般根据scheme（协议格式） & authority（协议名）判断（前两个参数）
                                         //假定传入进来的 url = "js://webview?arg1=111&arg2=222"（同时也是约定好的需要拦截的）

                                         Uri uri = Uri.parse(url);
                                         // 如果url的协议 = 预先约定的 js 协议
                                         // 就解析往下解析参数
                                         if (uri.getScheme().equals("js")) {

                                             // 如果 authority  = 预先约定协议里的 webview，即代表都符合约定的协议
                                             // 所以拦截url,下面JS开始调用Android需要的方法
                                             if (uri.getAuthority().equals("webview")) {

                                                 //  步骤3：
                                                 // 执行JS所需要调用的逻辑
                                                 toast("js调用了Android的方法2");
//                                                  System.out.println("js调用了Android的方法");
                                                 // 可以在协议上带有参数并传递到Android上
                                                 HashMap<String, String> params = new HashMap<>();
                                                 Set<String> collection = uri.getQueryParameterNames();

                                             }

                                             return true;
                                         }
                                         return super.shouldOverrideUrlLoading(view, url);
                                     }
                                 }
        );
    }


    //优点：使用简单 仅将Android对象和JS对象映射即可
//    存在严重的漏洞问题， https://www.jianshu.com/p/3a345d27cd42
    private void callAndroid1() {
        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        webview.addJavascriptInterface(new AndroidToJs(), "test");//AndroidToJS类对象映射到js的test对象
    }

    //Android调用js方法
    private void callJS() {
        //特别注意：JS代码调用一定要在 onPageFinished（） 回调之后才能调用，否则不会调用。
        //onPageFinished()属于WebViewClient类的方法，主要在页面加载结束时调用 所以通过点击按钮调用
        findViewById(R.id.btn_click).setOnClickListener(new View.OnClickListener() {
            // 注意调用的JS方法名要对应上,调用javascript的callJS()方法
            @Override
            public void onClick(View v) {
                //方式二：比第一种方法效率更高、使用更简洁。Android 4.4以上，有返回结果
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webview.evaluateJavascript("javascript:callJS('我是安卓入参')", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                            LogUtil.i("", "value=" + value);
                        }
                    });
                } else {//方式一：通用 效率低 会刷新页面
                    webview.loadUrl("javascript:callJS('native数据1')");
                }
               
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //继承自Object类：给js调用
    public class AndroidToJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void hello(String msg) {
            toast(msg);
        }


    }
}
