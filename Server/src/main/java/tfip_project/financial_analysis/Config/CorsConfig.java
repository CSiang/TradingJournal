package tfip_project.financial_analysis.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    
    @Bean
    public WebMvcConfigurer configureCors() {
        // must be "/**", "/*" doesn't work for path.
        //https://docs.spring.io/spring-framework/docs/5.3.x/javadoc-api/org/springframework/util/AntPathMatcher.html
        return new EnableCors("/**","*");
    }
}