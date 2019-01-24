# RecycleViewLoadmoreAdapter
一个加载更多的adapter，封装简化adapter代码，支持多item，参考大佬们的逻辑进行了部分修改


#使用

···
1.单一布局item的情况
继承CommonBaseAdapter
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
2.设置adapter
mAdapter = new CommonRefreshAdapter(this);
mRecyclerView.setAdapter(mAdapter);
···
