package com.ecoomerce.autorisation.config;


import com.ecoomerce.autorisation.config.auth.mock.MockWebSSOConfigurer;
import com.ecoomerce.autorisation.services.AccountService;
import com.ecoomerce.autorisation.utils.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
    prePostEnabled = true,
    securedEnabled = true,
    jsr250Enabled = true)
@Import(SecurityProblemSupport.class)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final ApplicationProperties props;
    private final AccountService accountService;
    private final SecurityProblemSupport problemSupport;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MessageUtil messages;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        http.cors();
        http.apply(securityMockWebSSOConfigurerAdapter());
        http.exceptionHandling().authenticationEntryPoint(problemSupport).accessDeniedHandler(problemSupport);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/login/**").permitAll();
        http.authorizeRequests().antMatchers("/pagedevie*").permitAll();
        http.authorizeRequests().antMatchers("/actuator/*").permitAll().antMatchers("/").permitAll();
        http.authorizeRequests().antMatchers("/pagedevie*").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), messages, props));
        http.addFilterBefore(new JWTAuthorizationFilter(props), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(props.getCors().getAllowedOrigins());
        configuration.setAllowedMethods(props.getCors().getAllowedMethods());
        configuration.setAllowedHeaders(props.getCors().getAllowedHeaders());
        configuration.setExposedHeaders(props.getCors().getExposedHeaders());
        configuration.setAllowCredentials(props.getCors().getAllowCredentials());
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private MockWebSSOConfigurer securityMockWebSSOConfigurerAdapter() {
        return new MockWebSSOConfigurer(props, accountService);
    }

}
