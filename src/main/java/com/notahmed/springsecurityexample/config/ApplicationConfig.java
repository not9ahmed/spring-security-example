package com.notahmed.springsecurityexample.config;

import com.notahmed.springsecurityexample.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


// TODO read more about these annotations
@Configuration

// lombok
//@RequiredArgsConstructor
public class ApplicationConfig {


    // dependency injection
    private final UserRepository userRepository;

    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    /*
     the actual return but can be replaced with lambda

        Actual interface
        UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
            }
        }
     */

    @Bean
    public UserDetailsService userDetailsService() {

        // fetch user from database
        // if not exists then throw UsernameNotFoundException exception

        return username -> userRepository.findByEmail(username)
                .orElseThrow(() ->  new UsernameNotFoundException("User not found"));
    }



    /*
    Data access object to fetch user details and encode password
     */

    @Bean
    public AuthenticationProvider authenticationProvider(){

        // implementation of the class
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // setting the UserDetailsService which is from above bean
        authProvider.setUserDetailsService(userDetailsService());


        // setting the password encoder
        // for example I can use other or custom password encoder
        authProvider.setPasswordEncoder(passwordEncoder());


        return authProvider;
    }



    /*
    method to set the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


}
