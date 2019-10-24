package com.javaniu;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.JacksonObjectMapperFactoryBean;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.util.SVNURLUtil;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.internal.wc.admin.SVNEntry;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.ibb.model.svn.SvnDicInfo;
import com.jcraft.jsch.Logger;

public class SVNUtil {
	  private String svnRoot;
	  private String userName;
	  private String password;
	  private SVNRepository repository;
	  private SVNClientManager ourClientManager;
	  private SVNURL repositoryOptUrl;
	  protected SVNClientManager clientManager;// svn客户操作服务
	  /***
	     * 构造方法
	     * @param svnRoot
	     *             svn根目录
	     */
	    public SVNUtil(String svnRoot) {
	       this.svnRoot=svnRoot;
	    }
	    /**
	     * 
	     * @param svnRoot 根目录
	     * @param userName 登录用户名
	     * @param password 登录密码
	     */
		public SVNUtil(String svnRoot, String userName, String password) {
			this.svnRoot = svnRoot;
			this.userName = userName;
			this.password = password;
		}
		/**
		 * 通过不同的协议初始化版本库
		 */
		private static void setupLibrary(){
			//对于使用http://和使用https://
			DAVRepositoryFactory.setup();
			//对于使用svn://和svn+xxx://
			SVNRepositoryFactoryImpl.setup();
			//对于使用file://
			FSRepositoryFactory.setup();
		}
		/**
		 * 登录验证
		 * 
		 */
		public boolean login(){
			setupLibrary();
			try {
				//创建库连接
				repository=SVNRepositoryFactory.create(SVNURL.parseURIEncoded(this.svnRoot));
				//身份验证
				ISVNAuthenticationManager authManager=SVNWCUtil.createDefaultAuthenticationManager(this.userName, this.password);
				//创建身份验证管理器
				repository.setAuthenticationManager(authManager);
				return true;
			
			} catch (SVNException e) {
				e.printStackTrace();
				return false;
			}
		}
			/***
		     *
		     * @param path
		     * @return 查询给定路径下的条目列表
		     * @throws SVNException
		     */
			@SuppressWarnings("rawtypes")
			 public List<SVN> listEntries(String path)
			           throws SVNException {
			       Collection entries = repository.getDir(path, -1, null,(Collection) null);
			       Iterator iterator = entries.iterator();
			       List<SVN> svns = new ArrayList<SVN>();
			       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			       while (iterator.hasNext()) {
			           SVNDirEntry entry = (SVNDirEntry) iterator.next();
			           SVN svn = new SVN();
			           svn.setCommitMessage(entry.getCommitMessage());
			           String date=sdf.format(entry.getDate());
			           svn.setDate(date);
			           svn.setKind(entry.getKind().toString());
			           svn.setName(entry.getName());
			           svn.setRepositoryRoot(entry.getRepositoryRoot().toString());
			           svn.setRevision(entry.getRevision());
			           svn.setSize(entry.getSize()/1024);
			           svn.setUrl(path.equals("") ? entry.getPath() : path + "/"+entry.getName());
			           svn.setAuthor(entry.getAuthor());
			           svn.setState(svn.getKind() == "file"?null:"closed");
			           svns.add(svn);
			       }
			       return svns;
			}
			//新建一个SVNServlet
			//添加一个方法用于把Java对象转换为json字符串
			public String object2Json(Object obj){
				ObjectMapper mapper = new ObjectMapper();
				StringWriter sw=new StringWriter();
				try {
					JsonGenerator gen=new JsonFactory().createJsonGenerator(sw);
					mapper.writeValue(gen, obj);
					gen.close();
					return sw.toString();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null; 
			}
		//下载
			public boolean checkOut(String checkUrl) {
				ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
				String username="fangzw";
				String password="fangzw";
				clientManager=SVNClientManager.newInstance((DefaultSVNOptions) options, username, password);
				try {
					File save = new File("D:/");
					SVNUpdateClient updateClient = clientManager.getUpdateClient();
					updateClient.setIgnoreExternals(false);
					//updateClient.doExport(SVNURL.parseURIEncoded(checkUrl), save, SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY, false);
					updateClient.doExport(repositoryOptUrl, save, SVNRevision.HEAD, SVNRevision.HEAD, "downloadModel",true,true);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
				return true;
			}
    

}
