package deti.tqs.phihub.configs;

import deti.tqs.phihub.repositories.MedicRepository;
import deti.tqs.phihub.repositories.StaffRepository;
import deti.tqs.phihub.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Generated
@Component
public class SecurityFilter extends OncePerRequestFilter {

    TokenProvider tokenService;
    UserRepository userRepository;
    StaffRepository staffRepository;
    MedicRepository medicRepository;

    public SecurityFilter(TokenProvider tokenService, UserRepository userRepository, StaffRepository staffRepository,
            MedicRepository medicRepository) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.staffRepository = staffRepository;
        this.medicRepository = medicRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = this.recoverToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var tokenSubject = tokenService.validateToken(token);

        UserDetails user = null;

        if (request.getRequestURI().contains("/patient")) {
            user = userRepository.findByUsername(tokenSubject);
        } else if (request.getRequestURI().contains("/staff")) {
            user = staffRepository.findByUsername(tokenSubject);
        } else if (request.getRequestURI().contains("/medic")) {
            user = medicRepository.findByUsername(tokenSubject);
        }

        if (user == null) {
            filterChain.doFilter(request, response);
            return;
        }
        var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            return null;
        return authHeader.replace("Bearer ", "");
    }
}
