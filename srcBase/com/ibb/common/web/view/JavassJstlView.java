package com.ibb.common.web.view;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.JstlView;

/**
 * 添加对于JavassContentNegotiatingViewResolver.java 的content-type支持
 * 
 * @author kin wong
 */
public class JavassJstlView extends JstlView {
    
    @Override
    protected void exposeHelpers(HttpServletRequest request) throws Exception {
        super.exposeHelpers(request);
    }
    
}
