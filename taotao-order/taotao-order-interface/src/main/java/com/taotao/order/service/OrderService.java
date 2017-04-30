package com.taotao.order.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.order.pojo.OrderInfo;

/**
 * Created by JesonLee
 * on 2017/4/29.
 */
public interface OrderService {
    TaotaoResult createOrder(OrderInfo orderInfo);

}
