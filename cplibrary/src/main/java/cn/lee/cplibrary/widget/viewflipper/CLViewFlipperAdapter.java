package cn.lee.cplibrary.widget.viewflipper;

import android.view.View;

/**
 * function:模仿BaseAdapter自定义的ViewFlipper的Adapter，用来填充数据，
 * 待完成：类似于ListView的缓存机制还没有做
 * Created by ChrisLee on 2018/4/8.
 */

public abstract class CLViewFlipperAdapter {

    protected abstract int getCount();

    protected abstract String getItemId(int position);

    protected abstract Object getItem(int position);//获取位于position的数据

    protected abstract View getView(int position, View convertView);//获取位于position的Item view

    protected abstract CLViewFlipper getMyViewFlipper();//获取绑定的ViewFlipper

    public void notifyDataSetChanged() {//刷新界面
        final CLViewFlipper flipper = getMyViewFlipper();
        flipper.removeAllViews();
        for (int i = 0; i < getCount(); i++) {
            final View view = getView(i, null);
            final int finalI = i;
            flipper.addView(view);
            if (flipper.getOnItemClickListener() != null) {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flipper.getOnItemClickListener().onItemClick(flipper, view, finalI, getItemId(finalI));
                    }
                });
            }
        }
    }
}
