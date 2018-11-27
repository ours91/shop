package com.shop.app.common;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.insplatform.core.utils.JsonUtil;
import com.insplatform.core.utils.TextUtil;
import com.shop.app.utils.BaseUtils;
import com.xiasuhuei321.loadingdialog.view.LoadingDialog;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {

    private final String URL = Constant.REQUEST_URL;
    private final String FILE_URL = Constant.FILE_URL;

    private int CONNECT_TIMEOUT = 60;
    private int READ_TIMEOUT = 100;
    private int WRITE_TIMEOUT = 60;

    /**
     * post请求
     *
     * @param context,传context,获取设备id需要
     * @param data,以map的形式传递参数
     * @param callBackSuccess,回调callback
     */
    public void post(final Context context, String url, Map<String, Object> data, final CallBackSuccess callBackSuccess) {
        if (BaseUtils.isNetworkConnected(context)) {
            post(context, url, data, callBackSuccess, false);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "请先检查网络链接!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void post(final Context context, String url, Map<String, Object> data, final CallBackSuccess callBackSuccess, boolean showLoading) {
        if (showLoading) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    new LoadingDialog(context).show();
                }
            });
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();
        if (data == null) {
            data = new HashMap<String, Object>();
        }
        if (TextUtil.isNotEmpty(BaseUtils.getSharedPreferences(context, "fu"))) {
            data.put("fu", BaseUtils.getSharedPreferences(context, "fu"));
        }
        FormBody.Builder formBody = new FormBody.Builder();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            formBody.add(BaseUtils.toString(entry.getKey()), BaseUtils.toString(entry.getValue()));
        }
        RequestBody requestBody = formBody.build();
        Request request = new Request.Builder().post(requestBody).url(URL + url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Exception", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String back = response.body().string();
                Map<String, Object> data = JsonUtil.toObject(back, Map.class);
                /*
                 * 成功统一处理
                 * 返回成功,则是data
                 */
                callBackSuccess.onCallBackSuccess(data.get("data"));
            }
        });

        if (showLoading) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    new LoadingDialog(context).close();
                }
            });
        }
    }
}
