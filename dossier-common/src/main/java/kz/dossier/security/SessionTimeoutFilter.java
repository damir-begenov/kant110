package kz.dossier.security;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.servlet.FilterChain;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SessionTimeoutFilter extends OncePerRequestFilter {

    private static final long MAX_INACTIVE_SESSION_TIME = 15 * 60 * 1000; // 15 minutes


    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            long lastAccessedTime = session.getLastAccessedTime();
            if (System.currentTimeMillis() - lastAccessedTime > MAX_INACTIVE_SESSION_TIME) {
                session.invalidate();
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session expired due to inactivity");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
