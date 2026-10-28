package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "conversation")
@Getter
@Setter
public class Conversation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conversation")
    private Long idConversation;

    @Column(name = "id_student")
    private Long idStudent;

    @Column(name = "id_recruiter")
    private Long idRecruiter;

    @Column(name = "creation_date")
    private LocalDate creationDate;

    private Boolean estado;
}
