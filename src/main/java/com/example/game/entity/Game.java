package com.example.game.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game")
public class Game extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gameId;

    private String gameCode;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_id", referencedColumnName = "categoryId")
    private Category category;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = GameName.class)
    private Set<GameName> gameNames = new HashSet<>();
}
