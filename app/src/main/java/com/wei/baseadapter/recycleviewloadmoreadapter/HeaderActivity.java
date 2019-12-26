package com.wei.baseadapter.recycleviewloadmoreadapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.wei.adapter.base.CommonBaseAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wei on 2019/1/19.
 */
public class HeaderActivity extends AppCompatActivity {

    private CommonBaseAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static void startActivity(Context context){
    	Intent intent = new Intent(context, HeaderActivity.class);
    	context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleview);

        //初始化adapter
        mAdapter = new CommonAdapter(this);

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
                mAdapter.setData(data);

            }
        }, 0);
    }

    //重写onCreateOptionMenu(Menu menu)方法，当菜单第一次被加载时调用
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //填充选项菜单（读取XML文件、解析、加载到Menu组件上）
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //重写OptionsItemSelected(MenuItem item)来响应菜单项(MenuItem)的点击事件（根据id来区分是哪个item）
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_header:
                Toast.makeText(this, "添加header", Toast.LENGTH_SHORT).show();
                TextView t1 = new TextView(HeaderActivity.this);
                t1.setText("我是header");
                mAdapter.addHeaderView(t1);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.add_foot:
                Toast.makeText(this, "添加footer", Toast.LENGTH_SHORT).show();
                TextView t2 = new TextView(HeaderActivity.this);
                t2.setText("我是footer");
                mAdapter.addFooterView(t2);
                mAdapter.notifyDataSetChanged();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
