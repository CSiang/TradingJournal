package tfip_project.financial_analysis.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tfip_project.financial_analysis.Security.Filters.CustomAuthenticationFilter;
import tfip_project.financial_analysis.Security.Filters.CustomAuthorizationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationConfiguration authConfig;

    @Autowired
    private UserDetailsService userDelSvc;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
         
        authProvider.setUserDetailsService(userDelSvc);
        authProvider.setPasswordEncoder(passwordEncoder());
     
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
      return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        
        http.csrf(csrf -> csrf.disable());
        // Check the import above for the "withDefault()" method, this is not suggested by IDE.
        http.cors(withDefaults());
        http.sessionManagement(sesMgnt -> sesMgnt.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // http.authorizeHttpRequests(
        //     authorize -> authorize.requestMatchers(HttpMethod.GET, "/login/**", "/token/refresh/**")
        //                           .permitAll()
        //                           .requestMatchers(HttpMethod.POST, "/auth/register/*")
        //                           .permitAll()
        //                           );

        http.authorizeHttpRequests(
            authorize -> authorize.requestMatchers(HttpMethod.POST, "/auth/register")
                                  .permitAll()
                                  .requestMatchers(HttpMethod.GET, "/auth/forgetPassword", 
                                                    "/auth/resetPassword","api/search","api/search/*")
                                  .permitAll()
                                  .requestMatchers("/login","/login/**", "/*", "/record")
                                  .permitAll()
                                    );
        http.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(HttpMethod.GET, "/auth/users")
                                .hasAnyAuthority("ADMIN")
                                );
        
        http.authorizeHttpRequests(httpRequest -> 
                httpRequest.anyRequest().authenticated()
        );
        
        

        http.addFilter(new CustomAuthenticationFilter(this.authenticationManager(authConfig)));

        // this filter will be activated before the UsernamePasswordAuthenticationFilter filter (1 of the system default filters).
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
