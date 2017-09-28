package com.jeeplus.common.utils;

import java.util.UUID;

/**
 * 用于生成32位UUID的工具类
 * @author luoxuelin
 *
 */
public final class UUIDUtil {
	/**
	 * <p>随机生成UUID字符串</p>
	 * @return	UUID字符串
	 */
	public static final String randomUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	public static void main(String[] args) {
		System.out.println(randomUUID());
	}
}
