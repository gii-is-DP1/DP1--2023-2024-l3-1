package com.dobble.configuration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.sql.DataSource;

import com.dobble.configuration.jwt.AuthEntryPointJwt;
import com.dobble.configuration.jwt.AuthTokenFilter;
import com.dobble.configuration.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	@Autowired
	DataSource dataSource;

	@Bean
	protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.headers((headers) -> headers.frameOptions((frameOptions) -> frameOptions.disable()))
				.exceptionHandling((exepciontHandling) -> exepciontHandling.authenticationEntryPoint(unauthorizedHandler))
				.authorizeHttpRequests(authorizeRequests -> authorizeRequests
						.requestMatchers("/webjars/**", "/static/**", "/v3/api-docs/**", "/swagger-resources/**",
								"/swagger-ui.html", "/swagger-ui/**")
						.permitAll()
						.requestMatchers("/api/docs/**").permitAll()
						.requestMatchers("/api/developers/**").permitAll()
						.requestMatchers("/api/player/login", "/api/player/validate", "/api/player/signup").permitAll()
						.requestMatchers("/api/player/**").authenticated()
						.requestMatchers("/api/achievements/**").authenticated()
						.requestMatchers("/api/games/**").authenticated()
						.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
						.anyRequest().authenticated())
				.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
