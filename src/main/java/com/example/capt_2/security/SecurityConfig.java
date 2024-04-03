package com.example.capt_2.security;

import com.example.capt_2.security.filters.JwtAuthenticationFilters;
import com.example.capt_2.security.filters.JwtAutorizationFilter;
import com.example.capt_2.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    JwtAutorizationFilter jwtAutorizationFilter;
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilters jwtAuthenticationFilters = new JwtAuthenticationFilters(jwtUtils);
        jwtAuthenticationFilters.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilters.setFilterProcessesUrl("/login");

        return httpSecurity
                .csrf(config ->config.disable())
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers("/hello").permitAll();
                    //auth.requestMatchers("/accessAdmin").hasAnyRole("ADMIN,USER");
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session ->{
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                })
                .addFilter(jwtAuthenticationFilters)
                .addFilterBefore(jwtAutorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
//    @Bean
//    UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("user")
//                .password("1234")
//                .roles()
//                .build());
//        return manager;
//    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    AuthenticationManager authenticationManager(HttpSecurity httpSecurity,PasswordEncoder passwordEncoder) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and().build();
    }

   /* public static void main(String[] args){
        System.out.println(new BCryptPasswordEncoder().encode("12342"));
    }*/
}
