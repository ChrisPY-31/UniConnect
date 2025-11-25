package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "project")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project")
    private Integer idProject;

    @Column(name = "id_student")
    private Integer idStudent;

    @Column(length = 150)
    private String name;

    private String description;

    @Column(length = 255)
    private String image;

    @Column(name = "start_date", columnDefinition = "DATE" )
    private LocalDate startDate;

    @Column(name = "end_date", columnDefinition = "DATE")
    private LocalDate endDate;

    private String gitHub;

    private String deploy;

    @ManyToOne
    @JoinColumn(name = "id_student" , referencedColumnName = "id" , insertable = false , updatable = false)
    private Student student;

    @ManyToMany
    @JoinTable(name = "project_mention" ,
            joinColumns = @JoinColumn(name = "id_project"),
            inverseJoinColumns = @JoinColumn(name = "id_student")
    )
    private Set<Student> mentions;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Technology> technologies;

}
