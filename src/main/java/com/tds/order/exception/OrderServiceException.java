package com.tds.order.exception;

public class OrderServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int errorCode;
	private String errorMsg;

	public OrderServiceException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public OrderServiceException(int errorCode, String errorMsg) {
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public OrderServiceException(int errorCode, String errorMsg, Exception e) {
		super(e);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
}
