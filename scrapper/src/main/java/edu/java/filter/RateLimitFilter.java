package edu.java.filter;

import edu.java.configuration.ratelimit.RateLimitManager;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {
    private static final int TOO_MANY_REQUESTS = 429;
    private final RateLimitManager rateLimitManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        Bucket bucket = rateLimitManager.resolveBucket(request.getRemoteAddr());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(TOO_MANY_REQUESTS, "Too many requests");
        }
    }
}
