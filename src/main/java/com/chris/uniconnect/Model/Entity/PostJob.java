package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.VacanteStatus;
import com.chris.uniconnect.Enum.WorkModality;
import com.chris.uniconnect.Enum.WorkingDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "post_job")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostJob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_post_job")
    private Long idPostJob;

    //reclutador que la creo y la edita
    @Column(name = "id_recruiter", nullable = false)
    private Integer idRecruiter;

    //la vacante es de la empresa: sobrevive aunque el reclutador se desactive
    @Column(name = "id_corporate_partnership", nullable = false)
    private Long idCorporatePartnership;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String ubication;

    @Enumerated(EnumType.STRING)
    private WorkingDay workDay;

    @Enumerated(EnumType.STRING)
    private WorkModality modality;

    @Column(name = "type_application")
    @Enumerated(EnumType.STRING)
    private TypeApplication typeApplication;

    @Column(name = "salary_min")
    private Integer salaryMin;

    @Column(name = "salary_max")
    private Integer salaryMax;

    //VENCIDA no se guarda, se calcula con closingDate (ver getEffectiveStatus)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VacanteStatus status = VacanteStatus.BORRADOR;

    //se fija la primera vez que se publica
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    //null = sin fecha de cierre
    @Column(name = "closing_date")
    private LocalDate closingDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_recruiter" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Recruiter recruiter;

    @ManyToOne
    @JoinColumn(name = "id_corporate_partnership", referencedColumnName = "id_corporate_partnership", insertable = false, updatable = false)
    private CorporatePartnershipsUAEMex corporatePartnershipsUAEMex;

    @ManyToMany
    @JoinTable(name = "post_job_career",
            joinColumns = @JoinColumn(name = "id_post_job"),
            inverseJoinColumns = @JoinColumn(name = "id_career")
    )
    private Set<Career> careers = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "post_job_technology",
            joinColumns = @JoinColumn(name = "id_post_job"),
            inverseJoinColumns = @JoinColumn(name = "id_technology")
    )
    private Set<Technology> technologies = new HashSet<>();

    @OneToMany(mappedBy = "postJob")
    private Set<JobApplication> jobApplications = new HashSet<>();

    @Transient
    public VacanteStatus getEffectiveStatus() {
        if (status == VacanteStatus.PUBLICADA && closingDate != null && closingDate.isBefore(LocalDate.now())) {
            return VacanteStatus.VENCIDA;
        }
        return status;
    }
}
