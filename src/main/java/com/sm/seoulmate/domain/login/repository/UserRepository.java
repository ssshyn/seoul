package com.sm.seoulmate.domain.login.repository;

import com.sm.seoulmate.domain.login.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByNicknameKor(String nicknameKor);
}
