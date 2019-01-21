package com.wei.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wei on 2018/10/25
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;   //缓存View
    private View mConvertView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }


    public static ViewHolder create(Context context, int layoutId, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ViewHolder(itemView);
    }

    public static ViewHolder create(View itemView) {
        return new ViewHolder(itemView);
    }

    /**
     * 通过id获得控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public View getSwipeView() {
        ViewGroup itemLayout = ((ViewGroup) mConvertView);
        if (itemLayout.getChildCount() == 2) {
            return itemLayout.getChildAt(1);
        }
        return null;
    }

    public ViewHolder setText(int viewId, String text) {
        TextView textView = getView(viewId);
        textView.setText(text);
        return this;
    }

    public ViewHolder setText(int viewId, int textId) {
        TextView textView = getView(viewId);
        textView.setText(textId);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int colorId) {
        TextView textView = getView(viewId);
        textView.setTextColor(colorId);
        return this;
    }

    public ViewHolder setBgResource(int viewId, int resId) {
        View view = getView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }

    public ViewHolder setBgColor(int viewId, int colorId) {
        View view = getView(viewId);
        view.setBackgroundColor(colorId);
        return this;
    }

    public ViewHolder setVisibility(int viewId, boolean visiable) {
        View view = getView(viewId);
        view.setVisibility(visiable ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder setClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        view.setOnClickListener(clickListener);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setOnClickListener(int viewId, View.OnClickListener clickListener) {
        View view = getView(viewId);
        view.setOnClickListener(clickListener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }
}
