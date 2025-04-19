package com.sm.seoulmate.config;

import com.google.common.base.Strings;
import com.sm.seoulmate.domain.login.entity.User;
import com.sm.seoulmate.domain.login.repository.UserRepository;
import com.sm.seoulmate.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        if(Strings.isNullOrEmpty(email)) {
            throw new RuntimeException("Invalid Login");
        }

        Optional<User> user = userRepository.findById(email);
        if(user.isEmpty()) {
            userRepository.save(User.builder()
                            .userId(email)

                    .build());
        }
        // ✅ JWT 발급
        String jwt = jwtUtil.generateAccessToken(email);

        // ✅ 클라이언트에 토큰 전달 (예: 리디렉션 또는 JSON 응답)
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"token\": \"" + jwt + "\"}");
//        System.out.println("{\"token\": \"" + jwt + "\"}");
//        response.sendRedirect("/swagger-ui/index.html");
    }
}
