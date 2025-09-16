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
public class PublicationInteractionPK  implements Serializable {

    @Column(name = "id_publication")
    private Integer idPublication;

    @Column(name = "id_person")
    private Integer idPerson;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PublicationInteractionPK that)) return false;
        return Objects.equals(idPublication, that.idPublication) && Objects.equals(idPerson, that.idPerson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPublication, idPerson);
    }
}
