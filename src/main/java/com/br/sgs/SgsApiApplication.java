package com.br.sgs;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

@SpringBootApplication
public class SgsApiApplication {

	
	@Autowired
	MessageSource messageSource;
	
	private static ApplicationContext APPLICATION_CONTEXT;
	
	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(SgsApiApplication.class, args);
	}
	
	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
	
	
	 @PostConstruct
	    public void postConstruct() {
	        System.out.println("Running Message Property Data");
	        System.out.println(messageSource.getMessage("api.error.user.not.found", null, Locale.ENGLISH));
	        System.out.println(messageSource.getMessage("api.error.user.already.registered", null, Locale.ENGLISH));
	        System.out.println(messageSource.getMessage("api.response.user.creation.successful", null, Locale.ENGLISH));
	        System.out.println(messageSource.getMessage("api.response.user.update.successful", null, Locale.ENGLISH));
	        System.out.println("End Message Property Data");
	    }

}
