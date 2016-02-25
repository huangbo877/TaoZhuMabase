package com.ruiyu.taozhuma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 常见问题类型Model
 * 
 * @author Fu
 * 
 */

public class TzmHelpCenterModel implements Serializable {
	public Integer fatherId;// 父级分类ID
	public String fatherName;// 父级分类名称
	public List<Son> son;
	public class Son {
		public Integer sonId;// 子级分类ID
		public String sonName;// 子级分类名称
	}

}
