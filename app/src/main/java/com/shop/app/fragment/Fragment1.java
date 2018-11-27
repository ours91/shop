package com.shop.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.shop.app.common.HeaderBar;
import com.shop.app.common.MyLog;
import com.shop.app.common.getPhotoFromPhotoAlbum;
import com.shop.app.shopactivity.WebViewActivity;
import com.shop.app.shopapplication.R;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class Fragment1 extends Fragment implements OnBannerListener {

    private final String TAG = "Fragment1";
    //系统返回值变量
    private final Integer REQUEST_TAKE_SCAN_CODE = 10;
    private final Integer REQUEST_TAKE_PHOTO_CODE = 11;
    private final Integer REQUEST_TAKE_PHOTO_PICKER_CODE = 12;
    //轮播图变量
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    //相机变量
    // /storage/emulated/0/pic
    public final static String SAVED_IMAGE_PATH1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/pic";//+"/pic";
    // /storage/emulated/0/Pictures
    public final static String SAVED_IMAGE_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();//.getAbsolutePath()+"/pic";//+"/pic";
    String photoPath;

    @BindView(R.id.fragment_1_banner)
    Banner banner;
    @BindView(R.id.headerBar)
    HeaderBar headerBar;
    @BindView(R.id.fragment_1_gridView)
    GridView gridView;
    Unbinder unbinder;


    //圆形按钮适配器
    private SimpleAdapter simpleAdapter;
    private List<Map<String, Object>> list_circle_btn;
    private int[] icon = {R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1, R.drawable.index1};
    private String[] iconName = {"通讯录", "日历", "照相机", "时钟", "游戏", "短信", "铃声", "设置", "语音", "天气", "浏览器", "视频"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(getActivity());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                initHeaderBar();
                initView();
                initData();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void initHeaderBar() {
        headerBar.setAppTitle("首页");
        headerBar.initViewsVisible(false, true, false, false);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        //圆形按钮集合
        list_circle_btn = new ArrayList<>();
    }

    private void initData() {
        /***********************轮播图*************************/
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        list_path.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2e7vsaj30ci08cglz.jpg");
        list_title.add("好好学习");
        list_title.add("天天向上");
        list_title.add("热爱劳动");
        list_title.add("不搞对象");
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();
        /***********************圆形按钮*************************/
        simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(), getData(), R.layout.adapter_circle_btn, new String[]{"image", "text"},
                new int[]{R.id.adapter_circle_image, R.id.adapter_circle_text});
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(l);
    }

    AdapterView.OnItemClickListener l = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position > -1) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putCharSequence("url", "https://www.baidu.com");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    private List<Map<String, Object>> getData() {
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            list_circle_btn.add(map);
        }
        return list_circle_btn;
    }

    private void scan() {
        //扫一扫
        Intent intent = new Intent(getActivity().getApplicationContext(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_TAKE_SCAN_CODE);
    }

    private void takePhoto() {
        //相机
        //获取SD卡安装状态
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //设置图片保存路径
            photoPath = SAVED_IMAGE_PATH + "/" + System.currentTimeMillis() + ".png";
            File imageDir = new File(photoPath);
            if (!imageDir.exists()) {
                try {
                    //根据一个 文件地址生成一个新的文件用来存照片
                    imageDir.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //实例化intent,指向摄像头
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //根据路径实例化图片文件
            File photoFile = new File(photoPath);
            //设置拍照后图片保存到文件中
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            //启动拍照activity并获取返回数据
            startActivityForResult(intent, REQUEST_TAKE_PHOTO_CODE);
        } else {
            Toast.makeText(getActivity(), "SD卡未插入", Toast.LENGTH_SHORT).show();
        }
    }

    private void takePhotoPicker() {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_TAKE_PHOTO_PICKER_CODE);
    }

    //轮播图的监听方法
    @Override
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第" + position + "张轮播图");
        if (position == 0) {
            scan();
        } else if (position == 1) {
            takePhoto();
        } else if (position == 2) {
            takePhotoPicker();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_SCAN_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                MyLog.w("扫描结果为：", content);
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO_CODE && resultCode == RESULT_OK) {
            File photoFile = new File(photoPath);
            if (photoFile.exists()) {
                MyLog.w("拍照的图片地址是", photoFile.getAbsolutePath());
            } else {
                Toast.makeText(getActivity(), "图片文件不存在", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO_PICKER_CODE && resultCode == RESULT_OK) {
            String photoPath = getPhotoFromPhotoAlbum.getRealPathFromUri(getActivity(), data.getData());
            MyLog.w("选中的图片地址是", photoPath);
        }
    }

    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
}
