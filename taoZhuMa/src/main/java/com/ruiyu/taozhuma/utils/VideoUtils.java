package com.ruiyu.taozhuma.utils;

public class VideoUtils {

	/**
	 * 获取优酷视频的m3u8地址
	 * @param url
	 * @return
	 */
	public static String getYouKu_m3u8(String url){
		//http://v.youku.com/v_show/id_XNjI1MzYwOTgw.html?f=19133846
		//http://v.youku.com/player/getRealM3U8/vid/XNjkxMzQwMTI0/video.m3u8
		int a=url.indexOf("id_");
		int b=url.indexOf(".html",a);
		String id=url.substring(a+3,b);
		return "http://v.youku.com/player/getRealM3U8/vid/"+id+"/video.m3u8";
	}
	
	/**
	 * 获取优酷视频的分享代码地址
	 * @param url
	 * @return
	 */
	public static String getYouKu_html(String url){
		//http://v.youku.com/v_show/id_XNjI1MzYwOTgw.html?f=19133846
		//http://v.youku.com/player/getRealM3U8/vid/XNjkxMzQwMTI0/video.m3u8
		int a=url.indexOf("id_");
		int b=url.indexOf(".html",a);
		String id=url.substring(a+3,b);
		//return "http://v.youku.com/player/getRealM3U8/vid/"+id+"/video.m3u8";
		return "<iframe height=95% width=100% src=\"http://player.youku.com/embed/"+id+"\" frameborder=0 allowfullscreen></iframe>";
	}
}
