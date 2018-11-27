package com.shop.app.adapter;

import android.content.Context;

import com.shop.app.common.AbstractBaseRecycleAdapter;
import com.shop.app.common.BaseViewHolder;

import java.util.List;
import java.util.Map;

public class CircleBtnAdapterAbstract extends AbstractBaseRecycleAdapter {
    /**
     * @param context  //上下文
     * @param layoutId //布局id
     * @param data     //数据源
     */
    public CircleBtnAdapterAbstract(Context context, int layoutId, List<Map<String, Object>> data) {
        super(context, layoutId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, Map<String, Object> map) {

    }
}
