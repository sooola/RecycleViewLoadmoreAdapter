package com.wei.baseadapter.recycleviewloadmoreadapter;

import android.content.Context;

import com.wei.adapter.ViewHolder;
import com.wei.adapter.base.CommonBaseAdapter;

/**
 * Created by wei on 2018/11/8
 */
public class CommonRefreshAdapter extends CommonBaseAdapter<String> {

    public CommonRefreshAdapter(Context context) {
        super(context);
    }

    @Override
    protected void convert(ViewHolder holder, final String data, final int position) {
        holder.setText(R.id.item_title, data);
    }


    @Override
    protected int getItemLayoutId() {
        return R.layout.item_test;
    }
}
