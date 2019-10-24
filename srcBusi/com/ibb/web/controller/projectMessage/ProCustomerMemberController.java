package com.ibb.web.controller.projectMessage;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.servlet.ModelAndView;

import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.dao.util.OrderBy;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.projectMessage.ProCustomerMember;
import com.ibb.model.projectMessage.ProInfo;
import com.ibb.service.projectMessage.IProCustomerMemberService;

@Controller
public class ProCustomerMemberController implements ServletContextAware{
	@Autowired
	private IProCustomerMemberService proCustomerMemberService;
	private ServletContext servletContext;
	@Autowired
    @Qualifier("hibernateTemplate")
    private HibernateTemplate hibernateTemplate;

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}
	/**
	 * 实现了ServletContextAware接口，就可以通过这样获得servletContext
	 */
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}
	@RequestMapping(value = "/proCustomerMember")
	public String toCompanyInfoPage() {
		return "proInfoMessage/proCustomerMember";
	}
	
	/**
	 * 查询信息列表	根据项目编号查询+分页查询
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proCustomerMember/listByProNum.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProCustomerMember> queryListByProNum(@RequestParam(value = "proNum", required = false)String proNum, HttpServletRequest request){
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("proNum"));
		
		// 项目编号
		if (StringUtils.isNotEmpty(proNum)) {
			query.add(Restrictions.eq("proNum", proNum));
		}
		query.add(Restrictions.eq("state", 1));
		Page<ProCustomerMember> page=proCustomerMemberService.queryListByCondition(query, order,pn,pageSize);
		return new EasyUIGridJsonModel<ProCustomerMember>(page);
		
	}
	
	
	/**
	 * 增加信息
	 * 
	 * @param proCustomerMember
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proCustomerMember/add.json", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView add(ProCustomerMember proCustomerMember, HttpServletRequest request) {
		String pro = request.getParameter("pro");
		if (proCustomerMember != null) {

			if (proCustomerMember.getProNum() != null) {
				try {
					proCustomerMember.setState(1);
					proCustomerMemberService.save(proCustomerMember);
				} catch (Exception e) {
					e.printStackTrace();
					return new ModelAndView("redirect:/mgr/"+pro);
				}
				return new ModelAndView("redirect:/mgr/"+pro);
			}
		}
		return new ModelAndView("redirect:/mgr/"+pro);
	}
	
	/**
	 * 删除记录
	 * 
	 * @param proMemberIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proCustomerMember/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String proCustomerMemberIdArr, HttpServletRequest request) {
		if (proCustomerMemberIdArr != null) {
			String[] proMemberIds = proCustomerMemberIdArr.split("#");
			try {
				for (String proMemberId : proMemberIds) {
					if (StringUtils.isNotEmpty(proMemberId)) {
						ProCustomerMember proCustomerMember=proCustomerMemberService.get(Integer.parseInt(proMemberId));
						proCustomerMember.setState(0);
						proCustomerMemberService.update(proCustomerMember);
						//proCustomerMemberService.delete(Integer.valueOf(proMemberId));
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return new CommonJsonModel(false, "更新失败！");
			}
			return new CommonJsonModel();
		}
		return new CommonJsonModel(false, "更新失败！");
	}
	
	/**
	 * 更新信息
	 * 
	 * @param ProCustomerMember
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proCustomerMember/update.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(ProCustomerMember proCustomerMember, HttpServletRequest request) {
		if (proCustomerMember != null) {
			if(proCustomerMember.getCustomerId()!=null&&proCustomerMember.getProNum()!=null){
				try{
					proCustomerMember.setState(1);
					proCustomerMemberService.update(proCustomerMember);
				} catch(Exception e){
					e.printStackTrace();
					return new CommonJsonModel(false,"更新失败！");
				}
				return new CommonJsonModel();
			}
		}
		return new CommonJsonModel(false,"更新失败！");
	}

}
