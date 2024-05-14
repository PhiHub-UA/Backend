package deti.tqs.phihub.configs;

import deti.tqs.phihub.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Generated
@Component
public class SecurityFilter extends OncePerRequestFilter {

    TokenProvider tokenService;
    UserRepository userRepository;

    public SecurityFilter(TokenProvider tokenService, UserRepository userRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = this.recoverToken(request);

        if (token != null) {
            var tokenSubject = tokenService.validateToken(token);
            var user = userRepository.findByUsername(tokenSubject);
            if (user == null) {
                SecurityContextHolder.getContext().setAuthentication(null);
                filterChain.doFilter(request, response);
                return;
            }
            var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication); // Set authentication context
            } else {
                SecurityContextHolder.getContext().setAuthentication(null);
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
