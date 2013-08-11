package com.smartrhomes.api.context;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.smartrhomes.greendata.connection.CassandraOpenConnection;

public class AppContextBuilder {
	
//	private WebAppContext webAppContext;
	private ContextHandler staticContext;
	private ServletContextHandler servletContext;
	
/*	public WebAppContext buildWebAppContext(){
		webAppContext = new WebAppContext();
		webAppContext.setDescriptor(webAppContext + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(".");
		webAppContext.setContextPath("/SmartRHomes");
		webAppContext.setAttribute("webContext", webAppContext);
//		ServletHolder h = new ServletHolder(new HttpServletDispatcher());
//		h.setInitParameter("javax.ws.rs.Application", "com.smartrhomes.api.services.HTMLServices");
//		servletContext.addServlet(h, "/*");
//		webappcontext.addServlet(new ServletHolder(new HelloServlet()), "/hello");
//		webAppContext.setAttribute("CassandraConnectionPool",new CassandraOpenConnection());
		return webAppContext;
	}*/
	
	public ServletContextHandler buildServletContext(){
		servletContext = new ServletContextHandler();
		servletContext.setContextPath("/SmartRHomes");
		servletContext.setResourceBase(".");
		ServletHolder h = new ServletHolder(new HttpServletDispatcher());
		h.setInitParameter("javax.ws.rs.Application", "com.smartrhomes.api.services.MyServices");
//		h.setInitParameter("resteasy.guice.modules", "com.smartrhomes.api.services.ServiceCollection");
		/*Map<String,String> params = new HashMap<String, String>();
		params.put("javax.ws.rs.Application", "com.smartrhomes.api.services.MyServices");
		params.put("resteasy.scan.providers", "true");
		h.setInitParameters(params);*/
		servletContext.addServlet(h, "/*");
		servletContext.setClassLoader(Thread.currentThread().getContextClassLoader());
		servletContext.setAttribute("servletContext", servletContext);
		//the service added which is starting with jetty.
		servletContext.setAttribute("CassandraConnectionPool",CassandraOpenConnection.openFor("localhost", 9160, null, null));
		return servletContext;
	}
	
	public ContextHandler buildStaticContext(){
		staticContext = new ContextHandler();
		staticContext.setContextPath("/SmartRHomes/static");
		staticContext.setHandler(new ResourceHandler());
		staticContext.setResourceBase("static");
		return staticContext;
	}

}
