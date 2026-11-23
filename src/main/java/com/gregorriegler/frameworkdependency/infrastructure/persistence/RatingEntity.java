package com.gregorriegler.frameworkdependency.infrastructure.persistence;

import com.gregorriegler.frameworkdependency.model.enums.RatingStars;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "rating")
public class RatingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rating_sequence")
    @SequenceGenerator(name = "rating_sequence", sequenceName = "rating_seq", allocationSize = 50)
    public Integer id;

    @Column(name = "user_name")
    public String user;

    @Enumerated(EnumType.STRING)
    public RatingStars stars;

    public String comment;

}
