package com.ruiyu.taozhuma.even;

import java.util.HashMap;

public class AgencyStockEven {
	private Integer position;
	private HashMap<Integer, Integer> number;

	public AgencyStockEven(Integer position, HashMap<Integer, Integer> number) {
		this.position = position;
		this.number = number;
	}

	public Integer getFatherPosition() {
		return position;
	}

	public HashMap<Integer, Integer> getNumber() {
		return number;
	}
}
