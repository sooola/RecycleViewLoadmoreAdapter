package com.wei.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.wei.adapter.ViewHolder;
import com.wei.adapter.listener.OnItemChildClickListener;
import com.wei.adapter.listener.OnItemClickListener;
import com.wei.adapter.listener.OnItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei on 2018/10/25
 */
public abstract class CommonBaseAdapter<T> extends BaseAdapter<T> {

    private OnItemClickListener<T> mItemClickListener;
    private OnItemLongClickListener<T> mItemLongClickListener;
    private ArrayList<Integer> mItemChildIds = new ArrayList<>();
    private ArrayList<OnItemChildClickListener<T>> mItemChildListeners = new ArrayList<>();

    public CommonBaseAdapter(Context context) {
        super(context, true);
    }

    public CommonBaseAdapter(Context context ,List<T> datas) {
        super(context, datas,true);
    }

    public CommonBaseAdapter(Context context, boolean isOpenLoadMore) {
        super(context, isOpenLoadMore);
    }

    public CommonBaseAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        super(context, datas , isOpenLoadMore);
    }

    protected abstract void convert(ViewHolder holder, T data, int position);

    protected abstract int getItemLayoutId();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isCommonItemView(viewType)) {
            return ViewHolder.create(mContext, getItemLayoutId(), parent);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = holder.getItemViewType();
        if (isCommonItemView(viewType)) {
            bindCommonItem(holder, position - getHeaderCount());
        }
    }

    private void bindCommonItem(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        convert(viewHolder, getAllData().get(position), position);

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(viewHolder, getAllData().get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mItemLongClickListener != null) {
                    mItemLongClickListener.onItemClick(viewHolder, getAllData().get(position), position);
                }
                return false;
            }
        });

        for (int i = 0; i < mItemChildIds.size(); i++) {
            final int tempI = i;
            if (viewHolder.getConvertView().findViewById(mItemChildIds.get(i)) != null) {
                viewHolder.getConvertView().findViewById(mItemChildIds.get(i)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemChildListeners.get(tempI).onItemChildClick(viewHolder, getAllData().get(position), position);
                    }
                });
            }
        }
    }

    @Override
    protected int getViewType(int position, T data) {
        return TYPE_COMMON_VIEW;
    }

    public void setOnItemClickListener(OnItemClickListener<T> itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        mItemLongClickListener = onItemLongClickListener;
    }

    public void setOnItemChildClickListener(int viewId, OnItemChildClickListener<T> itemChildClickListener) {
        mItemChildIds.add(viewId);
        mItemChildListeners.add(itemChildClickListener);
    }
}
