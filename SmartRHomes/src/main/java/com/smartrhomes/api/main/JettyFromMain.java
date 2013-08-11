package com.smartrhomes.api.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import com.smartrhomes.api.context.AppContextBuilder;
import com.smartrhomes.api.server.JettyServer;
import com.smartrhomes.greendata.connection.CassandraOpenConnection;

public class JettyFromMain {

	public static void main(String[] args) {
		
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		
		contexts.setHandlers(new Handler[] { new AppContextBuilder().buildStaticContext() ,new AppContextBuilder().buildServletContext()});
		
		final JettyServer jettyServer = new JettyServer();
		jettyServer.setHandler(contexts);
		try {
			jettyServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//Java Command to run server
//G:\workspace\SmartRHomes>java -cp target\SmartRHomes-0.1.jar;target\dependency\javax.servlet-3.0.0.v201112011016.jar com.smartrhomes.greendata.main.JettyFromMain