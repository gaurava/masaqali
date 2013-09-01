package com.smartrhomes.api.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.smartrhomes.api.context.AppContextBuilder;
import com.smartrhomes.api.server.JettyServer;

public class JettyFromMain {

	public static void main(String[] args) {
		
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		
		contexts.setHandlers(new Handler[] {new AppContextBuilder().buildWebAppContext(),new AppContextBuilder().buildServletContext()});
//		contexts.setHandlers(new Handler[] { new AppContextBuilder().buildStaticContext() ,new AppContextBuilder().buildServletContext()});
		
		final JettyServer jettyServer = new JettyServer();
		jettyServer.setHandler(contexts);
		try {
			jettyServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//Maven Command to build & start the server
//mvn clean compile exec:java
