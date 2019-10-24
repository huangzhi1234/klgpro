package com.ibb.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Properties;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

/**
 * 
 * @author sujiacheng
 *
 */
public class FTPChannel {

	static Properties props;
	static String host;
	static Integer port;
	static String username;
	static String password;
	
	static{
		props = new Properties();	
		try {
			props.load(FTPChannel.class.getClassLoader().getResourceAsStream("sftp.properties"));
			host = props.getProperty("host");
			port = Integer.valueOf(props.getProperty("port"));
			username = props.getProperty("username");
			password = props.getProperty("password");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 创建目录
	 * @param path 目录路径
	 */
	public static void makeDirectory(String path){
		 FTPClient ftpClient = new FTPClient();
		 try {
			 ftpClient.connect(host);       
	         ftpClient.login(username, password); 
	         if(!ftpClient.changeWorkingDirectory(path)){
            	String[] dirs = path.split("/");
            	for(int i=1; dirs!=null&&i<dirs.length; i++) {  
                    if(!ftpClient.changeWorkingDirectory(dirs[i])) {  
                        ftpClient.makeDirectory(dirs[i]);
                    }  
                    ftpClient.changeWorkingDirectory(dirs[i]);
                }
            }
		 } catch (IOException e) {       
            e.printStackTrace();       
            throw new RuntimeException("FTP客户端出错！", e);       
        } finally {       
            try {       
                ftpClient.disconnect();       
            } catch (IOException e) {       
                e.printStackTrace();       
                throw new RuntimeException("关闭FTP连接发生异常！", e);       
            }       
        }
	}
	
	/**
	 * 上传文件
	 * @param file 文件
	 * @param path 目录路径
	 * @param fileName 保存的文件名称
	 */
	public static void uploadFile(File file, String path, String fileName) {
        FTPClient ftpClient = new FTPClient();       
        FileInputStream fis = null;       
     
        try {
            ftpClient.connect(host);       
            ftpClient.login(username, password);       
            fis = new FileInputStream(file);       
            //设置上传目录       
            if(!ftpClient.changeWorkingDirectory(path)){
            	String[] dirs = path.split("/");
            	for(int i=1; dirs!=null&&i<dirs.length; i++) {  
                    if(!ftpClient.changeWorkingDirectory(dirs[i])) {  
                        ftpClient.makeDirectory(dirs[i]);
                    }  
                    ftpClient.changeWorkingDirectory(dirs[i]);
                }
            }
            ftpClient.changeWorkingDirectory(path);       
            ftpClient.setBufferSize(1024);       
            ftpClient.setControlEncoding("UTF-8");       
            //设置文件类型（二进制）       
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);       
            ftpClient.storeFile(fileName, fis);      
            fis.close();
        } catch (IOException e) {       
            e.printStackTrace();       
            throw new RuntimeException("FTP客户端出错！", e);       
        } finally {       
            try {       
                ftpClient.disconnect();       
            } catch (IOException e) {       
                e.printStackTrace();       
                throw new RuntimeException("关闭FTP连接发生异常！", e);       
            }       
        }       
    }
	
	/**
	 * 下载文件
	 * @param filePath 要下载的文件路径
	 * @param destPath 中间（临时）文件路径
	 * @param fileName 下载到浏览器上的文件的名称（可以随意定）
	 * @param response HttpServletResponse
	 */
	public static void downloadFile(String filePath, String destPath, String fileName, HttpServletResponse response) {       
        FTPClient ftpClient = new FTPClient();       
        FileOutputStream fos = null;       
        String filetype = getPostfix(destPath);
        try {       
        	ftpClient.connect(host);       
            ftpClient.login(username, password);       
     
            fos = new FileOutputStream(destPath);       
     
            ftpClient.setBufferSize(1024);       
            //设置文件类型（二进制）       
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);       
            ftpClient.retrieveFile(filePath, fos);
            fos.close();
            
            ServletOutputStream os = response.getOutputStream();
            byte buffer[] = new byte[1024];
			File fileLoad = new File(destPath);
			response.reset();
			if(filetype.equals("docx")){
				response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			}else if(filetype.equals("pdf")){
				response.setContentType("application/pdf");
			}else if(filetype.equals("xlsx")){
				response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			}
			response.addHeader("Content-Disposition", "attachment; filename =" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentLength((int) fileLoad.length());

			FileInputStream fis = new FileInputStream(fileLoad);

			int len;
			while ((len = fis.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			if(fileLoad.exists()){
				fileLoad.delete();			
			}
			fis.close();
			//文件传输完毕之后，把应用下的临时文件删除掉，要在输入流关闭之后删除，否则删不掉
			if(fileLoad.exists()){
				fileLoad.delete();			
			}
			os.flush();
			os.close();
        } catch (IOException e) {       
            e.printStackTrace();       
            throw new RuntimeException("FTP客户端出错！", e);       
        } finally {       
            try {       
                ftpClient.disconnect();       
            } catch (IOException e) {       
                e.printStackTrace();    
                System.out.println("失败");
                throw new RuntimeException("关闭FTP连接发生异常！", e);       
            }       
        }       
    }
	
	/**
	 * 删除文件
	 * @param filePath 文件目录路径
	 * @param fileName 文件名
	 */
	public static void deleteFile(String filePath, String fileName) {       
        FTPClient ftpClient = new FTPClient();       
        try {       
        	ftpClient.connect(host);       
            ftpClient.login(username, password);  
            
            if(!ftpClient.changeWorkingDirectory(filePath)){
            	String[] dirs = filePath.split("/");
            	for(int i=1; dirs!=null&&i<dirs.length; i++) {  
                    if(!ftpClient.changeWorkingDirectory(dirs[i])) {  
                        ftpClient.makeDirectory(dirs[i]);
                    }  
                    ftpClient.changeWorkingDirectory(dirs[i]);
                }
            }
            ftpClient.changeWorkingDirectory(filePath);
            ftpClient.deleteFile(fileName);
            
        } catch (IOException e) {       
            e.printStackTrace();       
            throw new RuntimeException("FTP客户端出错！", e);       
        } finally {       
            try {       
                ftpClient.disconnect();       
            } catch (IOException e) {       
                e.printStackTrace();    
                System.out.println("失败");
                throw new RuntimeException("关闭FTP连接发生异常！", e);       
            }       
        }       
    }
	
	/**
	 * 删除目录其下所有内容
	 * @param filePath 目录路径
	 */
	public static void deleteDirectory(String filePath) {
		FTPClient ftpClient = new FTPClient();       
        try {       
        	ftpClient.connect(host);       
            ftpClient.login(username, password);
            if(ftpClient.changeWorkingDirectory(filePath)){
            	FTPFile[] listFiles = ftpClient.listFiles();
            	for(FTPFile ff : listFiles){
            		if(ff.isFile()){
            			ftpClient.deleteFile(ff.getName());
            		}
            		if(ff.isDirectory()){
            			removeDirectory(ftpClient,filePath+"/"+ff.getName());
            		}
            	}
            	ftpClient.removeDirectory(filePath);
            }
        } catch (IOException e) {       
            e.printStackTrace();       
            throw new RuntimeException("FTP客户端出错！", e);       
        } finally {       
            try {       
                ftpClient.disconnect();       
            } catch (IOException e) {       
                e.printStackTrace();    
                System.out.println("失败");
                throw new RuntimeException("关闭FTP连接发生异常！", e);       
            }       
        }     
	}
	
	private static void removeDirectory(FTPClient ftpClient,String filePath) throws IOException{
		if(ftpClient.changeWorkingDirectory(filePath)){
        	FTPFile[] listFiles = ftpClient.listFiles();
        	for(FTPFile ff : listFiles){
        		if(ff.isFile()){
        			ftpClient.deleteFile(ff.getName());
        		}
        		if(ff.isDirectory()){
        			removeDirectory(ftpClient,filePath+"/"+ff.getName());
        		}
        	}
        	ftpClient.removeDirectory(filePath);
        }
	}
	
	/**
	 * 复制文件
	 * @param filePath 要复制的文件的路径
	 * @param destPath 目标文件路径
	 */
	public static void copyFile(String filePath, String destPath){
		FTPClient ftpClient = new FTPClient();       
        FileOutputStream fos = null;
        
        try {
			ftpClient.connect(host);       
			ftpClient.login(username, password);       
 
			fos = new FileOutputStream(destPath);
			
			ftpClient.setBufferSize(1024);       
            //设置文件类型（二进制）       
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);       
            ftpClient.retrieveFile(filePath, fos);
            fos.flush();
            fos.close();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {       
            try {       
                ftpClient.disconnect();       
            } catch (IOException e) {       
                e.printStackTrace();    
                System.out.println("失败");
                throw new RuntimeException("关闭FTP连接发生异常！", e);       
            }       
        } 
	}
	
	/**
	 * 获取文件的后缀名
	 */
	private static String getPostfix(String inputFilePath) 
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
