package com.smartrhomes.greendata.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;

import com.smartrhomes.greendata.context.AppContextBuilder;
import com.smartrhomes.greendata.server.JettyServer;

public class JettyFromMain {

	public static void main(String[] args) {
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		
		contexts.setHandlers(new Handler[] { new AppContextBuilder().buildWebAppContext()});
		
		final JettyServer jettyServer = new JettyServer();
		jettyServer.setHandler(contexts);
		try {
			jettyServer.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		Runnable runner = new Runnable() {
//			@Override
//			public void run() {
//				new ServerRunner(jettyServer);
//			}
//		};
//		EventQueue.invokeLater(runner);
	}
}

//Java Command to run server
//G:\workspace\SmartRHomes>java -cp target\SmartRHomes-0.1.jar;target\dependency\javax.servlet-3.0.0.v201112011016.jar com.smartrhomes.greendata.main.JettyFromMain