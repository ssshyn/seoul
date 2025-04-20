package com.sm.seoulmate.domain.attraction.service;

import com.google.common.base.Strings;
import com.sm.seoulmate.domain.attraction.dto.AttractionResponse;
import com.sm.seoulmate.domain.attraction.dto.AttractionSearchCondition;
import com.sm.seoulmate.domain.attraction.entity.AttractionId;
import com.sm.seoulmate.domain.attraction.entity.AttractionLikes;
import com.sm.seoulmate.domain.attraction.entity.VisitStamp;
import com.sm.seoulmate.domain.attraction.mapper.AttractionMapper;
import com.sm.seoulmate.domain.attraction.repository.AttractionIdRepository;
import com.sm.seoulmate.domain.attraction.repository.AttractionInfoRepository;
import com.sm.seoulmate.domain.attraction.repository.AttractionLikesRepository;
import com.sm.seoulmate.domain.attraction.repository.VisitStampRepository;
import com.sm.seoulmate.domain.challenge.entity.Comment;
import com.sm.seoulmate.domain.challenge.mapper.CommentMapper;
import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LanguageCode;
import com.sm.seoulmate.domain.user.repository.UserRepository;
import com.sm.seoulmate.util.UserInfoUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public Page<AttractionResponse> getAttractions(AttractionSearchCondition condition, Pageable pageable) {
        String keyword = StringUtils.trimToEmpty(condition.keyword());
        Page<AttractionId> attractionIdPage = attractionIdRepository.findDistinctByAttractionInfos_NameContainingIgnoreCase(keyword, pageable);
        return attractionIdPage.map(AttractionMapper::toResponse);
    }

    /**
     * 좋아요한 관광지 조회
     */
    public Page<AttractionResponse> my(Pageable pageable, LanguageCode languageCode) {
        String userId = UserInfoUtil.getUserId();

        if(Strings.isNullOrEmpty(userId)) {
            return null;
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("회원 정보를 찾을 수 없습니다."));
        Page<AttractionLikes> attractionIdPage = attractionLikesRepository.findByUser(user, pageable);
        return attractionIdPage.map(page -> AttractionMapper.toLikesResponse(page, languageCode));
    }

    /**
     * 관광지 좋아요 등록/취소
     */
    public Boolean updateLike(Long id) {
        String userId = UserInfoUtil.getUserId();

        if (Strings.isNullOrEmpty(userId)) {
            throw new UsernameNotFoundException("로그인 후 이용 가능합니다.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("사용자 정보를 찾을 수 없습니다."));
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
        String userId = UserInfoUtil.getUserId();

        if (Strings.isNullOrEmpty(userId)) {
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
}
