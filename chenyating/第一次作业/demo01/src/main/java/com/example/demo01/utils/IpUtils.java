package com.example.demo01.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;


//获取IP方法
public class IpUtils {
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
		}
		return "未知";
	}

}