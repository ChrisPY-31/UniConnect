package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.EducationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "education")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Education {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_education")
    private Integer idEducation;

    @Column(name = "id_person")
    private Integer idPerson;

    private String institution;

    private String degree;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "education_type")
    private EducationType educationType;

    @Column(name = "start_date" , columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(name = "end_date", columnDefinition = "DATE")
    private LocalDate endDate;


    @ManyToOne
    @JoinColumn(name = "id_person" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Person person;
}

