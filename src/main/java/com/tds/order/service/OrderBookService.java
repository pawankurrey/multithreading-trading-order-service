package com.tds.order.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.tds.order.exception.OrderServiceException;
import com.tds.order.model.OrderModel;
import com.tds.order.model.OrderType;

@Service
public class OrderBookService implements OrderBook {
	
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<OrderModel>> buyOrders;
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<OrderModel>> sellOrders;
	private final ExecutorService executorService;

	public OrderBookService() {
		buyOrders = new ConcurrentHashMap<>();
		sellOrders = new ConcurrentHashMap<>();
		executorService = Executors.newFixedThreadPool(10); // Pool with 10 threads
	}

	@Override
	public String placeOrder(OrderModel order) throws OrderServiceException {
		executorService.submit(() -> {
			if (order.getType() == OrderType.BUY) {
				buyOrders.computeIfAbsent(order.getSymbol(), k -> new ConcurrentLinkedQueue<>()).add(order);
			} else {
				sellOrders.computeIfAbsent(order.getSymbol(), k -> new ConcurrentLinkedQueue<>()).add(order);
			}
			matchOrders(order.getSymbol());
		});
		return "Order processed";
	}

	private void matchOrders(String symbol) {
		ConcurrentLinkedQueue<OrderModel> buys = buyOrders.get(symbol);
		ConcurrentLinkedQueue<OrderModel> sells = sellOrders.get(symbol);

		if (buys == null || sells == null)
			return;

		OrderModel buyOrder = buys.peek();
		OrderModel sellOrder = sells.peek();

		while (buyOrder != null && sellOrder != null) {
			if (buyOrder.getPrice() >= sellOrder.getPrice()) {
				int quantity = Math.min(buyOrder.getQuantity(), sellOrder.getQuantity());

				System.out.println("Match found: " + quantity + " of " + symbol + " at $" + sellOrder.getPrice());

				if (buyOrder.getQuantity() > sellOrder.getQuantity()) {
					buyOrder = new OrderModel(buyOrder.getId(), buyOrder.getSymbol(), buyOrder.getQuantity() - quantity,
							buyOrder.getPrice(), buyOrder.getType());
					buys.poll();
					buys.add(buyOrder);
					sells.poll();
				} else if (buyOrder.getQuantity() < sellOrder.getQuantity()) {
					sellOrder = new OrderModel(sellOrder.getId(), sellOrder.getSymbol(),
							sellOrder.getQuantity() - quantity, sellOrder.getPrice(), sellOrder.getType());
					sells.poll();
					sells.add(sellOrder);
					buys.poll();
				} else {
					buys.poll();
					sells.poll();
				}

				buyOrder = buys.peek();
				sellOrder = sells.peek();
			} else {
				break;
			}
		}
	}

	public void shutdown() {
		executorService.shutdown();
	}

}
