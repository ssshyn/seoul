package com.sm.seoulmate.domain.attraction.service;

import com.sm.seoulmate.config.LoginInfo;
import com.sm.seoulmate.domain.attraction.AttractionUtil;
import com.sm.seoulmate.domain.attraction.dto.*;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionInfo;
import com.sm.seoulmate.domain.attraction.entity.AttractionLikes;
import com.sm.seoulmate.domain.attraction.entity.VisitStamp;
import com.sm.seoulmate.domain.attraction.mapper.AttractionMapper;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.repository.AttractionInfoRepository;
import com.sm.seoulmate.domain.attraction.repository.AttractionLikesRepository;
import com.sm.seoulmate.domain.attraction.repository.VisitStampRepository;
import com.sm.seoulmate.domain.challenge.entity.Challenge;
import com.sm.seoulmate.domain.challenge.entity.ChallengeStatus;
import com.sm.seoulmate.domain.challenge.enumeration.ChallengeStatusCode;
import com.sm.seoulmate.domain.challenge.repository.ChallengeRepository;
import com.sm.seoulmate.domain.challenge.repository.ChallengeStatusRepository;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.exception.ErrorCode;
import com.sm.seoulmate.exception.ErrorException;
import com.sm.seoulmate.util.UserInfoUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionIdRepository attractionIdRepository;
    private final AttractionInfoRepository attractionInfoRepository;
    private final AttractionLikesRepository attractionLikesRepository;
    private final VisitStampRepository visitStampRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeStatusRepository challengeStatusRepository;
    private final AttractionUtil attractionUtil;


    /**
     * 전체 검색
     */
    public SearchResponse search(String keyword, LanguageCode languageCode) {
        keyword = StringUtils.trimToEmpty(keyword);

        // 관광지 조회
        List<AttractionId> attractionIds = attractionIdRepository.findDistinctByAttractionInfos_NameContainingIgnoreCase(keyword);
        List<SearchAttraction> searchAttractions = attractionIds.stream()
                .map(id -> new SearchAttraction(id, id.getAttractionInfos().stream()
                        .filter(x -> Objects.equals(x.getLanguageCode(), languageCode))
                        .findFirst().orElse(new AttractionInfo())))
                .toList();

        // 챌린지 조회
        List<SearchChallenge> searchChallenges = new ArrayList<>();
        attractionIds.forEach(id -> {
            List<Challenge> challenges = id.getChallenges();
            searchChallenges.addAll(challenges.stream().map(challenge -> new SearchChallenge(challenge, languageCode)).toList());
        });

        return new SearchResponse(searchAttractions, searchChallenges);
    }
    /**
     * 관광지 상세 조회
     */
    public AttractionDetailResponse getDetail(Long id, LanguageCode languageCode) {
        AttractionId attractionId = attractionIdRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.ATTRACTION_NOT_FOUND));

        Long userId = UserInfoUtil.getUserId();
        Boolean isLiked = null;

        if(!Objects.isNull(userId)) {
            User user = userRepository.findById(userId).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
            // 찜 여부 체크
            Optional<AttractionLikes> likesOptional = user.getAttractionLikes().stream().filter(likes -> Objects.equals(likes.getAttraction(), attractionId)).findFirst();
            isLiked = likesOptional.isPresent();
        }

        AttractionDetailResponse result = AttractionMapper.toResponse(attractionId, languageCode, isLiked);
        if(result.getImageUrl().isEmpty()) {
            result.setImageUrl(attractionUtil.getImageFromNaver(result.getName()));
        }
        return result;
    }

    /**
     * 좋아요한 관광지 조회
     */
    public List<AttractionDetailResponse> my(Pageable pageable, LanguageCode languageCode) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        List<AttractionLikes> attractionIds = attractionLikesRepository.findByUser(user, pageable);
        return attractionIds.stream().map(list -> {
            AttractionDetailResponse result =AttractionMapper.toLikesResponse(list, languageCode);
            if(result.getImageUrl().isEmpty()) {
                result.setImageUrl(attractionUtil.getImageFromNaver(result.getName()));
            }
            return result;
        }).toList();
    }

    /**
     * 관광지 좋아요 등록/취소
     */
    public AttractionLikedResponse updateLike(Long id) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        AttractionId attractionId = attractionIdRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.ATTRACTION_NOT_FOUND));

        Optional<AttractionLikes> attractionLikesOptional = attractionLikesRepository.findByUserAndAttraction(user, attractionId);

        if (attractionLikesOptional.isPresent()) {
            AttractionLikes attractionLikes = attractionLikesOptional.get();
            attractionLikesRepository.delete(attractionLikes);
            return new AttractionLikedResponse(attractionId.getId(), false);
        } else {
            attractionLikesRepository.save(AttractionLikes.builder()
                    .user(user)
                    .attraction(attractionId)
                    .build());
            return new AttractionLikedResponse(attractionId.getId(), true);
        }
    }

    /**
     * 관광지 스탬프 등록
     */
    public void saveStamp(Long id) {
        LoginInfo loginUser = UserInfoUtil.getUser().orElseThrow(() -> new ErrorException(ErrorCode.LOGIN_NOT_ACCESS));

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new ErrorException(ErrorCode.USER_NOT_FOUND));
        AttractionId attractionId = attractionIdRepository.findById(id).orElseThrow(() -> new ErrorException(ErrorCode.ATTRACTION_NOT_FOUND));

        Optional<VisitStamp> visitStampOptional = visitStampRepository.findByUserAndAttraction(user, attractionId);

        if (visitStampOptional.isEmpty()) {
            VisitStamp visitStamp = visitStampRepository.save(VisitStamp.builder()
                    .user(user)
                    .attraction(attractionId)
                    .build());

            // 만약 챌린지에 마지막 값이면 챌린지 상태 업데이트
            List<Challenge> visitChallenges = challengeRepository.findByAttractionIdsIn(Collections.singletonList(visitStamp.getAttraction()));
            for (Challenge visitChallenge : visitChallenges) {
                List<AttractionId> attractionIds = visitChallenge.getAttractionIds();
                Integer attractionCount = attractionIds.size();
                Integer visitCount = Math.toIntExact(attractionIds.stream().filter(
                        att -> att.getVisitStamps().stream().anyMatch(x -> Objects.equals(x.getUser(), user))
                ).count());

                if(attractionCount.equals(visitCount)) {
                    ChallengeStatus status = visitChallenge.getStatuses().stream().filter(x -> Objects.equals(x.getUser(), user)).findFirst().orElse(null);
                    if(status != null) {
                        status.setChallengeStatusCode(ChallengeStatusCode.COMPLETE);
                        challengeStatusRepository.save(status);
                    }
                }
            }
        }
    }

    public Integer getChallengeStamp(User user, Challenge challenge) {
        List<AttractionId> attractionIds = challenge.getAttractionIds();

        return user.getVisitStamps().stream().filter(x -> attractionIds.contains(x.getAttraction())).toList().size();
    }

    public List<NearbyAttractionDto> getLocationAttraction(LocationRequest locationRequest) {
        List<Object[]> nearAttractions = attractionInfoRepository.findNearbyAttraction(locationRequest.getLocationX(), locationRequest.getLocationY(), locationRequest.getRadius(), locationRequest.getLimit());

        if(!nearAttractions.isEmpty()) {
           return nearAttractions.stream()
                    .map(row -> new NearbyAttractionDto(
                            ((Number) row[0]).longValue(),
                            ((Number) row[1]).doubleValue(),
                            ((Number) row[2]).doubleValue(),
                            ((Number) row[3]).doubleValue()
                    )).toList();
        } else {
            return new ArrayList<>();
        }
//        return attractionInfoRepository.findNearbyAttraction(locationRequest.getLocationX(), locationRequest.getLocationY(), locationRequest.getRadius(), locationRequest.getLimit());
    }
}
