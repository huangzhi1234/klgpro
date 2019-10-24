/**
 * 
 */
package com.ibb.chart.view;

import java.awt.Font;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.springframework.web.servlet.view.AbstractView;

import com.ibb.chart.Chart;

/**
 * jfreechart图表视图
 * @author pansen
 *
 */
public class ChartView extends AbstractView {

	/**
	 * <pre>
	 * model:
	 * {@link Chart#CHART} must be supplied and JFreeChart.
	 * {@link Chart#WIDTH} must be integer default 500.
	 * {@link Chart#HEIGHT} must be integer default 400.
	 * {@link Chart#JPEG} must be boolean default false.
	 * {@link Chart#PNG} must be boolean default true.
	 * 
	 * 输出图片格式jpeg,png,默认输出png,
	 * 只有当{@link Chart#JPEG}为true而且{@link Chart#PNG}为false时，才输出jpeg格式。
	 * </pre>
	 * 
	 * @see org.springframework.web.servlet.view.AbstractView#renderMergedOutputModel(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		JFreeChart chart = (JFreeChart)model.get(Chart.CHART);
		
		if(chart == null){
			throw new Exception("Parameter 'IBB_CHART' must be supplied !");
		}
		
		//============================================================
		//以下三种样式是为了解决中文乱码问题
		//============================================================
		//标题
		TextTitle textTitle = chart.getTitle();
		textTitle.setFont(new Font("微软雅黑",Font.BOLD,20));
		
		//图释
		LegendTitle legend = chart.getLegend();
		if (legend != null) {
		   legend.setItemFont(new Font("微软雅黑", Font.CENTER_BASELINE, 15));
		}
		
		CategoryPlot plot = (CategoryPlot)chart.getPlot();
		CategoryAxis domainAxis = plot.getDomainAxis();//(柱状图的x轴)
		domainAxis.setTickLabelFont(new Font("微软雅黑",Font.CENTER_BASELINE,15));//设置x轴坐标上的字体
		domainAxis.setLabelFont(new Font("微软雅黑",Font.CENTER_BASELINE,15));//设置x轴上的标题的字体	
		ValueAxis valueAxis = plot.getRangeAxis();//(柱状图的y轴)
		valueAxis.setTickLabelFont(new Font("微软雅黑",Font.CENTER_BASELINE,15));//设置y轴坐标上的字体
		valueAxis.setLabelFont(new Font("微软雅黑",Font.CENTER_BASELINE,15));//设置y轴坐标上的标题的字体
		//============================================================
		
		//宽度，默认500
		Integer width = (Integer)model.get(Chart.WIDTH);
		int chartWidth = (width == null)? 500 : width.intValue();
		
		//高度，默认400
		Integer height = (Integer)model.get(Chart.HEIGHT);
		int chartHeight = (height == null)? 400 : height.intValue();
		
		//输出的图片格式：jpeg,png
		Boolean jpeg = (Boolean)model.get(Chart.JPEG);
		Boolean png = (Boolean)model.get(Chart.PNG);
		
		boolean isJpeg = (jpeg == null) ? false : jpeg.booleanValue();
		boolean isPng = (png == null) ? false : png.booleanValue();
		
		//默认输出png图片
		if(isJpeg && !isPng){
			ChartUtilities.writeChartAsJPEG(response.getOutputStream(), chart, chartWidth, chartHeight);
		}else{
			ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, chartWidth, chartHeight);
		}
	}

}
