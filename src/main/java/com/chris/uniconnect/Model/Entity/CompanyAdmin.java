package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "company_admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyAdmin extends Person {

    @Column(name = "id_corporate_partnership", nullable = false)
    private Long idCorporatePartnership;

    private String position;

    @ManyToOne
    @JoinColumn(name = "id_corporate_partnership", referencedColumnName = "id_corporate_partnership", insertable = false, updatable = false)
    private CorporatePartnershipsUAEMex corporatePartnershipsUAEMex;
}
