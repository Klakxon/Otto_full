package com.example.Otto.configurations;

import com.example.Otto.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * The SecurityConfig class configures security settings for the Otto application.
 * It extends WebSecurityConfigurerAdapter to provide custom security configuration,
 * including user authentication and authorization settings.
 * This class uses a custom user details service and bcrypt password encoding.
 *
 * @author Dmytro Ukrainets
 * @version 1.0
 * @since 09.06.2024
 */
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * Custom user details service to load user-specific data during authentication.
     */
    private final CustomUserDetailsService userDetailsService;

    /**
     * Configures HTTP security, specifying the access rules for various URL patterns and setting up form-based login
     * and logout.
     *
     * @param http the HttpSecurity object to configure.
     * @throws Exception if an error occurs during configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/perfumes/**", "/registration", "/login", "/error", "/images/**", "/css/**", "/js/**").permitAll()
                .antMatchers("/cart/**").authenticated() // Require authentication for /cart/** endpoints
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll();
    }

    /**
     * Configures authentication by specifying the custom user details service and password encoder.
     *
     * @param auth the AuthenticationManagerBuilder to configure.
     * @throws Exception if an error occurs during configuration.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    /**
     * Defines a bean for the password encoder, using BCrypt with a strength of 8.
     *
     * @return the PasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(8);
    }
}
