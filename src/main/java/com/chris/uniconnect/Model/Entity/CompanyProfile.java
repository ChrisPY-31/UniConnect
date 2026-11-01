package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//landing publica de la empresa: el convenio guarda los datos administrativos (los edita el ADMIN),
//el perfil la presentacion hacia los estudiantes (la edita el COMPANY_ADMIN)
@Entity
@Table(name = "company_profile")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_company_profile")
    private Long idCompanyProfile;

    //unique: un solo perfil por convenio
    @Column(name = "id_corporate_partnership", nullable = false, unique = true)
    private Long idCorporatePartnership;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "banner_url")
    private String bannerUrl;

    private String website;

    private String sector;

    //texto libre y no enum: Hibernate no actualiza los CHECK de enums con ddl-auto=update
    @Column(name = "company_size")
    private String companySize;

    private String location;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "id_corporate_partnership", referencedColumnName = "id_corporate_partnership", insertable = false, updatable = false)
    private CorporatePartnershipsUAEMex corporatePartnershipsUAEMex;
}
