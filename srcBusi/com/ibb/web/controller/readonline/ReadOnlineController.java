package com.ibb.web.controller.readonline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;

import com.ibb.ftp.FTPChannel;
import com.ibb.ftp.SFTPChannel;
import com.ibb.readonline.Office2Swf;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * 在线预览的Controller
 * @author zhangjian
 *
 */

@Controller
public class ReadOnlineController implements ServletContextAware {
	
	private ServletContext servletContext;

	@Override
	/**
	 * 实现了ServletContextAware接口，就可以通过这样获得servletContext
	 */
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	
	@RequestMapping(value = "/readonline")
	public String toEvaluationProjectPage() {
		return "readonline/readonline";
	}
	
	@RequestMapping(value = "/readonline/read", method = RequestMethod.GET)
	public ModelAndView readonline(@RequestParam("filePath")String filePath ,HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		
		/*try {
			SFTPChannel channel = new SFTPChannel();

			ChannelSftp chSftp = channel.getChannel();

			String filetype = filePath.substring(filePath.indexOf(".") + 1);

			String temppath = "";
			if(filetype.equals("docx")||filetype.equals("doc")){
				temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + ".docx";
			}else if(filetype.equals("pdf")){
				temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + ".pdf";
			}else if(filetype.equals("xlsx")){
				temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + ".xlsx";
			}else if(filetype.equals("jpg")){
				temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis() + ".jpg";
			}
			OutputStream out = new FileOutputStream(temppath);

			chSftp.get(filePath, out);
			chSftp.quit();	
			out.close();
					
			String outFilePath = temppath.replace(new File(temppath).getName(), System.currentTimeMillis() + ".swf");
			outFilePath = Office2Swf.office2Swf(temppath, outFilePath);
			request.getSession().setAttribute("fileName", new File(outFilePath).getName());
				
		} catch (JSchException e) {
			e.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
		String filetype = filePath.substring(filePath.indexOf(".") + 1);
		String temppath = this.servletContext.getRealPath("/") + System.currentTimeMillis()+"."+filetype;
		FTPChannel.copyFile(filePath, temppath);
		String outFilePath = temppath.replace(new File(temppath).getName(), System.currentTimeMillis() + ".swf");
		outFilePath = Office2Swf.office2Swf(temppath, outFilePath);
		request.getSession().setAttribute("fileName", new File(outFilePath).getName());
		File inputfile = new File(temppath);
		if(inputfile.exists()){
			inputfile.delete();			
		}
		return new ModelAndView("redirect:/mgr/readonline");
	}		
	
	@RequestMapping(value = "/readonline/delete", method = RequestMethod.GET)
	public void delete(@RequestParam("fileName")String fileName ,HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException {
		String filepath = this.servletContext.getRealPath("/") + fileName;
		File file = new File(filepath);
		if(file.exists()){
			file.delete();
		}
	}
}