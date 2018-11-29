package com.xenonsoft.client.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ACMEApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		try {
			SpringApplication.run(ACMEApplication.class);
		} catch (Throwable t) {
			t.printStackTrace();

		}
	}

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		try {

			return application.sources(ACMEApplication.class);
		} catch (Throwable t) {
			System.err.println("*ERROR*: Application failed to start!");
			t.printStackTrace(System.err);
			throw t;
		}
	}

}
