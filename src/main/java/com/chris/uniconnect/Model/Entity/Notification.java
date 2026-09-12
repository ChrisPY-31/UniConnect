package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_person", referencedColumnName = "id")
    private Person person;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private String message;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Column(nullable = false)
    private boolean read;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
