package com.example.springlv5.repository;

import com.example.springlv5.entity.CrudLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CrudLikeRepository extends JpaRepository<CrudLike, Long> {
    void deleteByCrudIdAndUserId(Long crudId,Long Id);
    boolean existsByCrudIdAndUserId(Long crudId, Long userId);
}
