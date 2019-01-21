package com.wei.adapter.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wei.adapter.R;
import com.wei.adapter.Util;
import com.wei.adapter.ViewHolder;
import com.wei.adapter.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei on 2018/10/25
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private String TAG = "BaseAdapter";

    static final int TYPE_COMMON_VIEW = 100001;//普通类型 Item
    private static final int TYPE_FOOTER_VIEW = 100002;//footer类型 Item
    private static final int TYPE_EMPTY_VIEW = 100003;//empty view，即初始化加载时的提示View
    private static final int TYPE_NODATA_VIEW = 100004;//初次加载无数据的默认空白view
    private static final int TYPE_RELOAD_VIEW = 100005;//初次加载无数据的可重新加载或提示用户的view
    private static final int TYPE_BASE_HEADER_VIEW = 200000;

    private SparseArrayCompat<View> mHeaderViews = new SparseArrayCompat<>();

    private OnLoadMoreListener mLoadMoreListener;

    protected Context mContext;
    private List<T> mDatas;
    private boolean isOpenLoadMore;//是否开启加载更多
    private boolean isAutoLoadMore;//是否自动加载，当数据不满一屏幕会自动加载
    private boolean isAutoLoadMoreEnd;//自动加载更多是否已经结束

    private View mLoadingView; //分页加载中view
    private View mLoadFailedView; //分页加载失败view
    private View mLoadEndView; //分页加载结束view
    private View mEmptyView; //首次预加载view
    private View mReloadView; //首次预加载失败、或无数据的view
    private RelativeLayout mFooterLayout;//FooterView

    private boolean isReset;//开始重新加载数据

    private boolean isLoading;//是否正在加载更多

    private boolean showHeaderView = true;//是否显示HeaderView

    protected abstract int getViewType(int position, T data);

    public BaseAdapter(Context context, boolean isOpenLoadMore) {
        mContext = context;
        mDatas =  new ArrayList<T>();
        this.isOpenLoadMore = isOpenLoadMore;
    }

    public BaseAdapter(Context context, List<T> datas, boolean isOpenLoadMore) {
        mContext = context;
        mDatas = datas == null ? new ArrayList<T>() : datas;
        this.isOpenLoadMore = isOpenLoadMore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (showHeaderView && mHeaderViews.get(viewType) != null) {
            return ViewHolder.create(mHeaderViews.get(viewType));
        }

        ViewHolder viewHolder = null;
        switch (viewType) {
            case TYPE_FOOTER_VIEW:
                if (mFooterLayout == null) {
                    mFooterLayout = new RelativeLayout(mContext);
                }
                viewHolder = ViewHolder.create(mFooterLayout);
                break;
            case TYPE_EMPTY_VIEW:
                viewHolder = ViewHolder.create(mEmptyView);
                break;
            case TYPE_NODATA_VIEW:
                viewHolder = ViewHolder.create(new View(mContext));
                break;
            case TYPE_RELOAD_VIEW:
                viewHolder = ViewHolder.create(mReloadView);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (mDatas.isEmpty() && (mEmptyView != null || mReloadView != null)) {
            return 1;
        }
        return mDatas.size() + getFooterViewCount() + getHeaderCount();
    }

    protected boolean isCommonItemView(int viewType) {
        return viewType != TYPE_EMPTY_VIEW && viewType != TYPE_FOOTER_VIEW
                && viewType != TYPE_NODATA_VIEW && viewType != TYPE_RELOAD_VIEW
                && !(viewType >= TYPE_BASE_HEADER_VIEW);
    }

    @Override
    public int getItemViewType(int position) {
        if (mDatas.isEmpty()) {
            Log.d("aaaa" , "mDatas.isEmpty()");
            if (mEmptyView != null) {
                return TYPE_EMPTY_VIEW;
            }

            if (mReloadView != null) {
                return TYPE_RELOAD_VIEW;
            }

            if (showHeaderView && isHeaderView(position)) {
                return mHeaderViews.keyAt(position);
            }

            return TYPE_NODATA_VIEW;
        }

        if (showHeaderView && isHeaderView(position)) {
            return mHeaderViews.keyAt(position);
        }

        if (isFooterView(position)) {
            return TYPE_FOOTER_VIEW;
        }
        return getViewType(position - getHeaderCount(), mDatas.get(position - getHeaderCount()));
    }

    private boolean isHeaderView(int position) {
        return position < getHeaderCount();
    }

    protected int getHeaderCount() {
        if (!showHeaderView) {
            return 0;
        }
        return mHeaderViews.size();
    }

    /**
     * 删除某个位置的数据
     *
     * @param position
     */
    public void remove(int position) {
        if (position >= mDatas.size() || position < 0) {
            return;
        }
        mDatas.remove(position);
        notifyItemRemoved(position + getHeaderCount());
        if (position != mDatas.size()) {
            notifyItemRangeChanged(position + getHeaderCount(), mDatas.size() - position);
        }
    }

    /**
     * 初始化emptyView
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
    }

    public void setNodataView(View reloadView) {
        mReloadView = reloadView;
    }

    /**
     * 移除emptyView
     */
    public void removeEmptyView() {
        mEmptyView = null;
        notifyDataSetChanged();
    }

    /**
     * 移除emptyView
     */
    public void removeNodataView() {
        mReloadView = null;
        notifyDataSetChanged();
    }

    /**
     * 从某个位置开始添加若干个数据
     *
     * @param datas
     * @param position
     */
    public void insert(List<T> datas, int position) {
        if (position > mDatas.size() || position < 0) {
            return;
        }
        mDatas.addAll(position, datas);
        notifyItemRangeInserted(position + getHeaderCount(), datas.size());
        notifyItemRangeChanged(position + getHeaderCount(), mDatas.size() - position);
    }

    /**
     * 给列表末尾追加多个数据
     *
     * @param datas
     */
    public void insert(List<T> datas) {
        insert(datas, mDatas.size());
    }

    /**
     * 添加单个数据到指定位置
     *
     * @param data
     * @param position
     */
    public void insert(T data, int position) {
        if (position > mDatas.size() || position < 0) {
            return;
        }
        mDatas.add(position, data);
        notifyItemInserted(position + getHeaderCount());
        notifyItemRangeChanged(position + getHeaderCount(), mDatas.size() - position);
    }

    /**
     * 给列表末尾追加单个数据
     *
     * @param data
     */
    public void insert(T data) {
        insert(data, mDatas.size());
    }


    public void isShowHeaderView(boolean showHeaderView) {
        this.showHeaderView = showHeaderView;
    }

    /**
     * 通过位置判断是否为FooterView
     * @param position
     * @return
     */
    private boolean isFooterView(int position) {
        return isOpenLoadMore && position >= getItemCount() - 1;
    }

    /**
     * 初始化加载中布局
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        mLoadingView = null;
        mLoadingView = loadingView;
        addFooterView(mLoadingView);
    }

    public void setLoadingView(int loadingId) {
        setLoadingView(Util.inflate(mContext, loadingId));
    }

    /**
     * 初始加载失败布局
     *
     * @param loadFailedView
     */
    public void setLoadFailedView(View loadFailedView) {
        mLoadFailedView = null;
        mLoadFailedView = loadFailedView;
    }

    public void setLoadFailedView(int loadFailedId) {
        setLoadFailedView(Util.inflate(mContext, loadFailedId));
    }

    /**
     * 初始化全部加载完成布局
     *
     * @param loadEndView
     */
    public void setLoadEndView(View loadEndView) {
        mLoadEndView = null;
        mLoadEndView = loadEndView;
    }

    public void setLoadEndView(int loadEndId) {
        setLoadEndView(Util.inflate(mContext, loadEndId));
    }

    /**
     * 添加HeaderView
     *
     * @param view
     */
    public void addHeaderView(View view) {
        if (view == null) {
            return;
        }
        mHeaderViews.put(TYPE_BASE_HEADER_VIEW + getHeaderCount(), view);
    }

    /**
     * 返回 footer view数量
     *
     * @return
     */
    private int getFooterViewCount() {
        return isOpenLoadMore && !mDatas.isEmpty() ? 1 : 0;
    }

    /**
     * StaggeredGridLayoutManager模式时，HeaderView、FooterView可占据一行
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getLayoutPosition();
        if (isFooterView(position) || isHeaderView(position)) {
            //头部或底部的情况
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    /**
     * GridLayoutManager模式时， HeaderView、FooterView可占据一行，判断RecyclerView是否到达底部
     *
     * @param recyclerView
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) layoutManager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (isFooterView(position) || isHeaderView(position)) {
                        return gridManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
        startLoadMore(recyclerView, layoutManager);
    }

    /**
     * 判断列表是否滑动到底部
     * @param recyclerView
     * @param layoutManager
     */
    private void startLoadMore(RecyclerView recyclerView, final RecyclerView.LayoutManager layoutManager) {
        if (!isOpenLoadMore || mLoadMoreListener == null) {
            return;
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //当前状态为停止滑动状态SCROLL_STATE_IDLE时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("BaseAdapter" , "in SCROLL_STATE_IDLE");
                    if (isAutoLoadMoreEnd && findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                        Log.d("BaseAdapter" , "onScrollStateChanged scrollLoadMore");
                        scrollLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (findLastVisibleItemPosition(layoutManager) + 1 == getItemCount()) {
                    if (mEmptyView != null || mReloadView != null
                            || (mHeaderViews.size() > 0 && showHeaderView && mDatas.isEmpty())) {
                        return;
                    }
                    if (isAutoLoadMore && !isAutoLoadMoreEnd) {
                        scrollLoadMore();
                    } else if (!isAutoLoadMoreEnd) {
                        loadEnd();
                        isAutoLoadMoreEnd = true;
                    }
                } else {
                    isAutoLoadMoreEnd = true;
                }
            }
        });
    }

    /**
     * 到达底部开始刷新
     */
    private void scrollLoadMore() {
        Log.d("BaseAdapter" , "in scrollLoadMore");
        if (isReset) {
            return;
        }
        if (mFooterLayout.getChildAt(0) == mLoadingView && !isLoading) {
            Log.d("BaseAdapter" , "in mFooterLayout scrollLoadMore");
            if (mLoadMoreListener != null) {
                isLoading = true;
                Log.d("BaseAdapter" , "onLoadMore");
                mLoadMoreListener.onLoadMore(false);
            }
        }
    }

    /**
     * 添加新的footer view
     *
     * @param footerView
     */
    public void addFooterView(View footerView) {
        Log.d("BaseAdapter" , "in addFooterView");
        if (footerView == null) {
            return;
        }

        if (mFooterLayout == null) {
            mFooterLayout = new RelativeLayout(mContext);
        }
        removeFooterView();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mFooterLayout.addView(footerView, params);
    }

    /**
     * 清空footer view
     */
    private void removeFooterView() {
        mFooterLayout.removeAllViews();
    }

    /**
     * 重置adapter，恢复到初始状态
     */
    public void reset() {
        if (mLoadingView != null) {
            addFooterView(mLoadingView);
        }
        isLoading = false;
        isReset = true;
        isAutoLoadMoreEnd = false;
        mDatas.clear();
    }

    public void setNewData(List<T> datas) {
        if (null != datas){
            if (isOpenLoadMore) {
                if (isReset) {
                    isReset = false;
                }
                isLoading = false;
                mEmptyView = null;
                mReloadView = null;
            }
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }
    }

    public void cleanData(){
        mDatas.clear();
        notifyDataSetChanged();
    }

    public void setData(List<T> datas) {
        if (null != datas && mDatas.size() == 0){
            if (isOpenLoadMore) {
                if (isReset) {
                    isReset = false;
                }
                isLoading = false;
                mEmptyView = null;
                mReloadView = null;
            }
            mDatas.clear();
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }else if(datas != null && datas.size() != 0) {
            isLoading = false;
            insert(datas, mDatas.size());
        }else if (datas == null && mDatas.size() != 0 ){
            //没有更多数据
            loadEnd();
        }
    }

    /**
     * 刷新加载更多的数据
     *
     * @param datas
     */
    public void setLoadMoreData(List<T> datas) {
        isLoading = false;
        insert(datas, mDatas.size());
    }

    /**
     * 获得列表数据个数
     *
     * @return
     */
    public int getDataCount() {
        return mDatas.size();
    }

    public List<T> getAllData() {
        return mDatas;
    }

    /**
     * 数据加载完成
     */
    public void loadEnd() {
        Log.d("BaseAdapter" , "in loadEnd");
        if (mLoadEndView != null) {
            addFooterView(mLoadEndView);
        } else {
            addFooterView(new View(mContext));
        }
    }

    public void noData(){
        mEmptyView = null;
        notifyDataSetChanged();
    }

    private int findLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(null);
            return Util.findMax(lastVisibleItemPositions);
        }
        return -1;
    }


    /**
     * 数据加载失败
     */
    public void loadFailed() {
        addFooterView(mLoadFailedView);
        mLoadFailedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFooterView(mLoadingView);
                if (mLoadMoreListener != null) {
                    mLoadMoreListener.onLoadMore(true);
                }
            }
        });
    }

    /**
     * 开启初次数据不满一屏自动加载更多
     */
    public void openAutoLoadMore() {
        this.isAutoLoadMore = true;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        setLoadingView(R.layout.load_loading_layout);
        setLoadFailedView(R.layout.load_failed_layout);
        setLoadEndView(R.layout.load_end_layout);
        mLoadMoreListener = loadMoreListener;
    }
}
