package com.example.game.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameName)) return false;
        GameName that = (GameName) o;
        return Objects.equals(languageId, that.languageId) &&
                Objects.equals(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, languageId, game);
    }

}
