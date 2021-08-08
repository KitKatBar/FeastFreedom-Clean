package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	private UserDetailsService aService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//System.out.println("configure order 1");
		http
				.authorizeRequests()
					.antMatchers(
						"/",
						"/default",
						"/kitchen_registration",
						"/user_registration",
						"/registration**",
						"/js/**",
						"/css/**",
						"/img/**",
						"/webjars/**").permitAll()
					.anyRequest().authenticated()
			/*.and()
				.authorizeRequests()
					.antMatchers(
								"/",
								"/kitchen**",
								"/kitchen/**",
								"/js/**",
								"/css/**",
								"/img/**",
								"/webjars/**").permitAll()
					.anyRequest().hasRole("KITCHEN")
			.and()
				.authorizeRequests()
					.antMatchers(
								"/",
								"/user**",
								"/user/**",
								"/js/**",
								"/css/**",
								"/img/**",
								"/webjars/**").permitAll()
					.anyRequest().hasRole("USER")*/
			.and()	
				.formLogin()
					.defaultSuccessUrl("/default")
					.loginPage("/login")
				.permitAll()
			.and()
				.logout()
					.invalidateHttpSession(true)
					.clearAuthentication(true)
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
					.logoutSuccessUrl("/?logout")
				.permitAll()
			.and()
				.csrf().disable();
	}
	/*
	@Configuration
	@Order(1)
	public static class KitchenConfiguration extends WebSecurityConfigurerAdapter {
		
		//@Autowired
		//KitchenService kService;
		
		//@Autowired
		//UserDetailsService kitchenService;
		
		// providing access to some type of urls for reg ,login, css, jss,html images
		// etc
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			//System.out.println("configure order 1");
			http.authorizeRequests()
					.antMatchers(
							"/",
							"/kitchen*",
							"/kitchen_registration",
							"/registration**",
							"/js/**",
							"/css/**",
							"/img/**",
							"/webjars/**").permitAll()
					.anyRequest().hasRole("KITCHEN")
				.and()
					.formLogin()
						.failureUrl("/kitchen_login?error")
						.defaultSuccessUrl("/kitchen_settings")
						.loginPage("/kitchen_login")
					.permitAll()
				.and()
					.logout()
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/?logout")
					.permitAll();
		}
		
		/*@Bean
		public DaoAuthenticationProvider authencationProviderKitchen() {
			System.out.println("in kitchen auth");
			DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
			auth.setUserDetailsService(kitchenService);
			auth.setPasswordEncoder(passwordEncoder());
			return auth;
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) {
			System.out.println("in auth configure");
			auth
				.authenticationProvider(authencationProviderKitchen());
		}*//*
	}
	/*
	@Configuration
	@Order(2)
	public static class UserConfiguration extends WebSecurityConfigurerAdapter {
		
		//@Autowired
		//UserService uService;
		
		//@Autowired
		//UserDetailsService userService;
		
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests()
					.antMatchers(
							"/",
							"/user*",
							"/user_registration",
							"/registration**",
							"/js/**",
							"/css/**",
							"/img/**",
							"/webiars/**").permitAll()
					.anyRequest().hasRole("USER")
				.and()
					.formLogin()
						.failureUrl("/user_login?error")
						.defaultSuccessUrl("/user_homepage")
						.loginPage("/user_login")
					.permitAll()
				.and()
					.logout()
						.invalidateHttpSession(true)
						.clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/?logout")
					.permitAll();
		}
		
		/*@Bean
		public DaoAuthenticationProvider authencationProviderUser() {
			System.out.println("in user auth");
			DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
			auth.setUserDetailsService(userService);
			auth.setPasswordEncoder(passwordEncoder());
			return auth;
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) {
			System.out.println("in auth configure");
			auth
				.authenticationProvider(authencationProviderUser());
		}*//*
	}
	*/
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authencationProvider() {
		//System.out.println("in kitchen auth");
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(aService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}
	
	/*@Bean
	public DaoAuthenticationProvider authencationProviderUser() {
		System.out.println("in user auth");
		DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
		auth.setUserDetailsService(uService);
		auth.setPasswordEncoder(passwordEncoder());
		return auth;
	}*/
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) {
		//System.out.println("in auth configure");
		auth.authenticationProvider(authencationProvider());
	}
}
