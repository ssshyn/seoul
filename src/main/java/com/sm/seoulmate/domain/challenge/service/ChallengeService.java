package com.sm.seoulmate.domain.challenge.service;

import com.google.common.base.Strings;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.challenge.dto.challenge.*;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeResponse;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeLikes;
import com.sm.seoulmate.domain.challenge.entity.ChallengeStatus;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.challenge.mapper.ChallengeMapper;
import com.sm.seoulmate.domain.challenge.mapper.ChallengeThemeMapper;
import com.sm.seoulmate.domain.challenge.repository.ChallengeLikesRepository;
import com.sm.seoulmate.domain.challenge.repository.ChallengeRepository;
import com.sm.seoulmate.domain.challenge.repository.ChallengeStatusRepository;
import com.sm.seoulmate.domain.challenge.repository.ChallengeThemeRepository;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.util.UserInfoUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeThemeRepository challengeThemeRepository;
    private final ChallengeStatusRepository challengeStatusRepository;
    private final ChallengeLikesRepository challengeLikesRepository;
    private final AttractionIdRepository attractionIdRepository;
    private final UserRepository userRepository;

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
     * 챌린지 상세 조회
     */
    public ChallenegeDetailResponse getDetail(LanguageCode languageCode, Long id) throws BadRequestException {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("챌린지를 찾을 수 없습니다."));

        String userId = UserInfoUtil.getUserId();
        Boolean isLiked = null;
        ChallengeStatusCode challengeStatusCode = null;

        if(!Strings.isNullOrEmpty(userId)) {
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
            // 찜 여부 체크
            Optional<ChallengeLikes> likesOptional = user.getChallengeLikes().stream().filter(likes -> Objects.equals(likes.getChallenge().getId(), challenge.getId())).findFirst();
            isLiked = likesOptional.isPresent();

            // 진행상태 체크
            Optional<ChallengeStatus> statusOptional = user.getChallengeStatuses().stream().filter(status -> Objects.equals(status.getChallenge().getId(), challenge.getId())).findFirst();
            if(statusOptional.isPresent()) {
                challengeStatusCode = statusOptional.get().getChallengeStatusCode();
            }
        }

        return ChallengeMapper.toDetailResponse(challenge, languageCode, isLiked, challengeStatusCode);
    }

    /**
     * 챌린지 상태 변경
     */
    public ChallengeStatusResponse updateStatus(Long id, ChallengeStatusCode challengeStatusCode) {
        String userId = UserInfoUtil.getUserId();

        if (Strings.isNullOrEmpty(userId)) {
            throw new UsernameNotFoundException("로그인 후 이용 가능합니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("챌린지를 찾을 수 없습니다."));

        Optional<ChallengeStatus> challengeStatusOptional = challengeStatusRepository.findByUserAndChallenge(user, challenge);

        if (challengeStatusOptional.isPresent()) {
            ChallengeStatus status = challengeStatusOptional.get();
            status.setChallengeStatusCode(challengeStatusCode);
            challengeStatusRepository.save(status);
        } else {
            challengeStatusRepository.save(
                    ChallengeStatus.builder()
                            .user(user)
                            .challenge(challenge)
                            .challengeStatusCode(challengeStatusCode)
                            .build()
            );
        }

        return new ChallengeStatusResponse(challenge.getId(), challengeStatusCode);
    }

    /**
     * 챌린지 좋아요 등록/취소
     */
    public Boolean updateLiked(Long id) {
        String userId = UserInfoUtil.getUserId();

        if (Strings.isNullOrEmpty(userId)) {
            throw new UsernameNotFoundException("로그인 후 이용 가능합니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("챌린지 id를 확인해 주세요."));

        Optional<ChallengeLikes> challengeLikesOptional = challengeLikesRepository.findByUserAndChallenge(user, challenge);

        if (challengeLikesOptional.isPresent()) {
            challengeLikesRepository.delete(challengeLikesOptional.get());
            return false;
        } else {
            challengeLikesRepository.save(ChallengeLikes.builder()
                    .user(user)
                    .challenge(challenge)
                    .build());
            return true;
        }
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
