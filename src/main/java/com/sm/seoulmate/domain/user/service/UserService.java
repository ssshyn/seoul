package com.sm.seoulmate.domain.user.service;

import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void setDefaultLanguage(LanguageCode languageCode) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setLanguageCode(languageCode);

        // 새 Authentication 객체 생성 (authorities는 기존 auth에서 그대로 가져와야 함)
        Authentication newAuth = new UsernamePasswordAuthenticationToken(user, null, List.of());
        // SecurityContextHolder 에 다시 저장
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        userRepository.save(user);
    }
}
