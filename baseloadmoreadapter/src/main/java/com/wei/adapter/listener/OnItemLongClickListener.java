package com.wei.adapter.listener;

import com.wei.adapter.ViewHolder;

/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnItemLongClickListener<T> {
    void onItemClick(ViewHolder viewHolder, T data, int position);
}
