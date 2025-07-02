package com.sm.seoulmate.domain.challenge.service;

import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.attraction.AttractionUtil;
import com.sm.seoulmate.domain.attraction.dto.ChallenegeAttractionResponse;
import com.sm.seoulmate.domain.attraction.dto.LocationRequest;
import com.sm.seoulmate.domain.attraction.dto.NearbyAttractionDto;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.VisitStamp;
import com.sm.seoulmate.domain.attraction.mapper.AttractionMapper;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.service.AttractionService;
import com.sm.seoulmate.domain.challenge.dto.ChallengeLikedResponse;
import com.sm.seoulmate.domain.challenge.dto.challenge.*;
import com.sm.seoulmate.domain.challenge.entity.*;
import com.sm.seoulmate.domain.challenge.enumeration.*;
import com.sm.seoulmate.domain.challenge.mapper.ChallengeMapper;
import com.sm.seoulmate.domain.challenge.repository.*;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.exception.ErrorCode;
import com.sm.seoulmate.exception.ErrorException;
import com.sm.seoulmate.util.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChallengeService {
    private final ChallengeRepository challengeRepository;
    private final ChallengeThemeRepository challengeThemeRepository;
    private final ChallengeStatusRepository challengeStatusRepository;
    private final ChallengeLikesRepository challengeLikesRepository;
    private final CulturalEventRepository culturalEventRepository;
    private final AttractionIdRepository attractionIdRepository;
    private final AttractionService attractionService;
    private final UserRepository userRepository;

    /**
     * 챌린지 목록 조회 - 참여형 챌린지
     */
    public List<CulturalChallenge> getCulturalChallenge(LanguageCode languageCode) {
        Long userId = UserInfoUtil.getUserId();
        List<ChallengeLikes> challengeLikes;

        if (userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
            challengeLikes = user.getChallengeLikes();
        } else {
            challengeLikes = new ArrayList<>();
        }

        List<CulturalEvent> culturalEvents = culturalEventRepository.findByCulturePeriodNot(CulturePeriod.PAST);
        List<CulturalEvent> sortEvents = culturalEvents.stream()
                .sorted(Comparator
                        .comparingInt((CulturalEvent event) ->
                                event.getCulturePeriod() != null ? event.getCulturePeriod().getDisplayRank() : Integer.MAX_VALUE
                        )
                        .thenComparingLong(event -> {
                            LocalDate today = LocalDate.now();
                            LocalDate endDate = event.getEndDate();
                            return (endDate != null) ? Math.abs(ChronoUnit.DAYS.between(today, endDate)) : Long.MAX_VALUE;
                        })
                        .thenComparing(x -> x.getChallenge().getName(), Comparator.nullsLast(String::compareTo))
                )
                .limit(5)
                .toList();

        return sortEvents.stream()
                .map(event -> {
                    boolean isLiked = challengeLikes.stream().anyMatch(like -> Objects.equals(like.getChallenge(), event.getChallenge()));
                    String homepageUrl = event.getChallenge().getAttractionIds().get(0).getAttractionInfos().get(0).getHomepageUrl();
                    return ChallengeMapper.toCulturalChallenge(event, languageCode, isLiked, homepageUrl);
                })
                .toList();
    }

    /**
     * 챌린지 목록 조회 - 서울 여행 마스터
     */
    public List<ChallengeResponse> getSeoulMaster(LanguageCode languageCode) {
        List<Long> ids = Arrays.asList(22L, 50L, 11L, 153L, 95L);
        List<Challenge> sample = challengeRepository.findAllById(ids);

        // 좋아요 여부 판단
        Long userId = UserInfoUtil.getUserId();
        List<ChallengeLikes> challengeLikes;

        if (userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
            challengeLikes = user.getChallengeLikes();
        } else {
            challengeLikes = new ArrayList<>();
        }

        return sample.stream().map(challenge -> {
            // 좋아요 여부 판단
            boolean isLiked = challengeLikes.stream().anyMatch(like -> Objects.equals(like.getChallenge(), challenge));
            return ChallengeMapper.toResponse(challenge, languageCode, isLiked);
        }).toList();
    }

    /**
     * 챌린지 목록 조회 - 근처 챌린지
     **/
    public NearChallengeResponse getLocationChallenge(LocationRequest locationRequest, LanguageCode languageCode) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));
        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        // 주변 챌린지(관광지) 데이터 수집
        LocationChallengeData challengeData = getChallengesForLocation(locationRequest);

        // Build challenge responses with like status and distance
        List<ChallengeResponse> result = buildChallengeResponses(challengeData, user, languageCode);

        return NearChallengeResponse.builder()
                .isJongGak(challengeData.isJongGak())
                .challenges(result)
                .build();
    }

    private LocationChallengeData getChallengesForLocation(LocationRequest locationRequest) {
        // 근처 챌린지 조회
        LocationChallengeData locationChallengeData = getNearChallenges(locationRequest);

        // 챌린지가 없으면 종각 기준으로 조회
        if(locationChallengeData.challenges().isEmpty()) {
            // 종각 좌표
            LocationRequest jongGakRequest = new LocationRequest(126.983217, 37.570313, 5000, 10);

            return getNearChallenges(jongGakRequest);
        }

        return locationChallengeData;
    }

    private LocationChallengeData getNearChallenges(LocationRequest locationRequest) {
        // 근처 관광지 정보
        List<AttractionId> attractionIds =
                attractionService.getLocationAttraction(locationRequest).stream()
                        .map(NearbyAttractionDto::getAttractionId)
                        .map(id -> attractionIdRepository.findById(id).orElseThrow())
                        .toList();

        // 근처 관광지 > 챌린지 조회
        List<Challenge> challenges = challengeRepository.findByAttractionIdsIn(attractionIds);

        return new LocationChallengeData(challenges, attractionIds, false);
    }

    private List<ChallengeResponse> buildChallengeResponses(LocationChallengeData challengeData, User user, LanguageCode languageCode) {
        // 챌린지 response
        return challengeData.challenges().stream()
                .map(challenge -> {
                    boolean isLiked = user.getChallengeLikes().stream()
                            .anyMatch(like -> Objects.equals(like.getChallenge(), challenge));

                    ChallengeResponse response = ChallengeMapper.toResponse(challenge, languageCode, isLiked);
                    // 챌린지와 현재 위치 거리 계산
                    response.setDistance(getClosestAttractionDistance(challenge, challengeData.attractionIds()));

                    return response;
                })
                .sorted(Comparator.comparing(ChallengeResponse::getDistance)
                        .thenComparing(ChallengeResponse::getName))
                .toList();
    }

    // 챌린지 거리 return
    private Long getClosestAttractionDistance(Challenge challenge, List<AttractionId> attractionIds) {
        // todo: 거리 계산 로직 필요
        return attractionIds.stream()
                .filter(attractionId -> challenge.getAttractionIds().contains(attractionId))
                .findFirst()
                .map(AttractionId::getId)
                .orElse(Long.MAX_VALUE);
    }

    /**
     * 챌린지 목록 조회 - 스탬프/미참여
     */
    public StampChallengeResponse getStampChallenge(Long attractionId, LanguageCode languageCode) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElse(null);

        // 로그인하지 않았을 때 전체 도전 가능 챌린지 반환
        if (loginUser == null) {
            return buildAllChallengeResponse(languageCode, "CHALLENGE");
        }

        User user = userRepository.findById(loginUser.getId())
                .orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        List<AttractionId> stampedAttractions = user.getVisitStamps().stream()
                .map(VisitStamp::getAttraction)
                .toList();
        List<Challenge> inProgressChallenges = user.getChallengeStatuses().stream()
                .map(ChallengeStatus::getChallenge)
                .toList();

        List<Challenge> challenges;
        String dataCode;

        if (attractionId == null || attractionId == 0) {
            // 스탬프한 모든 관광지에서 미참여 챌린지 조회
            challenges = challengeRepository.findByAttractionIdsIn(stampedAttractions).stream()
                    .filter(challenge -> !inProgressChallenges.contains(challenge))
                    .toList();
            dataCode = "MISSED";
        } else {
            // attractionId 유효성 체크 및 해당 관광지에서 미참여 챌린지 조회
            AttractionId attractionIdEntity = attractionIdRepository.findById(attractionId)
                    .orElseThrow(() -> new ErrorException(ErrorCode.ATTRACTION_NOT_FOUND));
            if (!stampedAttractions.contains(attractionIdEntity)) {
                throw new ErrorException(ErrorCode.WRONG_PARAMETER);
            }
            challenges = challengeRepository.findByAttractionIdsIn(Collections.singletonList(attractionIdEntity)).stream()
                    .filter(challenge -> !inProgressChallenges.contains(challenge))
                    .toList();
            dataCode = "PLACE";
        }

        if (challenges.isEmpty()) {
            return buildAllChallengeResponse(languageCode, "CAHLLENGE");
        }

        List<ChallengeResponse> result = challenges.stream()
                .map(challenge -> {
                    boolean isLiked = user.getChallengeLikes().stream()
                            .anyMatch(like -> Objects.equals(like.getChallenge(), challenge));
                    ChallengeResponse response = ChallengeMapper.toResponse(challenge, languageCode, isLiked);
                    response.setMyStampCount(attractionService.getChallengeStamp(user, challenge));
                    return response;
                })
                .sorted(Comparator.comparing(ChallengeResponse::getMyStampCount).reversed()
                        .thenComparing((ChallengeResponse cr) -> cr.getDisplayRank().getDisplayNum()).reversed()
                        .thenComparing(ChallengeResponse::getName))
                .limit(10)
                .toList();

        return StampChallengeResponse.builder()
                .dataCode(dataCode)
                .challenges(result)
                .build();
    }

    private StampChallengeResponse buildAllChallengeResponse(LanguageCode languageCode, String dataCode) {
        List<Challenge> allChallenges = challengeRepository.findByDisplayRankNot(DisplayRank.CULTURE);

        List<ChallengeResponse> result = allChallenges.stream()
                .map(challenge -> {
                    ChallengeResponse response = ChallengeMapper.toResponse(challenge, languageCode, false);
                    response.setMyStampCount(0);
                    return response;
                })
                .sorted(Comparator.comparing((ChallengeResponse c) -> (c.getLevel() == Level.EASY) && (c.getDisplayRank() == DisplayRank.HIGH))
                        .thenComparing(c -> c.getDisplayRank().getDisplayNum()).reversed()
                        .thenComparing(c -> c.getLevel() == null ? 99 : c.getLevel().getLevelNum())
                        .thenComparing(ChallengeResponse::getName))
                .limit(10)
                .toList();

        return StampChallengeResponse.builder()
                .dataCode(dataCode)
                .challenges(result)
                .build();
    }

    /**
     * 챌린지 목록 조회 - 테마별
     */
    public List<ChallengeResponse> getChallengeTheme(Long themeId, LanguageCode languageCode) {
        if (challengeThemeRepository.findById(themeId).isEmpty()) {
            throw new ErrorException(ErrorCode.CHALLENGE_THEME_NOT_FOUND);
        }

        // 좋아요 여부 판단
        Long userId = UserInfoUtil.getUserId();
        List<ChallengeLikes> challengeLikes;

        if (userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
            challengeLikes = user.getChallengeLikes();
        } else {
            challengeLikes = new ArrayList<>();
        }

        List<Challenge> challenges = challengeRepository.findByChallengeThemeId(themeId);

        // 문화행사의 경우, 날짜별 정렬 필요
        if(Objects.equals(themeId, 2L)) {
            return challenges.stream()
                    .map(challenge -> {
                        // 좋아요 여부 판단
                        boolean isLiked = challengeLikes.stream().anyMatch(like -> Objects.equals(like.getChallenge(), challenge));
                        return ChallengeMapper.toResponse(challenge, languageCode, isLiked);
                    })
                    .sorted(Comparator.comparing((ChallengeResponse cr) -> cr.getCulturePeriod().getDisplayRank())
                    .thenComparing(ChallengeResponse::getName))
                    .toList();
        }

        return challenges.stream()
                .map(challenge -> {
                    // 좋아요 여부 판단
                    boolean isLiked = challengeLikes.stream().anyMatch(like -> Objects.equals(like.getChallenge(), challenge));
                    return ChallengeMapper.toResponse(challenge, languageCode, isLiked);
                })
                .sorted(Comparator.comparing((ChallengeResponse cr) -> cr.getDisplayRank().getDisplayNum()).reversed()
                .thenComparing(ChallengeResponse::getName))
                .toList();
    }

    /**
     * 챌린지 랭킹 목록 조회
     */
    public List<ChallengeRankResponse> getRank(LanguageCode languageCode) {
        Long userId = UserInfoUtil.getUserId();
        List<ChallengeLikes> challengeLikes;

        if (userId != null) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
            challengeLikes = user.getChallengeLikes();
        } else {
            challengeLikes = new ArrayList<>();
        }

        // 참여자 있는거 5개 미만일 시 하드코딩 데이터
        if (challengeRepository.countByStatusesExists() < 5) {
            List<Long> ids = Arrays.asList(50L, 37L, 25L, 77L, 35L);
            List<Challenge> sample = challengeRepository.findAllById(ids);

            return sample.stream().map(challenge -> {
                // 좋아요 여부 판단
                boolean isLiked = challengeLikes.stream().anyMatch(like -> Objects.equals(like.getChallenge(), challenge));

                ChallengeRankResponse sampleResponse = ChallengeMapper.toRankResponse(challenge, languageCode, isLiked);
                if (sampleResponse.getId() == 50L) {
                    sampleResponse.setProgressCount(23);
                } else if (sampleResponse.getId() == 37L) {
                    sampleResponse.setProgressCount(17);
                } else if (sampleResponse.getId() == 25L) {
                    sampleResponse.setProgressCount(12);
                } else if (sampleResponse.getId() == 77L) {
                    sampleResponse.setProgressCount(8);
                } else {
                    sampleResponse.setProgressCount(4);
                }

                return sampleResponse;
            }).sorted(Comparator.comparing(ChallengeRankResponse::getProgressCount).reversed()).toList();
        } else {
            List<Challenge> challenges = challengeRepository.findAllOrderByStatusCountDesc();

            return challenges.stream().map(challenge -> {
                // 좋아요 여부 판단
                boolean isLiked = challengeLikes.stream().anyMatch(like -> Objects.equals(like.getChallenge(), challenge));

                return ChallengeMapper.toRankResponse(challenge, languageCode, isLiked);
            }).sorted(Comparator.comparing(ChallengeRankResponse::getProgressCount).reversed()
                    .thenComparing(ChallengeRankResponse::getId)).toList();
        }

    }

    /**
     * 나의 챌린지 조회
     */
    public List<ChallengeResponse> myChallenge(LanguageCode languageCode, MyChallengeCode myChallengeCode) {
        boolean isKorean = languageCode.equals(LanguageCode.KOR);

        // user 체크
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));
        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));

        // 챌린지가 진행중이거나 완료 상태이면
        if (Objects.isNull(myChallengeCode.getChallengeStatusCode())) {
            return user.getChallengeLikes().stream().map(
                    likes -> {
                        Challenge entity = likes.getChallenge();

                        return ChallengeResponse.builder()
                                .id(entity.getId())
                                .name(isKorean ? entity.getName() : entity.getNameEng())
                                .title(isKorean ? entity.getTitle() : entity.getTitleEng())
                                .likes(entity.getLikes().size())
                                .isLiked(true)
                                .commentCount(entity.getComments().size())
                                .attractionCount(entity.getAttractionIds().size())
                                .challengeThemeId(entity.getChallengeTheme().getId())
                                .challengeThemeName(isKorean ? entity.getChallengeTheme().getNameKor() : entity.getChallengeTheme().getNameEng())
                                .imageUrl(entity.getImageUrl())
                                .likedAt(likes.getCreatedAt())
                                .build();
                    }
            ).sorted(Comparator.comparing(ChallengeResponse::getLikedAt).reversed()).toList();

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
                        .imageUrl(entity.getImageUrl())
                        .attractions(entity.getAttractionIds().stream().map(x -> AttractionMapper.toChallengeResponse(x, languageCode)).toList())
                        .build();
            }).sorted(Comparator.comparing(ChallengeResponse::getMyStampCount).reversed()).toList();
        }
    }

    /**
     * 챌린지 뱃지 현황 조회
     */
    public List<ChallengeBadgeResponse> getBadgeStatus(Long themeId, LanguageCode languageCode) {
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

                    if (Objects.isNull(themeId) || themeId == 0L) {
                        result.add(
                                new ChallengeBadgeResponse(
                                        theme.getId(),
                                        isKorean ? theme.getNameKor() : theme.getNameEng(),
                                        theme.getChallenges().size(),
                                        completeCount.intValue(),
                                        theme.getChallenges().size() == completeCount.intValue())
                        );
                    } else {
                        if (Objects.equals(theme.getId(), themeId)) {
                            result.add(
                                    new ChallengeBadgeResponse(
                                            theme.getId(),
                                            isKorean ? theme.getNameKor() : theme.getNameEng(),
                                            theme.getChallenges().size(),
                                            completeCount.intValue(),
                                            theme.getChallenges().size() == completeCount.intValue())
                            );
                        }
                    }
                }
        );

        return result;
    }

    private final AttractionUtil attractionUtil;

    /**
     * 챌린지 상세 조회
     */
    public ChallengeDetailResponse getDetail(LanguageCode languageCode, Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_NOT_FOUND));

        LoginInfo loginUser = UserInfoUtil.getUser().orElse(null);
        Boolean isLiked = null;
        ChallengeStatusCode challengeStatusCode = null;
        Integer stampCount = 0;

        if (!Objects.isNull(loginUser)) {
            User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
            // 찜 여부 체크
            Optional<ChallengeLikes> likesOptional = user.getChallengeLikes().stream().filter(likes -> Objects.equals(likes.getChallenge().getId(), challenge.getId())).findFirst();
            isLiked = likesOptional.isPresent();

            // 진행상태 체크
            Optional<ChallengeStatus> statusOptional = user.getChallengeStatuses().stream().filter(status -> Objects.equals(status.getChallenge().getId(), challenge.getId())).findFirst();
            if (statusOptional.isPresent()) {
                challengeStatusCode = statusOptional.get().getChallengeStatusCode();
            }

            // 스탬프 카운트 체크
            stampCount = attractionService.getChallengeStamp(user, challenge);
        }

        ChallengeDetailResponse result = ChallengeMapper.toDetailResponse(challenge, languageCode, isLiked, stampCount, challengeStatusCode);
        for (ChallenegeAttractionResponse attraction : result.getAttractions()) {
            if (attraction.getImageUrl().isEmpty()) {
                String image = attractionUtil.getImageFromNaver(attraction.getName());
                attraction.setImageUrl(image);
            }
        }
        return result;
    }

    /**
     * 챌린지 상태 초기화
     */
    public void deleteStatus(Long id) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_NOT_FOUND));

        ChallengeStatus challengeStatus = challengeStatusRepository.findByUserAndChallenge(user, challenge).orElseThrow(() -> new ErrorException(ErrorCode.WRONG_PARAMETER));
        challengeStatusRepository.delete(challengeStatus);
    }

    /**
     * 챌린지 상태 변경
     */
    public ChallengeStatusResponse updateStatus(Long id, ChallengeStatusCode challengeStatusCode) {
        // user 체크
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.CHALLENGE_NOT_FOUND));

        // 완료 처리 시에는 관광지 스탬프 다 찍혔는지 확인
        List<AttractionId> attractionIds = challenge.getAttractionIds();
        List<AttractionId> visitAttractions = user.getVisitStamps().stream().map(VisitStamp::getAttraction).toList();

        if (Objects.equals(challengeStatusCode, ChallengeStatusCode.COMPLETE)) {
            if (attractionIds.stream().anyMatch(x -> !visitAttractions.contains(x))) {
                throw new ErrorException(ErrorCode.STATUS_NOT_ALLOWED);
            }
        }

        // 챌린지 status entity 조회
        Optional<ChallengeStatus> challengeStatusOptional = challengeStatusRepository.findByUserAndChallenge(user, challenge);

        if (challengeStatusOptional.isPresent()) {
            // 상태값 있으면 update
            ChallengeStatus status = challengeStatusOptional.get();
            status.setChallengeStatusCode(challengeStatusCode);
            challengeStatusRepository.save(status);
        } else {
            // 상태값 없으면 create
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
        if (requestAttractionIds.isEmpty()) {
            throw new ErrorException(ErrorCode.REQUIRED_PARAMETER);
        }
        if (requestAttractionIds.stream().distinct().toList().size() > 5) {
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
        if (requestAttractionIds.isEmpty()) {
            throw new ErrorException(ErrorCode.REQUIRED_PARAMETER);
        }
        if (requestAttractionIds.stream().distinct().toList().size() > 5) {
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

}
