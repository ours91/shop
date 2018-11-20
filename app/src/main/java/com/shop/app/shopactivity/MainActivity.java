package com.shop.app.shopactivity;

import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.shop.app.common.BaseActivity;
import com.shop.app.fragment.Fragment1;
import com.shop.app.fragment.Fragment2;
import com.shop.app.fragment.Fragment3;
import com.shop.app.fragment.Fragment4;
import com.shop.app.fragment.Fragment5;
import com.shop.app.shopapplication.R;

import butterknife.BindView;

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
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment f = null;
            switch (view.getId()){
                case R.id.activity_main_layout_1:
                    f = new Fragment1();
                    break;
                case R.id.activity_main_layout_2:
                    f = new Fragment2();
                    break;
                case R.id.activity_main_layout_3:
                    f = new Fragment3();
                    break;
                case R.id.activity_main_layout_4:
                    f = new Fragment4();
                    break;
                case R.id.activity_main_layout_5:
                    f = new Fragment5();
                    break;
            }
            ft.replace(R.id.activity_main_fragment, f);
            ft.commit();
        }
    };
}
