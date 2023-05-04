package com.example.springlv5.repository;

import com.example.springlv5.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    //Username을 이용하여 찾기
    void deleteByUsername(String username);
}