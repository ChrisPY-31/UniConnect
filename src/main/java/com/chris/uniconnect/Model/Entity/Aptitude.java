package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aptitude")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Aptitude {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aptitude")
    private Integer idAptitude;

    @Column(name = "id_person")
    private Integer idPerson;

    private String name;

    @ManyToOne
    @JoinColumn(name = "id_person" , referencedColumnName = "id" , insertable = false , updatable = false)
    private Person person;
}
