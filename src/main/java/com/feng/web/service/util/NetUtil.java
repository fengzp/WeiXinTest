package com.feng.web.service.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NetUtil {
	
	private final static String charset = "UTF-8";
	private final static int readTimeout = 15 * 1000;
	private final static int connectTimeour = 5 * 1000;

	public static String httpGet(String reqUrl) {
		try {
			URL url = new URL(reqUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(readTimeout);
			conn.setConnectTimeout(connectTimeour);
			conn.setRequestMethod("GET");
			
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String resptxt = toString(conn.getInputStream());
				System.out.println(resptxt);
				return resptxt;
			} else {
				System.out.println("连接失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String httpPost(String reqUrl, String reqData) {
		HttpURLConnection conn = null;
		OutputStream os = null;

		try {
			conn = (HttpURLConnection) new URL(reqUrl).openConnection();
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setReadTimeout(readTimeout);
			conn.setConnectTimeout(connectTimeour);
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			os = conn.getOutputStream();
			os.write(reqData.getBytes(charset));
			System.out.println("req:" + reqData);

			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				String resptxt = toString(conn.getInputStream());
				System.out.println(resptxt);
				return resptxt;
			} else {
				System.out.println("连接失败");
			}
		} catch (Exception e) {
			System.out.println("连接异常:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
				}

			if (conn != null)
				conn.disconnect();
		}
		return null;
	}

	private static String toString(InputStream input) throws IOException {
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(input, charset));

			StringBuilder builder = new StringBuilder();
			do {
				String line = reader.readLine();

				if (line == null)
					break;

				builder.append(line);
			} while (true);

			return builder.toString();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
				}

			if (input != null)
				input.close();
		}
	}

	public static String HttpsRequest(String reqUrl, String requestMethod) {
		URL url;
		InputStream is;
		String resultData = "";

		try {
			url = new URL(reqUrl);
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			TrustManager[] tm = { new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {

				}

				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {

				}
			} };

			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, tm, null);
			con.setSSLSocketFactory(ctx.getSocketFactory());
			con.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

			con.setDoInput(true); // 允许输入流，即允许下载

			con.setDoOutput(false); // 允许输出流，即允许上传
			con.setUseCaches(false); // 不使用缓冲

			if (null != requestMethod && !requestMethod.equals("")) {
				con.setRequestMethod(requestMethod); // 使用指定的方式
			} else {
				con.setRequestMethod("GET"); // 使用get请求
			}
			is = con.getInputStream(); // 获取输入流，此时才真正建立链接
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bufferReader = new BufferedReader(isr);
			String inputLine;
			while ((inputLine = bufferReader.readLine()) != null) {
				if (!inputLine.equals("") && !inputLine.equals("null"))
					resultData += inputLine + "\n";
			}
			System.out.println(resultData);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

		return resultData;
	}

	public static File download(String fileName, String path, String method,
			String body) {
		if (fileName == null || path == null || method == null) {
			return null;
		}

		File file = null;
		HttpURLConnection conn = null;
		InputStream inputStream = null;
		FileOutputStream fileOut = null;
		try {
			URL url = new URL(path);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod(method);
			if (null != body) {
				OutputStream outputStream = conn.getOutputStream();
				outputStream.write(body.getBytes("UTF-8"));
				outputStream.close();
			}

			inputStream = conn.getInputStream();
			if (inputStream != null) {
				file = new File(fileName);
			} else {
				return file;
			}

			// 写入到文件
			fileOut = new FileOutputStream(file);
			if (fileOut != null) {
				int c = inputStream.read();
				while (c != -1) {
					fileOut.write(c);
					c = inputStream.read();
				}
			}
		} catch (Exception e) {
		} finally {
			if (conn != null) {
				conn.disconnect();
			}

			/*
			 * 必须关闭文件流 否则JDK运行时，文件被占用其他进程无法访问
			 */
			try {
				inputStream.close();
				fileOut.close();
			} catch (IOException execption) {
			}
		}
		return file;
	}
}
