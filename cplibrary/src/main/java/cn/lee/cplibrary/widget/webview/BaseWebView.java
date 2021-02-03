package cn.lee.cplibrary.widget.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.lee.cplibrary.R;
import cn.lee.cplibrary.util.ToastUtil;
import cn.lee.cplibrary.util.netutil.NetworkUtil;

/**
 * 增加了webview加载错误时候的布局显示
 */

public class BaseWebView extends WebView {
    private static final String TAG = "debuginfo";
    private Activity mActivity;
    private ProgressBar progressBar;
    private View mErrorView; //加载错误的视图

    public BaseWebView(Context context) {
        super(context);
        mActivity = (Activity) context;
        init(context);
        initErrorPage();//初始化自定义页面
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity) context;
        init(context);
        initErrorPage();//初始化自定义页面
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init(Context context) {
        WebSettings webSettings = this.getSettings();
        webSettings.setJavaScriptEnabled(true);//设置webview支持javascript脚本
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); // 设置允许JS弹窗
        webSettings.setSupportZoom(true);// 是否可以缩放，默认true
        webSettings.setDomStorageEnabled(true);//是否可以使用dom存储
        webSettings.setGeolocationEnabled(true);
        this.getSettings().setBuiltInZoomControls(false);// 是否显示缩放按钮，默认false
        this.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
        // webView.getSettings().setAppCacheEnabled(true);//是否使用缓存
         webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        this.setWebViewClient(mWebViewClientBase);  // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        this.setWebChromeClient(mWebChromeClientBase);

        //新增webSettings权限
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
        webSettings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
        webSettings.setAllowFileAccessFromFileURLs(false); // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
        // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
        webSettings.setAllowUniversalAccessFromFileURLs(false);

    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        Log.i("debuginfo", "webviewbase-url:" + url);

    }

    private WebViewClientBase mWebViewClientBase = new WebViewClientBase();
    private WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase();


    private class WebViewClientBase extends WebViewClient {
        //重写这个方法 返回true，在当前 webView 打开，否则在浏览器中打开
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (null != url && !"".equals(url) && (url.contains("http://") || url.contains("https://"))) {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
//			DialogUtils.showProgressDialog(mActivity,"正在加载").show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//			DialogUtils.closeProgressDialog();
        }

        @Override//webView请求错误时候的处理
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.i(TAG, "errorCode:" + errorCode);
            ToastUtil.showToast(mActivity,"加载失败");
            showErrorPage(errorCode);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }


    }

    private class WebChromeClientBase extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar != null) {
                if (newProgress == 100) {
                    // 网页加载完成
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                } else {
                    // 加载中
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            } else {
                mActivity.setProgress(newProgress * 1000);
            }

        }

        @Override//拿到网页加载中的标题
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            Log.i("debuginfo", "title:" + title);
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
    }


    boolean isAddErrorView = false;

    /***
     * 显示加载失败时自定义的网页
     */
    private void initErrorPage() {
        if (mErrorView == null) {
            mErrorView = View.inflate(mActivity, R.layout.cp_layout_empty, null);
        }
    }

    /**
     * 显示自定义错误提示页面，用一个View覆盖在WebView
     */
    private void showErrorPage(int errorCode) {
        if (!isAddErrorView) {
            ViewGroup webParentView = (ViewGroup) getParent(); //获取父容器
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (webParentView instanceof LinearLayout) {//覆盖之前的View
                webParentView.addView(mErrorView, 0, layoutParams); //添加自定义的错误提示的View
            } else {
                webParentView.addView(mErrorView, layoutParams); //添加自定义的错误提示的View
            }
            isAddErrorView = true;
            mErrorView.setClickable(true);//防止被击穿
            mErrorView.setVisibility(GONE);
            mErrorView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    reload();
                }
            });
        }
        //请求错误时：页面显示
        ImageView ivImg = mErrorView.findViewById(R.id.iv_img);
        TextView tvMessage = mErrorView.findViewById(R.id.tv_message);
        tvMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        if (!NetworkUtil.hasNetwork(mActivity)) {
            ivImg.setImageResource(R.drawable.ic_state_no_network);
            tvMessage.setText("内容获取失败\n请检查网络后再重试......");
        } else if (errorCode == 404) {
            ivImg.setImageResource(R.drawable.ic_state_time_out);
            tvMessage.setText("哎呀,页面被外星人带走啦");
        } else {
            ivImg.setImageResource(R.drawable.ic_state_error);
            tvMessage.setText("内容获取失败\n请稍后重试......");
        }
        mErrorView.setVisibility(VISIBLE);
    }

    @Override
    public void reload() {
        super.reload();
        hideErrorView();
    }

    /**
     * 隐藏错误布局
     */
    public void hideErrorView() {
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
    }
}
