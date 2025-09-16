package com.chris.uniconnect.Model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class RecomendationPk {

    @Column(name = "id_student")
    private Integer idStudent;

    @Column(name = "id_teacher")
    private Integer idTeacher;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecomendationPk that)) return false;
        return Objects.equals(idStudent, that.idStudent) && Objects.equals(idTeacher, that.idTeacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idStudent, idTeacher);
    }
}
