package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "person")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_ubication")
    private Integer idUbication;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "last_name", length = 100)
    private String lastName;

    private String description;

    @Column(name = "birth_date", columnDefinition = "DATE")
    private LocalDate birthDate;

    private String image;

    @Column(length = 100)
    private String specialty;

    @Column(name = "resume_url")
    private String resumeUrl;

    @OneToMany(mappedBy = "person")
    private List<Education> educations; //revisar la relacion

    @OneToMany(mappedBy = "person")
    private List<Aptitude> aptitudes;

    @OneToMany(mappedBy = "person")
    private List<Skill> skills;

    @OneToMany(mappedBy = "person")
    private List<Language> languages;

    @OneToMany(mappedBy = "person")
    private List<Publication> publications;

    //relaciones compuestas

    @OneToMany(mappedBy = "person")
    private List<PublicationInteraction> publicationInteractions;

    @OneToMany(mappedBy = "person")
    private List<PersonContact> personContacts;

    //relacion ManyToOne

    @ManyToOne
    @JoinColumn(name = "id_ubication" , referencedColumnName = "id_ubication" , insertable = false, updatable = false)
    private Ubication ubication;

}
