package com.lee.cplibrary.event;

import com.lee.cplibrary.ui.fragment.OrderFragment;

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
