package com.sm.seoulmate.domain.user.service;

import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.user.dto.UserInfoResponse;
import com.sm.seoulmate.domain.user.dto.UserResponse;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.exception.ErrorCode;
import com.sm.seoulmate.exception.ErrorException;
import com.sm.seoulmate.util.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * 로그인 정보 조회
     */
    public UserResponse getLoginInfo() {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));
        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .loginType(user.getLoginType())
                .likedCount(user.getAttractionLikes().size())
                .commentCount(user.getComments().size())
                .badgeCount((int) user.getChallengeStatuses().stream()
                        .filter(x -> Objects.equals(x.getChallengeStatusCode(), ChallengeStatusCode.COMPLETE))
                        .count())
                .build();
    }

    /**
     * 닉네임 변경
     */
    public UserInfoResponse updateNickname(String nickname) {
        String newNick = StringUtils.trimToEmpty(nickname);
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        //중복체크
        userRepository.findByNickname(newNick).ifPresent(x -> {throw new ErrorException(ErrorCode.NICK_DUPLICATE); });

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        user.setNickname(StringUtils.trimToEmpty(nickname));
        userRepository.save(user);

        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .build();
    }

    /**
     * 회원 탈퇴
     */
    public void deleteUser(Long userId) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        if(!Objects.equals(user.getId(), loginUser.getId())) {
            throw new ErrorException(ErrorCode.PERMISSION_DENIED);
        }

        userRepository.delete(user);
    }
}
