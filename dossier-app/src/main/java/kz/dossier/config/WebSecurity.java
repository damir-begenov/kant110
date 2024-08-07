package kz.dossier.config;

import kz.dossier.security.SessionTimeoutFilter;
import kz.dossier.security.jwt.AuthEntryPointJwt;
import kz.dossier.security.jwt.AuthTokenFilter;
import kz.dossier.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true)
public class WebSecurity { // extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private SessionTimeoutFilter sessionTimeoutFilter;

    
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http.cors().and().csrf().disable()
//                 .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                 .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
// //                .authorizeRequests().antMatchers("/api/pandora/auth/**").permitAll()
// //                .antMatchers("/**").permitAll();
// //        .anyRequest().authenticated();
//                 .authorizeHttpRequests((authz) -> authz
//                         .requestMatchers("/api/pandora/auth/**", "/swagger-ui/**").permitAll()
//                         .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
//                         .anyRequest().authenticated()
//                 );

//         http.authenticationProvider(authenticationProvider());

//         http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http.cors().and().csrf().disable()
        //         .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        //         .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        //         .authorizeRequests(authz -> authz
        //                 .antMatchers("/api/pandora/auth/**", "/swagger-ui/**").permitAll()
        //                 .antMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
        //                 .anyRequest().authenticated()
        //         );
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests().antMatchers("/api/pandora/auth/**").permitAll()
//                .antMatchers("/**").permitAll();
//        .anyRequest().authenticated();
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/api/pandora/auth/**", "/swagger-ui/**").permitAll()
                        .requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**").permitAll()
                        .anyRequest().authenticated()
                );
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(sessionTimeoutFilter, SessionManagementFilter.class);

        return http.build();
    }
}

