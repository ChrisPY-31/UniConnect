package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "publication")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_publication")
    private Integer idPublication;

    @Column(name = "id_person")
    private Integer idPerson;

    private String description;

    private String image;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "id_person" , referencedColumnName = "id" , insertable = false , updatable = false)
    private Person person;

    @OneToMany(mappedBy = "publication")
    private List<PublicationInteraction> publicationInteractions;
}
