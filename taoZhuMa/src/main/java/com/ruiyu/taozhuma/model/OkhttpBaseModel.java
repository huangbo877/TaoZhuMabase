package com.ruiyu.taozhuma.model;

/**
 * 返回的基本模型
 * @author 林尧
 * 2016-1-11
 * @param <T>
 */
public class OkhttpBaseModel<T> {
	public boolean success;
	public String error_msg;
	public T result;

}
