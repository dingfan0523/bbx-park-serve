
package com.cgnpc.bbxpark.common.utils;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;
import java.util.regex.Pattern;


public final class WebUtil {

	/**
	 * LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

	/**
	 * 默认构造函数.
	 */
	private WebUtil() {

	}

	public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

	public static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

	/**
	 * 获取真实ip
	 *
	 * @param request 请求
	 * @return 真实ip
	 */
	public static String getIpFromRequest(HttpServletRequest request) {
		String ip = "";
		if (StringUtils.isNotBlank(ip = request.getHeader("X-Real-IP"))) {
			return ip;
		}
		boolean found = false;
		if (StringUtils.isNotBlank((ip = request.getHeader("x-forwarded-for")))) {
			StringTokenizer tokenizer = new StringTokenizer(ip, ",");
			while (tokenizer.hasMoreTokens()) {
				ip = tokenizer.nextToken().trim();
				if (isIPv4Valid(ip) && !isIPv4Private(ip)) {
					found = true;
					break;
				}
			}
		}

		if (!found) {
			ip = request.getRemoteAddr();
		}
		if (StringUtils.equals(ip, "0:0:0:0:0:0:0:1")) {
			LOGGER.error("未能解析IP, 可能原因[nginx未配置,传递真实IP]");
		}
		return ip;
	}

	/**
	 * 将long类型地址信息转换标准ip地址
	 * @param longIp long类型地址信息
	 * @return 地址信息
	 */
	public static String longToIpV4(long longIp) {
		int octet3 = (int) ((longIp >> 24) % 256);
		int octet2 = (int) ((longIp >> 16) % 256);
		int octet1 = (int) ((longIp >> 8) % 256);
		int octet0 = (int) ((longIp) % 256);
		return octet3 + "." + octet2 + "." + octet1 + "." + octet0;
	}

	/**
	 * 将ip地址转换long类型
	 * @param ip 地址信息
	 * @return long类型地址信息
	 */
	public static long ipV4ToLong(String ip) {
		String[] octets = ip.split("\\.");
		return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)
			+ (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);
	}

	/**
	 * 是否内网地址
	 * @param ip 地址信息
	 * @return 是否内网地址
	 */
	public static boolean isIPv4Private(String ip) {
		long longIp = ipV4ToLong(ip);
		return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))
			|| (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))
			|| longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");
	}

	/**
	 * 是否 ipv4地址
	 *
	 * @param ip 地址信息
	 * @return 是否 ipv4地址
	 */
	public static boolean isIPv4Valid(String ip) {
		return pattern.matcher(ip).matches();
	}

	public static UserAgent getAgent(HttpServletRequest request) {
		String agent = request.getHeader("user-agent");
		if (StringUtils.isNotBlank(agent)) {

			UserAgent parse = UserAgentUtil.parse(agent);
			return parse;
		}
		return null;
	}

	/**
	 * 获取 request 请求体
	 *
	 * @param servletInputStream servletInputStream
	 * @return body
	 */
	public static String getRequestBody(ServletInputStream servletInputStream) {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(servletInputStream, StandardCharsets.UTF_8));
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (servletInputStream != null) {
				try {
					servletInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

}
