package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "technology")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_technology")
    private Integer idTechnology;

    @Column(name = "id_project")
    private Integer idProject;

    @Column(length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_project" , referencedColumnName = "id_project" , insertable = false , updatable = false)
    private Project project;
}
