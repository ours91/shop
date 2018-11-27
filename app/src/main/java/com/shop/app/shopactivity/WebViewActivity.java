package com.shop.app.shopactivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.insplatform.core.utils.TextUtil;
import com.shop.app.common.BaseActivity;
import com.shop.app.utils.BaseUtils;
import com.shop.app.utils.MyLog;
import com.shop.app.shopapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.KeyEvent.KEYCODE_BACK;

public class WebViewActivity extends BaseActivity {

    private final String TAG = "WebViewActivity";
    @BindView(R.id.activity_web_view)
    WebView webView;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        initTitle();
        ButterKnife.bind(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        url = bundle.getString("url");
        if (TextUtil.isEmpty(url)) {
            BaseUtils.showToast("url地址不存在", this);
            finish();
        } else {
            init();
        }
    }

    @Override
    protected void init() {
        webViewSetting();
        webViewOperation();
    }

    private void webViewOperation() {
        /**
         * 安卓调用js的方法
         * 1. 传入test(名字可自定义)对象,则js中会有test对象
         * 2. test对象.某某方法, 则可以调到AndroidtoJs()(名字可自定义)类的方法
         * 或者可以用约定好的请求,用shouldOverrideUrlLoading,request.getUrl();获取到url后直接操作
         */
        webView.addJavascriptInterface(new AndroidtoJs(), "test");

//        //方式1. 加载一个网页：
        webView.loadUrl("http://192.168.100.72/insfile/aaa.html");
//
//        //方式2：加载apk包中的html页面
//        webView.loadUrl("file:///android_asset/test.html");
//
//        //方式3：加载手机本地的html页面
//        webView.loadUrl("content://com.android.htmlfileprovider/sdcard/test.html");

        // 方式4： 加载 HTML 页面的一小段内容
//        WebView.loadData(String data, String mimeType, String encoding)
// 参数说明：
// 参数1：需要截取展示的内容
// 内容里不能出现 ’#’, ‘%’, ‘\’ , ‘?’ 这四个字符，若出现了需用 %23, %25, %27, %3f 对应来替代，否则会出现异常
// 参数2：展示内容的类型
// 参数3：字节码

        //WebViewClient类
        //处理各种通知 & 请求事件
        webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                //页面加载url
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                //开始载入页面调用的，我们可以设定一个loading的页面，告诉用户程序在等待网络响应。
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                //在页面加载结束时调用。我们可以关闭loading 条，切换程序动作。
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                //在加载页面资源时会调用，每一个资源（比如图片）的加载都会调用一次。
                super.onLoadResource(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //加载页面的服务器出现错误时（如404）调用。
                switch (error.getErrorCode()) {
                    case 404:
                        view.loadUrl("file:///android_assets/error_handle.html");
                        break;
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();    //表示等待证书响应
                // handler.cancel();      //表示挂起连接，为默认方式
                // handler.handleMessage(null);    //可做其他处理
            }
        });

        //WebChromeClient类
        //作用：辅助 WebView 处理 Javascript 的对话框,网站图标,网站标题等等。

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                //作用：获得网页的加载进度并显示
                if (newProgress < 100) {
                    String progress = newProgress + "%";
//                    progress.setText(progress);
                    MyLog.w(TAG, progress);
                } else {
                    MyLog.w(TAG, "隐藏进度条");
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                //作用：获取Web页中的标题
                MyLog.w(TAG, title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                //作用：支持javascript的警告框
                new AlertDialog.Builder(WebViewActivity.this)
                        .setTitle("JsAlert")
                        .setMessage(message)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setCancelable(false)
                        .show();
                return true;

            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                //作用：支持javascript的确认框
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                //作用：支持javascript输入框
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }
        });
    }

    private void webViewSetting() {
        //WebSettings类
        //作用：对WebView进行配置和管理
        WebSettings webSettings = webView.getSettings();
        //5.0以上开启混合模式加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        //允许js代码
        webSettings.setJavaScriptEnabled(true);
        //允许SessionStorage/LocalStorage存储
        webSettings.setDomStorageEnabled(true);
        //禁用放缩
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        //禁用文字缩放
        webSettings.setTextZoom(100);
        //10M缓存，api 18后，系统自动管理。
        webSettings.setAppCacheMaxSize(10 * 1024 * 1024);
        //允许缓存，设置缓存位置
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(context.getDir("appcache", 0).getPath());
        //允许WebView使用File协议
        webSettings.setAllowFileAccess(true);
        //不保存密码
        webSettings.setSavePassword(false);
        //自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
    }

    // 继承自Object类
    public class AndroidtoJs extends Object {
        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void hello(String msg) {
            MyLog.w(TAG, "JS调用了Android的hello方法");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:callJS('我是安卓传递给js的参数')");
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
