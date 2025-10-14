package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "recruiter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Recruiter extends Person {

    @Column(name = "id_company")
    private Integer idCompany;

    private String position;

    @ManyToOne
    @JoinColumn(name = "id_company" , referencedColumnName = "id_company" , insertable = false , updatable = false)
    private Company company;

    @OneToMany(mappedBy = "recruiter")
    private List<JobFound> jobFounds;
}
