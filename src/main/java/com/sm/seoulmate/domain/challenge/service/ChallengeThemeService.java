package com.sm.seoulmate.domain.challenge.service;

import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.service.AttractionService;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeResponse;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;
import com.sm.seoulmate.domain.challenge.mapper.ChallengeThemeMapper;
import com.sm.seoulmate.domain.challenge.repository.*;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.exception.ErrorCode;
import com.sm.seoulmate.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeThemeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeThemeRepository challengeThemeRepository;
    private final ChallengeStatusRepository challengeStatusRepository;
    private final ChallengeLikesRepository challengeLikesRepository;
    private final CulturalEventRepository culturalEventRepository;
    private final AttractionIdRepository attractionIdRepository;
    private final AttractionService attractionService;
    private final UserRepository userRepository;

    /**
     * 챌린지 테마 목록 조회
     */
    public List<ChallengeThemeResponse> getTheme() {
        return challengeThemeRepository.findAll().stream().map(ChallengeThemeMapper::toResponse).toList();
    }

    /**
     * 챌린지 테마 생성
     */
    public ChallengeThemeResponse createTheme(ChallengeThemeCreateRequest request) {
        ChallengeTheme theme = ChallengeThemeMapper.toEntity(request);
        return ChallengeThemeMapper.toResponse(challengeThemeRepository.save(theme));
    }

    /**
     * 챌린지 테마 삭제
     */
    public void deleteChallengeTheme(Long id) throws BadRequestException {
        ChallengeTheme challengeTheme = challengeThemeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_THEME_NOT_FOUND));
        challengeThemeRepository.delete(challengeTheme);
    }
}
