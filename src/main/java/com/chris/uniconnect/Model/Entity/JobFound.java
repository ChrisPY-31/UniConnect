package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

//Tabla intermedia
@Entity
@Table(name = "jobfound")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class JobFound implements Serializable {

    @EmbeddedId
    private JobFoundPk id;

    @Column(name = "contract_date")
    private LocalDate contractDate;
    
    private String position;

    @ManyToOne
    @JoinColumn(name = "id_student" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_recruiter" , referencedColumnName = "id", insertable = false, updatable = false)
    private Recruiter recruiter;

    @ManyToOne
    @JoinColumn(name = "id_company" , referencedColumnName = "id_company" , insertable = false, updatable = false)
    private Company company;
}
