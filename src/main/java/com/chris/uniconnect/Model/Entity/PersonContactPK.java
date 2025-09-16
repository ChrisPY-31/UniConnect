package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PersonContactPK implements Serializable {

    @Column(name = "id_person")
    private Integer idPerson;

    @Column(name = "id_contact")
    private Integer idContact;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonContactPK that)) return false;
        return Objects.equals(idPerson, that.idPerson) && Objects.equals(idContact, that.idContact);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPerson, idContact);
    }

}
