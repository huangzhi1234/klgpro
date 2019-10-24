package com.ibb.utils;

import java.io.File;
import java.io.IOException;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class FileUtil {

	/**
	 * 文件类型转换
	 * @param multfile
	 * @return
	 * @throws IOException
	 */
	public static File multipartToFile(CommonsMultipartFile multfile) throws IOException {  
        DiskFileItem fi = (DiskFileItem) multfile.getFileItem();  
        File file = fi.getStoreLocation();  
     // 手动创建临时文件
 		if (file.length() < 2048) {
 			File tmpFile = new File(System.getProperty("java.io .tmpdir") + System.getProperty("file.separator") + file.getName());
 			multfile.transferTo(tmpFile);
 			return tmpFile;
 		}
        return file;  
    }
	
	/**
	 * 获取文件的后缀名
	 */
	public static String getPostfix(String inputFilePath) 
	{
		String[] p = inputFilePath.split("\\.");
		if (p.length > 0) 
		{
			return p[p.length - 1];
		} 
		else 
		{
			return null;
		}
	}
}
