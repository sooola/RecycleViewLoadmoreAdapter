package com.wei.baseadapter.recycleviewloadmoreadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;
import com.wei.adapter.ViewHolder;
import com.wei.adapter.base.CommonBaseAdapter;
import com.wei.adapter.listener.OnItemClickListener;
import com.wei.adapter.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class SingleActivity extends AppCompatActivity {

    private CommonBaseAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private boolean isFailed = true;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, SingleActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);

        //初始化adapter
        mAdapter = new CommonRefreshAdapter(this, null, true);

        mAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_layout, (ViewGroup) mRecyclerView.getParent(), false));
        //设置加载更多触发的事件监听
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
                loadMore();
            }
        });

        //设置item点击事件监听
        mAdapter.setOnItemClickListener(new OnItemClickListener<String>() {

            @Override
            public void onItemClick(ViewHolder viewHolder, String data, int position) {
                Toast.makeText(SingleActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);

        //延时2s刷新列表
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> data = new ArrayList<>();
                for (int i = 0; i < 18; i++) {
                    data.add("item--" + i);
                }
                //刷新数据
//                mAdapter.setNewData(data);
                mAdapter.setData(data);

            }
        }, 2000);
    }


    private void loadMore() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAdapter.getItemCount() >= 15 && isFailed) {
                    isFailed = false;
                    mAdapter.loadFailed();
                } else if (mAdapter.getItemCount() >= 20) {
                    mAdapter.loadEnd();
                } else {
                    final List<String> data = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        data.add("item--" + (mAdapter.getDataCount() + i));
                    }
                    //刷新数据
//                    mAdapter.setLoadMoreData(data);
                    mAdapter.setData(data);
                }
            }
        }, 2000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
