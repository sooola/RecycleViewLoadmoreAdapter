package com.wei.adapter.listener;

import com.wei.adapter.ViewHolder;

/**
 * Created by wei on 2019/1/17.
 */
public interface OnMultiItemClickListeners<T> {

    void onItemClick(ViewHolder viewHolder, T data, int position, int viewType);
}
