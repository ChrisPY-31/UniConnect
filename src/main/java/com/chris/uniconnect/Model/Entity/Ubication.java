package com.chris.uniconnect.Model.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "ubication")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Ubication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ubication")
    private Integer idUbication;

    private String country;

    private String city;

    @OneToMany(mappedBy = "ubication")
    private List<Person> person;

}
