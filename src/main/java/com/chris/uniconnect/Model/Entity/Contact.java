package com.chris.uniconnect.Model.Entity;

import com.chris.uniconnect.Enum.ContactType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "contact")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contact")
    private Integer idContact;

    @Enumerated(EnumType.STRING)
    private ContactType contact;

    @OneToMany(mappedBy = "contact")
    private List<PersonContact> personContacts;
}
