package dev.ime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		return http
				.authorizeHttpRequests( auth -> {
					auth.requestMatchers("/api/**").permitAll();
		            auth.requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll();
					auth.requestMatchers("/actuator/metrics").authenticated();
					auth.requestMatchers("/actuator/**").permitAll();
					auth.anyRequest().authenticated();
				})
                .csrf(AbstractHttpConfigurer::disable)			
                .oauth2ResourceServer( resourceServer -> resourceServer.jwt(Customizer.withDefaults()))
				.build();
		
	}
	
}
