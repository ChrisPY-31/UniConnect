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

    @Column(name = "id_corporate_partnership")
    private Long idCorporatePartnership;

    private String position;

    @ManyToOne
    @JoinColumn(name = "id_corporate_partnership" , referencedColumnName = "id_corporate_partnership" , insertable = false , updatable = false)
    private CorporatePartnershipsUAEMex corporatePartnershipsUAEMex;

    @OneToMany(mappedBy = "recruiter")
    private List<PostJob> postJobs;
}
