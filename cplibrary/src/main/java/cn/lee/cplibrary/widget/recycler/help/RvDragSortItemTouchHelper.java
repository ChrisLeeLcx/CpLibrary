package cn.lee.cplibrary.widget.recycler.help;

import android.app.Activity;
import android.app.Service;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 让RecyclerView具有可拖拽Item功能的ItemTouchHelper
 * 使用方式：只需要给recyclerView添加一个ItemTouchHelper对象就行，如下：
 * mItemTouchHelper = new RvDragSortItemTouchHelper(new RvDragSortItemTouchHelper.DragSortCallback() );
 * mItemTouchHelper.attachToRecyclerView(mRecyclerView);
 *
 * @author: ChrisLee
 * @time: 2018/8/17
 */

public class RvDragSortItemTouchHelper extends ItemTouchHelper {


    static Activity mActivity;
    private static Integer[] pois;
    private static List mDatas;
    private static RecyclerView.Adapter mAdapter;
//    public OnItemMovedListener mMovedListener;
    /**
     * 拖拽Item交换顺序的监听----如果设置了次监听说明改变了数据源和RecyclerView的显示问题
     */
    public interface OnItemMovedListener {
        void onItemMoved(int fromPosition, int toPosition);
    }

    private RvDragSortItemTouchHelper(Callback callback) {
        super(callback);
    }
    /**
     * @param recyclerViewAttached 设置ItemTouchHelper的即需要拖动排序的RecyclerView
     * @param adapter   RecyclerView的adapter
     * @param datas    RecyclerView的全部数据源
     * @param position 需要禁止拖拽的Item的position,每个position 必须 小于datas.size()
     */
    public static RvDragSortItemTouchHelper getInstance(final Activity activity, RecyclerView recyclerViewAttached, RecyclerView.Adapter adapter, List datas, final Integer... position) {
        pois = position;
        mDatas = datas;
        mAdapter = adapter;
        mActivity = activity;
        DragSortCallback callback = new DragSortCallback();
        RvDragSortItemTouchHelper helper = new RvDragSortItemTouchHelper(callback);
        setDragUnEnabledPosition(recyclerViewAttached, helper, pois);
        return helper;
    }
    public static RvDragSortItemTouchHelper getInstance(final Activity activity, RecyclerView recyclerViewAttached, RecyclerView.Adapter adapter, List datas,@Nullable OnItemMovedListener mMovedListener, final Integer... position) {
        pois = position;
        mDatas = datas;
        mAdapter = adapter;
        mActivity = activity;
        DragSortCallback callback = new DragSortCallback(mMovedListener);
        RvDragSortItemTouchHelper helper = new RvDragSortItemTouchHelper(callback);
        setDragUnEnabledPosition(recyclerViewAttached, helper, pois);
        return helper;
    }
    private static void setDragUnEnabledPosition(RecyclerView recyclerViewAttached, final RvDragSortItemTouchHelper helper, final Integer... position) {
        //没有禁止拖拽的item
        //position的所有元素必须在datas的长度范围内
        if (position == null || position.length <= 0) {
           return;
        }
        recyclerViewAttached.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerViewAttached) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
            }

            @Override
            public void onItemLongClick(RecyclerView.ViewHolder vh) {
                List<Integer> list = Arrays.asList(position);
                //判断被拖拽的是是否禁止的position，如果不是则执行拖拽
                if (!list.contains(vh.getLayoutPosition())) {
                    helper.startDrag(vh);
                    //获取系统震动服务
                    Vibrator vib = (Vibrator) mActivity.getSystemService(Service.VIBRATOR_SERVICE);//震动70毫秒
                    vib.vibrate(70);

                }
            }
        });
    }

    public static class DragSortCallback extends Callback {
        private OnItemMovedListener mMovedListener;

        public DragSortCallback() {

        }
        public DragSortCallback(OnItemMovedListener mMovedListener) {
            this.mMovedListener = mMovedListener;
        }

        /**
         * 是否处理滑动事件 以及拖拽和滑动的方向 如果是列表类型的RecyclerView的只存在UP和DOWN，
         * 如果是网格类RecyclerView则还应该多有LEFT和RIGHT
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                final int swipeFlags = 0;
                return makeMovementFlags(dragFlags, swipeFlags);
            } else {
                final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                final int swipeFlags = 0;
//                    final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        }

        //拖动的时候不断回调的方法，在这里我们需要将正在拖拽的item和集合的item进行交换元素，然后在通知适配器更新数据
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(mDatas, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(mDatas, i, i - 1);
                }
            }
            if (mMovedListener!=null) {
                mMovedListener.onItemMoved(fromPosition, toPosition);
            } else {
                mAdapter.notifyItemMoved(fromPosition, toPosition);//直接换位置
            }
            return true;
        }

        /**
         * 滑动 后调用 ，用于滑动删除Item
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //是替换后调用的方法，可以不用管
//                int position = viewHolder.getAdapterPosition();
//                myAdapter.notifyItemRemoved(position);
//                datas.remove(position);\

        }

        /**
         * 实际功能中有可能存在，排头前两个的不需改变它的顺序，即有些item允许拖拽，有些则不允许，所以我们需要重写isLongPressDragEnabled（）
         * 设置不允许长按拖拽,
         * 然后在重写RecycleView的长按监听（这个要自己写个接口去实现），在返回的长按方法中判断是否为不可拖拽的item，
         * 若不是，则调用ItemTouchHelper的startDrag（）方法
         * 重写拖拽可用
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return pois == null || pois.length <= 0;
        }
        //在拖拽的时候将被拖拽的Item高亮，这样用户体验要好很多，所以我们要重写CallBack对象中的onSelectedChanged（）和clearView（）方法

        /**
         * 长按选中Item的时候开始调用,选中的时候设置高亮背景色
         * Item被选中时候回调
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原,在完成的时候移除高亮背景色
         * 用户操作完毕或者动画完毕后会被调用
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
            if (mMovedListener!=null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 拖拽时候检测 禁止拖拽功能的position 是否超过数据的size
     */
    private void  checkUnDragPois(final Integer... position){
        if (position != null || position.length > 0) {
            for (int i = 0; i < position.length; i++) {
                if (position[i] >= mDatas.size()) {
                    throw new IllegalArgumentException("参数违规，position必须小于datas.size(),而"+position[i]+"不小于"+ mDatas.size());
                }
            }
        }
    }


}
