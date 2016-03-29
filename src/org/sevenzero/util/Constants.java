package org.sevenzero.util;

import java.io.File;

import android.os.Environment;

/**
 * 
 * @author linger
 *
 * @since 2015-9-22
 *
 */
public class Constants {
	
	public static final String PREFIX = Environment.getExternalStorageDirectory().getPath();
	
	// 存储目录
	public static final String SAVE_PATH = PREFIX + File.separator + "QMEDICAL";
		
	// 下载文件目录
	public static final String TMP_PATH = SAVE_PATH + File.separator + "tmp";
	
	// ali
	public static final String ALI_path = SAVE_PATH + File.separator + "ali";
	
	public static final String REQUEST_URL = "http://192.168.1.141:8087";
	
//	public static final String REQUEST_URL = "http://192.168.6.173:8087";

}
