package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "student")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student extends Person{

    private String semester;

    //Relaciones OneToMany

    @OneToMany(mappedBy = "student")
    private List<Project> projects;

    //Relaciones OnetoOne

    @OneToOne(mappedBy = "student")
    private Career career;

    //relacion llave compuesta OneToMany
    @OneToMany(mappedBy = "student")
    private List<Recomendation> recomendations;

    @OneToMany(mappedBy = "student")
    private List<JobFound> jobFounds;




}
