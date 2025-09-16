package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "publication_interaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicationInteraction {

    @EmbeddedId
    private PublicationInteractionPK id;

    private Boolean liked;

    private String comment;

    @Column(name = "created_at")
    private LocalDateTime createdAt;;

    @ManyToOne
    @JoinColumn(name = "id_person" , referencedColumnName = "id" , insertable = false, updatable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "id_publication" , referencedColumnName = "id_publication" , insertable = false , updatable = false)
    private Publication publication;

}
