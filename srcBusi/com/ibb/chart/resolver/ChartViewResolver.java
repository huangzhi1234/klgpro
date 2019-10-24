/**
 * 
 */
package com.ibb.chart.resolver;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

/**
 * jfreechart图表解析器
 * @author pansen
 *
 */
public class ChartViewResolver extends AbstractCachingViewResolver implements Ordered{
	
	private int order = Integer.MAX_VALUE;  // default: same as non-Ordered
	//后缀
	private String suffix;
	//图表视图
	private View view;

	/**
	 * @see org.springframework.web.servlet.view.AbstractCachingViewResolver#loadView(java.lang.String, java.util.Locale)
	 */
	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		
		View view = null;
		if(viewName.endsWith(this.suffix)){
			view = this.view;
		}
		
		return view;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}

	public int getOrder() {
		return this.order;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
}
