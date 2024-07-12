package com.tds.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OrderModel {
	private String id;
	private String symbol;
	private int quantity;
	private double price;
	private OrderType type;

}
