package com.shop.app.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.shop.app.utils.MyLog;
import com.shop.app.utils.Utils;
import com.xiasuhuei321.loadingdialog.manager.StyleManager;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

/**
 * 极光推送   一般建议在自定义 Application 类里初始化。也可以在主 Activity 里。
 */
public class BaseApplication extends Application implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "BaseApplication";
    public static boolean isBackGround = true;//是否在前台
    private static Handler handler;
    private static BaseApplication application;
    private Context context;

    @Override
    public void onCreate() {
        //初始化方法写这
        super.onCreate();
        context = this;
        //圆形头像初始化
        Fresco.initialize(this);
        Utils.init(getApplicationContext());
        handler = new Handler();
        application = this;
        //极光推送初始化
//        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
//        JPushInterface.init(this);     		// 初始化 JPush
        //初始化loading
        StyleManager s = new StyleManager();
        //在这里调用方法设置s的属性
        //code here...
        s.Anim(false).repeatTime(0).contentSize(-1).intercept(true);
        LoadingDialog.initStyle(s);
    }

    public static Handler getHandler() {
        return handler;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackGround = true;
            MyLog.i("----", "APP遁入后台");
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (isBackGround) {
            isBackGround = false;
            MyLog.i("bo", "APP回到了前台");
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

}
