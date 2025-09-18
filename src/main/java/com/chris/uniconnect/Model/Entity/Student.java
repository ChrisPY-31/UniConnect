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

    @Column(name = "id_career")
    private Integer idCareer;

    private String semester;

    //Relaciones OneToMany

    @OneToMany(mappedBy = "student")
    private List<Project> projects;

    //Relaciones ManytoOne

    @ManyToOne
    @JoinColumn(name = "id_career" , referencedColumnName = "id_career" , insertable = false , updatable = false)
    private Career career;

    //relacion llave compuesta OneToMany
    @OneToMany(mappedBy = "student")
    private List<Recomendation> recomendations;

    @OneToMany(mappedBy = "student")
    private List<JobFound> jobFounds;




}
