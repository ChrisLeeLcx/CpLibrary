package com.lee.demo.event;

import com.lee.demo.ui.fragment.OrderFragment;

/**
 * @author: ChrisLee
 * @time: 2018/11/27
 */

public class OrderTabSelectedEvent {
    private OrderFragment.BuyCarOrderType buyCarOrderType;

    public OrderTabSelectedEvent(OrderFragment.BuyCarOrderType buyCarOrderType) {
        this.buyCarOrderType = buyCarOrderType;
    }


    public OrderFragment.BuyCarOrderType getBuyCarOrderType() {
        return buyCarOrderType;
    }

}
