package com.wei.baseadapter.recycleviewloadmoreadapter;

import android.content.Context;
import com.wei.adapter.ViewHolder;
import com.wei.adapter.base.MulBaseAdapter;

import java.util.List;

/**
 * Created by wei on 2019/1/17.
 */
public class MulRefreshAdapter extends MulBaseAdapter<String> {

    public MulRefreshAdapter(Context context, List<String> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, final String data, int position, int viewType) {
        if (viewType == 0) {
            holder.setText(R.id.item_title, data);
        } else {
            holder.setText(R.id.item_title1, data);
        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.item_layout;
        }
        return R.layout.item_layout1;
    }

    @Override
    protected int getViewType(int position, String data) {
        if (position % 2 == 0) {
            return 0;
        }
        return 1;
    }
}
