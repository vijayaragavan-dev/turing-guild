package com.turingguild.tgms.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimiterFilter extends OncePerRequestFilter {

    private final ConcurrentHashMap<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private static final int MAX_REQUESTS = 10;
    private static final long WINDOW_MS = 60_000;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/auth/login")) {
            String clientIp = getClientIP(request);
            long now = System.currentTimeMillis();
            String key = clientIp + ":" + (now / WINDOW_MS);

            AtomicInteger count = requestCounts.computeIfAbsent(key, k -> new AtomicInteger(0));
            if (count.incrementAndGet() > MAX_REQUESTS) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("{\"message\":\"Too many login attempts. Please try again later.\"}");
                return;
            }
            requestCounts.entrySet().removeIf(entry -> {
                long keyTime = Long.parseLong(entry.getKey().split(":")[1]);
                return (now / WINDOW_MS) - keyTime > 1;
            });
        }
        filterChain.doFilter(request, response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
