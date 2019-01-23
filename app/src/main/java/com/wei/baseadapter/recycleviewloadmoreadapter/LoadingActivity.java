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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.wei.adapter.ViewHolder;
import com.wei.adapter.base.CommonBaseAdapter;
import com.wei.adapter.listener.OnItemClickListener;

public class LoadingActivity extends AppCompatActivity {

    private CommonBaseAdapter mAdapter;

    private RecyclerView mRecyclerView;

    public static void startActivity(Context context){
    	Intent intent = new Intent(context, LoadingActivity.class);
    	context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);
        //初始化adapter
        mAdapter = new CommonRefreshAdapter(this);

        //初始化EmptyView
        View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_layout, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.setEmptyView(emptyView);

        //初始化EmptyView
        View nodataView = LayoutInflater.from(this).inflate(R.layout.nodata_layout, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.setNodataView(nodataView);

        //设置item点击事件监听
        mAdapter.setOnItemClickListener(new OnItemClickListener<String>() {

            @Override
            public void onItemClick(ViewHolder viewHolder, String data, int position) {
                Toast.makeText(LoadingActivity.this, data, Toast.LENGTH_SHORT).show();
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
                mAdapter.noData();
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
