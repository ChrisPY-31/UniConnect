package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company")
    private Integer idCompany;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String sector;

    @Column(length = 100)
    private String location;

    @OneToMany(mappedBy = "company")
    private List<Recruiter> recruiters;

    @OneToMany(mappedBy = "company")
    private List<JobFound> jobFounds;


}
