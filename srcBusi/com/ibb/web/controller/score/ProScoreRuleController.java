package com.ibb.web.controller.score;

import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
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
import com.ibb.common.util.pagination.IPageContext;
import com.ibb.common.util.pagination.Page;
import com.ibb.common.web.model.CommonJsonModel;
import com.ibb.common.web.model.EasyUIGridJsonModel;
import com.ibb.model.patch.UploadCheck;
import com.ibb.model.score.ProScore;
import com.ibb.model.score.ProScoreRule;
import com.ibb.service.patch.IUploadCheckService;
import com.ibb.service.score.IProScoreRuleService;
import com.ibb.service.score.IProScoreService;
import com.ibb.sys.model.RoleBean;

@Controller
public class ProScoreRuleController implements ServletContextAware{
	@Autowired
	private IProScoreRuleService proScoreRuleService;
	@Autowired
	private IProScoreService proScoreService;
	@Autowired
	private IUploadCheckService uploadCheckService;
	@Autowired
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
	@RequestMapping(value = "/scoreRule")
	public String toCompanyInfoPage() {
		return "score/scoreRule";
	}
	/**
	 * 查询申请列表 分页查询+模糊查询
	 * 
	 * @param proInfo
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/scoreRule/list.json", method = RequestMethod.POST)
	@ResponseBody
	public EasyUIGridJsonModel<ProScoreRule> list(ProScore proScore,HttpServletRequest request) {
		int pn = ServletRequestUtils.getIntParameter(request, "page", 1); // 页码，1表示默认值
		int pageSize = ServletRequestUtils.getIntParameter(request, "rows", IPageContext.DEFAULT_PAGE_SIZE);
		ConditionQuery query = new ConditionQuery();
		OrderBy order = new OrderBy();
		order.add(Order.desc("ruleId"));
		Page<ProScoreRule> page=proScoreRuleService.queryListByCondition(query,order, pn, pageSize);
		return new EasyUIGridJsonModel<ProScoreRule>(page);
	}
	/**
	 * 更新一条记录
	 * @param role 角色对象
	 * @return 操作结果json对象
	 */
	@RequestMapping(value = "/scoreRule/update.json",method=RequestMethod.POST)
	@ResponseBody
	public CommonJsonModel update(ProScoreRule proScoreRule){
		if(proScoreRule != null){
			try {
				proScoreRuleService.update(proScoreRule);//先修改规则，为后面重新计算积分做准备
				/*-------------更新总积分------------------*/
				/*//1.查询出所有积分表数据list
				Integer score=0;//总积分
				int score1=proScoreRuleService.get(2).getScore();//单个上传类型所得积分
				ConditionQuery query = new ConditionQuery();
				List<ProScore> list=proScoreService.queryListByCondition(query);
				if(list!=null&&list.size()>0){
					for (ProScore proScore : list) {//逐个对象进行更新
						Integer userId=proScore.getUserId();//取得当前对象的userid，用于下面方法的参数传递
						score=proScoreRuleService.getUp(userId)+proScoreRuleService.getDown(userId);//计算总积分
						proScore.setScore(score);//保存最新总积分在对象中
						proScoreService.update(proScore);//更新对象
					}
				}*/
				proScoreRuleService.getAllScore();
				
				return new CommonJsonModel(true,"更新成功"); 
			} catch (Exception e) {
				e.printStackTrace();
				return new CommonJsonModel(false,"操作失败！");
			}
		}else{
			return new CommonJsonModel(false,"操作失败！");
		}
	}
}
