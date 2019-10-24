package com.ibb.ftp;

import java.io.IOException;
import java.util.Properties;

public class LoadPrefix {

    public static Properties props;
	public static String prefix;

    static{
		props = new Properties();	
		try {
			props.load(SFTPChannel.class.getClassLoader().getResourceAsStream("uploadFile.properties"));
			prefix = props.getProperty("prefix");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}