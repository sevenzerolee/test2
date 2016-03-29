package org.sevenzero.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.UUID;
import java.util.logging.Logger;

import android.util.Log;

/**
 * 
 * @author sevenzero
 *
 * @since 2012-6-8
 *
 */
public class Util {
	
	static Logger log = Logger.getLogger(Util.class.getName());
	
	/**
	 * Check empty or null
	 * @param str
	 * @return true if empty or null; <br/> else false;
	 */
	public static boolean checkEmptyOrNull(String str) {
		if (null == str || "".equals(str)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check Object whether is null
	 * @param obj
	 * @return true if null; <br/> else false;
	 */
	public static boolean checkNull(Object obj) {
		if (null == obj) {
			return true;
		}
		
		return false;
	}
	
	public static String replace(String str) {
		if (checkEmptyOrNull(str)) {
			return null;
		}		
		
		return str.replaceAll(":|/", "");
	}
	
	public static String convertFileSize(String size) {
		log.info("Util-Size-" + size);
		if (checkEmptyOrNull(size)) {
			return null;
		}
		
		long fileSize = 0L;
		try {
			fileSize = Long.parseLong(size);
		}
		catch (Exception ex) {
			log.info("Util-convert file size exception.");
			ex.printStackTrace();
			return null;
		}
		
		String unit = "", result = "-";
		float fSize = fileSize * 1.0F;
		float tmp = 0.0F;

		if ((tmp = fSize / (1024 * 1024 * 1024)) > 0.5) {
			unit = "GB";
		}
		else if ((tmp = fSize / (1024 * 1024)) > 0.5) {
			unit = "MB";
		}
		else if ((tmp = fSize / (1024)) > 0.5) {
			unit = "KB";
		}
		else {
			unit = "B";
			tmp = fileSize;
		}
		log.info("Util-" + tmp);
		
		if (String.valueOf(tmp).endsWith("0")) {
			NumberFormat nf = NumberFormat.getIntegerInstance();
			result = nf.format(tmp) + unit;
		}
		else {
			DecimalFormat df = new DecimalFormat("0.00");
			result = df.format(tmp) + unit;
		}
		
		return result;
	}
	
	public static String getStringFromInputStream(InputStream input) {
		if (null == input) {
			return null;
		}
		
		BufferedReader br = null;
		try {			
			br = new BufferedReader(new InputStreamReader(input));
			StringBuffer sb = new StringBuffer(2048);
			String line = null;
			
			while (null != br && null != (line = br.readLine())) {
				sb.append(line).append("\n");
			}
			
			return sb.toString();
		}
		catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		finally {
			if (null != br) {
				try {
					br.close();
				}
				catch (IOException e) {
				}
			}
		}
	}
	
	public static byte[] streamToByte(InputStream is) {
		if (null == is) {
			return null;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] data = null;
		int ch;
		try {
			while ((ch = is.read()) != -1) {
				baos.write(ch);
			}
			data = baos.toByteArray();
		}
		catch (IOException e) {
			e.printStackTrace();
			data = null;
		}
		
		return data;
	}
	
	public static InputStream byteToStream(byte[] data) {
		if (null == data) {
			return null;
		}
		
		return new ByteArrayInputStream(data);
	}
	
	public static final String ENCODING = "UTF-8";
	
	/**
	 * 
	 * @param param
	 * @return null if param is empty or null; <br/>  the translated string;
	 * 
	 */
	public static String encodeParam(String param) {
		if (checkEmptyOrNull(param)) {
			return null;
		}
		
		String temp = null;
		try {
			temp = URLEncoder.encode(param, ENCODING);
		}
		catch (UnsupportedEncodingException e) {
			temp = URLEncoder.encode(param);
		}
		
		return temp;
	}
	
	/**
	 * md5
	 * @param data
	 * @return null if exception occur; <br/>
	 */
	public static String md5(String data) {
		byte[] bytes = data.getBytes();
		StringBuilder sb = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(bytes);			
			byte[] md5Bytes = md.digest();
			
			sb = new StringBuilder();
			final int length = md5Bytes.length;
			for (int i=0; i<length; i++) {
				int val = ((int) md5Bytes[i]) & 0xFF;
				if (val < 16) {
					sb.append(0);
				}
				sb.append(Integer.toHexString(val));
			}
			
			return sb.toString();
		}
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	public static String format(int n) {
		DecimalFormat df = new DecimalFormat("0000");
		return df.format(n);
	}
	
	public static void main(String[] args) {
		log.info(getUuid());
		System.out.println(format(-20));
	}

	static boolean debug = true;
	
	public static void printLog(String tag, String msg) {
		if (debug) {
			if (null != tag) {
				Log.d(tag, String.valueOf(msg));
			}
		}
	}
	
}
