package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "skill")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_skill")
    private Integer idSkill;

    @Column(name = "id_person")
    private Integer idPerson;

    @Column(length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "id_person" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Person person;


}
