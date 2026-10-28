package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "corporate_partnerships_uaemex")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CorporatePartnershipsUAEMex {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_corporate_partnership")
    private Long idCorporatePartnership;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    private String category;

    @Column(name = "agreement_type")
    private String agreementType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    //null = convenio sin fecha de termino
    @Column(name = "end_date")
    private LocalDate endDate;

    //baja logica: desactivar no borra el historial de la empresa
    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean active = true;

    @OneToMany(mappedBy = "corporatePartnershipsUAEMex")
    private List<Recruiter> recruiters;

    @Transient
    public boolean isVigente() {
        LocalDate today = LocalDate.now();
        return Boolean.TRUE.equals(active)
                && startDate != null && !startDate.isAfter(today)
                && (endDate == null || !endDate.isBefore(today));
    }

}
