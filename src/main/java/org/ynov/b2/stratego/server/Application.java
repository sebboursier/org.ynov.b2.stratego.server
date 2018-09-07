/**
 *
 */
package org.ynov.b2.stratego.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

/**
 * @author sebboursier
 *
 */
@SpringBootApplication
public class Application extends RepositoryRestConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(Application.class);
		springApplication.run(args);
	}

}