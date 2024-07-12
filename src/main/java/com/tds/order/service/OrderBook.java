package com.tds.order.service;

import com.tds.order.exception.OrderServiceException;
import com.tds.order.model.OrderModel;

public interface OrderBook {

	public String placeOrder(OrderModel order) throws OrderServiceException;

}
