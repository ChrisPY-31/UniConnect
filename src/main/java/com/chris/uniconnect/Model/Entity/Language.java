package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.LanguageLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "language")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_languaje")
    private Integer idLanguaje;

    @Column(name = "id_person")
    private Integer idPerson;

    private String name;

    @Enumerated(EnumType.STRING)
    private LanguageLevel level;

    @ManyToOne
    @JoinColumn(name = "id_person" , referencedColumnName = "id" , insertable = false , updatable = false)
    private Person person;
}
