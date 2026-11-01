package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.IssuerType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "message", indexes = @Index(name = "ix_message_conversation_sent", columnList = "id_conversation, send_date"))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_message")
    private Long idMessage;

    @Column(name = "id_conversation", nullable = false)
    private Long idConversation;

    @Column(name = "id_sender", nullable = false)
    private Integer idSender;

    @Enumerated(EnumType.STRING)
    @Column(name = "sender_type", nullable = false)
    private IssuerType senderType;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "send_date", nullable = false)
    private LocalDateTime sentAt;

    //null = el destinatario no lo ha leido
    @Column(name = "read_at")
    private LocalDateTime readAt;

    @ManyToOne
    @JoinColumn(name = "id_conversation", referencedColumnName = "id_conversation", insertable = false, updatable = false)
    private Conversation conversation;
}
