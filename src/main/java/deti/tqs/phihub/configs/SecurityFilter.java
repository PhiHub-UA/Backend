package deti.tqs.phihub.configs;
import deti.tqs.phihub.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    TokenProvider tokenService;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

                
        var token = this.recoverToken(request);

        if (token != null) {
            var tokenSubject = tokenService.validateToken(token);
            var user = userRepository.findByUsername(tokenSubject);
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication); // Set authentication context
            } else {
                // Handle invalid token (e.g., return 401 Unauthorized)
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}
