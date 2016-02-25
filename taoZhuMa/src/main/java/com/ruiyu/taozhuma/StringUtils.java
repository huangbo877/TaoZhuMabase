package com.ruiyu.taozhuma;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

/**
 * 处理字符串相关的工具类
 * 
 * @author Toby
 * 
 */
public class StringUtils {

	public StringUtils() {
		super();
	}// 传入的CharSequence是String的接口，同样StringBuffer这些也是，可适用这里。Sequence的英语是序列的意思。

	/**
	 * 追加0
	 * 
	 * @param num
	 * @return
	 */
	public static String pad(int num) {
		if (num >= 10) {
			return String.valueOf(num);
		} else {
			return "0" + String.valueOf(num);
		}
	}

	/***
	 * 判断字符串是否为空白
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isBlank(CharSequence cs) {
		// 标记字符长度，
		int strLen;
		// 字符串不存在或者长度为0
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			// 判断空格，回车，换行等，如果有一个不是上述字符，就返回false
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串不为空白
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(CharSequence cs) {
		return !StringUtils.isBlank(cs);
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * 判断字符不为空
	 * 
	 * @param cs
	 * @return
	 */
	public static boolean isNotEmpty(CharSequence cs) {
		return !StringUtils.isEmpty(cs);
	}

	// 直接采用的是字符串的去空格方法，多加了一个判空
	public static String trim(String str) {
		return str == null ? null : str.trim();
	}

	/**
	 * 获取文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileNameByFilePath(String path) {
		if (null == path)
			return "";
		int last = path.lastIndexOf("/");
		if (last < 0) {
			return path;
		} else {
			return path.substring(last + 1);
		}
	}

	/**
	 * 拼接ArrayList<String>
	 * 
	 * @param list
	 *            the list
	 * @param separator
	 *            the separator
	 * @return the string
	 */
	public static String join(List<String> list, String separator) {
		if (list == null || list.size() <= 0) {
			return null;
		}
		StringBuffer buf = new StringBuffer(256);
		String first = list.get(0);
		if (first != null) {
			buf.append(first);
		}
		int count = list.size();
		for (int i = 1; i < count; i++) {
			buf.append(separator);
			buf.append(list.get(i));
		}
		return buf.toString();
	}

	/**
	 * 截取图片文件名
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static String subImageUrl(String imageUrl) {
		int index = imageUrl.lastIndexOf("/");
		String str = imageUrl.substring(index + 1);

		return str;
	}

	/**
	 * 截取文件后缀名
	 * 
	 * @param imageUrl
	 * @return
	 */
	public static String getFileSuffix(String imageUrl) {

		int index = imageUrl.lastIndexOf(".");
		String str = imageUrl.substring(index + 1);

		return str.toUpperCase();
	}

	/**
	 * 判断字符串是否为email地址
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmailStr(CharSequence str) {
		String srePattern = "^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";
		Pattern p = Pattern.compile(srePattern);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNum(String str) {
		return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
	}

	/**
	 * 判断登录密码的验证规则
	 * 
	 * @param str
	 * @return
	 */
	public static boolean ischeckpass(String str) {
		return str.matches("^[a-zA-Z0-9]{0,6}$|^[a-zA-Z0-9]{7,}$|^[a-zA-Z]{10}$|^[0-9]{6}$");
	}
	/**
	 * 过滤Html字符,空格字符(即：&nbsp;)
	 * 
	 * @param htmlStr
	 * @return
	 */
	public static String removeHTMLTag(String htmlStr) {
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		java.util.regex.Pattern p_html1;
		java.util.regex.Matcher m_html1;
		java.util.regex.Pattern p_html2;
		java.util.regex.Matcher m_html2;

		try {
			String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
			String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
			String regEx_html1 = "<[^>]+";
			String regEx_html2 = "&\\w+;";// 匹配&nbsp;

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
			m_html1 = p_html1.matcher(htmlStr);
			htmlStr = m_html1.replaceAll(""); // 过滤html标签

			p_html2 = Pattern.compile(regEx_html2, Pattern.CASE_INSENSITIVE);
			m_html2 = p_html2.matcher(htmlStr);
			htmlStr = m_html2.replaceAll(""); // 过滤html标签
			textStr = htmlStr;

		} catch (Exception e) {
			LogUtil.Log("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}

	/**
	 * 判断是否为手机号码
	 * 
	 * @param mobiles
	 * @return [0-9]{5,9}
	 */
	public static boolean isMobileNO(String mobiles) {
		if (mobiles.length() != 11) {
			return false;
		}

		boolean flag = true;
		try {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0,1-9]))\\d{8}$");
			Matcher m = p.matcher(mobiles);
			flag = m.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}
