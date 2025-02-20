package farming.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class AuthorizationConfigurations {

	//@Autowired
	//CustomWebSecurity customWebSecurity;
	
	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults()).csrf(csrf -> csrf.disable())
		.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		//http.addFilterBefore(new SessionTokenFilter(), BasicAuthenticationFilter.class);
		

		http.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(HttpMethod.POST, "/account/register").permitAll()
				.requestMatchers("/account/revoke/*", "/account/activate/*", 
						"/account/user/*/role/*").hasRole("ADMIN")

				.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name"))
								.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
				
				.requestMatchers(HttpMethod.GET, "/account/*/{login}")
				.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))

				.anyRequest().authenticated());
		return http.build();
	}
}