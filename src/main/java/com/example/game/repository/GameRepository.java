package com.example.game.repository;

import com.example.game.entity.Game;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    @Query("SELECT DISTINCT g FROM Game g JOIN g.gameNames gn " +
            "WHERE (:keyword IS NULL OR LOWER(gn.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:categoryId IS NULL OR g.category.categoryId = :categoryId)")
    Page<Game> searchGames(@Param("keyword") String keyword,
                           @Param("categoryId") Integer categoryId,
                           Pageable pageable);

    Game findByGameCode(String gameCode);
}
