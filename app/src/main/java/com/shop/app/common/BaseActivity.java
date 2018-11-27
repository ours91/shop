package com.shop.app.common;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.shop.app.shopapplication.R;
import com.shop.app.utils.BaseUtils;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.util.List;

public abstract class BaseActivity extends Activity {
    private final String TAG = "BaseActivity";
    protected LoadingDialog loadingDialog;
    protected Context context;

    ActivityManager activityManager = null;
    String activityName = null;
    String activity_last = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //添加Activity到堆栈
        AppManager.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
        context = this;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

    protected boolean isActive = true; //是否活跃

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!isActive) {
            //app 从后台唤醒，进入前台
            isActive = true;
            MyLog.i(TAG, "进入前台");
        }
    }

    /**
     * 是否有返回按钮
     */
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:// 点击bar action 返回图标事件
                goBack();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
        }
        return true;
    }

    /**
     * 显示界面
     */
    protected void showView() {
    }

    protected <T extends View> T $(View v, int resId) {
        return (T) v.findViewById(resId);
    }

    protected <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 返回前做处理
     */
    public boolean beforeBack() {
        return true;
    }

    /**
     * @throws
     * @Title 返回
     * @Description TODO
     * @author LH
     */
    public void goBack() {
        if (!beforeBack()) {
            return;
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.pre_fade_in, R.anim.pre_fade_out);
    }

    protected void openActivityForResult(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivityForResult(intent, 0);
        overridePendingTransition(R.anim.pre_fade_in, R.anim.pre_fade_out);
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        if (!isAppOnForeground()) {
            //app 进入后台
            //全局变量 记录当前已经进入后台
            isActive = false;
            MyLog.i(TAG, "进入后台");
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        MyLog.i("memory -- info -->", level + "");
    }

    @Override
    protected void onDestroy() {
        //结束Activity&从堆栈中移除
        AppManager.getInstance().killActivity(this);
        super.onDestroy();
    }

    protected void showLoading() {
        loadingDialog = new LoadingDialog(BaseActivity.this);
        loadingDialog.show();
    }

    protected void closeLoading() {
        loadingDialog.close();
    }

    protected void successfulLoading() {
        loadingDialog.loadSuccess();
    }

    protected void failedLoading() {
        loadingDialog.loadFailed();
    }

    protected void showToast(Object msg) {
        Toast.makeText(BaseActivity.this, BaseUtils.toString(msg), Toast.LENGTH_SHORT).show();
    }

    //控制点击其他位置时关闭键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    protected abstract void init();

    protected abstract void initData();

    protected abstract void initTitle();

}
