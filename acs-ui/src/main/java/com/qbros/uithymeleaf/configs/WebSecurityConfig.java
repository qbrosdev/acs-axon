package com.qbros.uithymeleaf.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("secured")
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = getPassEncoder();
        auth
                .inMemoryAuthentication()
                .passwordEncoder(getPassEncoder())
                .withUser(User.withUsername("admin").password(encoder.encode("pass")).roles("ADMIN"))
                .withUser(User.withUsername("user").password(encoder.encode("pass")).roles("USER"));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
//                .loginPage("/login")
                .and()
                // .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                //			.invalidateHttpSession(true)
                //			.deleteCookies("JSESSIONID").and()
                .authorizeRequests()
                .mvcMatchers("/login", "/logout").permitAll()
                .mvcMatchers("/static/css/**", "static/img/**", "static/js/**", "static/mdb/**").permitAll()
                .mvcMatchers("/**").authenticated()
                .mvcMatchers("/sentry/**").hasAnyRole("ADMIN", "SENTRY")
                .mvcMatchers("/manager/**").hasAnyRole("ADMIN");
    }

    @Bean
    public PasswordEncoder getPassEncoder() {
        return new BCryptPasswordEncoder();
    }
}
