package com.ibb.web.controller.projectMessage;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;
import com.ibb.common.dao.util.ConditionQuery;
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.projectMessage.ProMember;
import com.ibb.service.projectMessage.IProInfoService;
import com.ibb.service.projectMessage.IProMemberService;
import com.ibb.service.projectMessage.IProSourceCodeService;

/**
 * 
 * @author WangGangWei
 * @date 2017年4月20日 下午3:08:31
 *
 */
@Controller
public class ProMemberController implements ServletContextAware {
	@Autowired
	private IProInfoService proInfoService;
	@Autowired
	private IProMemberService proMemberService;
	@Autowired
	private IProSourceCodeService proSourceCodeService;

	@SuppressWarnings("unused")
	private ServletContext servletContext;

	/**
	 * 实现了ServletContextAware接口，就可以通过这样获得servletContext
	 */
	@Override
	public void setServletContext(ServletContext context) {
		this.servletContext = context;
	}

	@RequestMapping(value = "/proMember")
	public String toCompanyInfoPage() {
		return "proInfoMessage/proMember";
	}

	/**
	 * 根据项目ID查询成员信息列表
	 * 
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/proMember/queryMember.json", method = RequestMethod.POST)
	@ResponseBody
	public List<ProMember> queryMember(String proNum, HttpServletRequest request) {
		ConditionQuery query = new ConditionQuery();
		if (StringUtils.isNotEmpty(proNum)) {
			query.add(Restrictions.eq("proNum", proNum));
		}
		query.add(Restrictions.eq("state", 1));
		List<ProMember> proMember = proMemberService.queryListByCondition(query);
		return proMember;
	}
	
	/**
	 * 根据成员ID查询成员信息列表
	 * 
	 * @return 列表json对象
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/proMember/queryById.json", method = RequestMethod.POST)
	@ResponseBody
	public ProMember queryById(Integer memberId, HttpServletRequest request) {
		if (memberId!=null) {
			return proMemberService.get(memberId);
		}
		return null;
	}

	/**
	 * 查询信息列表 分页查询+模糊查询
	 * 
	 * @param proMember
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proMember/listByLike.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProMember> queryListByLike(ProMember proMember, HttpServletRequest request) {

		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();

		// 姓名
		if (StringUtils.isNotEmpty(proMember.getMemberName())) {
			query.add(Restrictions.like("memberName", "%" + proMember.getMemberName().trim() + "%"));
		}
		// 角色
		if (StringUtils.isNotEmpty(proMember.getRoleaName())) {
			query.add(Restrictions.like("roleaName", "%" + proMember.getRoleaName().trim() + "%"));
		}
		query.add(Restrictions.eq("state", 1));
		Page<ProMember> page = proMemberService.queryListByCondition(query, pn, pageSize);
		return new EasyUIGridJsonModel<ProMember>(page);
	}

	/**
	 * 查询信息列表 根据项目编号查询+分页查询
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/proMember/listByProNum.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProMember> queryListByProNum(
			@RequestParam(value = "proNum", required = false) String proNum, HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);

		ConditionQuery query = new ConditionQuery();
		// 项目编号
		if (StringUtils.isNotEmpty(proNum)) {
			query.add(Restrictions.eq("proNum", proNum));
		}
		query.add(Restrictions.eq("state", 1));
		Page<ProMember> page = proMemberService.queryListByCondition(query, pn, pageSize);
		return new EasyUIGridJsonModel<ProMember>(page);
	}

	/**
	 * 增加信息
	 * 
	 * @param proMember
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proMember/add.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel add(ProMember proMember, HttpServletRequest request) {

		if (proMember != null) {

			if (proMember.getProNum() != null && proMember.getMemberName() != null
					&& proMember.getRoleaName() != null) {

				try {
					proMember.setState(1);
					proMemberService.save(proMember);
				} catch (Exception e) {
					e.printStackTrace();
					return new CommonJsonModel(false, "增加失败！");
				}
				return new CommonJsonModel();
			}
		}
		return new CommonJsonModel(false, "增加失败！");
	}

	/**
	 * 更新信息
	 * 
	 * @param proMember
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proMember/update.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(ProMember proMember, HttpServletRequest request) {
		if (proMember != null) {
			if (proMember.getProNum() != null && proMember.getMemberName() != null
					&& proMember.getRoleaName() != null) {
				try {
					proMember.setState(1);
					proMemberService.update(proMember);
				} catch (Exception e) {
					e.printStackTrace();
					return new CommonJsonModel(false, "更新失败！");
				}
				return new CommonJsonModel();
			}
		}
		return new CommonJsonModel(false, "更新失败！");
	}

	/**
	 * 删除记录
	 * 
	 * @param proMemberIdArr
	 *            主键字符串
	 * @param request
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/proMember/delete.json", method = RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel delete(String proMemberIdArr, HttpServletRequest request) {
		if (proMemberIdArr != null) {
			String[] proMemberIds = proMemberIdArr.split("#");
			try {
				for (String proMemberId : proMemberIds) {
					if (StringUtils.isNotEmpty(proMemberId)) {
						/*-------做数据备份：0为表示删除--------------*/
						ProMember proMember=proMemberService.get(Integer.parseInt(proMemberId));
						proMember.setState(0);
						proMemberService.update(proMember);
						//proMemberService.delete(Integer.valueOf(proMemberId));
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

}