package com.springboot.comercio.filter;

import com.springboot.comercio.exception.RateLimitExceededException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final HandlerExceptionResolver handlerExceptionResolver;
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            if(!"/api/v1/auth/login".equals(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }

            String ip = request.getRemoteAddr();
            Bucket bucket = buckets.computeIfAbsent(ip, key -> criarBucket());

            if(bucket.tryConsume(1)) {
                filterChain.doFilter(request, response);
            } else {
                handlerExceptionResolver.resolveException(
                        request, response, null,
                        new RateLimitExceededException("Muitas tentativas de login. Tente novamente mais tarde.")
                );
            }


    }

    private Bucket criarBucket() {
        return Bucket.builder().addLimit(limit -> limit.capacity(5).refillGreedy(5, Duration.ofMinutes(1)))
                .build();
    }
}
