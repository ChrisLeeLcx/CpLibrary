package com.lee.cplibrary.ui.fragment;

import android.os.Bundle;
import android.widget.TextView;

import com.lee.cplibrary.R;
import com.lee.cplibrary.base.BaseFragment;
import com.lee.cplibrary.event.OrderTabSelectedEvent;
import com.lee.cplibrary.ui.activity.OrderActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.lee.cplibrary.util.LogUtil;

/**
 * @author: ChrisLee
 * @time: 2018/11/27
 */

public class OrderFragment extends BaseFragment {
    private BuyCarOrderType orderType = BuyCarOrderType.TYPE_APPOINTMENT;

    public enum BuyCarOrderType {
        /**
         * 预约
         **/TYPE_APPOINTMENT,
        /**
         * 待支付
         **/TYPE_WAIT_PAY,
        /**
         * 已完成
         **/TYPE_FINISH,
        /**
         * 退款中
         **/TYPE_REFUND
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initView() {
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setText(orderType.name());
    }

    public static OrderFragment getInstance(BuyCarOrderType orderType) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderType", orderType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void receiveData(Bundle arguments) {
        orderType = (BuyCarOrderType) arguments.getSerializable("orderType");

    }

    @Override
    protected BaseFragment getSelfFragment() {
        return this;
    }

    @Override
    public void initData() {
        if (BuyCarOrderType.TYPE_APPOINTMENT == orderType) {//解决 第一个fragment第一次 初始化后接收不到OrderTabSelectedEvent消息问题
            request();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void comEvent(OrderTabSelectedEvent event) {//选择tab
        if (event.getBuyCarOrderType() == orderType) {//排除未选择的却已经初始化的Fragment收到信息后加载数据
            request();
        }
    }

    public void request() {
        LogUtil.i("", OrderActivity.class, "当前选择的是:" + orderType);
    }
}
