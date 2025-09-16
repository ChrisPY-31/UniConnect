package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;



@Entity
@Table(name = "recomendation")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Recomendation {

    @EmbeddedId
    private RecomendationPk id;

    private String Comment;

    @Column(name = "recomendation_date" , columnDefinition = "DATE")
    private LocalDate recomendationDate;

    @ManyToOne
    @JoinColumn(name = "id_student" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_teacher" , referencedColumnName = "id" , insertable = false , updatable = false)
    private Teacher teacher;
}
