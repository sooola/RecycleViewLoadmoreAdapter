# RecycleViewLoadmoreAdapter
到底部自动加载更多的adapter，封装简化adapter代码，支持多样式item布局

# 引入  
1.在根目录的build.gradle 
```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
2.在项目build.gradle 
```
implementation 'com.github.sooola:RecycleViewLoadmoreAdapter:1.1'
```

# 使用
### 单一布局item的情况
继承CommonBaseAdapter

```
public class CommonRefreshAdapter extends CommonBaseAdapter<String> {

    public CommonRefreshAdapter(Context context) {
        super(context);
    }

    @Override
    protected void convert(ViewHolder holder, final String data, final int position) {
        holder.setText(R.id.item_title, data);
    }


    @Override
    protected int getItemLayoutId() {
        return R.layout.item_test;
    }
}

```
效果图  
![image](https://github.com/sooola/RecycleViewLoadmoreAdapter/blob/master/screenshots/list1.png)


### 多样式item情况  
继承MulCommonBaseAdapter

getViewType 根据data的情况返回不用的item type  
getItemLayoutId 根据不同的type返回布局

```
public class MulRefreshAdapter extends MulCommonBaseAdapter<String> {

    public MulRefreshAdapter(Context context, List<String> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
    }

    @Override
    protected void convert(ViewHolder holder, final String data, int position, int viewType) {
        if (viewType == 0) {
            holder.setText(R.id.item_title, data);
        } else {
            holder.setText(R.id.item_title1, data);
        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.item_layout;
        }
        return R.layout.item_layout1;
    }

    @Override
    protected int getViewType(int position, String data) {
        if (position % 2 == 0) {
            return 0;
        }
        return 1;
    }
}

```

效果图   
![image](https://github.com/sooola/RecycleViewLoadmoreAdapter/blob/master/screenshots/listmul.png)

### 添加头部，尾部
##### 添加头部
```
TextView t1 = new TextView(HeaderActivity.this);
t1.setText("我是header");
mAdapter.addHeaderView(t1);
mAdapter.notifyDataSetChanged();
```

##### 添加尾部
```
TextView t2 = new TextView(HeaderActivity.this);
t2.setText("我是footer");
mAdapter.addFooterView(t2);
mAdapter.notifyDataSetChanged();
```

效果图   
![image](https://github.com/sooola/RecycleViewLoadmoreAdapter/blob/master/screenshots/head.png)

##### 加载更多
```
在加载更多回调中添加数据
mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
    @Override
    public void onLoadMore(boolean isReload) {

    }
});
```

##### 添加loading中布局
```
View emptyView = LayoutInflater.from(this).inflate(R.layout.empty_layout, (ViewGroup) mRecyclerView.getParent(), false);
mAdapter.setEmptyView(emptyView);
```

##### 添加没有数据布局
```
View nodataView = LayoutInflater.from(this).inflate(R.layout.nodata_layout, (ViewGroup) mRecyclerView.getParent(), false);
mAdapter.setNodataView(nodataView);
```
##### 提供的使用示例 
![image](https://github.com/sooola/RecycleViewLoadmoreAdapter/blob/master/screenshots/demo.png)

### 感谢
感谢大神们的付出，已上基于下面项目进行修改  
[https://github.com/hongyangAndroid/baseAdapter](https://github.com/hongyangAndroid/baseAdapter)    
[https://github.com/SheHuan/RecyclerViewAdapter](https://github.com/SheHuan/RecyclerViewAdapter) 
