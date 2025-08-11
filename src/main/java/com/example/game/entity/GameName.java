package com.example.game.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "game_name")
public class GameName extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nameId;

    private boolean isDefault;

    @Column(name = "game_name")
    private String name;
    private String languageId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", referencedColumnName = "gameId")
    private Game game;
}
