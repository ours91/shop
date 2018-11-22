package com.shop.app.shopactivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.Resource;
import com.shop.app.common.BaseActivity;
import com.shop.app.common.PermissionUtils;
import com.shop.app.fragment.Fragment1;
import com.shop.app.fragment.Fragment2;
import com.shop.app.fragment.Fragment3;
import com.shop.app.fragment.Fragment4;
import com.shop.app.fragment.Fragment5;
import com.shop.app.shopapplication.R;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initTitle();
        init();
        initData();
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        //首页fragment处理
        fragment1 = new Fragment1();
        FragmentManager fm = getFragmentManager();//获取到一个FragmentManger
        FragmentTransaction ft = fm.beginTransaction();//开启一个事务
        ft.add(R.id.activity_main_fragment, fragment1);
        ft.commit();
        imageView1.setImageResource(R.drawable.index_1_active);
        textView1.setTextColor(getResources().getColor(R.color.forestgreen));

        linearLayout1.setOnClickListener(l);
        linearLayout2.setOnClickListener(l);
        linearLayout3.setOnClickListener(l);
        linearLayout4.setOnClickListener(l);
        linearLayout5.setOnClickListener(l);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initTitle() {

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

    //检测权限
    private void checkPermissions() {
        PermissionUtils p = new PermissionUtils(context);
        List<String> l = new ArrayList<>();
        l.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        l.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        l.add(Manifest.permission.RECORD_AUDIO);
        l.add(Manifest.permission.INTERNET);
        l.add(Manifest.permission.ACCESS_NETWORK_STATE);
        l.add(Manifest.permission.ACCESS_WIFI_STATE);
        l.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        l.add(Manifest.permission.CHANGE_WIFI_STATE);
        l.add(Manifest.permission.CAMERA);
        l.add(Manifest.permission.READ_PHONE_STATE);
        l.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
        p.requestPermissions(l);
    }
}
