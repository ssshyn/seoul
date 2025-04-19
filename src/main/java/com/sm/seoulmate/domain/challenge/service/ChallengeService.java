package com.sm.seoulmate.domain.challenge.service;

import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.challenge.dto.ChallengeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.ChallengeResponse;
import com.sm.seoulmate.domain.challenge.dto.ChallengeSearchCondition;
import com.sm.seoulmate.domain.challenge.dto.ChallengeUpdateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeResponse;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;
import com.sm.seoulmate.domain.challenge.mapper.ChallengeMapper;
import com.sm.seoulmate.domain.challenge.mapper.ChallengeThemeMapper;
import com.sm.seoulmate.domain.challenge.repository.ChallengeRepository;
import com.sm.seoulmate.domain.challenge.repository.ChallengeThemeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeThemeRepository challengeThemeRepository;
    private final AttractionIdRepository attractionIdRepository;

    /**
     * 챌린지 조회
     */
    public Page<ChallengeResponse> getChallenge(ChallengeSearchCondition condition, Pageable pageable) {
        String keyword = StringUtils.trimToEmpty(condition.keyword());
        Page<Challenge> challengePage = challengeRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return challengePage.map(ChallengeMapper::toResponse);
    }

    /**
     * 챌린지 테마 조회
     */
    public List<ChallengeThemeResponse> getChallengeTheme() {
        return challengeThemeRepository.findAll().stream().map(ChallengeThemeMapper::toResponse).toList();
    }

    /**
     * 챌린지 생성
     */
    public ChallengeResponse create(ChallengeCreateRequest request) throws BadRequestException {
        ChallengeTheme theme = challengeThemeRepository.findById(request.challengeThemeId())
                .orElseThrow(() -> new EntityNotFoundException("챌린지 테마를 다시 확인해 주세요."));

        // 관광지 id 예외처리
        List<Long> requestAttractionIds = request.attractionIdList().stream().distinct().toList();
        if(requestAttractionIds.isEmpty() ||
                requestAttractionIds.stream().distinct().toList().size() > 5) {
            throw new BadRequestException("관광지 id를 확인해 주세요.");
        }

        // 관광지 목록 체크
        List<AttractionId> attractionIds = new ArrayList<>();
        request.attractionIdList().forEach(
                attractionId -> {
                    AttractionId attraction = attractionIdRepository.findById(attractionId).orElseThrow(() -> new EntityNotFoundException("관광지 ID를 다시 확인해 주세요."));
                    attractionIds.add(attraction);
                }
        );

        Challenge challenge = ChallengeMapper.toEntity(request, theme, attractionIds);
        return ChallengeMapper.toResponse(challengeRepository.save(challenge));
    }

    /**
     * 챌린지 수정
     */
    public ChallengeResponse update(ChallengeUpdateRequest request) throws BadRequestException {
        ChallengeTheme theme = challengeThemeRepository.findById(request.challengeThemeId())
                .orElseThrow(() -> new EntityNotFoundException("챌린지 테마를 다시 확인해 주세요."));

        // 관광지 id 예외처리
        List<Long> requestAttractionIds = request.attractionIdList().stream().distinct().toList();
        if(requestAttractionIds.isEmpty() ||
                requestAttractionIds.stream().distinct().toList().size() > 5) {
            throw new BadRequestException("관광지 id를 확인하세요.");
        }

        // 관광지 목록 체크
        List<AttractionId> attractionIds = new ArrayList<>();
        request.attractionIdList().forEach(
                attractionId -> {
                    AttractionId attraction = attractionIdRepository.findById(attractionId).orElseThrow(() -> new EntityNotFoundException("관광지 ID를 다시 확인해 주세요."));
                    attractionIds.add(attraction);
                }
        );

        Challenge challenge = ChallengeMapper.toUpdatedEntity(request, theme, attractionIds);
        return ChallengeMapper.toResponse(challengeRepository.save(challenge));
    }

    /**
     * 챌린지 삭제
     */
    public void deleteChallenge(Long id) throws BadRequestException {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new BadRequestException("Id를 다시 확인해 주세요."));
        challengeRepository.delete(challenge);
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
        ChallengeTheme challengeTheme = challengeThemeRepository.findById(id).orElseThrow(() -> new BadRequestException("Id를 다시 확인해 주세요."));
        challengeThemeRepository.delete(challengeTheme);
    }
}
