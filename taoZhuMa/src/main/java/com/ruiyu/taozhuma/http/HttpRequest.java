package com.ruiyu.taozhuma.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;





import com.ruiyu.taozhuma.utils.LogUtil;

import android.util.Log;

/***
 * 与服务器处理Http请求的包装
 * 
 * @author Toby
 * 
 */
public class HttpRequest {

	/**
	 * 通过Post方式发送数所给服务器
	 * 
	 * @param path
	 *            请求的路径
	 * @param params
	 *            参数值
	 * @return
	 * @throws Exception
	 */
	public static String request(String path,Map<String, String> params)
			throws Exception {

		StringBuilder sb = new StringBuilder();
		StringBuilder result = new StringBuilder();
		
		
		// 拼接请求的参数
		if (params != null && params.size() != 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				if (null != entry.getValue()) {
					sb.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), "utf-8"));
					sb.append("&");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		LogUtil.Log("path:" + path);
		LogUtil.Log("请求的数据包:" + sb.toString());

		// entity为请求体部分内容//如果有中文则以UTF-8编码为username=%E4%B8%AD%E5%9B%BD&password=123
		byte[] entity = sb.toString().getBytes();
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求超时的时间
		conn.setConnectTimeout(5000);
		// 以POST方式发送请求体
		conn.setRequestMethod("POST");
		// 要向外输出数据，要设置这个
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", entity.length + "");

		OutputStream os = conn.getOutputStream();
		os.write(entity);
		// 得到输入流
		InputStream inputStream = conn.getInputStream();

		if (conn.getResponseCode() == 200) {// 表明请求数据成功
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String temp = null;
			while ((temp = reader.readLine()) != null) {
				result.append(temp);				
			}
		}

		LogUtil.Log("result:" + result);

		os.close();
		inputStream.close();
		// 最后关闭连接
		conn.disconnect();

		return result.toString();

	}

	/**
	 * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws IOException
	 */
	public static String uploadFile(String actionUrl,
			Map<String, String> params, Map<String, File> files)
			throws IOException {

		StringBuilder sbResult = new StringBuilder();
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(50 * 1000); // 缓存的最长时间
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		LogUtil.Log("request", "path:" + actionUrl);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		if (params != null && params.size() != 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getKey() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(entry.getValue());
				sb.append(LINEND);
			}
		}
		LogUtil.Log("request", "请求的数据包:" + sb.toString());

		DataOutputStream outStream = new DataOutputStream(
				conn.getOutputStream());
		outStream.write(sb.toString().getBytes());
		InputStream in = null;
		// 发送文件数据
		if (files != null) {
			for (Map.Entry<String, File> file : files.entrySet()) {
				StringBuilder sb1 = new StringBuilder();
				sb1.append(PREFIX);
				sb1.append(BOUNDARY);
				sb1.append(LINEND);
				sb1.append("Content-Disposition: form-data; name=\"attachments\"; filename=\""
						+ file.getKey() + "\"" + LINEND);
				sb1.append("Content-Type: application/octet-stream; charset="
						+ CHARSET + LINEND);
				sb1.append(LINEND);
				outStream.write(sb1.toString().getBytes());

				InputStream is = new FileInputStream(file.getValue());
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}

				is.close();
				outStream.write(LINEND.getBytes());
			}

			// 请求结束标志
			byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
			outStream.write(end_data);
			outStream.flush();
			// 得到响应码
			int res = conn.getResponseCode();
			// 得到输入流
			InputStream inputStream = conn.getInputStream();

			if (res == 200) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(inputStream));
				String temp = null;
				while ((temp = reader.readLine()) != null) {
					sbResult.append(temp);
					LogUtil.Log("response", "temp:" + temp);
				}

			}
			outStream.close();
			conn.disconnect();
		}
		int tep = sbResult.toString().indexOf("{");
		LogUtil.Log("response", "result:" + sbResult.toString().substring(tep));

		return sbResult.toString().substring(tep);
	}

}
