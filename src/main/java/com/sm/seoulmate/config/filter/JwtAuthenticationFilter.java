package com.sm.seoulmate.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.exception.ErrorCode;
import com.sm.seoulmate.exception.ErrorException;
import com.sm.seoulmate.exception.ErrorResponse;
import com.sm.seoulmate.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            try {
                String accessToken = authorization.split(" ")[1];

                validateAccessToken(accessToken);

                String userId = jwtUtil.parseToken(accessToken).getSubject();
                Long id = Long.parseLong(userId);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = userRepository.findById(id) // 또는 findByUsername, findById 등
                            .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

                    LoginInfo loginInfo = LoginInfo.of(user);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(loginInfo, null, List.of());

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                }

                filterChain.doFilter(request, response);
            } catch (ErrorException e) {
                handleErrorResponse(response, e); // 응답 여기서 직접 작성
                return;
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private void handleErrorResponse(HttpServletResponse response, ErrorException e) throws IOException {
        ErrorCode errorCode = e.getErrorCode();

        response.setStatus(errorCode.getStatus());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String body = objectMapper.writeValueAsString(ErrorResponse.of(errorCode));
        response.getWriter().write(body);
    }

    private void validateAccessToken(String accessToken) {
        jwtUtil.isValidToken(accessToken);
    }

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            String token = authHeader.substring(7);
//            try {
//                String userId = jwtUtil.parseToken(token).getSubject();
//                Long id = Long.parseLong(userId);
//
//                if (SecurityContextHolder.getContext().getAuthentication() == null) {
//                    User user = userRepository.findById(id) // 또는 findByUsername, findById 등
//                            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//                    LoginInfo loginInfo = LoginInfo.of(user);
//
//                    UsernamePasswordAuthenticationToken authentication =
//                            new UsernamePasswordAuthenticationToken(loginInfo, null, List.of());
//
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            } catch (Exception e) {
//                logger.error("Invalid JWT token: {}");
//            }
//
//        }
//
//        filterChain.doFilter(request, response);
//    }

}
