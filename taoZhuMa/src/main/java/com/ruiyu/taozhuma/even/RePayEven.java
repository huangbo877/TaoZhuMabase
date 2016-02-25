package com.ruiyu.taozhuma.even;

public class RePayEven {
	private String orderNumber;
	private double orderPrice;
	private String isWallet;

	public RePayEven(String orderNumber, double orderPrice,String isWallet) {
		this.orderNumber = orderNumber;
		this.orderPrice = orderPrice;
		this.isWallet = isWallet;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public double getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(double orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getIsWallet() {
		return isWallet;
	}

	public void setIsWallet(String isWallet) {
		this.isWallet = isWallet;
	}

}
