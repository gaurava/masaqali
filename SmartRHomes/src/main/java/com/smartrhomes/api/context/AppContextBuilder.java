package com.smartrhomes.api.context;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.smartrhomes.greendata.connection.CassandraOpenConnection;

public class AppContextBuilder {
	
	private WebAppContext webAppContext;
	private ServletContextHandler servletContext;
	
	public WebAppContext buildWebAppContext(){
		webAppContext = new WebAppContext();
		webAppContext.setDescriptor(webAppContext + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(".");
		webAppContext.setContextPath("/SmartRHomes");
		webAppContext.setAttribute("webContext", webAppContext);
//		webappcontext.addServlet(new ServletHolder(new HelloServlet()), "/hello");
//		webAppContext.setAttribute("CassandraConnectionPool",new CassandraOpenConnection());
		return webAppContext;
	}
	
	public ServletContextHandler buildServletContext(){
		servletContext = new ServletContextHandler();
		servletContext.setContextPath("/SmartRHomes/Service");
		ServletHolder h = new ServletHolder(new HttpServletDispatcher());
		h.setInitParameter("javax.ws.rs.Application", "com.smartrhomes.api.services.MyServices");
//		h.setInitParameter("resteasy.guice.modules", "com.smartrhomes.api.services.ServiceCollection");
		/*Map<String,String> params = new HashMap<String, String>();
		params.put("javax.ws.rs.Application", "com.smartrhomes.api.services.MyServices");
		params.put("resteasy.guice.modules", "com.smartrhomes.api.services.ServiceCollection");
		h.setInitParameters(params);*/
		servletContext.addServlet(h, "/*");
		servletContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		servletContext.setAttribute("servletContext", servletContext);
		//the service added which is starting with jetty.
		servletContext.setAttribute("CassandraConnectionPool",CassandraOpenConnection.openFor("localhost", 9160, null, null));
		return servletContext;
	}
	
	public ServletContextHandler getServletContext(){
		return servletContext;
	}
}
