package com.shop.app.shopactivity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.shop.app.common.AbstractCallBackLocationListener;
import com.shop.app.common.BaseActivity;
import com.shop.app.fragment.Fragment1;
import com.shop.app.fragment.Fragment2;
import com.shop.app.fragment.Fragment3;
import com.shop.app.fragment.Fragment4;
import com.shop.app.fragment.Fragment5;
import com.shop.app.shopapplication.R;
import com.shop.app.utils.AMapLocationClientUtils;
import com.shop.app.utils.BaseUtils;
import com.shop.app.utils.MyLog;
import com.shop.app.utils.PermissionUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @BindView(R.id.activity_main_layout_1)
    LinearLayout linearLayout1;
    @BindView(R.id.activity_main_layout_2)
    LinearLayout linearLayout2;
    @BindView(R.id.activity_main_layout_3)
    LinearLayout linearLayout3;
    @BindView(R.id.activity_main_layout_4)
    LinearLayout linearLayout4;
    @BindView(R.id.activity_main_layout_5)
    LinearLayout linearLayout5;

    @BindView(R.id.activity_main_image_1)
    ImageView imageView1;
    @BindView(R.id.activity_main_image_2)
    ImageView imageView2;
    @BindView(R.id.activity_main_image_3)
    ImageView imageView3;
    @BindView(R.id.activity_main_image_4)
    ImageView imageView4;
    @BindView(R.id.activity_main_image_5)
    ImageView imageView5;

    @BindView(R.id.activity_main_text_1)
    TextView textView1;
    @BindView(R.id.activity_main_text_2)
    TextView textView2;
    @BindView(R.id.activity_main_text_3)
    TextView textView3;
    @BindView(R.id.activity_main_text_4)
    TextView textView4;
    @BindView(R.id.activity_main_text_5)
    TextView textView5;

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;
    Fragment4 fragment4;
    Fragment5 fragment5;

    private Context context;
    private final Integer REQUEST_PERMISSION_LOCATION_CODE = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTitle();
        init();
        initData();
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        context = this;

        imageView1.setImageResource(R.drawable.index_1_active);
        textView1.setTextColor(getResources().getColor(R.color.forestgreen));

        linearLayout1.setOnClickListener(l);
        linearLayout2.setOnClickListener(l);
        linearLayout3.setOnClickListener(l);
        linearLayout4.setOnClickListener(l);
        linearLayout5.setOnClickListener(l);

        boolean b = new PermissionUtils(context).requestLocationPermission(REQUEST_PERMISSION_LOCATION_CODE);
        if (b) {
            getLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_LOCATION_CODE) {
            getLocation();
        }
    }

    private void getLocation() {
        new AMapLocationClientUtils(context).initLocation(new AbstractCallBackLocationListener() {
            @Override
            public void onCallBackSuccess(AMapLocation location) {
                if (null != location) {
                    StringBuffer sb = new StringBuffer();
                    //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                    if (location.getErrorCode() == 0) {
                        sb.append("定位成功" + "\n");
                        sb.append("经    度    : " + location.getLongitude() + "\n");
                        sb.append("纬    度    : " + location.getLatitude() + "\n");
                        sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
                        sb.append("国    家    : " + location.getCountry() + "\n");
                        sb.append("省          : " + location.getProvince() + "\n");
                        sb.append("市          : " + location.getCity() + "\n");
                    }
                    //解析定位结果，
                    String result = sb.toString();
                    MyLog.w("---", result);
                }
            }
        });
    }

    @Override
    protected void initData() {
        //首页fragment处理
        fragment1 = new Fragment1();
        FragmentManager fm = getFragmentManager();//获取到一个FragmentManger
        FragmentTransaction ft = fm.beginTransaction();//开启一个事务
        ft.add(R.id.activity_main_fragment, fragment1);
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        new AMapLocationClientUtils(context).destroyLocation();
    }

    @Override
    protected void initTitle() {
        BaseUtils.initHeaderBar(this);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            // 每次选中之前先清楚掉上次的选中状态
            clearSelection();
            switch (view.getId()) {
                case R.id.activity_main_layout_1:
                    if (fragment1 == null) {
                        fragment1 = new Fragment1();
                        ft.add(R.id.activity_main_fragment, fragment1);
                    }
                    //隐藏所有fragment
                    hideFragment(ft);
                    //显示需要显示的fragment
                    ft.show(fragment1);

//                    第二种方式(replace)，初始化fragment
//                    if (fragment1 == null) {
//                        fragment1 = new Fragment1();
//                    }
//                    ft.replace(R.id.activity_main_fragment, fragment1);

                    selectSelection(imageView1, textView1, R.drawable.index_1_active);
                    break;
                case R.id.activity_main_layout_2:
                    if (fragment2 == null) {
                        fragment2 = new Fragment2();
                        ft.add(R.id.activity_main_fragment, fragment2);
                    }
                    hideFragment(ft);
                    ft.show(fragment2);
                    selectSelection(imageView2, textView2, R.drawable.index_2_active);
                    break;
                case R.id.activity_main_layout_3:
                    if (fragment3 == null) {
                        fragment3 = new Fragment3();
                        ft.add(R.id.activity_main_fragment, fragment3);
                    }
                    hideFragment(ft);
                    ft.show(fragment3);
                    selectSelection(imageView3, textView3, R.drawable.index_3_active);
                    break;
                case R.id.activity_main_layout_4:
                    if (fragment4 == null) {
                        fragment4 = new Fragment4();
                        ft.add(R.id.activity_main_fragment, fragment4);
                    }
                    hideFragment(ft);
                    ft.show(fragment4);
                    selectSelection(imageView4, textView4, R.drawable.index_4_active);
                    break;
                case R.id.activity_main_layout_5:
                    if (fragment5 == null) {
                        fragment5 = new Fragment5();
                        ft.add(R.id.activity_main_fragment, fragment5);
                    }
                    hideFragment(ft);
                    ft.show(fragment5);
                    selectSelection(imageView5, textView5, R.drawable.index_5_active);
                    break;
            }
            ft.addToBackStack(null);//将一个事务添加到一个返回栈中，按back键后返回到之前的fragment中
            ft.commit();
        }
    };

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (fragment1 != null) {
            transaction.hide(fragment1);
        }
        if (fragment2 != null) {
            transaction.hide(fragment2);
        }
        if (fragment3 != null) {
            transaction.hide(fragment3);
        }
        if (fragment4 != null) {
            transaction.hide(fragment4);
        }
        if (fragment5 != null) {
            transaction.hide(fragment5);
        }
    }

    /**
     * 设置选中状态。
     */
    private void selectSelection(ImageView imageView, TextView textView, int resource) {
        imageView.setImageResource(resource);
        textView.setTextColor(getResources().getColor(R.color.forestgreen));
    }

    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        imageView1.setImageResource(R.drawable.index_1);
        imageView2.setImageResource(R.drawable.index_2);
        imageView3.setImageResource(R.drawable.index_3);
        imageView4.setImageResource(R.drawable.index_4);
        imageView5.setImageResource(R.drawable.index_5);

        textView1.setTextColor(getResources().getColor(R.color.colorTextAssistant));
        textView2.setTextColor(getResources().getColor(R.color.colorTextAssistant));
        textView3.setTextColor(getResources().getColor(R.color.colorTextAssistant));
        textView4.setTextColor(getResources().getColor(R.color.colorTextAssistant));
        textView5.setTextColor(getResources().getColor(R.color.colorTextAssistant));
    }
}
