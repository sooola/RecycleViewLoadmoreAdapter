package com.wei.baseadapter.recycleviewloadmoreadapter.interfaces;

import com.wei.baseadapter.recycleviewloadmoreadapter.ViewHolder;

/**
 * Author: Othershe
 * Time: 2016/8/29 10:48
 */
public interface OnItemChildClickListener<T> {
    void onItemChildClick(ViewHolder viewHolder, T data, int position);
}
