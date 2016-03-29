package org.sevenzero.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.sevenzero.task.AsyncUploadFileTask;

/**
 * 
 * @author sevenzero
   *
 * @since 2012-11-21
   *
 */
public class SendFileUtil {
	
	private static Logger log = Logger.getLogger(SendFileUtil.class.getSimpleName());

	private static final String BOUNDARY = "---------sevenzero"; // 数据分隔线
	private static final String MULTIPART_FORM_DATA = "multipart/form-data";

	/**
	 * 发送多个文件 
	 */
	public static void send() {
		List<String> list = new ArrayList<String>();
		list.add("/home/linger/pdf/jstl.pdf");
		list.add("/home/linger/linux.txt");
		
		try {
			String uri = "http://localhost:8087/webstudy/fileUpload2Svlt";
			uri = "http://localhost:8087/webstudy/multFileUploadSvlt";
			URL url = new URL(uri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);		// 允许输入
			conn.setDoOutput(true);		// 允许输出
			conn.setUseCaches(false);	// 不使用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
			
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  // 数据分割线
			
			final int len = list.size();
			for (int i = 0; i < len; i++) {
				String name = list.get(i);
				File file = new File(name);
				StringBuilder sb = new StringBuilder();
				sb.append("--");
				sb.append(BOUNDARY);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data; name=\"file" + i
						+ "\"; filename=\"" + file.getName() + "\"\r\n");
				sb.append("Content-Type: application/octet-stream \r\n\r\n");

				byte[] data = sb.toString().getBytes();
				out.write(data);
				DataInputStream in = new DataInputStream(new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
				in.close();
			}
			out.write(end_data);
			out.flush();
			out.close();
			
			
			// 定义BufferedReader输入流来读取URL的响应
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			
//			Map<String, List<String>> map = conn.getHeaderFields();
//			Set<String> sets = map.keySet();
//			for (String str : sets) {
//				System.out.print(str + ", ");
//				List<String> list2 = (List<String>) map.get(str);
//				for (String s : list2) {
//					System.out.print(s + ", ");
//				}
//				System.out.println();
//			}
//			
//			System.out.println(conn.getHeaderField("Set-Cookie"));
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			
		}
	}
	
	public static void send(String addr, String filename) throws FileNotFoundException {
		File file = new File(filename);
		send(addr, file);
	}
	
	/**
	 * 发送单个文件 
	 * 
	 * @param addr
	 * @param File
	 * @throws FileNotFoundException 
	 */
	public static void send(String addr, File file) throws FileNotFoundException {
		if (null == addr || "".equals(addr)) {
			throw new NullPointerException(addr + " is null.");
		}
		if (!file.exists()) {
			throw new FileNotFoundException(file.getPath() + " doesnot exist.");
		}
		OutputStream out = null;
		DataInputStream in = null;
		BufferedReader reader = null;
		try {
//			String uri = "http://localhost:8087/webstudy/fileUpload2Svlt";
//			uri = "http://localhost:8087/webstudy/multFileUploadSvlt";
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);		// 允许输入
			conn.setDoOutput(true);		// 允许输出
			conn.setUseCaches(false);	// 不使用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
			
			
			out = new DataOutputStream(conn.getOutputStream());
			
			byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();  // 数据分割线
			
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + file.getName()
					+ "\"; filename=\"" + file.getName() + "\"\r\n");
			sb.append("Content-Type: application/octet-stream \r\n\r\n");

			byte[] data = sb.toString().getBytes();
			out.write(data);
			in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
//			out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
//			in.close();
			
			out.write(end_data);
			out.flush();
//			out.close();
			
			
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
			
//			Map<String, List<String>> map = conn.getHeaderFields();
//			Set<String> sets = map.keySet();
//			for (String str : sets) {
//				System.out.print(str + ", ");
//				List<String> list2 = (List<String>) map.get(str);
//				for (String s : list2) {
//					System.out.print(s + ", ");
//				}
//				System.out.println();
//			}
//			
//			System.out.println(conn.getHeaderField("Set-Cookie"));
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (null != in) {
				try {
					in.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != reader) {
				try {
					reader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void sendForProgress(String addr, String filename, AsyncUploadFileTask task) throws FileNotFoundException {
		File file = new File(filename);
		sendForProgress(addr, file, task);
	}
	
	/**
	 * 发送单个文件 
	 * 显示实时进度条
	 * 
	 * @param addr
	 * @param File
	 * @throws FileNotFoundException 
	 */
	public static void sendForProgress(String addr, File file, AsyncUploadFileTask task) throws FileNotFoundException {
		if (null == addr || "".equals(addr)) {
			throw new NullPointerException(addr + " is null.");
		}
		if (!file.exists()) {
			throw new FileNotFoundException(file.getPath() + " doesnot exist.");
		}
		
		OutputStream out = null;
		DataInputStream in = null;
		BufferedReader reader = null;
		try {
//			String uri = "http://localhost:8087/webstudy/fileUpload2Svlt";
//			uri = "http://localhost:8087/webstudy/multFileUploadSvlt";
			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);		// 允许输入
			conn.setDoOutput(true);		// 允许输出
			conn.setUseCaches(false);	// 不使用Cache
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
			
			String endStr = "\r\n--" + BOUNDARY + "--\r\n";  // 数据分割线
			byte[] end_data = endStr.getBytes();  // 数据分割线
			
			StringBuilder sb = new StringBuilder();
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data; name=\"" + file.getName()
					+ "\"; filename=\"" + file.getName() + "\"\r\n");
			sb.append("Content-Type: application/octet-stream \r\n\r\n");
			
			byte[] data = sb.toString().getBytes();
			int fileLen = (int) file.length();
			int sum = sb.toString().length() + fileLen + endStr.length();
			log.info("数据总长度 " + sum);
			
			conn.setRequestProperty("Content-length", "" + sum);
			conn.setFixedLengthStreamingMode((int) sum);
			
			out = new DataOutputStream(conn.getOutputStream());
			out.write(data);
			
			int sendLen = 0;
			in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
				
				sendLen = sendLen + bytes;
				task.updateProgress(sendLen);
			}
			log.info("已发送 " + sendLen);
//			out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
//			in.close();
			
			out.write(end_data);
			out.flush();
//			out.close();
			
			
			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				log.info("上传结果值 " + line);
			}
			
//			Map<String, List<String>> map = conn.getHeaderFields();
//			Set<String> sets = map.keySet();
//			for (String str : sets) {
//				System.out.print(str + ", ");
//				List<String> list2 = (List<String>) map.get(str);
//				for (String s : list2) {
//					System.out.print(s + ", ");
//				}
//				System.out.println();
//			}
//			
//			System.out.println(conn.getHeaderField("Set-Cookie"));
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		finally {
			if (null != in) {
				try {
					in.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != reader) {
				try {
					reader.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @param args
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {

//		send();
		
		String addr = "http://localhost:8087/webstudy/fileUpload2Svlt";
		String filename = "/home/linger/Pictures/th.jpeg";
		send(addr, filename);

	}

}
