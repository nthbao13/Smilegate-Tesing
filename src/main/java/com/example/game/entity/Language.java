package com.example.game.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "language")
public class Language extends BaseEntity{
    @Id
    @Column(length = 5)
    private String languageId;

    private String languageName;
}
