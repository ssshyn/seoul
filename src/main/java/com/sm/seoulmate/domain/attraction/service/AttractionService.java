package com.sm.seoulmate.domain.attraction.service;

import com.sm.seoulmate.domain.attraction.dto.AttractionDetailResponse;
import com.sm.seoulmate.domain.attraction.dto.SearchAttraction;
import com.sm.seoulmate.domain.attraction.dto.SearchChallenge;
import com.sm.seoulmate.domain.attraction.dto.SearchResponse;
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
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.util.UserInfoUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttractionService {
    private final AttractionIdRepository attractionIdRepository;
    private final AttractionInfoRepository attractionInfoRepository;
    private final AttractionLikesRepository attractionLikesRepository;
    private final VisitStampRepository visitStampRepository;
    private final UserRepository userRepository;

    /**
     * 관광지 목록 조회 - 페이징
     */
//    public Page<AttractionResponse> getAttractions(AttractionSearchCondition condition, Pageable pageable) {
//        String keyword = StringUtils.trimToEmpty(condition.keyword());
//        Page<AttractionId> attractionIdPage = attractionIdRepository.findDistinctByAttractionInfos_NameContainingIgnoreCase(keyword, pageable);
//        return attractionIdPage.map(AttractionMapper::toResponse);
//    }

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
    public AttractionDetailResponse getDetail(Long id, LanguageCode languageCode) throws BadRequestException {
        AttractionId attractionId = attractionIdRepository.findById(id).orElseThrow(() -> new BadRequestException("관광지 id를 다시 확인해 주세요."));

        Long userId = UserInfoUtil.getUserId();
        Boolean isLiked = null;

        if(!Objects.isNull(userId)) {
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
            // 찜 여부 체크
            Optional<AttractionLikes> likesOptional = user.getAttractionLikes().stream().filter(likes -> Objects.equals(likes.getAttraction(), attractionId)).findFirst();
            isLiked = likesOptional.isPresent();
        }

        return AttractionMapper.toResponse(attractionId, languageCode, isLiked);
    }

    /**
     * 좋아요한 관광지 조회
     */
    public List<AttractionDetailResponse> my(Pageable pageable, LanguageCode languageCode) {
        Long userId = UserInfoUtil.getUserId();

        if(Objects.isNull(userId)) {
            return null;
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));
        List<AttractionLikes> attractionIdPage = attractionLikesRepository.findByUser(user, pageable);
        return attractionIdPage.stream().map(page -> AttractionMapper.toLikesResponse(page, languageCode)).toList();
    }

    /**
     * 관광지 좋아요 등록/취소
     */
    public Boolean updateLike(Long id) {
        Long userId = UserInfoUtil.getUserId();

        if (Objects.isNull(userId)) {
            throw new UsernameNotFoundException("로그인 후 이용 가능합니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("사용자 정보를 찾을 수 없습니다."));
        AttractionId attractionId = attractionIdRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("관광지 id를 확인해 주세요."));

        Optional<AttractionLikes> attractionLikesOptional = attractionLikesRepository.findByUserAndAttraction(user, attractionId);

        if (attractionLikesOptional.isPresent()) {
            attractionLikesRepository.delete(attractionLikesOptional.get());
            return false;
        } else {
            attractionLikesRepository.save(AttractionLikes.builder()
                    .user(user)
                    .attraction(attractionId)
                    .build());
            return true;
        }
    }

    /**
     * 관광지 스탬프 등록
     */
    public void saveStamp(Long id) {
        Long userId = UserInfoUtil.getUserId();

        if (Objects.isNull(userId)) {
            throw new UsernameNotFoundException("로그인 후 이용 가능합니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));
        AttractionId attractionId = attractionIdRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("관광지 id를 확인해 주세요."));

        Optional<VisitStamp> visitStampOptional = visitStampRepository.findByUserAndAttraction(user, attractionId);

        if (visitStampOptional.isEmpty()) {
            visitStampRepository.save(VisitStamp.builder()
                    .user(user)
                    .attraction(attractionId)
                    .build());
        }
    }

    public Integer getChallengeStamp(User user, Challenge challenge) {
        List<AttractionId> attractionIds = challenge.getAttractionIds();

        return user.getVisitStamps().stream().filter(x -> attractionIds.contains(x.getAttraction())).toList().size();
    }
}
