package com.wei.baseadapter.recycleviewloadmoreadapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by wei on 2019/1/3.
 */
public class ExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        findViewById(R.id.btn_single).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(SingleActivity.getIntent(view.getContext()));
            }
        });

        findViewById(R.id.btn_mul).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MulExampleActivity.startActivity(view.getContext());
            }
        });

        findViewById(R.id.btn_loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingActivity.startActivity(view.getContext());
            }
        });

        findViewById(R.id.btn_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HeaderActivity.startActivity(view.getContext());
            }
        });
    }
}
