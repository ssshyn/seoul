package com.sm.seoulmate.domain.user.repository;

import com.sm.seoulmate.domain.user.entity.User;
import com.sm.seoulmate.domain.user.enumeration.LoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndLoginType(String email, LoginType loginType);
    Optional<User> findByNicknameOrNickname(String nicknameKor, String nicknameEng);
}
