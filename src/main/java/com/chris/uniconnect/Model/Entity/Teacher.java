package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.AcademicDegree;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Teacher extends Person {


    @Enumerated(EnumType.STRING)
    @Column(name = "academic_degree")
    private AcademicDegree academicDegree;

    @OneToMany(mappedBy = "teacher")
    private List<Recomendation> recomendations;
}
