package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.AplicationsStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_application",
        uniqueConstraints = @UniqueConstraint(name = "ux_job_application_student_post_job", columnNames = {"id_student", "id_post_job"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_job_application")
    private Long idJobApplication;

    @Column(name = "id_student", nullable = false)
    private Integer idStudent;

    @Column(name = "id_post_job", nullable = false)
    private Long idPostJob;

    //copia del CV del perfil al momento de postularse: cambiar el CV despues no altera esta postulacion
    @Column(name = "resume_url", nullable = false)
    private String resumeUrl;

    @Column(name = "cover_letter", columnDefinition = "TEXT")
    private String coverLetter;

    @Column(name = "application_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AplicationsStatus aplicationStatus = AplicationsStatus.ENVIADA;

    @Column(name = "applied_at", nullable = false)
    private LocalDateTime appliedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "id_student" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_post_job" , referencedColumnName = "id_post_job" , insertable = false , updatable = false)
    private PostJob postJob;

}
