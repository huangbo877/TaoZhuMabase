package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *	购物车checkbox 状态 Model
 * @author Fu
 *
 */
public class PositionModel implements Serializable{
	public int parentPosition;//父position
	public int childPosition;//子position
	public Boolean status;//状态
	public String cid;
	
}
