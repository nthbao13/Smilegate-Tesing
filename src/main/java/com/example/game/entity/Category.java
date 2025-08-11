package com.example.game.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryId;

    private String categoryName;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = Game.class)
    private Set<Game> games = new HashSet<>();
}
