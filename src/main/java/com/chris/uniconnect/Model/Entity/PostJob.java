package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.TypeApplication;
import com.chris.uniconnect.Enum.WorkingDay;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
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

    @Column(name = "id_recruiter")
    private Long idRecruiter;

    private String title;

    private String description;

    private String ubication;

    @Enumerated(EnumType.STRING)
    private WorkingDay workDay;

    @Column(name = "type_application")
    @Enumerated(EnumType.STRING)
    private TypeApplication typeApplication;

    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @ManyToOne
    @JoinColumn(name = "id_recruiter" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Recruiter recruiter;

    @OneToMany(mappedBy = "postJobs")
    private Set<JobApplication> jobApplications = new HashSet<>();


}


