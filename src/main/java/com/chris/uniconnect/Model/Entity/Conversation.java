package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.ConversationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

//una conversacion por par estudiante-reclutador; la inicia el reclutador desde una postulacion
@Entity
@Table(name = "conversation",
        uniqueConstraints = @UniqueConstraint(name = "ux_conversation_student_recruiter", columnNames = {"id_student", "id_recruiter"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversation")
    private Long idConversation;

    @Column(name = "id_student", nullable = false)
    private Integer idStudent;

    @Column(name = "id_recruiter", nullable = false)
    private Integer idRecruiter;

    //postulacion desde la que se abrio el chat (contexto de la vacante)
    @Column(name = "id_job_application", nullable = false)
    private Long idJobApplication;

    //solo el estudiante bloquea/desbloquea
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConversationStatus status = ConversationStatus.ACTIVA;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_message_at")
    private LocalDateTime lastMessageAt;

    @ManyToOne
    @JoinColumn(name = "id_student", referencedColumnName = "id", insertable = false, updatable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "id_recruiter", referencedColumnName = "id", insertable = false, updatable = false)
    private Recruiter recruiter;

    @ManyToOne
    @JoinColumn(name = "id_job_application", referencedColumnName = "id_job_application", insertable = false, updatable = false)
    private JobApplication jobApplication;
}
