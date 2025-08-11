package com.example.game.repository;

import com.example.game.entity.GameName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameNameRepository extends JpaRepository<GameName, Integer> {
}
