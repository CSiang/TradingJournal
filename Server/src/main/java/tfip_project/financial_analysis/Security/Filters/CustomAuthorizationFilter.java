package tfip_project.financial_analysis.Security.Filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    // @Value("${trading.app.jwtSecret}")
    private String secretKey = "CS_TradingApp_SecretKey";

    Logger logger = Logger.getLogger(CustomAuthorizationFilter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // This is to check the request path, if user is coming to "/login", it will allow the user to pass this current filter and proceed to next filter chain.
        if(request.getServletPath().equals("/login")  || 
            request.getServletPath().equals("/api/token/refresh") ){

                filterChain.doFilter(request, response);
            } else {
    // to check if the user has authorization header that is the key of the token.
    String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    logger.info("Authorization header is %s.".formatted(authorizationHeader));
    // It is "bearer " which is end with a space.
    // We will send a request with the JWT token back to front end after authentication, and at the httpheader we will send it with the name that prefixed with "bearer "
    if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){

        try{
            // This token is to get the token content. At the HttpRequest header, the token will be started with the prefix "bearer ", hence to get only the token value, we use the substring method, and the starting index will be equal to the length of the word "bearer ".
            logger.info("Performing authorization...");
            String token  = authorizationHeader.substring("Bearer ".length());

            logger.info("Processed token: %s".formatted(token));

            // This algorithm is copied from the CustomAuthenticationFilter class, successfulAuthentication() method. Because it used this method to sign the token, hence we will have to use this same algorithm to get the token.
            // Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes());

            // Now we can use the algorithm to verify the token.
            // 1. Build the verifier.
            JWTVerifier verifier = JWT.require(algorithm).build();

            //2. Decode the token.
            DecodedJWT decodedJWT  = verifier.verify(token);

            // 3. Once we verify that the token is valid, then we get the username.
            String username = decodedJWT.getSubject();

            // 4. Get the role. The claim-name "roles" is the 1 we assign in CustomAuthenticationFilter class, successfulAuthentication() method, within the JWT.withClaim() method..
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

            // 5. Convert the role into GrantedAuthority object (SimpleGrantedAuthority extends GrantedAuthority)
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Arrays.stream(roles).forEach( role -> {
                    authorities.add(new SimpleGrantedAuthority(role));
                }
            );
            // We have no password, so credential is null.
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authorities);
            
            logger.info("Authentication Token: %s\n".formatted(authenticationToken));

            // This tells spring security about the user and its role and what the user can do in this application.
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            // Then proceed the request to the next filter.
            filterChain.doFilter(request, response);

        } catch (Exception ex){
            // This is the portion when token invalid, expired, or not able to be verified.
            
            logger.log(Level.SEVERE, "Error logging in: %s\n".formatted(ex.getMessage()));
            response.setHeader("error", ex.getMessage());
            response.setStatus(HttpStatus.FORBIDDEN.value());

            // Send the error message at the response body.
            Map<String, String> error = new HashMap<>();
            error.put("error_message", ex.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            
            // This is Jackson Objectmapper, is to convert the java object into Json format.
            new ObjectMapper().writeValue(response.getOutputStream(), error);

        }
    } else {
        logger.info("something missing..not going through authorization..");
        filterChain.doFilter(request, response);
    }

    }


    }
    
}

