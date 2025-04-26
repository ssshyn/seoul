package com.sm.seoulmate.domain.challenge.service;

import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.VisitStamp;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.service.AttractionService;
import com.sm.seoulmate.domain.challenge.dto.ChallengeLikedResponse;
import com.sm.seoulmate.domain.challenge.dto.challenge.*;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeCreateRequest;
import com.sm.seoulmate.domain.challenge.dto.theme.ChallengeThemeResponse;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeLikes;
import com.sm.seoulmate.domain.challenge.entity.ChallengeStatus;
import com.sm.seoulmate.domain.challenge.entity.ChallengeTheme;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.challenge.enumeration.MyChallengeCode;
import com.sm.seoulmate.domain.challenge.mapper.ChallengeMapper;
import com.sm.seoulmate.domain.challenge.mapper.ChallengeThemeMapper;
import com.sm.seoulmate.domain.challenge.repository.ChallengeLikesRepository;
import com.sm.seoulmate.domain.challenge.repository.ChallengeRepository;
import com.sm.seoulmate.domain.challenge.repository.ChallengeStatusRepository;
import com.sm.seoulmate.domain.challenge.repository.ChallengeThemeRepository;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.exception.ErrorCode;
import com.sm.seoulmate.exception.ErrorException;
import com.sm.seoulmate.util.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final AttractionService attractionService;
    private final UserRepository userRepository;

    /**
     * 챌린지 조회
     */
    public Page<ChallengeResponsess> getChallenge(ChallengeSearchCondition condition, Pageable pageable) {
        String keyword = StringUtils.trimToEmpty(condition.keyword());
        Page<Challenge> challengePage = challengeRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return challengePage.map(ChallengeMapper::toResponsess);
    }

    /**
     * 챌린지 목록 조회 - 테마별
     */
    public List<ChallengeResponse> getChallengeTheme(Long themeId, LanguageCode languageCode) {
        if(challengeThemeRepository.findById(themeId).isEmpty()) {
            throw new ErrorException(ErrorCode.CHALLENGE_THEME_NOT_FOUND);
        }

        // 좋아요 여부 판단
        Long userId = UserInfoUtil.getUserId();
        List<ChallengeLikes> challengeLikes;

        if(userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
            challengeLikes = user.getChallengeLikes();
        } else {
            challengeLikes = new ArrayList<>();
        }

        List<Challenge> challenges = challengeRepository.findByChallengeThemeId(themeId);
        return challenges.stream().map(challenge -> {
            // 좋아요 여부 판단
            boolean isLiked = challengeLikes.stream().anyMatch(like -> Objects.equals(like.getChallenge(), challenge));
            return ChallengeMapper.toResponse(challenge, languageCode, isLiked);
        }).toList();
    }

    /**
     * 챌린지 랭킹 목록 조회
     */
    public List<ChallengeRankResponse> getRank(LanguageCode languageCode) {
        Long userId = UserInfoUtil.getUserId();
        List<ChallengeLikes> challengeLikes;

        if(userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
            challengeLikes = user.getChallengeLikes();
        } else {
            challengeLikes = new ArrayList<>();
        }

        List<Challenge> challenges = challengeRepository.findAllOrderByStatusCountDesc();

        return challenges.stream().map(challenge -> {
            // 좋아요 여부 판단
            boolean isLiked = challengeLikes.stream().anyMatch(like -> Objects.equals(like.getChallenge(), challenge));

            return ChallengeMapper.toRankResponse(challenge, languageCode, isLiked);
        }).toList();
    }

    /**
     * 나의 챌린지 조회
     */
    public List<ChallengeResponse> myChallenge(LanguageCode languageCode, MyChallengeCode myChallengeCode) {
        boolean isKorean = languageCode.equals(LanguageCode.KOR);
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        if(Objects.isNull(myChallengeCode.getChallengeStatusCode())) {
            return user.getChallengeLikes().stream().map(
                    likes -> {
                        Challenge entity = likes.getChallenge();

                        return ChallengeResponse.builder()
                                .id(entity.getId())
                                .name(isKorean ? entity.getName() : entity.getNameEng())
                                .title(isKorean ? entity.getTitle() : entity.getTitleEng())
                                .likes(entity.getLikes().size())
                                .commentCount(entity.getComments().size())
                                .attractionCount(entity.getAttractionIds().size())
                                .challengeThemeId(entity.getChallengeTheme().getId())
                                .challengeThemeName(isKorean ? entity.getChallengeTheme().getNameKor() : entity.getChallengeTheme().getNameEng())
                                .build();
                    }
            ).toList();
        } else {
            List<ChallengeStatus> challengeStatuses = challengeStatusRepository.findByUserAndChallengeStatusCode(user, myChallengeCode.getChallengeStatusCode());
            return challengeStatuses.stream().map(status -> {
                Challenge entity = status.getChallenge();

                return ChallengeResponse.builder()
                        .id(entity.getId())
                        .name(isKorean ? entity.getName() : entity.getNameEng())
                        .title(isKorean ? entity.getTitle() : entity.getTitleEng())
                        .likes(entity.getLikes().size())
                        .commentCount(entity.getComments().size())
                        .attractionCount(entity.getAttractionIds().size())
                        .myStampCount(attractionService.getChallengeStamp(user, entity))
                        .challengeThemeId(entity.getChallengeTheme().getId())
                        .challengeThemeName(isKorean ? entity.getChallengeTheme().getNameKor() : entity.getChallengeTheme().getNameEng())
                        .build();
            }).toList();
        }
    }

    /**
     * 챌린지 뱃지 현황 조회
     */
    public List<ChallengeBadgeResponse> getBadgeStatus(LanguageCode languageCode) {
        List<ChallengeBadgeResponse> result = new ArrayList<>();
        boolean isKorean = languageCode.equals(LanguageCode.KOR);
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        // 전체 테마 목록 조회
        List<ChallengeTheme> themes = challengeThemeRepository.findAll();
        // 내가 완료한 테마 리스트
        List<ChallengeStatus> completeStatuses = user.getChallengeStatuses().stream()
                .filter(status -> Objects.equals(status.getChallengeStatusCode(), ChallengeStatusCode.COMPLETE))
                .toList();

        themes.forEach(
                theme -> {
                    Long completeCount = completeStatuses.stream()
                            .filter(status -> Objects.equals(theme, status.getChallenge().getChallengeTheme()))
                            .count();
                    result.add(
                            new ChallengeBadgeResponse(
                                    theme.getId(),
                                    isKorean ? theme.getNameKor() : theme.getNameEng(),
                                    theme.getChallenges().size(),
                                    completeCount.intValue(),
                                    theme.getChallenges().size() == completeCount.intValue())
                    );
                }
        );

        return result;
    }

    /**
     * 챌린지 상세 조회
     */
    public ChallengeDetailResponse getDetail(LanguageCode languageCode, Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_NOT_FOUND));

        LoginInfo loginUser = UserInfoUtil.getUser().orElse(null);
        Boolean isLiked = null;
        ChallengeStatusCode challengeStatusCode = null;

        if(!Objects.isNull(loginUser)) {
            User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
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
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_NOT_FOUND));

        // 완료 처리 시에는 관광지 스탬프 다 찍혔는지 확인
        List<AttractionId> attractionIds = challenge.getAttractionIds();
        List<AttractionId> visitAttractions = user.getVisitStamps().stream().map(VisitStamp::getAttraction).toList();

        if(Objects.equals(challengeStatusCode, ChallengeStatusCode.COMPLETE)) {
            if (attractionIds.stream().anyMatch(x -> !visitAttractions.contains(x))) {
                throw new ErrorException(ErrorCode.STATUS_NOT_ALLOWED);
            }
        }

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
    public ChallengeLikedResponse updateLiked(Long id) throws BadRequestException {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_NOT_FOUND));

        Optional<ChallengeLikes> challengeLikesOptional = challengeLikesRepository.findByUserAndChallenge(user, challenge);

        if (challengeLikesOptional.isPresent()) {
            ChallengeLikes challengeLikes = challengeLikesOptional.get();
            challengeLikesRepository.delete(challengeLikes);
            return new ChallengeLikedResponse(challenge.getId(), false);
        } else {
            challengeLikesRepository.save(ChallengeLikes.builder()
                    .user(user)
                    .challenge(challenge)
                    .build());
            return new ChallengeLikedResponse(challenge.getId(), true);
        }
    }

    /**
     * 챌린지 생성
     */
    public ChallengeResponse create(ChallengeCreateRequest request) throws BadRequestException {
        ChallengeTheme theme = challengeThemeRepository.findById(request.challengeThemeId())
                .orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_THEME_NOT_FOUND));

        // 관광지 id 예외처리
        List<Long> requestAttractionIds = request.attractionIdList().stream().distinct().toList();
        if(requestAttractionIds.isEmpty()) {
            throw new ErrorException(ErrorCode.REQUIRED_PARAMETER);
        }
        if(requestAttractionIds.stream().distinct().toList().size() > 5) {
            throw new ErrorException(ErrorCode.MAX_SIZE);
        }

        // 관광지 목록 체크
        List<AttractionId> attractionIds = new ArrayList<>();
        request.attractionIdList().forEach(
                attractionId -> {
                    AttractionId attraction = attractionIdRepository.findById(attractionId).orElseThrow(() -> new ErrorException(ErrorCode.ATTRACTION_NOT_FOUND));
                    attractionIds.add(attraction);
                }
        );

        Challenge challenge = ChallengeMapper.toEntity(request, theme, attractionIds);
        return ChallengeMapper.toProdResponse(challengeRepository.save(challenge));
    }

    /**
     * 챌린지 수정
     */
    public ChallengeResponse update(ChallengeUpdateRequest request) throws BadRequestException {
        ChallengeTheme theme = challengeThemeRepository.findById(request.challengeThemeId())
                .orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_THEME_NOT_FOUND));

        // 관광지 id 예외처리
        List<Long> requestAttractionIds = request.attractionIdList().stream().distinct().toList();
        if(requestAttractionIds.isEmpty()) {
            throw new ErrorException(ErrorCode.REQUIRED_PARAMETER);
        }
        if(requestAttractionIds.stream().distinct().toList().size() > 5) {
            throw new ErrorException(ErrorCode.MAX_SIZE);
        }

        // 관광지 목록 체크
        List<AttractionId> attractionIds = new ArrayList<>();
        request.attractionIdList().forEach(
                attractionId -> {
                    AttractionId attraction = attractionIdRepository.findById(attractionId).orElseThrow(() -> new ErrorException(ErrorCode.ATTRACTION_NOT_FOUND));
                    attractionIds.add(attraction);
                }
        );

        Challenge challenge = ChallengeMapper.toUpdatedEntity(request, theme, attractionIds);
        return ChallengeMapper.toProdResponse(challengeRepository.save(challenge));
    }

    /**
     * 챌린지 삭제
     */
    public void deleteChallenge(Long id) throws BadRequestException {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_THEME_NOT_FOUND));
        challengeRepository.delete(challenge);
    }

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
