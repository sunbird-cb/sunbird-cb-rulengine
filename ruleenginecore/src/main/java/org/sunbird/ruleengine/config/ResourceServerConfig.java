package org.sunbird.ruleengine.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
 
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
 
	@Override
	public void configure(HttpSecurity http) throws Exception {
		//-- define URL patterns to enable OAuth2 security
		http
		 .csrf().disable()
         .authorizeRequests()
             .antMatchers(HttpMethod.OPTIONS, "/oauth/token").permitAll().antMatchers("/OIP/public/ping").permitAll().antMatchers("/actuator/prometheus").permitAll()
             .antMatchers("/**").authenticated();
		 
		 
		
	}
}