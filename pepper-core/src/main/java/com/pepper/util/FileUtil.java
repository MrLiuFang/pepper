package com.pepper.util;

import java.util.UUID;

/**
 * 
 * @author mrliu
 *
 */
public class FileUtil {
	public static final String getRealFileName(final String fileName) {
		if (fileName.indexOf("/") != -1) {
			return fileName.substring(fileName.lastIndexOf("/") + 1);
		}
		if (fileName.indexOf("\\") != -1) {
			return fileName.substring(fileName.lastIndexOf("\\") + 1);
		}
		return UUID.randomUUID().toString();
	}
}
