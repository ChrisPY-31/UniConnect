package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "personContact")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonContact {

    @EmbeddedId
    private PersonContactPK id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "id_person" , referencedColumnName = "id" , insertable = false , updatable = false)
    private Person person;

    @ManyToOne
    @JoinColumn(name = "id_contact" , referencedColumnName = "id_contact" , insertable = false , updatable = false)
    private Contact contact;

}
