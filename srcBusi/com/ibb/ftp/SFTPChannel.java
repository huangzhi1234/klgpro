package com.ibb.ftp;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SFTPChannel {
	
	Session session = null;
    Channel channel = null;
    
    static Properties props;
	static String host;
	static Integer port;
	static String username;
	static String password;

    private static final Logger LOG = Logger.getLogger(SFTPChannel.class.getName());
    
    static{
		props = new Properties();	
		try {
			props.load(SFTPChannel.class.getClassLoader().getResourceAsStream("sftp.properties"));
			host = props.getProperty("host");
			port = Integer.valueOf(props.getProperty("port"));
			username = props.getProperty("username");
			password = props.getProperty("password");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public ChannelSftp getChannel() throws JSchException {
    	       
        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(username, host, port); // 根据用户名，主机ip，端口获取一个Session对象
        LOG.debug("Session created.");
        if (password != null) {
            session.setPassword(password); // 设置密码
        }
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config); // 为Session对象设置properties
        session.setTimeout(60000); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        LOG.debug("Session connected.");

        LOG.debug("Opening Channel.");
        channel = session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        LOG.debug("Connected successfully to ftpHost = " + host + ",as ftpUserName = " + username
                + ", returning: " + channel);
       
        return (ChannelSftp) channel;
    }

    public void closeChannel() throws Exception {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }	
}