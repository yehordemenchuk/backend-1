package com.gamestudio.data.repository;

import com.gamestudio.data.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreJpaRepository extends JpaRepository<Score, Long> {
    List<Score> findTop10ByGameOrderByPointsDesc(String game);
}
