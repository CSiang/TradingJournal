package tfip_project.financial_analysis.Security.Filters;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    // @Value("${trading.app.jwtSecret}")
    // private String secretKey;
    private String secretKey = "CS_TradingApp_SecretKey";

    Logger logger = Logger.getLogger(CustomAuthenticationFilter.class.getName());

    // This is to authenticate the user
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    } 


    // This is to authenticate the user. Authentication is done by AuthenticationManager.authenticate() method, if authentication successful, then it will proceed to the below successfulAuthentication() method to generate JWT token. Authentication is done within the AuthenticationManager by the AuthenticationProvider interface (AuthenticationManager uses DaoAuthenticationProvider (with help of UserDetailsService & PasswordEncoder) to validate instance of UsernamePasswordAuthenticationToken, then returns a fully populated Authentication instance on successful authentication).
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
    throws AuthenticationException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        logger.info("Username is %s\n".formatted(username));
        logger.info("Password is %s\n".formatted(password));

        UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }
    

    // This is to configure to send the access and refresh token whenever the login is successful.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

    User user  = (User) authResult.getPrincipal();
    // Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
    logger.info("Secret key: %s\n".formatted(secretKey));
    Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());

    // Create token
    // 24*60*60*1000 is for 1 day.
    Date expiryDate = new Date(System.currentTimeMillis() + 24*60*60*1000);

    String accessToken = JWT.create().withSubject(user.getUsername())
                            .withExpiresAt(expiryDate)
                            .withIssuer(request.getRequestURI().toString())
                            .withClaim("roles",user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                            .sign(algorithm);

    // This is for refresh token
    String refreshToken = JWT.create().withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 24*60*60*1000))
                        .withIssuer(request.getRequestURI().toString())
                        .sign(algorithm);

    // This is to set the tokens onto the response header.
    // response.setHeader("accessToken",accessToken );
    // response.setHeader("refreshToken",refreshToken );

    // Below is to make the tokens to be contained at the Response body.

    Map<String, String> tokens = new HashMap<>();
    tokens.put("username",user.getUsername());
    tokens.put("accessToken",accessToken);
    tokens.put("accessTokenExpiry",expiryDate.toString());
    tokens.put("accessTokenExpiryLong",String.valueOf(expiryDate.getTime()));
    tokens.put("refreshToken",refreshToken );

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);

    logger.info("Authenticated!!!!");
    
    // This is Jackson ObjectMapper, is to convert the java object into Json format.
    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }


}
