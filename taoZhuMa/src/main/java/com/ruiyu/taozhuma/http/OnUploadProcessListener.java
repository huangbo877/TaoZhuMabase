package com.ruiyu.taozhuma.http;

public interface OnUploadProcessListener {
	/**
	 * 上传响应
	 * @param responseCode
	 * @param message
	 */
	void onUploadDone(int responseCode, String message);
	/**
	 * 上传中
	 * @param uploadSize
	 */
	void onUploadProcess(int uploadSize);
	/**
	 * 准备上传
	 * @param fileSize
	 */
	void initUpload(int fileSize);
}
