package cn.lee.cplibrary.widget.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


public class BaseWebView extends WebView {
	private Activity mActivity;
	private ProgressBar progressBar;

	public BaseWebView(Context context) {
		super(context);
		mActivity = (Activity) context;
		init(context);
	}

	public BaseWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mActivity = (Activity) context;
		init(context);
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar =progressBar;
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void init(Context context) {
		WebSettings webSettings = this.getSettings();
		webSettings.setJavaScriptEnabled(true);//设置webview支持javascript脚本
		webSettings.setSupportZoom(true);// 是否可以缩放，默认true
		webSettings.setDomStorageEnabled(true);
		webSettings.setGeolocationEnabled(true);
		this.getSettings().setBuiltInZoomControls(false);// 是否显示缩放按钮，默认false
		this.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题
		// webView.getSettings().setAppCacheEnabled(true);//是否使用缓存
		// webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
		this.setWebViewClient(mWebViewClientBase);  // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
		this.setWebChromeClient(mWebChromeClientBase);
		// this.loadUrl(DEFAULT_URL);
		// this.onResume();

	}

	@Override
	public void loadUrl(String url) {
		super.loadUrl(url);
		Log.i("debuginfo","webviewbase-url:" + url);

	}

	private WebViewClientBase mWebViewClientBase = new WebViewClientBase();
	private WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase();


	private class WebViewClientBase extends WebViewClient {
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

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
//			DialogUtils.closeProgressDialog();
			Log.i("debuginfo","errorCode:" + errorCode);
//			EventBus.getDefault().post(new AdErroEvent());
			Toast.makeText(mActivity,"加载失败",Toast.LENGTH_SHORT).show();
		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
			super.doUpdateVisitedHistory(view, url, isReload);
		}

	}
	private class WebChromeClientBase extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (progressBar!=null) {
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

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
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
}
