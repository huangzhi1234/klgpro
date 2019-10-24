package com.ibb.web.controller.score;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.StringUtils;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.score.ProScore;
import com.ibb.service.score.IProScoreService;
import com.ibb.sys.model.UserBean;
import com.ibb.sys.service.IUserService;

@Controller
public class OrderScoreController implements ServletContextAware{
	@Autowired
	private IProScoreService proScoreService;
	@Autowired
	private IUserService userService;
	@SuppressWarnings("unused")
	private ServletContext servletContext;
	@Autowired
	@Qualifier("hibernateTemplate")
	private HibernateTemplate hibernateTemplate;
	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	@Override
	public void setServletContext(ServletContext arg0) {
		this.servletContext = arg0;
	}
	@RequestMapping(value = "/orderScore")
	public String toPatchInfoPage() {
		return "score/orderScore";
	}
	/**
	 * 查询申请列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderScore/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProScore> list(ProScore proScore,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("score"));
		Page<ProScore> page=proScoreService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<ProScore>(page);
	}
	/**
	 * 获取用户
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/orderScore/getUserBean.json", method = RequestMethod.POST)//处理请求地址映射的注解
	@ResponseBody
	public UserBean getUserBean(HttpServletRequest request){
		String userId=request.getParameter("userId");
		UserBean userBean=null;//令depart为空
		if(StringUtils.isNotEmpty(userId)){
			userBean=userService.get(Integer.parseInt(userId));
		}
		return userBean;
	}
}
