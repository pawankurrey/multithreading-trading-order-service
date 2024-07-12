package com.tds.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tds.order.model.OrderModel;
import com.tds.order.model.OrderType;
import com.tds.order.service.OrderBookService;

@SpringBootApplication
public class TradingOrderServiceApplication implements CommandLineRunner {

	@Autowired
	private OrderBookService orderService;

	public static void main(String[] args) {
		SpringApplication.run(TradingOrderServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		orderService.placeOrder(new OrderModel("1", "INFY", 100, 150.00, OrderType.BUY));
		orderService.placeOrder(new OrderModel("2", "INFY", 50, 149.50, OrderType.SELL));
		orderService.placeOrder(new OrderModel("3", "INFY", 100, 150.00, OrderType.SELL));
		orderService.placeOrder(new OrderModel("4", "INFY", 100, 151.00, OrderType.BUY));
		orderService.shutdown();
	}

}
