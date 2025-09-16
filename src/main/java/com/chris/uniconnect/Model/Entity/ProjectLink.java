package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.ProjectLinks;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projectLink")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_project_link")
    private Integer idProjectLink;

    @Column(name = "id_project")
    private Integer idProject;

    @Column(length = 255)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_link")
    private ProjectLinks projectLink;

    @ManyToOne
    @JoinColumn(name = "id_project" , referencedColumnName = "id_project" , insertable = false , updatable = false)
    private Project project;

}
