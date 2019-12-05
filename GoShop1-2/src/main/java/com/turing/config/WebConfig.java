package com.turing.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.turing.interceptor.LoginInterceptor;


@Configuration //配置类
public class WebConfig implements WebMvcConfigurer {

	//添加视图映射(地址-模板)
		@Override
		public void addViewControllers(ViewControllerRegistry registry) {
			registry.addViewController("/").setViewName("login");
			//去主页
			registry.addViewController("/index").setViewName("index.html");
			registry.addViewController("/leftRequire").setViewName("leftRequire.html");
			registry.addViewController("/bar").setViewName("bar.html");
			registry.addViewController("/mainRequire").setViewName("mainRequire.html");
			//需求录入
			registry.addViewController("/stu").setViewName("planman/pclass_select.html");
			//录入需求计划
			registry.addViewController("/od").setViewName("planman/Order_newform.htm");
			//需求计划展示
			registry.addViewController("/Order_ytb_list").setViewName("planman/Order_ytb_list.htm");
			//编辑采购计划
			registry.addViewController("/Order_wbxjfa_list").setViewName("planman/Order_wbxjfa_list.htm");
		    //采购计划planman/bianzhicaigoujihua.html
			//采购计划查询
			registry.addViewController("/Project_list").setViewName("planman/Project_list.html");
			//supply.html 添加供应商
			registry.addViewController("/supply.html").setViewName("planman/supply.html");
			registry.addViewController("planman/xjfatz_xjfamx2").setViewName("planman/xjfatz_xjfamx2.html");
			registry.addViewController("planman/xjfatz_xjfamx4").setViewName("planman/xjfatz_xjfamx4.html");
			registry.addViewController("planman/xjfatz_xjfamx5").setViewName("planman/xjfatz_xjfamx5.html");
		     //contractmanager/Apply_daishencaiwu.htm
			registry.addViewController("/Apply_daishencaiwu").setViewName("contractmanager/Apply_daishencaiwu.htm");
			registry.addViewController("/Apply_daishencaiwu2").setViewName("contractmanager/Apply_daishencaiwu2.htm");
			registry.addViewController("/Apply_daishencaiwu3").setViewName("contractmanager/Apply_daishencaiwu3.htm");
			//Apply_caiwushenpi.htm
			registry.addViewController("/Apply_caiwushenpi").setViewName("contractmanager/Apply_caiwushenpi.htm");
		}
		
		//注册拦截器
		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			registry.addInterceptor(new LoginInterceptor())
			.addPathPatterns("/**")
			.excludePathPatterns("/","/tologin","/css/**","/js/**");
			
		}
		//配置过滤器解决 客户端 http>put/delete 方法
		@Bean
		public FilterRegistrationBean testFilterRegistration() {
			FilterRegistrationBean  registration = new FilterRegistrationBean(new HiddenHttpMethodFilter());
			registration.addUrlPatterns("/*"); // 
			registration.setName("hiddenHttpMethodFilter");
			return registration;
		}

	

}
