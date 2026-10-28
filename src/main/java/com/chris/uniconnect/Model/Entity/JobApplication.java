package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.AplicationsStatus;
import com.chris.uniconnect.Enum.EducationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "job_application")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idJobApplication;

    @Column(name = "id_student")
    private Long idStudent;

    @Column(name = "id_post_job")
    private Long idPostJob;

    private String curriculum;

    @Column(name = "jobDate")
    private LocalDate jobDate;

    @Column(name = "application_status")
    @Enumerated(EnumType.STRING)
    private AplicationsStatus aplicationStatus;

    @ManyToOne
    @JoinColumn(name = "id_student" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Student students;

    @ManyToOne
    @JoinColumn(name = "id_post_job" , referencedColumnName = "id_post_job" , insertable = false , updatable = false)
    private PostJob postJobs;

}
