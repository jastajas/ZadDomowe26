package com.example.socialnet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/koduj").permitAll()
                    .antMatchers("/login").permitAll()
                    .antMatchers("/registrationForm").permitAll()
                    .antMatchers("/register").permitAll()
                    .antMatchers("/main").hasRole("USER")
                    .antMatchers("/api/users/").hasRole("USER")
                    .antMatchers("/editUser").hasRole("USER")
                    .antMatchers("/executeEdition").hasRole("USER")
                    .antMatchers("/userDetails").hasRole("USER")
                    .antMatchers("/invitations").hasRole("USER")
                    .antMatchers("/allFriends").hasRole("USER")
                    .antMatchers("/adminManager").hasRole("ADMIN")
                    .antMatchers("/blockUser").hasRole("ADMIN")
                    .antMatchers("/deleteUser").hasRole("ADMIN")
                    .antMatchers("/sendMail").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .logout()
                .logoutUrl("/logout").logoutSuccessUrl("/")
                .and().formLogin().loginPage("/login");
    }

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from user where username=?")
                .authoritiesByUsernameQuery("select username, role from user_role where username=?");
    }
}
