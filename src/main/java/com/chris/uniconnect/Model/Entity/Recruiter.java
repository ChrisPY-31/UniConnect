package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "recruiter")
public class Recruiter extends Person {

    @Column(name = "id_company")
    private Integer idCompany;

    @ManyToOne
    @JoinColumn(name = "id_company" , referencedColumnName = "id_company" , insertable = false , updatable = false)
    private Company company;

    @OneToMany(mappedBy = "recruiter")
    private List<JobFound> jobFounds;
}
