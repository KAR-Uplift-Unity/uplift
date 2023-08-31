package com.KARUpliftUnity.security;

import com.KARUpliftUnity.models.User;
import com.KARUpliftUnity.repositories.UserRepository;
import com.KARUpliftUnity.services.UserDetailsLoader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private UserDetailsLoader usersLoader;

    private UserRepository userDao;

    public SecurityConfiguration(UserDetailsLoader usersLoader, UserRepository userDao) {
        this.usersLoader = usersLoader;
        this.userDao = userDao;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthHandler(){

        return new AuthenticationSuccessHandler() {

            @Override
            public void onAuthenticationSuccess(HttpServletRequest request,
                                                HttpServletResponse response,
                                                Authentication authentication)
                    throws IOException, ServletException {

                String redirectUrl = (String)request.getSession().getAttribute("redirectUrl");

                User user = (User) authentication.getPrincipal();

                long id = user.getId();
                boolean userArchive = user.getArchive();


                if(redirectUrl != null){
                    if(userArchive){
                        User user1 = userDao.findUserById(id);
                        user1.setArchive(false);
                        user1.setArchiveDate(null);
                        userDao.save(user1);
                    }
                    response.sendRedirect(redirectUrl);
                }
                else {
                    if(userArchive){
                        User user1 = userDao.findUserById(id);
                        user1.setArchive(false);
                        user1.setArchiveDate(null);
                        userDao.save(user1);
                    }
                    response.sendRedirect("/");
                }

            }

        };

    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        /* Pages that require authentication
                         * only authenticated users can create and edit ads */
                        .requestMatchers("/posts/create", "/posts/*/edit", "/profile",
                                "/settings", "/admin-panel", "/admin-panel/*", "/posts/*/comments", "/posts/*/toggle-like",
                                "/posts/*/archive", "/posts/*/unarchive", "/settings", "/update-password",
                                "/update-details", "/update-profile-image", "/posts/*/delete-image", "/comments/*/edit",
                                "/comments/*/delete", "/report-post", "/report-comment", "/admin-panel/posts/*", "/posts/*/review",
                                "/admin-panel/comments/*", "/admin-panel/comments/{id}/un-flag", "/admin-panel/posts/{id}/un-flag", "/user/archive/*").authenticated()
                        /* Pages that do not require authentication
                         * anyone can visit the home page, register, login, and view ads */
                        .requestMatchers("/", "/posts", "/posts/*", "/signup", "/login", "/contact_us", "/mission", "/feed", "/posts/search", "/favicon.ico", "/posts/category/*").permitAll()
                        // allow loading of static resources
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                )
                /* Login configuration */
                .formLogin((login) -> login.loginPage("/login").defaultSuccessUrl("/").successHandler(customAuthHandler()))

                /* Logout configuration */
                .logout((logout) -> logout.logoutSuccessUrl("/"))
                .httpBasic(withDefaults());
        return http.build();
    }

}

