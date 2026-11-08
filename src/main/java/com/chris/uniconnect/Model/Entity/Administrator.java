package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//perfil del admin principal: existe para que pueda publicar e interactuar en el feed (todo apunta a una Person).
//en el feed aparece como la institucion (perfil compartido entre todos los admins)
@Entity
@Table(name = "administrator")
@Getter
@Setter
@NoArgsConstructor
public class Administrator extends Person {

    @ManyToOne
    @JoinColumn(name = "id_institution_profile")
    private InstitutionProfile institutionProfile;
}
