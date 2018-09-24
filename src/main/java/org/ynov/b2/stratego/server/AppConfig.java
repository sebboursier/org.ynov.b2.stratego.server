/**
 *
 */
package org.ynov.b2.stratego.server;

import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * @author sebboursier
 *
 */
@Configuration
public class AppConfig extends RepositoryRestConfigurerAdapter {

	@Override
	public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
	}

	@Override
	public void configureRepositoryRestConfiguration(final RepositoryRestConfiguration config) {
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);
		provider.addIncludeFilter(new AnnotationTypeFilter(Entity.class));
		final Set<BeanDefinition> beans = provider.findCandidateComponents("org.ynov.b2.stratego.server.jpa.model");
		for (final BeanDefinition bean : beans) {
			try {
				config.exposeIdsFor(Class.forName(bean.getBeanClassName()));
			} catch (final ClassNotFoundException e) {
				throw new IllegalStateException("Failed to expose `id` field due to", e);
			}
		}
	}

	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.addAllowedMethod(HttpMethod.OPTIONS);
		config.addAllowedMethod(HttpMethod.PATCH);
		config.addAllowedMethod(HttpMethod.DELETE);
		config.addAllowedMethod(HttpMethod.PUT);
		config.addAllowedMethod(HttpMethod.POST);
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

}
