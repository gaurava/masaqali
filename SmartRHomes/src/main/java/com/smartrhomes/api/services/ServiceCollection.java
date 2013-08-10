package com.smartrhomes.api.services;

import org.jboss.resteasy.plugins.server.servlet.HttpServletDispatcher;

import com.google.inject.AbstractModule;
import com.google.inject.servlet.ServletModule;
import com.smartrhomes.greendata.service.InsertDataService;
import com.smartrhomes.greendata.service.InsertDataServiceImpl;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

public class ServiceCollection extends ServletModule {//Not in use

	@Override
	protected void configureServlets() {
		bind(InsertDataService.class);
		serveRegex(".+(?<!\\.(html|css|png|jpg))")
        .with(HttpServletDispatcher.class);
	}
	/*@Override
	protected void configure() {
//		bind(GuiceContainer.class);
		bind(InsertDataService.class).to(InsertDataServiceImpl.class);
	}*/
}
