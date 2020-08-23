package com.vighnesh153.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/user/register*").permitAll()
                .antMatchers("/mentee-dashboard*").hasAnyRole("MENTEE", "ADMIN")
                .antMatchers("/mentor-dashboard*").hasAnyRole("MENTOR", "ADMIN")
                .antMatchers("/admin-dashboard*").hasAnyRole("ADMIN")
                .antMatchers("/email/get*").hasAuthority("READ_EMAIL_ADDRESS")
                .and()
                .formLogin()
                .loginProcessingUrl("/processLoginHere");
    }

    /*
    * Disables the authentication part of Spring Security (no login configuration is done now)
    */
    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return authentication -> {
            throw new AuthenticationServiceException("Cannot authenticate " + authentication);
        };
    }
}
