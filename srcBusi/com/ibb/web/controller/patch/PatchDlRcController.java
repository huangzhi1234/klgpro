package com.ibb.web.controller.patch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.patch.PatDownNum;
import com.ibb.model.patch.PatUpNum;
import com.ibb.model.patch.PatchDlRc;
import com.ibb.service.patch.IPatchDlRcService;

@Controller
public class PatchDlRcController implements ServletContextAware {
	
	@Autowired
	private IPatchDlRcService patchDlRcService;

	@Autowired
	@Qualifier("hibernateTemplate")
	private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	@SuppressWarnings("unused")
	private ServletContext servletContext;

	@Override
	public void setServletContext(ServletContext arg0) {
		this.servletContext = arg0;
	}
	
	
	/**
	 * 查询项目信息列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/patchDlRc/listByPatId.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<PatchDlRc> queryList(@RequestParam(value = "patId", required = false) String patId, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("dlTime"));
		if (StringUtils.isNotEmpty(patId)) {
			query.add(Restrictions.eq("patId",Integer.parseInt(patId)));
		}
		
		Page<PatchDlRc> page = patchDlRcService.queryListByCondition(query, order, pn, pageSize);
		if(page.getItems()==null||page.getItems().size()==0){
			PatchDlRc pdr = new PatchDlRc();
			pdr.setDlId(0);
			pdr.setDlMan(0);
			pdr.setDlTime("");
			pdr.setPatId(0);
			page.getItems().add(pdr);
			page.setIndex(1);
			page.setHasNext(true);
			page.setHasPre(true);
		}
		return new EasyUIGridJsonModel<PatchDlRc>(page);
	}
	

	@RequestMapping(value = "/patchDlRc/getDownNun.json", method = RequestMethod.POST)
	@ResponseBody
	private List<PatDownNum> showDown(String startTime, String endTime) {
		List<PatDownNum> lst = null;
		PatchDlRc patchDlRc = null;
		startTime = "2017-01-01";
		endTime = "2017-08-08";
		StringBuffer hql = new StringBuffer(
				"select pi.patName as patName,count(pdr.dlMan) as downNum,ub.userName from PatchDlRc pdr , PatchInfo pi ,UserBean ub where pdr.patId=pi.patId and pi.upMan=ub.userId ");
		if (startTime != null && !"".equals(startTime.trim()))
			hql.append(" and pdr.dlTime > '" + startTime + "'");
		if (endTime != null && !"".equals(endTime.trim()))
			hql.append(" and pdr.dlTime < '" + endTime + "'");
		Query query = this.getHibernateTemplate().getSessionFactory().openSession().createQuery(hql.toString());
		List list = query.list();
		if (list != null && list.size() > 0) {
			lst = new ArrayList();
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				Object[] object = (Object[]) iterator.next();
				PatDownNum patDownNum = new PatDownNum();
				patDownNum.setPatName(object[0] + "");
				patDownNum.setDownNum(object[1] + "");
				patDownNum.setUpMan(object[2] + "");
				lst.add(patDownNum);
				System.out.println("成果名称:" + patDownNum.getPatName() + ",下载次数" + patDownNum.getDownNum() + ",上传人："
						+ patDownNum.getUpMan());

			}
		}
		return lst;
	}
	
	
	//获取某段时间内上传成果的情况
		@RequestMapping(value = "/patchDlRc/getUpNun.json", method = RequestMethod.POST)
		@ResponseBody
		private List<PatUpNum> showUp(String startTime,String endTime) {
			List<PatUpNum> lst= null;
			startTime = "2017-01-01";
			endTime = "2017-08-08";
			StringBuffer hql = new StringBuffer("select ub.userName,count(pi.patName) as patNum from PatchInfo pi,UserBean ub where pi.upMan=ub.userId ");
			if(startTime!=null&&!"".equals(startTime.trim()))
				hql.append(" and upTime > '"+startTime+"'");
			if(endTime!=null&&!"".equals(endTime.trim()))
				hql.append(" and upTime < '"+endTime+"'");
			Query query =this.getHibernateTemplate().getSessionFactory().openSession().createQuery(hql.toString());
			List list = query.list();
			if(list!=null&&list.size()>0){
				lst = new ArrayList();
				for (Iterator iterator = list.iterator(); iterator.hasNext();) {  
		            Object[] object = (Object[]) iterator.next();  
		            PatUpNum patNum = new PatUpNum();
		            patNum.setUpMan(object[0]+"");
		            patNum.setPatNum(object[1]+"");
		            lst.add(patNum);
		            System.out.println("上传人:"+patNum.getUpMan()+",上传次数" + patNum.getPatNum());
		        }  
			}
			return lst;
		}

}
