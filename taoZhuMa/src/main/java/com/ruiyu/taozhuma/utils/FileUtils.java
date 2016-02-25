package com.ruiyu.taozhuma.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class FileUtils {

	public static ArrayList<String> readFileByLine(File file) {
		ArrayList<String> result = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String tmpString = null;
			int line = 1;
			while ((tmpString = reader.readLine()) != null) {
				result.add(tmpString);
				line++;
			}
			reader.close();
		} catch (Exception e) {

		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;
	}

	/**
	 * 判断是否有SD卡,且可写
	 * @return
	 */
	public static boolean hasSDCard() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}
}
